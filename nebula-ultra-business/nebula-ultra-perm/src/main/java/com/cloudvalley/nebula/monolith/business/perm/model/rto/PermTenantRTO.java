package com.cloudvalley.nebula.monolith.business.perm.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermTenantRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统租户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sTenantId;

    /**
     * 系统权限ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sPermId;

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
