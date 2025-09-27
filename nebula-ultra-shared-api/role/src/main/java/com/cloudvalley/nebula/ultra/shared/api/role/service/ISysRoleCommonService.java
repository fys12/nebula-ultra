package com.cloudvalley.nebula.ultra.shared.api.role.service;

import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;

import java.util.List;
import java.util.Map;

public interface ISysRoleCommonService {

    /**
     * 根据id查询系统角色
     * @param id 系统角色ID
     * @return 系统角色信息
     */
    SysRoleVO getSysRoleById(Long id);

    /**
     * 根据多个系统角色ID全量查询角色信息（不分页）
     * @param ids 系统角色ID列表
     * @return 所有匹配的 SysRoleVO 列表
     */
    List<SysRoleVO> getSysRolesByIds(List<Long> ids);

}
