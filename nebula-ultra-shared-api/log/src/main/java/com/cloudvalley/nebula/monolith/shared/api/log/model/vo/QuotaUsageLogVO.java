package com.cloudvalley.nebula.monolith.shared.api.log.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotaUsageLogVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 租户配额ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tQuotaId;

    /**
     * 使用场景
     */
    String scene;

    /**
     * 操作人用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long userId;

    /**
     * 使用时间
     */
    LocalDateTime usedAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 使用量
     */
    Integer amount;

    /**
     * 备注
     */
    String remark;

    /**
     * 软删
     */
    Boolean deleted;

}
