package com.cloudvalley.nebula.ultra.business.combo.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboRole;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboRoleRTO;
import com.cloudvalley.nebula.ultra.business.combo.service.IComboRoleService;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐-角色绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/combo-role")
public class ComboRoleController {

    @Autowired
    private IComboRoleService comboRoleService;

    /**
     * 查询套餐 - 角色绑定列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 角色绑定列表
     */
    @GetMapping
    public SaResult getComboRoleList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<ComboRoleVO> result = comboRoleService.getComboRoleList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐 - 角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐 - 角色绑定列表成功").setData(result);
    }

    /**
     * 根据绑定 id 批量查询绑定关系 [分页]
     * @param ids 绑定ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/batch")
    public SaResult getComboRolesByIds(@RequestParam List<Long> ids,
                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<ComboRoleVO> result = comboRoleService.getComboRolesByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐 - 角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐 - 角色绑定信息成功").setData(result);
    }

    /**
     * 根据系统套餐 id 查询绑定关系 [分页] - 查看某套餐包含的角色
     * @param sComboId 系统套餐ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 角色绑定列表
     */
    @GetMapping("/by-scombo/{sComboId}")
    public SaResult getComboRolesBySComboId(@PathVariable Long sComboId,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<ComboRoleVO> result = comboRoleService.getComboRolesBySComboId(sComboId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该套餐暂无绑定的角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐绑定的角色列表成功").setData(result);
    }

    /**
     * 根据系统套餐 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-scombos")
    public SaResult getComboRolesBySComboIds(@RequestParam List<Long> sComboIds,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboRoleVO>>> result = comboRoleService.getComboRolesBySComboIds(sComboIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些套餐暂无绑定的角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐绑定的角色列表成功").setData(result);
    }

    /**
     * 根据系统角色 id 查询绑定关系 [分页] - 查看某角色被哪些套餐绑定
     * @param sRoleId 系统角色ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐 - 角色绑定列表
     */
    @GetMapping("/by-srole/{sRoleId}")
    public SaResult getComboRolesBySRoleId(@PathVariable Long sRoleId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<ComboRoleVO> result = comboRoleService.getComboRolesBySRoleId(sRoleId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该角色暂未被任何套餐绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取绑定该角色的套餐列表成功").setData(result);
    }

    /**
     * 根据系统角色 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sRoleIds 系统角色ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分组分页结果
     */
    @GetMapping("/by-sroles")
    public SaResult getComboRolesBySRoleIds(@RequestParam List<Long> sRoleIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboRole> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboRoleVO>>> result = comboRoleService.getComboRolesBySRoleIds(sRoleIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些角色暂未被任何套餐绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取绑定这些角色的套餐列表成功").setData(result);
    }

    /**
     * 新增套餐 - 角色绑定
     * @param comboRoleRTO 绑定信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createComboRole(@RequestBody ComboRoleRTO comboRoleRTO) {
        boolean result = comboRoleService.createComboRole(comboRoleRTO);
        if (result) {
            return SaResult.ok("创建套餐 - 角色绑定成功");
        }
        return SaResult.error("创建套餐 - 角色绑定失败");
    }

    /**
     * 更新套餐 - 角色绑定
     * @param id 绑定ID
     * @param comboRoleRTO 绑定信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateComboRole(@PathVariable Long id, @RequestBody ComboRoleRTO comboRoleRTO) {
        comboRoleRTO.setId(id);
        boolean result = comboRoleService.updateComboRole(comboRoleRTO);
        if (result) {
            return SaResult.ok("更新套餐 - 角色绑定成功");
        }
        return SaResult.error("更新套餐 - 角色绑定失败");
    }

    /**
     * 删除套餐 - 角色绑定（真删）
     * @param id 绑定ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteComboRole(@PathVariable Long id) {
        boolean result = comboRoleService.deleteComboRole(id);
        if (result) {
            return SaResult.ok("删除套餐 - 角色绑定成功");
        }
        return SaResult.error("删除套餐 - 角色绑定失败");
    }

    /**
     * 软删除套餐 - 角色绑定
     * @param id 绑定ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteComboRole(@PathVariable Long id) {
        boolean result = comboRoleService.softDeleteComboRole(id);
        if (result) {
            return SaResult.ok("软删除套餐 - 角色绑定成功");
        }
        return SaResult.error("软删除套餐 - 角色绑定失败");
    }

    /**
     * 批量删除套餐 - 角色绑定（真删）
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteComboRoles(@RequestParam List<Long> ids) {
        boolean result = comboRoleService.batchDeleteComboRoles(ids);
        if (result) {
            return SaResult.ok("批量删除套餐 - 角色绑定成功");
        }
        return SaResult.error("批量删除套餐 - 角色绑定失败");
    }

    /**
     * 批量软删除套餐 - 角色绑定
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteComboRoles(@RequestParam List<Long> ids) {
        boolean result = comboRoleService.batchSoftDeleteComboRoles(ids);
        if (result) {
            return SaResult.ok("批量软删除套餐 - 角色绑定成功");
        }
        return SaResult.error("批量软删除套餐 - 角色绑定失败");
    }

}
