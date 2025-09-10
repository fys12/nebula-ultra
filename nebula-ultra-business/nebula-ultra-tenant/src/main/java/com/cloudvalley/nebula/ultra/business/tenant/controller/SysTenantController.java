package com.cloudvalley.nebula.ultra.business.tenant.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.cloudvalley.nebula.ultra.business.tenant.model.rto.SysTenantRTO;
import com.cloudvalley.nebula.ultra.business.tenant.service.ISysTenantService;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-tenant")
public class SysTenantController {

    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 分页查询系统租户列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 SysTenantVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getSysTenantList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysTenant> page = new Page<>(current, size);
        IPage<SysTenantVO> result = sysTenantService.getSysTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统租户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统租户列表成功").setData(result);
    }

    /**
     * 根据多个ID分页批量查询系统租户
     * @param ids 系统租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 SysTenantVO 列表，表示匹配的系统租户记录
     */
    @GetMapping("/batch")
    public SaResult getSysTenantsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysTenant> page = new Page<>(current, size);
        IPage<SysTenantVO> result = sysTenantService.getSysTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统租户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统租户信息成功").setData(result);
    }

    /**
     * 新增系统租户
     * @param sysTenantRTO 请求传输对象，包含租户编码、名称、联系人、联系方式等信息
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createSysTenant(@RequestBody SysTenantRTO sysTenantRTO) {
        boolean result = sysTenantService.createSysTenant(sysTenantRTO);
        if (result) {
            return SaResult.ok("创建系统租户成功");
        }
        return SaResult.error("创建系统租户失败");
    }

    /**
     * 更新系统租户信息
     * @param id 系统租户ID（路径参数）
     * @param sysTenantRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateSysTenant(@PathVariable Long id, @RequestBody SysTenantRTO sysTenantRTO) {
        sysTenantRTO.setId(id);
        boolean result = sysTenantService.updateSysTenant(sysTenantRTO);
        if (result) {
            return SaResult.ok("更新系统租户成功");
        }
        return SaResult.error("更新系统租户失败");
    }

    /**
     * 更新系统租户状态（启用/禁用）
     * @param id 系统租户ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateSysTenantState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysTenantService.updateSysTenantState(id, state);
        if (result) {
            return SaResult.ok("更新系统租户状态成功");
        }
        return SaResult.error("更新系统租户状态失败");
    }

    /**
     * 删除系统租户（物理删除）
     * @param id 系统租户ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysTenant(@PathVariable Long id) {
        boolean result = sysTenantService.deleteSysTenant(id);
        if (result) {
            return SaResult.ok("删除系统租户成功");
        }
        return SaResult.error("删除系统租户失败");
    }

    /**
     * 软删除系统租户（标记 deleted = true）
     * @param id 系统租户ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysTenant(@PathVariable Long id) {
        boolean result = sysTenantService.softDeleteSysTenant(id);
        if (result) {
            return SaResult.ok("软删除系统租户成功");
        }
        return SaResult.error("软删除系统租户失败");
    }

    /**
     * 批量删除多个系统租户（物理删除）
     * @param ids 系统租户ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysTenants(@RequestParam List<Long> ids) {
        boolean result = sysTenantService.batchDeleteSysTenants(ids);
        if (result) {
            return SaResult.ok("批量删除系统租户成功");
        }
        return SaResult.error("批量删除系统租户失败");
    }

    /**
     * 批量软删除多个系统租户（标记 deleted = true）
     * @param ids 系统租户ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysTenants(@RequestParam List<Long> ids) {
        boolean result = sysTenantService.batchSoftDeleteSysTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除系统租户成功");
        }
        return SaResult.error("批量软删除系统租户失败");
    }

}
