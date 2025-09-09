package com.cloudvalley.nebula.monolith.shared.api.perm.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermRoleVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 租户权限ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tPermId;

    /**
     * 租户角色ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tRoleId;

    /**
     * 软删
     */
    Boolean deleted;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 是否启用
     */
    Boolean state;

}
