package com.cloudvalley.nebula.ultra.business.tenant.model.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysTenantRTO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 父租户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户描述
     */
    private String desc;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 负责人用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ownerUserId;

    /**
     * 许可证开始时间
     */
    private LocalDateTime licenseStartAt;

    /**
     * 许可证结束时间
     */
    private LocalDateTime licenseEndAt;

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
     * 色标
     */
    private String color;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Boolean state;

    /**
     * 软删
     */
    private Boolean deleted;

}
