package com.cloudvalley.nebula.ultra.business.combo.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SysComboRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐描述
     */
    private String desc;

    /**
     * 价格/周期
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createdById;

    /**
     * 软删
     */
    private Boolean deleted;

}
