package com.cloudvalley.nebula.ultra.business.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;

public interface RoleAggregatorService {

    /**
     * 获取 角色列表
     * @param current 当前页
     * @param size 每页数量
     * @return 角色列表
     */
    IPage<SysRoleVO> getRoleList(Integer current, Integer size);

    /**
     * 获取 角色详情
     * @param id 角色Id
     * @return 角色详情
     */
    SysRoleVO getRoleDetail(Long id);
}
