package com.cloudvalley.nebula.ultra.business.tenant.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 租户
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 父租户ID
     */
    private Long parentId;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户描述
     */
    private String tDesc;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 负责人用户ID
     */
    private Long ownerUserId;

    /**
     * 许可证开始时间
     */
    private LocalDateTime licenseStartAt;

    /**
     * 许可证结束时间
     */
    private LocalDateTime licenseEndAt;

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
     * 色标
     */
    private String color;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Boolean state;

    /**
     * 软删
     */
    private Boolean deleted;


}
