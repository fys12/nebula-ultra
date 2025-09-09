package com.cloudvalley.nebula.monolith.shared.api.dept.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDeptVO {

    /**
     * 主键ID（雪花算法ID，null不返回）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
     * 创建人用户ID（null不返回）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long createdById;

    /**
     * 更新人用户ID（null不返回）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long updatedById;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 软删状态
     */
    Boolean deleted;

}
