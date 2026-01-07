package com.cloudvalley.nebula.ultra.business.combo.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboDetailsVO {

    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    /**
     * 套餐名称
     */
    String name;

    /**
     * 套餐描述
     */
    String desc;

    /**
     * 价格
     */
    BigDecimal price;

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
    SysUserVO createdByUser;

    /**
     * 色标
     */
    String color;

    /**
     * 软删标识
     */
    Boolean deleted;

    /**
     * 套餐包含的权限
     */
    List<SysPermVO> perms;

    /**
     * 套餐包含的角色
     */
    List<SysRoleVO> roles;

    /**
     * 套餐包含的配额
     */
    List<QuotaDetail> quotas;

    @Data
    public static class QuotaDetail {
        /**
         * 主键ID（雪花算法ID）
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Long id;

        /**
         * 配额类型名称
         */
        String name;

        /**
         * 配额类型编码（如 root_cap/sub_cap/user_cap/storage_gb/db_gb/ai_tokens）
         */
        String code;

        /**
         * 描述
         */
        String desc;

        /**
         * 单价
         */
        BigDecimal price;

        /**
         * 单位（count/GB/tokens等）
         */
        String unit;

        /**
         * 色标
         */
        String color;

        /**
         * 配额 可用量
         */
        Integer value;
    }

}
