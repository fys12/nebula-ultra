package com.cloudvalley.nebula.ultra.business.tenant.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantDetailsVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 父租户
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String parentTenant;

    /**
     * 租户名称
     */
    String name;

    /**
     * 租户描述
     */
    String desc;

    /**
     * 联系电话
     */
    String phone;

    /**
     * 联系地址
     */
    String address;

    /**
     * 负责人用户
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String ownerUser;

    /**
     * 许可证开始时间
     */
    LocalDateTime licenseStartAt;

    /**
     * 许可证结束时间
     */
    LocalDateTime licenseEndAt;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 创建人用户
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String createdByUser;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用：0-禁用，1-启用
     */
    Boolean state;

    /**
     * 子租户
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<TenantDetailsVO> childTenant;

}
