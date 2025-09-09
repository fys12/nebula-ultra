package com.cloudvalley.nebula.monolith.business.quota.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.SysQuota;
import com.cloudvalley.nebula.monolith.business.quota.model.rto.SysQuotaRTO;
import com.cloudvalley.nebula.monolith.business.quota.service.ISysQuotaService;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.SysQuotaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统配额 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-quota")
public class SysQuotaController {

    @Autowired
    private ISysQuotaService sysQuotaService;

    /**
     * 分页查询系统配额列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 SysQuotaVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getSysQuotaList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysQuota> page = new Page<>(current, size);
        IPage<SysQuotaVO> result = sysQuotaService.getSysQuotaList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统配额数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统配额列表成功").setData(result);
    }

    /**
     * 根据多个ID分页批量查询系统配额
     * @param ids 系统配额ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 SysQuotaVO 列表，表示匹配的配额定义；ids为空时返回空分页
     */
    @GetMapping("/batch")
    public SaResult getSysQuotasByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysQuota> page = new Page<>(current, size);
        IPage<SysQuotaVO> result = sysQuotaService.getSysQuotasByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统配额数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统配额信息成功").setData(result);
    }

    /**
     * 新增系统配额（定义配额类型，如存储、AI tokens）
     * @param sysQuotaRTO 请求传输对象，包含配额编码、名称、单位、描述等信息
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createSysQuota(@RequestBody SysQuotaRTO sysQuotaRTO) {
        boolean result = sysQuotaService.createSysQuota(sysQuotaRTO);
        if (result) {
            return SaResult.ok("创建系统配额成功");
        }
        return SaResult.error("创建系统配额失败");
    }

    /**
     * 更新系统配额信息（如名称、单位、描述）
     * @param id 系统配额ID（路径参数）
     * @param sysQuotaRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateSysQuota(@PathVariable Long id, @RequestBody SysQuotaRTO sysQuotaRTO) {
        sysQuotaRTO.setId(id);
        boolean result = sysQuotaService.updateSysQuota(sysQuotaRTO);
        if (result) {
            return SaResult.ok("更新系统配额成功");
        }
        return SaResult.error("更新系统配额失败");
    }

    /**
     * 更新系统配额状态（启用/禁用）
     * @param id 系统配额ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateSysQuotaState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysQuotaService.updateSysQuotaState(id, state);
        if (result) {
            return SaResult.ok("更新系统配额状态成功");
        }
        return SaResult.error("更新系统配额状态失败");
    }

    /**
     * 删除系统配额（物理删除）
     * @param id 系统配额ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysQuota(@PathVariable Long id) {
        boolean result = sysQuotaService.deleteSysQuota(id);
        if (result) {
            return SaResult.ok("删除系统配额成功");
        }
        return SaResult.error("删除系统配额失败");
    }

    /**
     * 软删除系统配额（标记 deleted = true）
     * @param id 系统配额ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysQuota(@PathVariable Long id) {
        boolean result = sysQuotaService.softDeleteSysQuota(id);
        if (result) {
            return SaResult.ok("软删除系统配额成功");
        }
        return SaResult.error("软删除系统配额失败");
    }

    /**
     * 批量删除多个系统配额（物理删除）
     * @param ids 系统配额ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysQuotas(@RequestParam List<Long> ids) {
        boolean result = sysQuotaService.batchDeleteSysQuotas(ids);
        if (result) {
            return SaResult.ok("批量删除系统配额成功");
        }
        return SaResult.error("批量删除系统配额失败");
    }

    /**
     * 批量软删除多个系统配额（标记 deleted = true）
     * @param ids 系统配额ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysQuotas(@RequestParam List<Long> ids) {
        boolean result = sysQuotaService.batchSoftDeleteSysQuotas(ids);
        if (result) {
            return SaResult.ok("批量软删除系统配额成功");
        }
        return SaResult.error("批量软删除系统配额失败");
    }

}
