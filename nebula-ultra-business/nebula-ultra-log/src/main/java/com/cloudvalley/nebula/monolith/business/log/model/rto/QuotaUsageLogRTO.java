package com.cloudvalley.nebula.monolith.business.log.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotaUsageLogRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 租户配额ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tQuotaId;

    /**
     * 使用场景
     */
    private String scene;

    /**
     * 操作人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 使用时间
     */
    private LocalDateTime usedAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 使用量
     */
    private Integer amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 软删
     */
    private Boolean deleted;

}
