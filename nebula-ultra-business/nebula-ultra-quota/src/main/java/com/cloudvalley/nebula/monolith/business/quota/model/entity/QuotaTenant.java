package com.cloudvalley.nebula.monolith.business.quota.model.entity;

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
 * 租户配额总览
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quota_tenant")
public class QuotaTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 系统租户ID
     */
    private Long sTenantId;

    /**
     * 系统配额ID
     */
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
