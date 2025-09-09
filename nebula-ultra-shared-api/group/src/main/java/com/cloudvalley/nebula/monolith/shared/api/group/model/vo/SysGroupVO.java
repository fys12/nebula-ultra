package com.cloudvalley.nebula.monolith.shared.api.group.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysGroupVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 组名称
     */
    String name;

    /**
     * 组描述
     */
    String desc;

    /**
     * 组类型
     */
    String groupType;

    /**
     * 组级别
     */
    String groupLevel;

    /**
     * 创建者用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long creatorById;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 色标
     */
    String color;

    /**
     * 软删
     */
    Boolean deleted;

}
