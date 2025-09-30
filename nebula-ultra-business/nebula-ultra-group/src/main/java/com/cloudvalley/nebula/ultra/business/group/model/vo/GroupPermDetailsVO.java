package com.cloudvalley.nebula.ultra.business.group.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupPermDetailsVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    /**
     * 系统组
     */
    SysGroupVO sysGroup;

    /**
     * 权限
     */
    SysPermVO sysPerm;

    /**
     * 组绑定的部门信息
     */
    List<SysDeptVO> groupBindDept;

    /**
     * 组绑定的角色信息
     */
    List<SysRoleVO> groupBindRole;

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
