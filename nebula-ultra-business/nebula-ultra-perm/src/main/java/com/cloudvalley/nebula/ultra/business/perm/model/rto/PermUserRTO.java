package com.cloudvalley.nebula.ultra.business.perm.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermUserRTO {

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
     * 状态
     */
    private Boolean status;

    /**
     * 软删
     */
    private Boolean deleted;

}
