package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupAggregatorService;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupPermService;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupPermVO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.service.ISysGroupCommonService;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.IPermTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.ISysPermCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupAggregatorServiceImpl implements IGroupAggregatorService {

    @Autowired
    private IGroupPermService iGroupPermService;

    @Autowired
    private IGroupTenantCommonService iGroupTenantCommonService;

    @Autowired
    private ISysGroupCommonService iSysGroupCommonService;

    @Autowired
    private IPermTenantCommonService iPermTenantCommonService;

    @Autowired
    private ISysPermCommonService iSysPermCommonService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    /**
     * 获取 权限组 详细 详细
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 详细 详细
     */
    @Override
    public IPage<GroupPermDetailsVO> getPermGroupInfo(Integer current, Integer size) {
        // 1. 查询 权限组 基本信息
        IPage<GroupPermVO> groupPermList = iGroupPermService.getGroupPermList(new Page<>(current, size));

        // 1.2 获取 租户组Id
        List<Long> tGroupIdList = groupPermList.getRecords().stream()
                .map(GroupPermVO::getTGroupId)
                .toList();

        // 1.3 获取 租户权限Id
        List<Long> tPermIdList = groupPermList.getRecords().stream()
                .map(GroupPermVO::getTPermId)
                .toList();

        // 2. 根据 租户组Id 查询 租户组信息
        Map<Long, GroupTenantVO> tenantGroupMap = iGroupTenantCommonService.getGroupTenantsByIds(tGroupIdList).stream()
                .collect(Collectors.toMap(GroupTenantVO::getId, groupTenant -> groupTenant));

        // 2.1 获取 系统组Id
        List<Long> sGroupIdList = tenantGroupMap.values().stream()
                .map(GroupTenantVO::getSGroupId)
                .toList();

        // 2.2 根据 系统组Id 查询 系统组信息
        Map<Long, SysGroupVO> sysGroupMap = iSysGroupCommonService.getSysGroupsByIds(sGroupIdList).stream()
                .collect(Collectors.toMap(SysGroupVO::getId, sysGroup -> sysGroup));

        // 2.3 获取 用户Id
        List<Long> userIdList = sysGroupMap.values().stream()
                .map(SysGroupVO::getCreatorById)
                .toList();

        // 3. 根据 租户权限Id 查询 租户权限信息
        Map<Long, PermTenantVO> tenantPermMap = iPermTenantCommonService.getPermTenantsByIds(tPermIdList).stream()
                .collect(Collectors.toMap(PermTenantVO::getId, permTenant -> permTenant));

        // 3.1 获取 系统权限Id
        List<Long> sPermIdList = tenantPermMap.values().stream()
                .map(PermTenantVO::getSPermId)
                .toList();

        // 3.2 根据 系统权限Id 查询 系统权限信息
        Map<Long, SysPermVO> sysPermMap = iSysPermCommonService.getSysPermsByIds(sPermIdList).stream()
                .collect(Collectors.toMap(SysPermVO::getId, sysPerm -> sysPerm));

        // 4. 根据 用户Id 获取 用户信息
        Map<Long, SysUserVO> userMap = iSysUserCommonService.getUsersByIds(userIdList).stream()
                .collect(Collectors.toMap(SysUserVO::getId, sysUser -> sysUser));

        // 5. 组装数据
        List<GroupPermDetailsVO> detailsList = groupPermList.getRecords().stream()
                .map(perm -> {
                    GroupTenantVO tenantGroup = tenantGroupMap.get(perm.getTGroupId());
                    SysGroupVO sysGroup = (tenantGroup != null) ? sysGroupMap.get(tenantGroup.getSGroupId()) : null;

                    PermTenantVO tenantPerm = tenantPermMap.get(perm.getTPermId());
                    SysPermVO sysPerm = (tenantPerm != null) ? sysPermMap.get(tenantPerm.getSPermId()) : null;

                    return new GroupPermDetailsVO(
                            perm.getId(),
                            sysGroup,
                            sysPerm,
                            perm.getCreatedAt(),
                            perm.getUpdatedAt(),
                            perm.getDeleted()
                    );
                })
                .toList();

        IPage<GroupPermDetailsVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(detailsList);
        resultPage.setTotal(groupPermList.getTotal());
        resultPage.setSize(groupPermList.getSize());
        resultPage.setCurrent(groupPermList.getCurrent());

        return resultPage;
    }
}
