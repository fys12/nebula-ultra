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
 * 组-租户部门绑定
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_bind_dept")
public class GroupBindDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法ID）
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 系统组ID
     */
    private Long sGroupId;

    /**
     * 租户部门ID
     */
    private Long tDeptId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 软删
     */
    private Boolean deleted;


}
