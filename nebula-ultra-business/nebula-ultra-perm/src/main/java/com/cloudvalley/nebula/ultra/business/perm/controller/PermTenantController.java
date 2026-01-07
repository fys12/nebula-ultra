package com.cloudvalley.nebula.ultra.business.perm.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermTenant;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.PermTenantRTO;
import com.cloudvalley.nebula.ultra.business.perm.service.IPermTenantService;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限-租户绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/perm-tenant")
public class PermTenantController {

    @Autowired
    private IPermTenantService permTenantService;

    /**
     * 查询权限租户绑定列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 权限租户列表
     */
    @GetMapping
    public SaResult getPermTenantList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<PermTenantVO> result = permTenantService.getPermTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限租户绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限租户绑定列表成功").setData(result);
    }

    /**
     * 根据ID批量查询绑定关系 [分页]
     * @param ids 权限租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 权限租户信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getPermTenantsByIds(@RequestParam List<Long> ids,
                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<PermTenantVO> result = permTenantService.getPermTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限租户绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限租户绑定信息成功").setData(result);
    }

    /**
     * 根据系统租户ID查询绑定关系 [分页] - 查看某租户拥有的系统权限
     * @param tenantId 租户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 权限租户列表
     */
    @GetMapping("/by-tenant/{tenantId}")
    public SaResult getPermTenantsByTenantId(@PathVariable Long tenantId,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<PermTenantVO> result = permTenantService.getPermTenantsByTenantId(tenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂无权限绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户权限列表成功").setData(result);
    }

    /**
     * 根据系统租户ID批量查询绑定关系 [分页] - 返回分组结果
     * @param tenantIds 租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户ID分组的权限租户分页列表
     */
    @GetMapping("/by-tenants")
    public SaResult getPermTenantsByTenantIds(@RequestParam List<Long> tenantIds,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<PermTenantVO>>> result = permTenantService.getPermTenantsByTenantIds(tenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂无权限绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户权限列表成功").setData(result);
    }

    /**
     * 根据系统权限ID查询绑定关系 [分页] - 查看某系统权限被哪些租户拥有
     * @param permId 权限ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 权限租户列表
     */
    @GetMapping("/by-perm/{permId}")
    public SaResult getPermTenantsByPermId(@PathVariable Long permId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<PermTenantVO> result = permTenantService.getPermTenantsByPermId(permId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该权限暂未分配给任何租户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限租户列表成功").setData(result);
    }

    /**
     * 根据系统权限ID批量查询绑定关系 [分页] - 返回分组结果
     * @param permIds 权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按权限ID分组的权限租户分页列表
     */
    @GetMapping("/by-perms")
    public SaResult getPermTenantsByPermIds(@RequestParam List<Long> permIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<PermTenantVO>>> result = permTenantService.getPermTenantsByPermIds(permIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些权限暂未分配给任何租户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限租户列表成功").setData(result);
    }

    /**
     * 新增权限租户绑定
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createPermTenant(@RequestBody PermTenantRTO permTenantRTO) {
        boolean result = permTenantService.createPermTenant(permTenantRTO);
        if (result) {
            return SaResult.ok("创建权限租户绑定成功");
        }
        return SaResult.error("创建权限租户绑定失败");
    }

    /**
     * 更新权限租户绑定
     * @param id 权限租户ID
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updatePermTenant(@PathVariable Long id, @RequestBody PermTenantRTO permTenantRTO) {
        permTenantRTO.setId(id);
        boolean result = permTenantService.updatePermTenant(permTenantRTO);
        if (result) {
            return SaResult.ok("更新权限租户绑定成功");
        }
        return SaResult.error("更新权限租户绑定失败");
    }

    /**
     * 更新绑定状态（启用/禁用该权限在租户中的生效）
     * @param id 权限租户ID
     * @param state 状态
     * @return 操作结果
     */
    @PatchMapping("/{id}/state")
    public SaResult updatePermTenantState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = permTenantService.updatePermTenantState(id, state);
        if (result) {
            return SaResult.ok("更新权限租户状态成功");
        }
        return SaResult.error("更新权限租户状态失败");
    }

    /**
     * 删除绑定关系（真删）
     * @param id 权限租户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deletePermTenant(@PathVariable Long id) {
        boolean result = permTenantService.deletePermTenant(id);
        if (result) {
            return SaResult.ok("删除权限租户绑定成功");
        }
        return SaResult.error("删除权限租户绑定失败");
    }

    /**
     * 软删除绑定关系
     * @param id 权限租户ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeletePermTenant(@PathVariable Long id) {
        boolean result = permTenantService.softDeletePermTenant(id);
        if (result) {
            return SaResult.ok("软删除权限租户绑定成功");
        }
        return SaResult.error("软删除权限租户绑定失败");
    }

    /**
     * 批量删除绑定关系（真删）
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeletePermTenants(@RequestParam List<Long> ids) {
        boolean result = permTenantService.batchDeletePermTenants(ids);
        if (result) {
            return SaResult.ok("批量删除权限租户绑定成功");
        }
        return SaResult.error("批量删除权限租户绑定失败");
    }

    /**
     * 批量软删除绑定关系
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeletePermTenants(@RequestParam List<Long> ids) {
        boolean result = permTenantService.batchSoftDeletePermTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除权限租户绑定成功");
        }
        return SaResult.error("批量软删除权限租户绑定失败");
    }

}
