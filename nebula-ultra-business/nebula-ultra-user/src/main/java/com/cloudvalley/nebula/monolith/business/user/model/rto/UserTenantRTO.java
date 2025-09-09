package com.cloudvalley.nebula.monolith.business.user.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTenantRTO {

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
     * 系统用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sUserId;

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
