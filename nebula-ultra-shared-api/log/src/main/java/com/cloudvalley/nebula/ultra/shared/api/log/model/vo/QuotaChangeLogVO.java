package com.cloudvalley.nebula.ultra.shared.api.log.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotaChangeLogVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 租户配额ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tQuotaId;

    /**
     * 变更前值
     */
    Integer oldValue;

    /**
     * 变更后值
     */
    Integer newValue;

    /**
     * 变更原因
     */
    String reason;

    /**
     * 申请人系统用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long applicantSUserId;

    /**
     * 申请时间
     */
    LocalDateTime appliedAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 审批人系统用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long approverSUserId;

    /**
     * 审批状态：pending/approved/rejected
     */
    String approvalStatus;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 备注
     */
    String remark;

    /**
     * 软删
     */
    Boolean deleted;

}
