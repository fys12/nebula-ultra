package com.cloudvalley.nebula.ultra.business.role.converter;

import com.cloudvalley.nebula.ultra.business.role.model.entity.SysRole;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    /**
     * 将 SysRole 实体转换为 SysRoleVO 视图对象
     * @param sysRole 源实体对象
     * @return 转换后的 VO 对象
     */
    SysRoleVO EnToVO(SysRole sysRole);

    /**
     * 将 SysRole 实体列表批量转换为 VO 列表
     * @param sysRoleList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<SysRoleVO> EnListToVOList(List<SysRole> sysRoleList);

}
