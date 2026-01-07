package com.cloudvalley.nebula.ultra.business.group.model.entity;

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
 * 组
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_group")
public class SysGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组描述
     */
    private String gDesc;

    /**
     * 组类型
     */
    private String groupType;

    /**
     * 组级别
     */
    private String groupLevel;

    /**
     * 创建者用户ID
     */
    private Long creatorById;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 色标
     */
    private String color;

    /**
     * 软删
     */
    private Boolean deleted;


}
