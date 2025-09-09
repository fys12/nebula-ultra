package com.cloudvalley.nebula.monolith.business.user.model.vo;

import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.monolith.shared.api.tenant.model.vo.SysTenantVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAggregatorInfoVO {

    /**
     * 主键ID（雪花算法ID）
     */
    Long id;

    /**
     * 用户名
     */
    String username;

    /**
     * 密码哈希
     */
    String passwordHash;

    /**
     * 二次认证密钥
     */
    String mfaSecret;

    /**
     * 所属租户
     */
    List<SysTenantVO> tenantInfo;

    /**
     * 所属部门
     */
    List<SysDeptVO> deptInfo;

    /**
     * 所属角色
     */
    List<SysRoleVO> roleInfo;

    /**
     * 最近登录时间
     */
    LocalDateTime lastLoginAt;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 创建人用户名
     */
    String createdByUser;

    /**
     * 更新人用户ID
     */
    String updatedByUser;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 软删
     */
    Boolean deleted;

}
