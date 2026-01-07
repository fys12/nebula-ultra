package com.cloudvalley.nebula.ultra.business.dept.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptListVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    /**
     * 用于前端树形结构的唯一标识，带层级后缀
     */
    String displayId;

    /**
     * 部门名称
     */
    String name;

    /**
     * 色标
     */
    String color;

    /**
     * 部门所绑定用户数量
     */
    Integer userCount;

    /**
     * 子部门
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<DeptListVO> childDept;

}
