package com.cloudvalley.nebula.ultra.business.dept.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.DeptTenant;
import com.cloudvalley.nebula.ultra.business.dept.model.rto.DeptTenantRTO;
import com.cloudvalley.nebula.ultra.business.dept.service.IDeptTenantService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-部门绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/dept-tenant")
public class DeptTenantController {

    @Autowired
    private IDeptTenantService tenantDeptService;

    /**
     * 查询租户 - 部门绑定列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 租户 - 部门绑定列表
     */
    @GetMapping
    public SaResult getDeptTenantList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<DeptTenantVO> result = tenantDeptService.getDeptTenantList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户 - 部门绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户 - 部门绑定列表成功").setData(result);
    }

    /**
     * 根据租户 - 部门绑定 id 批量查询绑定关系 [分页]
     * @param ids 绑定ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 绑定信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getDeptTenantsByIds(@RequestParam List<Long> ids,
                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<DeptTenantVO> result = tenantDeptService.getDeptTenantsByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户 - 部门绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取绑定信息成功").setData(result);
    }

    /**
     * 根据系统租户 id 查询绑定关系 [分页]
     * @param sTenantId 系统租户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 绑定信息分页列表
     */
    @GetMapping("/by-stenant/{sTenantId}")
    public SaResult getDeptTenantsBySTenantId(@PathVariable Long sTenantId,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<DeptTenantVO> result = tenantDeptService.getDeptTenantsBySTenantId(sTenantId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂无绑定的部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户绑定的部门列表成功").setData(result);
    }

    /**
     * 根据系统租户 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按系统租户ID分组的绑定分页列表
     */
    @GetMapping("/by-stenants")
    public SaResult getDeptTenantsBySTenantIds(@RequestParam List<Long> sTenantIds,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<DeptTenantVO>>> result = tenantDeptService.getDeptTenantsBySTenantIds(sTenantIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂无绑定的部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户绑定的部门列表成功").setData(result);
    }

    /**
     * 根据系统部门 id 查询绑定关系 [分页]
     * @param sDeptId 系统部门ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 绑定信息分页列表
     */
    @GetMapping("/by-sdept/{sDeptId}")
    public SaResult getDeptTenantsBySDeptId(@PathVariable Long sDeptId,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<DeptTenantVO> result = tenantDeptService.getDeptTenantsBySDeptId(sDeptId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该部门暂未被租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取绑定该部门的租户列表成功").setData(result);
    }

    /**
     * 根据系统部门 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sDeptIds 系统部门ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按系统部门ID分组的绑定分页列表
     */
    @GetMapping("/by-sdepts")
    public SaResult getDeptTenantsBySDeptIds(@RequestParam List<Long> sDeptIds,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<DeptTenantVO>>> result = tenantDeptService.getDeptTenantsBySDeptIds(sDeptIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些部门暂未被租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取绑定这些部门的租户列表成功").setData(result);
    }

    /**
     * 新增租户 - 部门绑定
     * @param tenantDeptRTO 绑定信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createDeptTenant(@RequestBody DeptTenantRTO tenantDeptRTO) {
        boolean result = tenantDeptService.createDeptTenant(tenantDeptRTO);
        if (result) {
            return SaResult.ok("创建租户 - 部门绑定成功");
        }
        return SaResult.error("创建租户 - 部门绑定失败");
    }

    /**
     * 更新租户 - 部门绑定
     * @param id 绑定ID
     * @param tenantDeptRTO 绑定信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateDeptTenant(@PathVariable Long id, @RequestBody DeptTenantRTO tenantDeptRTO) {
        tenantDeptRTO.setId(id);
        boolean result = tenantDeptService.updateDeptTenant(tenantDeptRTO);
        if (result) {
            return SaResult.ok("更新租户 - 部门绑定成功");
        }
        return SaResult.error("更新租户 - 部门绑定失败");
    }

    /**
     * 更新绑定状态（该部门在该租户中的启用状态）
     * @param id 绑定ID
     * @param state 状态
     * @return 操作结果
     */
    @PatchMapping("/{id}/state")
    public SaResult updateDeptTenantState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = tenantDeptService.updateDeptTenantState(id, state);
        if (result) {
            return SaResult.ok("更新绑定状态成功");
        }
        return SaResult.error("更新绑定状态失败");
    }

    /**
     * 删除租户 - 部门绑定（真删）
     * @param id 绑定ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteDeptTenant(@PathVariable Long id) {
        boolean result = tenantDeptService.deleteDeptTenant(id);
        if (result) {
            return SaResult.ok("删除租户 - 部门绑定成功");
        }
        return SaResult.error("删除租户 - 部门绑定失败");
    }

    /**
     * 软删除租户 - 部门绑定
     * @param id 绑定ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteDeptTenant(@PathVariable Long id) {
        boolean result = tenantDeptService.softDeleteDeptTenant(id);
        if (result) {
            return SaResult.ok("软删除租户 - 部门绑定成功");
        }
        return SaResult.error("软删除租户 - 部门绑定失败");
    }

    /**
     * 批量删除租户 - 部门绑定（真删）
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteDeptTenants(@RequestParam List<Long> ids) {
        boolean result = tenantDeptService.batchDeleteDeptTenants(ids);
        if (result) {
            return SaResult.ok("批量删除租户 - 部门绑定成功");
        }
        return SaResult.error("批量删除租户 - 部门绑定失败");
    }

    /**
     * 批量软删除租户 - 部门绑定
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteDeptTenants(@RequestParam List<Long> ids) {
        boolean result = tenantDeptService.batchSoftDeleteDeptTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除租户 - 部门绑定成功");
        }
        return SaResult.error("批量软删除租户 - 部门绑定失败");
    }

}
