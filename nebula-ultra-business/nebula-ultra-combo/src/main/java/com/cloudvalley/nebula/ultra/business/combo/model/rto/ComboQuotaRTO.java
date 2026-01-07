package com.cloudvalley.nebula.ultra.business.combo.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComboQuotaRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统套餐ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sComboId;

    /**
     * 系统配额ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sQuotaId;

    /**
     * 配额值
     */
    private Integer value;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 软删标识
     */
    private Boolean deleted;

}
