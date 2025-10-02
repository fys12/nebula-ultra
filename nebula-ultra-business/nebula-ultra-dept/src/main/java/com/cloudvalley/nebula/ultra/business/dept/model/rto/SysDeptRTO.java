package com.cloudvalley.nebula.ultra.business.dept.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysDeptRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
    private String desc;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createdById;

    /**
     * 更新人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
