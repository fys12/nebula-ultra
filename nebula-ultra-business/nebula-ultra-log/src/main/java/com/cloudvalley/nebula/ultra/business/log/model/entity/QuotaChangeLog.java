package com.cloudvalley.nebula.ultra.business.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配额变更日志
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quota_change_log")
public class QuotaChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 租户配额ID
     */
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
