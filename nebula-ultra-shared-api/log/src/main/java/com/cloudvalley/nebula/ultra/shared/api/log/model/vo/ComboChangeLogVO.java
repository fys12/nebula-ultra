package com.cloudvalley.nebula.ultra.shared.api.log.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboChangeLogVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 系统租户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sTenantId;

    /**
     * 旧系统套餐ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long oldSComboId;

    /**
     * 新系统套餐ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long newSComboId;

    /**
     * 变更时间
     */
    LocalDateTime changedAt;

    /**
     * 操作人用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long operatorUserId;

    /**
     * 备注
     */
    String remark;

    /**
     * 软删
     */
    Boolean deleted;

}
