package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckDeptVO {

    /**
     * 有效部门 [ 系统级 ]
     */
    List<SysDeptVO> validSysDept;

    /**
     * 有效部门 [ 租户级 ]
     */
    List<SysDeptVO> validTenantDept;

    /**
     * 有效部门 [ 用户级 ]
     */
    List<SysDeptVO> validUserDept;

    /**
     * 禁用部门 [ 系统级 ]
     */
    List<SysDeptVO> disabledSysDept;

    /**
     * 禁用部门 [ 租户级 ]
     */
    List<SysDeptVO> disabledTenantDept;

    /**
     * 禁用部门 [ 用户级 ]
     */
    List<SysDeptVO> disabledUserDept;

}
