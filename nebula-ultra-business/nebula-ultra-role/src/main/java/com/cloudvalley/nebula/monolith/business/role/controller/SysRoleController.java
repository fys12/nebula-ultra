package com.cloudvalley.nebula.monolith.business.role.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.role.model.entity.SysRole;
import com.cloudvalley.nebula.monolith.business.role.model.rto.SysRoleRTO;
import com.cloudvalley.nebula.monolith.business.role.service.ISysRoleService;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.SysRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 分页查询系统角色列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 SysRoleVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getSysRoleList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysRole> page = new Page<>(current, size);
        IPage<SysRoleVO> result = sysRoleService.getSysRoleList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统角色数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统角色列表成功").setData(result);
    }

    /**
     * 根据多个ID分页批量查询系统角色
     * @param ids 系统角色ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 SysRoleVO 列表，表示匹配的系统角色记录
     */
    @GetMapping("/batch")
    public SaResult getSysRolesByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysRole> page = new Page<>(current, size);
        IPage<SysRoleVO> result = sysRoleService.getSysRolesByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统角色数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统角色信息成功").setData(result);
    }

    /**
     * 新增系统角色
     * @param sysRoleRTO 请求传输对象，包含角色编码、名称、描述等信息
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createSysRole(@RequestBody SysRoleRTO sysRoleRTO) {
        boolean result = sysRoleService.createSysRole(sysRoleRTO);
        if (result) {
            return SaResult.ok("创建系统角色成功");
        }
        return SaResult.error("创建系统角色失败");
    }

    /**
     * 更新系统角色信息
     * @param id 系统角色ID（路径参数）
     * @param sysRoleRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateSysRole(@PathVariable Long id, @RequestBody SysRoleRTO sysRoleRTO) {
        sysRoleRTO.setId(id);
        boolean result = sysRoleService.updateSysRole(sysRoleRTO);
        if (result) {
            return SaResult.ok("更新系统角色成功");
        }
        return SaResult.error("更新系统角色失败");
    }

    /**
     * 更新系统角色状态（启用/禁用）
     * @param id 系统角色ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateSysRoleState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysRoleService.updateSysRoleState(id, state);
        if (result) {
            return SaResult.ok("更新系统角色状态成功");
        }
        return SaResult.error("更新系统角色状态失败");
    }

    /**
     * 删除系统角色（物理删除）
     * @param id 系统角色ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysRole(@PathVariable Long id) {
        boolean result = sysRoleService.deleteSysRole(id);
        if (result) {
            return SaResult.ok("删除系统角色成功");
        }
        return SaResult.error("删除系统角色失败");
    }

    /**
     * 软删除系统角色（标记 deleted = true）
     * @param id 系统角色ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysRole(@PathVariable Long id) {
        boolean result = sysRoleService.softDeleteSysRole(id);
        if (result) {
            return SaResult.ok("软删除系统角色成功");
        }
        return SaResult.error("软删除系统角色失败");
    }

    /**
     * 批量删除多个系统角色（物理删除）
     * @param ids 系统角色ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysRoles(@RequestParam List<Long> ids) {
        boolean result = sysRoleService.batchDeleteSysRoles(ids);
        if (result) {
            return SaResult.ok("批量删除系统角色成功");
        }
        return SaResult.error("批量删除系统角色失败");
    }

    /**
     * 批量软删除多个系统角色（标记 deleted = true）
     * @param ids 系统角色ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysRoles(@RequestParam List<Long> ids) {
        boolean result = sysRoleService.batchSoftDeleteSysRoles(ids);
        if (result) {
            return SaResult.ok("批量软删除系统角色成功");
        }
        return SaResult.error("批量软删除系统角色失败");
    }

}
