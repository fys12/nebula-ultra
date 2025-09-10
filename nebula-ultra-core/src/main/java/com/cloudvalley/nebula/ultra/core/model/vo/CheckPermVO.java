package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckPermVO {

    /**
     * 有效权限 [ 系统级 ]
     */
    List<SysPermVO> validSysPerm;

    /**
     * 有效权限 [ 租户级 ]
     */
    List<SysPermVO> validTenantPerm;

    /**
     * 有效权限 [ 部门级 ]
     */
    List<SysPermVO> validDeptPerm;

    /**
     * 有效权限 [ 角色级 ]
     */
    List<SysPermVO> validRolePerm;

    /**
     * 有效权限 [ 用户级 ]
     */
    List<SysPermVO> validUserPerm;

    /**
     * 禁用权限 [ 系统级 ]
     */
    List<SysPermVO> disabledSysPerm;

    /**
     * 禁用权限 [ 租户级 ]
     */
    List<SysPermVO> disabledTenantPerm;

    /**
     * 禁用权限 [ 部门级 ]
     */
    List<SysPermVO> disabledDeptPerm;

    /**
     * 禁用权限 [ 角色级 ]
     */
    List<SysPermVO> disabledRolePerm;

    /**
     * 禁用权限 [ 用户级 ]
     */
    List<SysPermVO> disabledUserPerm;

}
