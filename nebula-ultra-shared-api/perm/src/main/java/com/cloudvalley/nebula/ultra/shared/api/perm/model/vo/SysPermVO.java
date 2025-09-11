package com.cloudvalley.nebula.ultra.shared.api.perm.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPermVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 权限名称
     */
    String name;

    /**
     * 权限别名
     */
    String alias;

    /**
     * 权限描述
     */
    String desc;

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
     * 更新人用户ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long updatedById;

    /**
     * 色标
     */
    String color;

    /**
     * 是否启用
     */
    Boolean state;

    /**
     * 级联禁用 [ system tenant ]
     * system -因系统级禁用而级联禁用 tenant -因租户级禁用而级联禁用 dept -因部门级禁用而级联禁用 role -因角色级禁用而级联禁用
     * 如果 该权限没有级联禁用 则为 null
     */
    String cascadeDisable;

    /**
     * 软删
     */
    Boolean deleted;

}
