package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckRoleVO {

    /**
     * 有效角色 [ 系统级 ]
     */
    List<SysRoleVO> validSysRole;

    /**
     * 有效角色 [ 租户级 ]
     */
    List<SysRoleVO> validTenantRole;

    /**
     * 有效角色 [ 用户级 ]
     */
    List<SysRoleVO> validUserRole;

    /**
     * 禁用角色 [ 系统级 ]
     */
    List<SysRoleVO> disabledSysRole;

    /**
     * 禁用角色 [ 租户级 ]
     */
    List<SysRoleVO> disabledTenantRole;

    /**
     * 禁用角色 [ 用户级 ]
     */
    List<SysRoleVO> disabledUserRole;
    
}
