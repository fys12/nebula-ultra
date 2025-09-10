package com.cloudvalley.nebula.ultra.business.quota.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotaTenantRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统租户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sTenantId;

    /**
     * 系统配额ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sQuotaId;

    /**
     * 总配额
     */
    private Integer total;

    /**
     * 已使用
     */
    private Integer used;

    /**
     * 剩余配额
     */
    private Integer remain;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最近更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 软删
     */
    private Boolean deleted;

}
