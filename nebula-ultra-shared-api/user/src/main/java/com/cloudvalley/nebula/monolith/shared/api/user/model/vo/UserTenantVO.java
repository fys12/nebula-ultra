package com.cloudvalley.nebula.monolith.shared.api.user.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTenantVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 系统租户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sTenantId;

    /**
     * 系统用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sUserId;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long createdById;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 软删
     */
    Boolean deleted;

}
