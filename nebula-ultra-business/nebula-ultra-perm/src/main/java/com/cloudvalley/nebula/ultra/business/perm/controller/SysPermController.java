package com.cloudvalley.nebula.ultra.business.perm.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.SysPerm;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.SysPermRTO;
import com.cloudvalley.nebula.ultra.business.perm.service.ISysPermService;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-perm")
public class SysPermController {

    @Autowired
    private ISysPermService sysPermService;

    /**
     * 分页查询系统权限列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 SysPermVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getSysPermList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysPerm> page = new Page<>(current, size);
        IPage<SysPermVO> result = sysPermService.getSysPermList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统权限数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统权限列表成功").setData(result);
    }

    /**
     * 根据多个ID分页批量查询系统权限
     * @param ids 系统权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 SysPermVO 列表，按创建时间倒序排列；ids为空时返回空分页
     */
    @GetMapping("/batch")
    public SaResult getSysPermsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysPerm> page = new Page<>(current, size);
        IPage<SysPermVO> result = sysPermService.getSysPermsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统权限数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统权限信息成功").setData(result);
    }

    /**
     * 新增系统权限
     * @param sysPermRTO 请求传输对象，包含权限编码、名称、描述等信息
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createSysPerm(@RequestBody SysPermRTO sysPermRTO) {
        boolean result = sysPermService.createSysPerm(sysPermRTO);
        if (result) {
            return SaResult.ok("创建系统权限成功");
        }
        return SaResult.error("创建系统权限失败");
    }

    /**
     * 更新系统权限信息
     * @param id 系统权限ID（路径参数）
     * @param sysPermRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateSysPerm(@PathVariable Long id, @RequestBody SysPermRTO sysPermRTO) {
        sysPermRTO.setId(id);
        boolean result = sysPermService.updateSysPerm(sysPermRTO);
        if (result) {
            return SaResult.ok("更新系统权限成功");
        }
        return SaResult.error("更新系统权限失败");
    }

    /**
     * 更新系统权限状态（启用/禁用）
     * @param id 系统权限ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateSysPermState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysPermService.updateSysPermState(id, state);
        if (result) {
            return SaResult.ok("更新系统权限状态成功");
        }
        return SaResult.error("更新系统权限状态失败");
    }

    /**
     * 删除系统权限（物理删除）
     * @param id 系统权限ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysPerm(@PathVariable Long id) {
        boolean result = sysPermService.deleteSysPerm(id);
        if (result) {
            return SaResult.ok("删除系统权限成功");
        }
        return SaResult.error("删除系统权限失败");
    }

    /**
     * 软删除系统权限（标记 deleted = true）
     * @param id 系统权限ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysPerm(@PathVariable Long id) {
        boolean result = sysPermService.softDeleteSysPerm(id);
        if (result) {
            return SaResult.ok("软删除系统权限成功");
        }
        return SaResult.error("软删除系统权限失败");
    }

    /**
     * 批量删除多个系统权限（物理删除）
     * @param ids 系统权限ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysPerms(@RequestParam List<Long> ids) {
        boolean result = sysPermService.batchDeleteSysPerms(ids);
        if (result) {
            return SaResult.ok("批量删除系统权限成功");
        }
        return SaResult.error("批量删除系统权限失败");
    }

    /**
     * 批量软删除多个系统权限（标记 deleted = true）
     * @param ids 系统权限ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysPerms(@RequestParam List<Long> ids) {
        boolean result = sysPermService.batchSoftDeleteSysPerms(ids);
        if (result) {
            return SaResult.ok("批量软删除系统权限成功");
        }
        return SaResult.error("批量软删除系统权限失败");
    }

}
