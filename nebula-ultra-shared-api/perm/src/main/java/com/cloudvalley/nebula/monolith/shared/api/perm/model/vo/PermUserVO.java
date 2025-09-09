package com.cloudvalley.nebula.monolith.shared.api.perm.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermUserVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 租户用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tUserId;

    /**
     * 租户权限ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tPermId;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 状态
     */
    Boolean status;

    /**
     * 软删
     */
    Boolean deleted;

}
