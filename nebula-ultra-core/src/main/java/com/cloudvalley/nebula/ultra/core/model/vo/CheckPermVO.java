package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckPermVO {

    /**
     * 有效权限 [ 系统级 ]
     */
    Map<Long, List<SysPermVO>> validSysPerm;

    /**
     * 有效权限 [ 租户级 ]
     */
    Map<Long, List<SysPermVO>> validTenantPerm;

    /**
     * 有效权限 [ 部门级 ]
     */
    Map<Long, List<SysPermVO>> validDeptPerm;

    /**
     * 有效权限 [ 角色级 ]
     */
    Map<Long, List<SysPermVO>> validRolePerm;

    /**
     * 有效权限 [ 用户级 ]
     */
    Map<Long, List<SysPermVO>> validUserPerm;

    /**
     * 禁用权限 [ 系统级 ]
     */
    Map<Long, List<SysPermVO>> disabledSysPerm;

    /**
     * 禁用权限 [ 租户级 ]
     */
    Map<Long, List<SysPermVO>> disabledTenantPerm;

    /**
     * 禁用权限 [ 部门级 ]
     */
    Map<Long, List<SysPermVO>> disabledDeptPerm;

    /**
     * 禁用权限 [ 角色级 ]
     */
    Map<Long, List<SysPermVO>> disabledRolePerm;

    /**
     * 禁用权限 [ 用户级 ]
     */
    Map<Long, List<SysPermVO>> disabledUserPerm;

    /**
     * 因 租户禁用 级联禁用的权限
     * cascadeDisable 为 null
     */
    Map<Long, List<SysPermVO>> disabledPermByTenant;

    /**
     * 因 部门禁用 级联禁用的权限
     * cascadeDisable 为 null
     */
    Map<Long, List<SysPermVO>> disabledPermByDept;

    /**
     * 因 角色禁用 级联禁用的权限
     * cascadeDisable 为 null
     */
    Map<Long, List<SysPermVO>> disabledPermByRole;

    /**
     * 有效角色 [ 全局 ]
     */
    Map<Long, List<SysPermVO>> validGlobalPerm;

}
