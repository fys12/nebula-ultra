package com.cloudvalley.nebula.ultra.shared.api.combo.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboQuotaVO {

    /**
     * 主键ID（雪花算法ID）
     */
    Long id;

    /**
     * 系统套餐ID
     */
    Long sComboId;

    /**
     * 系统配额ID
     */
    Long sQuotaId;

    /**
     * 配额值
     */
    Integer value;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 软删标识（true表示已删除，false表示未删除）
     */
    Boolean deleted;

}
