package com.cloudvalley.nebula.ultra.shared.api.combo.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboPermVO {

    /**
     * 主键ID（采用雪花算法生成，全局唯一）
     */
    Long id;

    /**
     * 系统套餐ID（关联系统套餐表的主键）
     */
    Long sComboId;

    /**
     * 系统权限ID（关联系统权限表的主键）
     */
    Long sPermId;

    /**
     * 记录创建时间（自动填充，无需手动设置）
     */
    LocalDateTime createdAt;

    /**
     * 记录更新时间（自动更新，无需手动设置）
     */
    LocalDateTime updatedAt;

    /**
     * 软删除标识（true-已删除，false-未删除；逻辑删除字段）
     */
    Boolean deleted;

}
