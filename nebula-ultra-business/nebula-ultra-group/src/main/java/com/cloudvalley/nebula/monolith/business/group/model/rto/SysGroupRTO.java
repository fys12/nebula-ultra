package com.cloudvalley.nebula.monolith.business.group.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysGroupRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组描述
     */
    private String desc;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
