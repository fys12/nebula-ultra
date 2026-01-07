package com.cloudvalley.nebula.ultra.business.perm.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermRoleRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 租户权限ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tPermId;

    /**
     * 租户角色ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tRoleId;

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
     * 是否启用
     */
    private Boolean state;

    /**
     * 软删
     */
    private Boolean deleted;

}
