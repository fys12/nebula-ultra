package com.cloudvalley.nebula.monolith.business.combo.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.SysCombo;
import com.cloudvalley.nebula.monolith.business.combo.model.rto.SysComboRTO;
import com.cloudvalley.nebula.monolith.business.combo.service.ISysComboService;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.SysComboVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-combo")
public class SysComboController {

    @Autowired
    private ISysComboService sysComboService;

    /**
     * 查询系统套餐列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 系统套餐列表
     */
    @GetMapping
    public SaResult getSysComboList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysCombo> page = new Page<>(current, size);
        IPage<SysComboVO> result = sysComboService.getSysComboList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统套餐数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统套餐列表成功").setData(result);
    }

    /**
     * 根据ID批量查询系统套餐 [分页]
     * @param ids 套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 系统套餐分页结果
     */
    @GetMapping("/batch")
    public SaResult getSysCombosByIds(@RequestParam List<Long> ids,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysCombo> page = new Page<>(current, size);
        IPage<SysComboVO> result = sysComboService.getSysCombosByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统套餐数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统套餐信息成功").setData(result);
    }

    /**
     * 新增系统套餐
     * @param sysComboRTO 系统套餐信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createSysCombo(@RequestBody SysComboRTO sysComboRTO) {
        boolean result = sysComboService.createSysCombo(sysComboRTO);
        if (result) {
            return SaResult.ok("创建系统套餐成功");
        }
        return SaResult.error("创建系统套餐失败");
    }

    /**
     * 更新系统套餐
     * @param id 套餐ID
     * @param sysComboRTO 系统套餐信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateSysCombo(@PathVariable Long id, @RequestBody SysComboRTO sysComboRTO) {
        sysComboRTO.setId(id);
        boolean result = sysComboService.updateSysCombo(sysComboRTO);
        if (result) {
            return SaResult.ok("更新系统套餐成功");
        }
        return SaResult.error("更新系统套餐失败");
    }

    /**
     * 删除系统套餐（真删）
     * @param id 套餐ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysCombo(@PathVariable Long id) {
        boolean result = sysComboService.deleteSysCombo(id);
        if (result) {
            return SaResult.ok("删除系统套餐成功");
        }
        return SaResult.error("删除系统套餐失败");
    }

    /**
     * 软删除系统套餐
     * @param id 套餐ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysCombo(@PathVariable Long id) {
        boolean result = sysComboService.softDeleteSysCombo(id);
        if (result) {
            return SaResult.ok("软删除系统套餐成功");
        }
        return SaResult.error("软删除系统套餐失败");
    }

    /**
     * 批量删除系统套餐（真删）
     * @param ids 套餐ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysCombos(@RequestParam List<Long> ids) {
        boolean result = sysComboService.batchDeleteSysCombos(ids);
        if (result) {
            return SaResult.ok("批量删除系统套餐成功");
        }
        return SaResult.error("批量删除系统套餐失败");
    }

    /**
     * 批量软删除系统套餐
     * @param ids 套餐ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysCombos(@RequestParam List<Long> ids) {
        boolean result = sysComboService.batchSoftDeleteSysCombos(ids);
        if (result) {
            return SaResult.ok("批量软删除系统套餐成功");
        }
        return SaResult.error("批量软删除系统套餐失败");
    }

}
