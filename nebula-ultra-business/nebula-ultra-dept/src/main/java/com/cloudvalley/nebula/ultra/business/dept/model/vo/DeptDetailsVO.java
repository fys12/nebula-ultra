package com.cloudvalley.nebula.ultra.business.dept.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptDetailsVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    /**
     * 部门名称
     */
    String name;

    /**
     * 部门描述
     */
    String desc;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 创建人用户
     */
    String createdByUser;

    /**
     * 更新人用户ID
     */
    String updatedByUser;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 软删
     */
    Boolean deleted;
    
}
