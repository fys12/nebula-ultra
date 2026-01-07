package com.cloudvalley.nebula.ultra.business.quota.model.entity;

import java.math.BigDecimal;
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
 * 系统配额
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_quota")
public class SysQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
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
    private String qDesc;

    /**
     * 单位（count/GB/tokens等）
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

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
    private Long createdById;

    /**
     * 软删
     */
    private Boolean deleted;


}
