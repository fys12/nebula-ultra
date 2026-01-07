package com.cloudvalley.nebula.ultra.business.combo.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboPerm;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboPermRTO;
import com.cloudvalley.nebula.ultra.business.combo.service.IComboPermService;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboPermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐-权限绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/combo-perm")
public class ComboPermController {

    @Autowired
    private IComboPermService comboPermService;

    /**
     * 查询套餐 - 权限绑定列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 权限绑定列表
     */
    @GetMapping
    public SaResult getComboPermList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<ComboPermVO> result = comboPermService.getComboPermList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐 - 权限绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐 - 权限绑定列表成功").setData(result);
    }

    /**
     * 根据绑定 id 批量查询绑定关系 [分页]
     * @param ids 绑定ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/batch")
    public SaResult getComboPermsByIds(@RequestParam List<Long> ids,
                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<ComboPermVO> result = comboPermService.getComboPermsByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐 - 权限绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐 - 权限绑定信息成功").setData(result);
    }

    /**
     * 根据系统套餐 id 查询绑定关系 [分页] - 查看某套餐包含的权限
     * @param sComboId 系统套餐ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 权限绑定列表
     */
    @GetMapping("/by-scombo/{sComboId}")
    public SaResult getComboPermsBySComboId(@PathVariable Long sComboId,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<ComboPermVO> result = comboPermService.getComboPermsBySComboId(sComboId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该套餐暂无绑定的权限").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐绑定的权限列表成功").setData(result);
    }

    /**
     * 根据系统套餐 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-scombos")
    public SaResult getComboPermsBySComboIds(@RequestParam List<Long> sComboIds,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboPermVO>>> result = comboPermService.getComboPermsBySComboIds(sComboIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些套餐暂无绑定的权限").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐绑定的权限列表成功").setData(result);
    }

    /**
     * 根据系统权限 id 查询绑定关系 [分页] - 查看某权限被哪些套餐绑定
     * @param sPermId 系统权限ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 权限绑定列表
     */
    @GetMapping("/by-sperm/{sPermId}")
    public SaResult getComboPermsBySPermId(@PathVariable Long sPermId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<ComboPermVO> result = comboPermService.getComboPermsBySPermId(sPermId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该权限暂未被任何套餐绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取绑定该权限的套餐列表成功").setData(result);
    }

    /**
     * 根据系统权限 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sPermIds 系统权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-sperms")
    public SaResult getComboPermsBySPermIds(@RequestParam List<Long> sPermIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboPerm> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboPermVO>>> result = comboPermService.getComboPermsBySPermIds(sPermIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些权限暂未被任何套餐绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取绑定这些权限的套餐列表成功").setData(result);
    }

    /**
     * 新增套餐 - 权限绑定
     * @param comboPermRTO 绑定信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createComboPerm(@RequestBody ComboPermRTO comboPermRTO) {
        boolean result = comboPermService.createComboPerm(comboPermRTO);
        if (result) {
            return SaResult.ok("创建套餐 - 权限绑定成功");
        }
        return SaResult.error("创建套餐 - 权限绑定失败");
    }

    /**
     * 更新套餐 - 权限绑定
     * @param id 绑定ID
     * @param comboPermRTO 绑定信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateComboPerm(@PathVariable Long id, @RequestBody ComboPermRTO comboPermRTO) {
        comboPermRTO.setId(id);
        boolean result = comboPermService.updateComboPerm(comboPermRTO);
        if (result) {
            return SaResult.ok("更新套餐 - 权限绑定成功");
        }
        return SaResult.error("更新套餐 - 权限绑定失败");
    }

    /**
     * 删除套餐 - 权限绑定（真删）
     * @param id 绑定ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteComboPerm(@PathVariable Long id) {
        boolean result = comboPermService.deleteComboPerm(id);
        if (result) {
            return SaResult.ok("删除套餐 - 权限绑定成功");
        }
        return SaResult.error("删除套餐 - 权限绑定失败");
    }

    /**
     * 软删除套餐 - 权限绑定
     * @param id 绑定ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteComboPerm(@PathVariable Long id) {
        boolean result = comboPermService.softDeleteComboPerm(id);
        if (result) {
            return SaResult.ok("软删除套餐 - 权限绑定成功");
        }
        return SaResult.error("软删除套餐 - 权限绑定失败");
    }

    /**
     * 批量删除套餐 - 权限绑定（真删）
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteComboPerms(@RequestParam List<Long> ids) {
        boolean result = comboPermService.batchDeleteComboPerms(ids);
        if (result) {
            return SaResult.ok("批量删除套餐 - 权限绑定成功");
        }
        return SaResult.error("批量删除套餐 - 权限绑定失败");
    }

    /**
     * 批量软删除套餐 - 权限绑定
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteComboPerms(@RequestParam List<Long> ids) {
        boolean result = comboPermService.batchSoftDeleteComboPerms(ids);
        if (result) {
            return SaResult.ok("批量软删除套餐 - 权限绑定成功");
        }
        return SaResult.error("批量软删除套餐 - 权限绑定失败");
    }

}
