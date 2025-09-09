package com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotaTenantVO {

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
     * 系统配额ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sQuotaId;

    /**
     * 总配额
     */
    Integer total;

    /**
     * 已使用
     */
    Integer used;

    /**
     * 剩余配额
     */
    Integer remain;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 最近更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 软删
     */
    Boolean deleted;

}
