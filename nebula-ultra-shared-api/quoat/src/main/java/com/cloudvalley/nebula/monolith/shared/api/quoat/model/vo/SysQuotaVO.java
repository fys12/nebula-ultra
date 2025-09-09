package com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysQuotaVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 配额类型编码（如 root_cap/sub_cap/user_cap/storage_gb/db_gb/ai_tokens）
     */
    String code;

    /**
     * 配额类型名称
     */
    String name;

    /**
     * 描述
     */
    String desc;

    /**
     * 单价
     */
    BigDecimal price;

    /**
     * 单位（count/GB/tokens等）
     */
    String unit;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用
     */
    Boolean state;

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
     * 软删
     */
    Boolean deleted;

}
