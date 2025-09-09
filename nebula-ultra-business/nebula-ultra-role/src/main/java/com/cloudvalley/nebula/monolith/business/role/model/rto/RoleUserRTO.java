package com.cloudvalley.nebula.monolith.business.role.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleUserRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 租户用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tUserId;

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
     * 创建人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createdById;

    /**
     * 是否启用
     */
    private Boolean state;

    /**
     * 软删
     */
    private Boolean deleted;

}
