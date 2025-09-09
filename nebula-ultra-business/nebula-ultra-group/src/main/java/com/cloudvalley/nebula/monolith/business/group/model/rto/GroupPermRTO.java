package com.cloudvalley.nebula.monolith.business.group.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupPermRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统组ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sGroupId;

    /**
     * 租户权限ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tPermId;

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
