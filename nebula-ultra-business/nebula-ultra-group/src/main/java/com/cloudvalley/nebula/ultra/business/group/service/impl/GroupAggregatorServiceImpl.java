package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupAggregatorService;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupPermService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.*;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupBindDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupBindRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.group.service.ISysGroupCommonService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        // 5. 根据 租户组Id 查询 组 绑定的租户部门信息
        Map<Long, List<GroupBindDeptVO>> groupBindDeptMap = iGroupBindDeptCommonService.getGroupBindDeptsBySGroupIds(tGroupIdList);

        // 5.1 获取 租户部门Id
        List<Long> tDeptIdList = groupBindDeptMap.values().stream()
                .flatMap(List::stream)
                .map(GroupBindDeptVO::getTDeptId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 5.2 根据 租户部门Id 查询 租户部门信息
        Map<Long, DeptTenantVO> deptTenantMap = iDeptTenantCommonService.getDeptTenantsByIds(tDeptIdList).stream()
                .collect(Collectors.toMap(DeptTenantVO::getId, deptTenant -> deptTenant));

        // 5.3 获取 系统部门Id
        List<Long> sDeptIdList = deptTenantMap.values().stream()
                .map(DeptTenantVO::getSDeptId)
                .toList();

        // 5.4 根据 系统部门Id 查询 系统部门信息
        Map<Long, SysDeptVO> sysDeptMap = iSysDeptCommonService.getSysDeptsByIds(sDeptIdList).stream()
                .collect(Collectors.toMap(SysDeptVO::getId, sysDept -> sysDept));

        // 6. 根据 租户组Id 查询 组 绑定的租户角色信息
        Map<Long, List<GroupBindRoleVO>> groupBindRolesMap = iGroupBindRoleCommonService.getGroupBindRolesByGroupIds(tGroupIdList);

        // 6.1 获取 租户角色Id
        List<Long> tRoleIdList = groupBindRolesMap.values().stream()
                .flatMap(List::stream)
                .map(GroupBindRoleVO::getTRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 6.2 根据 租户角色Id 查询 租户角色信息
        Map<Long, RoleTenantVO> roleTenantMap = iRoleTenantCommonService.getRoleTenantsByIds(tRoleIdList).stream()
                .collect(Collectors.toMap(RoleTenantVO::getId, roleTenant -> roleTenant));

        // 6.3 获取 系统角色Id
        List<Long> sRoleIdList = roleTenantMap.values().stream()
                .map(RoleTenantVO::getSRoleId)
                .toList();

        // 6.4 根据 系统角色Id 查询 系统角色信息
        Map<Long, SysRoleVO> sysRoleMap = iSysRoleCommonService.getSysRolesByIds(sRoleIdList).stream()
                .collect(Collectors.toMap(SysRoleVO::getId, sysRole -> sysRole));

        // 7. 组装数据
        List<GroupPermDetailsVO> detailsList = groupPermList.getRecords().stream()
                .map(perm -> {
                    GroupTenantVO tenantGroup = tenantGroupMap.get(perm.getTGroupId());
                    SysGroupVO sysGroup = (tenantGroup != null) ? sysGroupMap.get(tenantGroup.getSGroupId()) : null;

                    PermTenantVO tenantPerm = tenantPermMap.get(perm.getTPermId());
                    SysPermVO sysPerm = (tenantPerm != null) ? sysPermMap.get(tenantPerm.getSPermId()) : null;

                    // 获取组绑定的部门信息
                    List<SysDeptVO> groupBindDept = new ArrayList<>();
                    List<GroupBindDeptVO> bindDeptList = groupBindDeptMap.get(perm.getTGroupId());
                    if (bindDeptList != null) {
                        groupBindDept = bindDeptList.stream()
                                .map(bindDept -> {
                                    DeptTenantVO deptTenant = deptTenantMap.get(bindDept.getTDeptId());
                                    return (deptTenant != null) ? sysDeptMap.get(deptTenant.getSDeptId()) : null;
                                })
                                .filter(Objects::nonNull)
                                .toList();
                    }

                    // 获取组绑定的角色信息
                    List<SysRoleVO> groupBindRole = new ArrayList<>();
                    List<GroupBindRoleVO> bindRoleList = groupBindRolesMap.get(perm.getTGroupId());
                    if (bindRoleList != null) {
                        groupBindRole = bindRoleList.stream()
                                .map(bindRole -> {
                                    RoleTenantVO roleTenant = roleTenantMap.get(bindRole.getTRoleId());
                                    return (roleTenant != null) ? sysRoleMap.get(roleTenant.getSRoleId()) : null;
                                })
                                .filter(Objects::nonNull)
                                .toList();
                    }

                    return new GroupPermDetailsVO(
                            perm.getId(),
                            sysGroup,
                            sysPerm,
                            groupBindDept,
                            groupBindRole,
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
