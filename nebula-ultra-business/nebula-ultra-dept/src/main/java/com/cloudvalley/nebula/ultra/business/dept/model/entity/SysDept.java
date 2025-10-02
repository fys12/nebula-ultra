package com.cloudvalley.nebula.ultra.business.dept.model.entity;

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
 * 部门
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 父部门ID
     */
    Long parentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门描述
     */
    private String dDesc;

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
     * 更新人用户ID
     */
    private Long updatedById;

    /**
     * 色标
     */
    private String color;

    /**
     * 是否启用
     */
    private Boolean state;

    /**
     * 软删
     */
    private Boolean deleted;


}
