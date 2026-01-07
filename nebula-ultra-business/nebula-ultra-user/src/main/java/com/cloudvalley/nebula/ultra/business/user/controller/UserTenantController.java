package com.cloudvalley.nebula.ultra.business.user.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.model.entity.UserTenant;
import com.cloudvalley.nebula.ultra.business.user.model.rto.UserTenantRTO;
import com.cloudvalley.nebula.ultra.business.user.service.IUserTenantService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-用户关联 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/user-tenant")
public class UserTenantController {

    @Autowired
    private IUserTenantService UserTenantService;

    /**
     * 查询租户用户列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 租户用户列表
     */
    @GetMapping
    public SaResult getUserTenantList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<UserTenantVO> result = UserTenantService.getUserTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户用户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户用户列表成功").setData(result);
    }

    /**
     * 根据租户用户ID批量查询租户用户 [分页]
     * @param ids 租户用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 租户用户信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getUserTenantsByIds(@RequestParam List<Long> ids,
                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<UserTenantVO> result = UserTenantService.getUserTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户用户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户用户信息成功").setData(result);
    }

    /**
     * 根据租户ID查询租户用户 [分页]
     * @param tenantId 租户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 租户用户列表
     */
    @GetMapping("/by-tenant/{tenantId}")
    public SaResult getUserTenantsByTenantId(@PathVariable Long tenantId,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<UserTenantVO> result = UserTenantService.getUserTenantsByTenantId(tenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户用户列表成功").setData(result);
    }

    /**
     * 根据租户ID批量查询租户用户 [分页] - 返回分组结果
     * @param tenantIds 租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户ID分组的租户用户分页列表
     */
    @GetMapping("/by-tenants")
    public SaResult getUserTenantsByTenantIds(@RequestParam List<Long> tenantIds,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<UserTenantVO>>> result = UserTenantService.getUserTenantsByTenantIds(tenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户用户列表成功").setData(result);
    }

    /**
     * 根据用户ID查询租户用户 [分页]
     * @param userId 用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 租户用户列表
     */
    @GetMapping("/by-user/{userId}")
    public SaResult getUserTenantsByUserId(@PathVariable Long userId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<UserTenantVO> result = UserTenantService.getUserTenantsByUserId(userId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该用户暂未关联任何租户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户租户列表成功").setData(result);
    }

    /**
     * 根据用户ID批量查询租户用户 [分页] - 返回分组结果
     * @param userIds 用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按用户ID分组的租户用户分页列表
     */
    @GetMapping("/by-users")
    public SaResult getUserTenantsByUserIds(@RequestParam List<Long> userIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<UserTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<UserTenantVO>>> result = UserTenantService.getUserTenantsByUserIds(userIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些用户暂未关联任何租户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户租户列表成功").setData(result);
    }

    /**
     * 新增租户用户
     * @param tenantUserRTO 租户用户信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createUserTenant(@RequestBody UserTenantRTO tenantUserRTO) {
        boolean result = UserTenantService.createUserTenant(tenantUserRTO);
        if (result) {
            return SaResult.ok("创建租户用户关联成功");
        }
        return SaResult.error("创建租户用户关联失败");
    }

    /**
     * 更新租户用户
     * @param id 租户用户ID
     * @param tenantUserRTO 租户用户信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateUserTenant(@PathVariable Long id, @RequestBody UserTenantRTO tenantUserRTO) {
        tenantUserRTO.setId(id);
        boolean result = UserTenantService.updateUserTenant(tenantUserRTO);
        if (result) {
            return SaResult.ok("更新租户用户关联成功");
        }
        return SaResult.error("更新租户用户关联失败");
    }

    /**
     * 更新租户用户状态
     * @param id 租户用户ID
     * @param state 状态
     * @return 操作结果
     */
    @PatchMapping("/{id}/state")
    public SaResult updateUserTenantState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = UserTenantService.updateUserTenantState(id, state);
        if (result) {
            return SaResult.ok("更新租户用户状态成功");
        }
        return SaResult.error("更新租户用户状态失败");
    }

    /**
     * 删除租户用户（真删除）
     * @param id 租户用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteUserTenant(@PathVariable Long id) {
        boolean result = UserTenantService.deleteUserTenant(id);
        if (result) {
            return SaResult.ok("删除租户用户关联成功");
        }
        return SaResult.error("删除租户用户关联失败");
    }

    /**
     * 软删除租户用户
     * @param id 租户用户ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteUserTenant(@PathVariable Long id) {
        boolean result = UserTenantService.softDeleteUserTenant(id);
        if (result) {
            return SaResult.ok("软删除租户用户关联成功");
        }
        return SaResult.error("软删除租户用户关联失败");
    }

    /**
     * 批量删除租户用户（真删除）
     * @param ids 租户用户ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteUserTenants(@RequestParam List<Long> ids) {
        boolean result = UserTenantService.batchDeleteUserTenants(ids);
        if (result) {
            return SaResult.ok("批量删除租户用户关联成功");
        }
        return SaResult.error("批量删除租户用户关联失败");
    }

    /**
     * 批量软删除租户用户
     * @param ids 租户用户ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteUserTenants(@RequestParam List<Long> ids) {
        boolean result = UserTenantService.batchSoftDeleteUserTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除租户用户关联成功");
        }
        return SaResult.error("批量软删除租户用户关联失败");
    }
    
}
