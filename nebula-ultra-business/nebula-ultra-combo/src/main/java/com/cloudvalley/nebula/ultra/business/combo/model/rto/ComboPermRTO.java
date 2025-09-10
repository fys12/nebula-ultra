package com.cloudvalley.nebula.ultra.business.combo.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComboPermRTO {

    /**
     * 主键ID（采用雪花算法生成的全局唯一标识，JSON序列化时转为字符串类型避免精度丢失）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 系统套餐ID（关联系统套餐表的主键，JSON序列化时转为字符串类型避免精度丢失）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sComboId;

    /**
     * 系统权限ID（关联系统权限表的主键，JSON序列化时转为字符串类型避免精度丢失）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sPermId;

    /**
     * 记录创建时间（数据插入时自动生成，格式为标准时间戳）
     */
    private LocalDateTime createdAt;

    /**
     * 记录更新时间（数据更新时自动刷新，格式为标准时间戳）
     */
    private LocalDateTime updatedAt;

    /**
     * 软删除标识（逻辑删除字段，true表示已删除，false表示正常状态）
     */
    private Boolean deleted;

}
