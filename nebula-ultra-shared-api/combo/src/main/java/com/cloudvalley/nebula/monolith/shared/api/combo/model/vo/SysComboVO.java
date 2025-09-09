package com.cloudvalley.nebula.monolith.shared.api.combo.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysComboVO {

    /**
     * 主键ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 套餐名称
     */
    String name;

    /**
     * 套餐描述
     */
    String desc;

    /**
     * 价格
     */
    BigDecimal price;

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
     * 软删标识
     */
    Boolean deleted;

}
