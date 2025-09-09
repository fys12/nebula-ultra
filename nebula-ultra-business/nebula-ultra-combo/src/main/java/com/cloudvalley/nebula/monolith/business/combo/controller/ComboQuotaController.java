package com.cloudvalley.nebula.monolith.business.combo.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboQuota;
import com.cloudvalley.nebula.monolith.business.combo.model.rto.ComboQuotaRTO;
import com.cloudvalley.nebula.monolith.business.combo.service.IComboQuotaService;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboQuotaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐配额配置 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/combo-quota")
public class ComboQuotaController {

    @Autowired
    private IComboQuotaService comboQuotaService;

    /**
     * 查询套餐配额配置列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐配额配置列表
     */
    @GetMapping
    public SaResult getComboQuotaList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<ComboQuotaVO> result = comboQuotaService.getComboQuotaList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐配额配置数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐配额配置列表成功").setData(result);
    }

    /**
     * 根据 id 批量查询套餐配额配置 [分页]
     * @param ids 配额配置ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/batch")
    public SaResult getComboQuotasByIds(@RequestParam List<Long> ids,
                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<ComboQuotaVO> result = comboQuotaService.getComboQuotasByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐配额配置数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐配额配置信息成功").setData(result);
    }

    /**
     * 根据系统套餐 id 查询配额配置 [分页] - 查看某套餐的配额配置
     * @param sComboId 系统套餐ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额配置列表
     */
    @GetMapping("/by-scombo/{sComboId}")
    public SaResult getComboQuotasBySComboId(@PathVariable Long sComboId,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<ComboQuotaVO> result = comboQuotaService.getComboQuotasBySComboId(sComboId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该套餐暂无配额配置").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐配额配置列表成功").setData(result);
    }

    /**
     * 根据系统套餐 id 批量查询配额配置 [分页] - 返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-scombos")
    public SaResult getComboQuotasBySComboIds(@RequestParam List<Long> sComboIds,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboQuotaVO>>> result = comboQuotaService.getComboQuotasBySComboIds(sComboIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些套餐暂无配额配置").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐配额配置列表成功").setData(result);
    }

    /**
     * 根据系统配额 id 查询配额配置 [分页] - 查看某配额被哪些套餐配置
     * @param sQuotaId 系统配额ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额配置列表
     */
    @GetMapping("/by-squota/{sQuotaId}")
    public SaResult getComboQuotasBySQuotaId(@PathVariable Long sQuotaId,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<ComboQuotaVO> result = comboQuotaService.getComboQuotasBySQuotaId(sQuotaId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该配额暂未被任何套餐配置").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取包含该配额的套餐列表成功").setData(result);
    }

    /**
     * 根据系统配额 id 批量查询配额配置 [分页] - 返回分组结果
     * @param sQuotaIds 系统配额ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-squotas")
    public SaResult getComboQuotasBySQuotaIds(@RequestParam List<Long> sQuotaIds,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboQuota> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboQuotaVO>>> result = comboQuotaService.getComboQuotasBySQuotaIds(sQuotaIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些配额暂未被任何套餐配置").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取包含这些配额的套餐列表成功").setData(result);
    }

    /**
     * 新增套餐配额配置
     * @param comboQuotaRTO 配置信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createComboQuota(@RequestBody ComboQuotaRTO comboQuotaRTO) {
        boolean result = comboQuotaService.createComboQuota(comboQuotaRTO);
        if (result) {
            return SaResult.ok("创建套餐配额配置成功");
        }
        return SaResult.error("创建套餐配额配置失败");
    }

    /**
     * 更新套餐配额配置
     * @param id 配额配置ID
     * @param comboQuotaRTO 配置信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateComboQuota(@PathVariable Long id, @RequestBody ComboQuotaRTO comboQuotaRTO) {
        comboQuotaRTO.setId(id);
        boolean result = comboQuotaService.updateComboQuota(comboQuotaRTO);
        if (result) {
            return SaResult.ok("更新套餐配额配置成功");
        }
        return SaResult.error("更新套餐配额配置失败");
    }

    /**
     * 删除套餐配额配置（真删）
     * @param id 配额配置ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteComboQuota(@PathVariable Long id) {
        boolean result = comboQuotaService.deleteComboQuota(id);
        if (result) {
            return SaResult.ok("删除套餐配额配置成功");
        }
        return SaResult.error("删除套餐配额配置失败");
    }

    /**
     * 软删除套餐配额配置
     * @param id 配额配置ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteComboQuota(@PathVariable Long id) {
        boolean result = comboQuotaService.softDeleteComboQuota(id);
        if (result) {
            return SaResult.ok("软删除套餐配额配置成功");
        }
        return SaResult.error("软删除套餐配额配置失败");
    }

    /**
     * 批量删除套餐配额配置（真删）
     * @param ids 配额配置ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteComboQuotas(@RequestParam List<Long> ids) {
        boolean result = comboQuotaService.batchDeleteComboQuotas(ids);
        if (result) {
            return SaResult.ok("批量删除套餐配额配置成功");
        }
        return SaResult.error("批量删除套餐配额配置失败");
    }

    /**
     * 批量软删除套餐配额配置
     * @param ids 配额配置ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteComboQuotas(@RequestParam List<Long> ids) {
        boolean result = comboQuotaService.batchSoftDeleteComboQuotas(ids);
        if (result) {
            return SaResult.ok("批量软删除套餐配额配置成功");
        }
        return SaResult.error("批量软删除套餐配额配置失败");
    }

}
