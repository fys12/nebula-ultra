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
 * 套餐变更日志
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("combo_change_log")
public class ComboChangeLog implements Serializable {

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
     * 旧系统套餐ID
     */
    private Long oldSComboId;

    /**
     * 新系统套餐ID
     */
    private Long newSComboId;

    /**
     * 变更时间
     */
    private LocalDateTime changedAt;

    /**
     * 操作人用户ID
     */
    private Long operatorUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 软删
     */
    private Boolean deleted;


}
