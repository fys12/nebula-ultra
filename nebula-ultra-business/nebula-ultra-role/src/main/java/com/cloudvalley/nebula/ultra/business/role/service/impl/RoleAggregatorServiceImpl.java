package com.cloudvalley.nebula.ultra.business.role.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.role.service.ISysRoleService;
import com.cloudvalley.nebula.ultra.business.role.service.RoleAggregatorService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleAggregatorServiceImpl implements RoleAggregatorService {

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysRoleCommonService iSysRoleCommonService;

    /**
     * 获取 角色列表
     * @param current 当前页
     * @param size 每页数量
     * @return 角色列表
     */
    @Override
    public IPage<SysRoleVO> getRoleList(Integer current, Integer size) {
        // 1. 查询 角色 基本信息
        IPage<SysRoleVO> sysRoleList = iSysRoleService.getSysRoleList(new Page<>(current, size));

        return sysRoleList;
    }

    /**
     * 获取 角色详情
     * @param id 角色Id
     * @return 角色详情
     */
    @Override
    public SysRoleVO getRoleDetail(Long id) {
        // 1. 根据角色Id 查询 角色基本信息

        // 2. 根据角色Id 获取 角色权限信息

        return null;
    }
}
