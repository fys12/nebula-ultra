package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermListVO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupAggregatorService;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupPermService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.*;
import com.cloudvalley.nebula.ultra.shared.api.group.service.*;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.IPermTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.ISysPermCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupAggregatorServiceImpl implements IGroupAggregatorService {

    @Autowired
    private IGroupPermService iGroupPermService;

    @Autowired
    private IGroupPermCommonService iGroupPermCommonService;

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

    @Autowired
    private IGroupBindDeptCommonService iGroupBindDeptCommonService;

    @Autowired
    private IDeptTenantCommonService iDeptTenantCommonService;

    @Autowired
    private ISysDeptCommonService iSysDeptCommonService;

    @Autowired
    private IGroupBindRoleCommonService iGroupBindRoleCommonService;

    @Autowired
    private IRoleTenantCommonService iRoleTenantCommonService;

    @Autowired
    private ISysRoleCommonService iSysRoleCommonService;

    /**
     * 获取 权限组 列表
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 列表
     */
    @Override
    public IPage<GroupPermListVO> getPermGroupInfo(Integer current, Integer size) {
        // 1. 查询 权限组 基本信息
        IPage<GroupPermVO> groupPermList = iGroupPermService.getGroupPermList(new Page<>(current, size));

        // 2. 获取 租户组Id 列表
        List<Long> tGroupIdList = groupPermList.getRecords().stream()
                .map(GroupPermVO::getTGroupId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 3. 根据 租户组Id 查询 租户组信息
        Map<Long, GroupTenantVO> tenantGroupMap = iGroupTenantCommonService.getGroupTenantsByIds(tGroupIdList).stream()
                .collect(Collectors.toMap(GroupTenantVO::getId, groupTenant -> groupTenant));

        // 4. 获取 系统组Id 列表
        List<Long> sGroupIdList = tenantGroupMap.values().stream()
                .map(GroupTenantVO::getSGroupId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 5. 根据 系统组Id 查询 系统组信息
        Map<Long, SysGroupVO> sysGroupMap = iSysGroupCommonService.getSysGroupsByIds(sGroupIdList).stream()
                .collect(Collectors.toMap(SysGroupVO::getId, sysGroup -> sysGroup));

        // 6. 组装数据
        List<GroupPermListVO> groupList = groupPermList.getRecords().stream()
                .map(perm -> {
                    GroupTenantVO tenantGroup = tenantGroupMap.get(perm.getTGroupId());
                    SysGroupVO sysGroup = (tenantGroup != null) ? sysGroupMap.get(tenantGroup.getSGroupId()) : null;

                    // 使用租户组的颜色替换系统组的颜色
                    String color = (tenantGroup != null) ? tenantGroup.getColor() : null;
                    if (sysGroup != null) {
                        sysGroup = new SysGroupVO(
                                sysGroup.getId(),
                                sysGroup.getName(),
                                sysGroup.getDesc(),
                                sysGroup.getGroupType(),
                                sysGroup.getGroupLevel(),
                                sysGroup.getCreatorById(),
                                sysGroup.getCreatedAt(),
                                sysGroup.getUpdatedAt(),
                                color, // 使用租户组的颜色
                                sysGroup.getDeleted()
                        );
                    }

                    return new GroupPermListVO(
                            tenantGroup.getId(),
                            sysGroup
                    );
                })
                .toList();

        // 7. 创建分页结果
        IPage<GroupPermListVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(groupList);
        resultPage.setTotal(groupPermList.getTotal());
        resultPage.setSize(groupPermList.getSize());
        resultPage.setCurrent(groupPermList.getCurrent());

        return resultPage;
    }

    /**
     * 获取 权限组 详细信息
     * @param groupId 权限组Id
     * @return 权限组 详细信息
     */
    @Override
    public GroupPermDetailsVO getPermGroupDetails(Long groupId) {
        // 1. 根据 租户组Id 查询 权限组 基本信息
        List<GroupPermVO> groupPermsByGroupId = iGroupPermCommonService.getGroupPermsByGroupId(groupId);

        // 2. 根据 租户组Id 查询 租户组信息
        GroupTenantVO tenantGroup = iGroupTenantCommonService.getGroupTenantById(groupId);

        // 3. 根据 系统组Id 查询 系统组信息
        SysGroupVO sysGroup = null;
        if (tenantGroup != null && tenantGroup.getSGroupId() != null) {
            sysGroup = iSysGroupCommonService.getSysGroupById(tenantGroup.getSGroupId());
        }

        // 4. 根据 租户权限Id列表 查询 租户权限信息
        List<Long> tPermIds = groupPermsByGroupId.stream()
                .map(GroupPermVO::getTPermId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 5. 根据 系统权限Id列表 查询 系统权限信息
        List<SysPermVO> sysPerms = new ArrayList<>();
        if (!tPermIds.isEmpty()) {
            List<PermTenantVO> permTenants = iPermTenantCommonService.getPermTenantsByIds(tPermIds);
            List<Long> sPermIds = permTenants.stream()
                    .map(PermTenantVO::getSPermId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            if (!sPermIds.isEmpty()) {
                sysPerms = iSysPermCommonService.getSysPermsByIds(sPermIds);
            }
        }

        // 6. 根据 租户组Id 查询 组 绑定的租户部门信息
        List<GroupBindDeptVO> groupBindDepts = iGroupBindDeptCommonService.getGroupBindDeptsBySGroupId(groupId);

        // 6.1 获取 租户部门Id
        List<Long> tDeptIds = groupBindDepts.stream()
                .map(GroupBindDeptVO::getTDeptId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 6.2 根据 租户部门Id 查询 租户部门信息
        List<DeptTenantVO> deptTenants = new ArrayList<>();
        if (!tDeptIds.isEmpty()) {
            deptTenants = iDeptTenantCommonService.getDeptTenantsByIds(tDeptIds);
        }

        // 6.3 获取 系统部门Id
        List<Long> sDeptIds = deptTenants.stream()
                .map(DeptTenantVO::getSDeptId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 6.4 根据 系统部门Id 查询 系统部门信息
        List<SysDeptVO> sysDepts = new ArrayList<>();
        if (!sDeptIds.isEmpty()) {
            sysDepts = iSysDeptCommonService.getSysDeptsByIds(sDeptIds);
        }

        // 7. 根据 租户组Id 查询 组 绑定的租户角色信息
        List<GroupBindRoleVO> groupBindRoles = iGroupBindRoleCommonService.getGroupBindRolesByGroupId(groupId);

        // 7.1 获取 租户角色Id
        List<Long> tRoleIds = groupBindRoles.stream()
                .map(GroupBindRoleVO::getTRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 7.2 根据 租户角色Id 查询 租户角色信息
        List<RoleTenantVO> roleTenants = new ArrayList<>();
        if (!tRoleIds.isEmpty()) {
            roleTenants = iRoleTenantCommonService.getRoleTenantsByIds(tRoleIds);
        }

        // 7.3 获取 系统角色Id
        List<Long> sRoleIds = roleTenants.stream()
                .map(RoleTenantVO::getSRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 7.4 根据 系统角色Id 查询 系统角色信息
        List<SysRoleVO> sysRoles = new ArrayList<>();
        if (!sRoleIds.isEmpty()) {
            sysRoles = iSysRoleCommonService.getSysRolesByIds(sRoleIds);
        }

        // 8. 获取组绑定的部门信息
        List<SysDeptVO> groupBindDeptList = sysDepts;

        // 9. 获取组绑定的角色信息
        List<SysRoleVO> groupBindRoleList = sysRoles;

        // 10. 使用租户组的颜色替换系统组的颜色
        if (sysGroup != null && tenantGroup != null) {
            sysGroup = new SysGroupVO(
                    sysGroup.getId(),
                    sysGroup.getName(),
                    sysGroup.getDesc(),
                    sysGroup.getGroupType(),
                    sysGroup.getGroupLevel(),
                    sysGroup.getCreatorById(),
                    sysGroup.getCreatedAt(),
                    sysGroup.getUpdatedAt(),
                    tenantGroup.getColor(), // 使用租户组的颜色
                    sysGroup.getDeleted()
            );
        }

        // 11. 组装并返回数据
        return new GroupPermDetailsVO(
                groupId,
                sysGroup,
                sysPerms,
                groupBindDeptList,
                groupBindRoleList,
                tenantGroup.getCreatedAt(),
                tenantGroup.getUpdatedAt(),
                tenantGroup.getDeleted()
        );
    }
}
