package com.cloudvalley.nebula.ultra.business.combo.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComboRoleRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统套餐ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sComboId;

    /**
     * 系统角色ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sRoleId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 软删
     */
    private Boolean deleted;

}
