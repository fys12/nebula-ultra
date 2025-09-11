package com.cloudvalley.nebula.ultra.shared.api.dept.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * 级联禁用 [ system tenant ]
     * system -因系统禁用而级联禁用 tenant -因租户禁用而级联禁用
     * 如果 该部门没有级联禁用 则为 null
     */
    String cascadeDisable;

    /**
     * 软删状态
     */
    Boolean deleted;

}
