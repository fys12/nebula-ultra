package com.cloudvalley.nebula.ultra.business.group.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.SysGroup;
import com.cloudvalley.nebula.ultra.business.group.model.rto.SysGroupRTO;
import com.cloudvalley.nebula.ultra.business.group.service.ISysGroupService;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 组 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-group")
public class SysGroupController {

    @Autowired
    private ISysGroupService sysGroupService;

    /**
     * 查询系统组列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 系统组列表
     */
    @GetMapping
    public SaResult getSysGroupList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysGroup> page = new Page<>(current, size);
        IPage<SysGroupVO> result = sysGroupService.getSysGroupList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统组数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统组列表成功").setData(result);
    }

    /**
     * 根据ID批量查询系统组 [分页]
     * @param ids 系统组ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 系统组信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getSysGroupsByIds(@RequestParam List<Long> ids,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysGroup> page = new Page<>(current, size);
        IPage<SysGroupVO> result = sysGroupService.getSysGroupsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统组数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统组信息成功").setData(result);
    }

    /**
     * 新增系统组
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createSysGroup(@RequestBody SysGroupRTO sysGroupRTO) {
        boolean result = sysGroupService.createSysGroup(sysGroupRTO);
        if (result) {
            return SaResult.ok("创建系统组成功");
        }
        return SaResult.error("创建系统组失败");
    }

    /**
     * 更新系统组
     * @param id 系统组ID
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateSysGroup(@PathVariable Long id, @RequestBody SysGroupRTO sysGroupRTO) {
        sysGroupRTO.setId(id);
        boolean result = sysGroupService.updateSysGroup(sysGroupRTO);
        if (result) {
            return SaResult.ok("更新系统组成功");
        }
        return SaResult.error("更新系统组失败");
    }

    /**
     * 删除系统组（真删）
     * @param id 系统组ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysGroup(@PathVariable Long id) {
        boolean result = sysGroupService.deleteSysGroup(id);
        if (result) {
            return SaResult.ok("删除系统组成功");
        }
        return SaResult.error("删除系统组失败");
    }

    /**
     * 软删除系统组
     * @param id 系统组ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysGroup(@PathVariable Long id) {
        boolean result = sysGroupService.softDeleteSysGroup(id);
        if (result) {
            return SaResult.ok("软删除系统组成功");
        }
        return SaResult.error("软删除系统组失败");
    }

    /**
     * 批量删除系统组（真删）
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysGroups(@RequestParam List<Long> ids) {
        boolean result = sysGroupService.batchDeleteSysGroups(ids);
        if (result) {
            return SaResult.ok("批量删除系统组成功");
        }
        return SaResult.error("批量删除系统组失败");
    }

    /**
     * 批量软删除系统组
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysGroups(@RequestParam List<Long> ids) {
        boolean result = sysGroupService.batchSoftDeleteSysGroups(ids);
        if (result) {
            return SaResult.ok("批量软删除系统组成功");
        }
        return SaResult.error("批量软删除系统组失败");
    }

}
