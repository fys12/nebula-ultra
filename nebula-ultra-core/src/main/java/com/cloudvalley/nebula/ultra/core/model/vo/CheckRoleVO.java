package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckRoleVO {

    /**
     * 有效角色 [ 系统级 ]
     */
    Map<Long, List<SysRoleVO>> validSysRole;

    /**
     * 有效角色 [ 租户级 ]
     */
    Map<Long, List<SysRoleVO>> validTenantRole;

    /**
     * 有效角色 [ 用户级 ]
     */
    Map<Long, List<SysRoleVO>> validUserRole;

    /**
     * 禁用角色 [ 系统级 ]
     */
    Map<Long, List<SysRoleVO>> disabledSysRole;

    /**
     * 禁用角色 [ 租户级 ]
     */
    Map<Long, List<SysRoleVO>> disabledTenantRole;

    /**
     * 禁用角色 [ 用户级 ]
     */
    Map<Long, List<SysRoleVO>> disabledUserRole;

    /**
     * 因 租户禁用 级联禁用的角色
     * cascadeDisable 为 null
     */
    Map<Long, List<SysRoleVO>> disabledRoleByTenant;

    /**
     * 有效角色 [ 全局 ]
     */
    Map<Long, List<SysRoleVO>> validGlobalRole;

    /**
     * 禁用角色 [ 全局 ]
     */
    Map<Long, List<SysRoleVO>> disabledGlobalRole;
    
}
