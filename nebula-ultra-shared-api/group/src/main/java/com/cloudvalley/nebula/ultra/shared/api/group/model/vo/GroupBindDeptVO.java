package com.cloudvalley.nebula.ultra.shared.api.group.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupBindDeptVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;

    /**
     * 租户组ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tGroupId;

    /**
     * 租户部门ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long tDeptId;

    /**
     * 创建时间
     */
    LocalDateTime createdAt;

    /**
     * 更新时间
     */
    LocalDateTime updatedAt;

    /**
     * 软删
     */
    Boolean deleted;

}
