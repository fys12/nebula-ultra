package com.cloudvalley.nebula.monolith.business.log.model.entity;

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
 * 配额使用流水
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quota_usage_log")
public class QuotaUsageLog implements Serializable {

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
     * 使用场景
     */
    private String scene;

    /**
     * 操作人用户ID
     */
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
