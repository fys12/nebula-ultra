package com.cloudvalley.nebula.monolith.business.quota.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysQuotaRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 配额类型编码（如 root_cap/sub_cap/user_cap/storage_gb/db_gb/ai_tokens）
     */
    private String code;

    /**
     * 配额类型名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 单位（count/GB/tokens等）
     */
    private String unit;

    /**
     * 色标
     */
    private String color;

    /**
     * 是否启用
     */
    private Boolean state;

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
