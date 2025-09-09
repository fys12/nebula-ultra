package com.cloudvalley.nebula.monolith.shared.api.user.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserVO {

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
     * 创建人用户ID
     */
    Long createdById;

    /**
     * 更新人用户ID
     */
    Long updatedById;

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
