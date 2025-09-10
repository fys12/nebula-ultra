package com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantSubscribeVO {

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
     * 系统套餐ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sComboId;

    /**
     * 生效时间
     */
    LocalDateTime startAt;

    /**
     * 结束时间
     */
    LocalDateTime endAt;

    /**
     * 备注
     */
    String remark;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 创建人用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long createdById;

    /**
     * 状态：active/expired/cancelled/pending
     */
    String status;

    /**
     * 软删
     */
    Boolean deleted;

}
