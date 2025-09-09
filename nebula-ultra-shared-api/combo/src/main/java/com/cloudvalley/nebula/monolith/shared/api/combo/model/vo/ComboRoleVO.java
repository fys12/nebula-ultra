package com.cloudvalley.nebula.monolith.shared.api.combo.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboRoleVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 系统套餐ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sComboId;

    /**
     * 系统角色ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long sRoleId;

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
