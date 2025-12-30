package com.cloudvalley.nebula.ultra.business.role.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.role.service.RoleAggregatorService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roleAggregator")
public class RoleAggregatorController {

    @Autowired
    private RoleAggregatorService roleAggregatorService;

    /**
     * 获取 角色列表
     * @param current 当前页
     * @param size 每页数量
     * @return 角色列表
     */
    @GetMapping("/{current}/{size}")
    public SaResult getRoleList(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<SysRoleVO> sysRoleVO = roleAggregatorService.getRoleList(current, size);
        return SaResult.ok("角色信息列表获取成功").setData(sysRoleVO);
    }

    /**
     * 获取 角色详情
     * @param id 角色Id
     * @return 角色详情
     */
    @GetMapping("/{id}")
    public SaResult getRoleDetail(@PathVariable Long id) {
        if (id == null) {
            return SaResult.error("参数缺失");
        }
        SysRoleVO sysRoleVO = roleAggregatorService.getRoleDetail(id);
        return SaResult.ok("角色详情信息获取成功").setData(sysRoleVO);
    }

}
