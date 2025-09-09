package com.cloudvalley.nebula.monolith.business.perm.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermDept;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermDeptRTO;
import com.cloudvalley.nebula.monolith.business.perm.service.IPermDeptService;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户部门-租户权限关联 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/perm-dept")
public class PermDeptController {

    @Autowired
    private IPermDeptService permDeptService;

    /**
     * 分页查询租户部门-租户权限关联列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 PermDeptVO 列表，包含关联信息
     */
    @GetMapping
    public SaResult getPermDeptList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<PermDeptVO> result = permDeptService.getPermDeptList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户部门-租户权限关联数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户部门-租户权限关联列表成功").setData(result);
    }

    /**
     * 根据多个ID分页批量查询租户部门-租户权限关联
     * @param ids 关联ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermDeptVO 列表，按创建时间倒序排列
     */
    @GetMapping("/batch")
    public SaResult getPermDeptsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<PermDeptVO> result = permDeptService.getPermDeptsByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户部门-租户权限关联数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取关联信息成功").setData(result);
    }

    /**
     * 根据租户部门ID分页查询其关联的权限列表（查看某部门拥有的权限）
     * @param tDeptId 租户部门ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermDeptVO 列表，表示该部门拥有的权限
     */
    @GetMapping("/by-tdept/{tDeptId}")
    public SaResult getPermDeptsByTDeptId(
            @PathVariable Long tDeptId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<PermDeptVO> result = permDeptService.getPermDeptsByTDeptId(tDeptId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该部门暂无租户权限关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取部门拥有的权限列表成功").setData(result);
    }

    /**
     * 根据多个租户部门ID分页批量查询关联权限，并按部门ID分组返回
     * @param tDeptIds 租户部门ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tDeptId，值为对应部门的权限VO列表
     */
    @GetMapping("/by-tdepts")
    public SaResult getPermDeptsByTDeptIds(
            @RequestParam List<Long> tDeptIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<Map<Long, List<PermDeptVO>>> result = permDeptService.getPermDeptsByTDeptIds(tDeptIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些部门暂无租户权限关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取部门拥有的权限列表成功").setData(result);
    }

    /**
     * 根据租户权限ID分页查询拥有该权限的部门列表（查看某权限被哪些部门使用）
     * @param tPermId 租户权限ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermDeptVO 列表，表示使用该权限的部门
     */
    @GetMapping("/by-tperm/{tPermId}")
    public SaResult getPermDeptsByTPermId(
            @PathVariable Long tPermId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<PermDeptVO> result = permDeptService.getPermDeptsByTPermId(tPermId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户权限暂未分配给任何部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取拥有该权限的部门列表成功").setData(result);
    }

    /**
     * 根据多个租户权限ID分页批量查询使用部门，并按权限ID分组返回
     * @param tPermIds 租户权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tPermId，值为对应权限的部门VO列表
     */
    @GetMapping("/by-tperms")
    public SaResult getPermDeptsByTPermIds(
            @RequestParam List<Long> tPermIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermDept> page = new Page<>(current, size);
        IPage<Map<Long, List<PermDeptVO>>> result = permDeptService.getPermDeptsByTPermIds(tPermIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户权限暂未分配给任何部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取拥有这些权限的部门列表成功").setData(result);
    }

    /**
     * 新增租户部门-租户权限关联关系
     * @param permDeptRTO 请求传输对象，包含 tDeptId 和 tPermId
     * @return 创建结果，成功或失败提示
     */
    @PostMapping
    public SaResult createPermDept(@RequestBody PermDeptRTO permDeptRTO) {
        boolean result = permDeptService.createPermDept(permDeptRTO);
        if (result) {
            return SaResult.ok("创建租户部门-租户权限关联成功");
        }
        return SaResult.error("创建租户部门-租户权限关联失败");
    }

    /**
     * 更新租户部门-租户权限关联信息
     * @param id 关联关系ID
     * @param permDeptRTO 更新数据传输对象
     * @return 更新结果，成功或失败提示
     */
    @PutMapping("/{id}")
    public SaResult updatePermDept(@PathVariable Long id, @RequestBody PermDeptRTO permDeptRTO) {
        permDeptRTO.setId(id);
        boolean result = permDeptService.updatePermDept(permDeptRTO);
        if (result) {
            return SaResult.ok("更新租户部门-租户权限关联成功");
        }
        return SaResult.error("更新租户部门-租户权限关联失败");
    }

    /**
     * 更新关联关系状态（启用/禁用该权限在部门中的生效）
     * @param id 关联ID
     * @param status 目标状态：true 启用，false 禁用
     * @return 操作结果
     */
    @PatchMapping("/{id}/status")
    public SaResult updatePermDeptStatus(@PathVariable Long id, @RequestParam Boolean status) {
        boolean result = permDeptService.updatePermDeptStatus(id, status);
        if (result) {
            return SaResult.ok("更新关联状态成功");
        }
        return SaResult.error("更新关联状态失败");
    }

    /**
     * 删除租户部门-租户权限关联（物理删除）
     * @param id 关联ID
     * @return 删除结果，成功或失败提示
     */
    @DeleteMapping("/{id}")
    public SaResult deletePermDept(@PathVariable Long id) {
        boolean result = permDeptService.deletePermDept(id);
        if (result) {
            return SaResult.ok("删除关联关系成功");
        }
        return SaResult.error("删除关联关系失败");
    }

    /**
     * 软删除租户部门-租户权限关联（标记 deleted = true）
     * @param id 关联ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeletePermDept(@PathVariable Long id) {
        boolean result = permDeptService.softDeletePermDept(id);
        if (result) {
            return SaResult.ok("软删除关联关系成功");
        }
        return SaResult.error("软删除关联关系失败");
    }

    /**
     * 批量删除多个租户部门-租户权限关联（物理删除）
     * @param ids 关联ID列表
     * @return 批量删除结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeletePermDepts(@RequestParam List<Long> ids) {
        boolean result = permDeptService.batchDeletePermDepts(ids);
        if (result) {
            return SaResult.ok("批量删除关联关系成功");
        }
        return SaResult.error("批量删除关联关系失败");
    }

    /**
     * 批量软删除多个租户部门-租户权限关联（标记 deleted = true）
     * @param ids 关联ID列表
     * @return 批量软删除结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeletePermDepts(@RequestParam List<Long> ids) {
        boolean result = permDeptService.batchSoftDeletePermDepts(ids);
        if (result) {
            return SaResult.ok("批量软删除关联关系成功");
        }
        return SaResult.error("批量软删除关联关系失败");
    }

}
