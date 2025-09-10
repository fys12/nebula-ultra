package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckDeptVO {

    /**
     * 有效部门 [ 系统级 ]
     */
    Map<Long, List<SysDeptVO>> validSysDept;

    /**
     * 有效部门 [ 租户级 ]
     */
    Map<Long, List<SysDeptVO>> validTenantDept;

    /**
     * 有效部门 [ 用户级 ]
     */
    Map<Long, List<SysDeptVO>> validUserDept;

    /**
     * 禁用部门 [ 系统级 ]
     */
    Map<Long, List<SysDeptVO>> disabledSysDept;

    /**
     * 禁用部门 [ 租户级 ]
     */
    Map<Long, List<SysDeptVO>> disabledTenantDept;

    /**
     * 禁用部门 [ 用户级 ]
     */
    Map<Long, List<SysDeptVO>> disabledUserDept;

}
