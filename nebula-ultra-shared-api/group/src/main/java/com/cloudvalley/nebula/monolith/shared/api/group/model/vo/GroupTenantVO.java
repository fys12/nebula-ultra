package com.cloudvalley.nebula.monolith.shared.api.group.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupTenantVO {

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
     * 系统组ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sGroupId;

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
     * 色标
     */
    String color;

    /**
     * 软删
     */
    Boolean deleted;

}
