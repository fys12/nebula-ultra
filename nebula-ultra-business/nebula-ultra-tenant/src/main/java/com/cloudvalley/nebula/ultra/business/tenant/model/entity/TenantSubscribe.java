package com.cloudvalley.nebula.ultra.business.tenant.model.entity;

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
 * 租户订阅的套餐
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tenant_subscribe")
public class TenantSubscribe implements Serializable {

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
     * 系统套餐ID
     */
    private Long sComboId;

    /**
     * 生效时间
     */
    private LocalDateTime startAt;

    /**
     * 结束时间
     */
    private LocalDateTime endAt;

    /**
     * 备注
     */
    private String remark;

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
     * 状态：active/expired/cancelled/pending
     */
    private String state;

    /**
     * 软删
     */
    private Boolean deleted;


}
