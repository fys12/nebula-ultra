package com.cloudvalley.nebula.ultra.business.log.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotaChangeLogRTO {

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
     * 变更前值
     */
    private Integer oldValue;

    /**
     * 变更后值
     */
    private Integer newValue;

    /**
     * 变更原因
     */
    private String reason;

    /**
     * 申请人系统用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long applicantSUserId;

    /**
     * 申请时间
     */
    private LocalDateTime appliedAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 审批人系统用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long approverSUserId;

    /**
     * 审批状态：pending/approved/rejected
     */
    private String approvalStatus;

    /**
     * 是否启用
     */
    private Boolean state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 软删
     */
    private Boolean deleted;

}
