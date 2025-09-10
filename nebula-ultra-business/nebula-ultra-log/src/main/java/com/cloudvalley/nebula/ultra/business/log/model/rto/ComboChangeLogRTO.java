package com.cloudvalley.nebula.ultra.business.log.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComboChangeLogRTO {

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
     * 旧系统套餐ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long oldSComboId;

    /**
     * 新系统套餐ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long newSComboId;

    /**
     * 变更时间
     */
    private LocalDateTime changedAt;

    /**
     * 操作人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operatorUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 软删
     */
    private Boolean deleted;

}
