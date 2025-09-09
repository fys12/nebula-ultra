package com.cloudvalley.nebula.monolith.business.perm.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermUser;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermUserRTO;
import com.cloudvalley.nebula.monolith.business.perm.service.IPermUserService;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户权限绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/perm-user")
public class PermUserController {

    @Autowired
    private IPermUserService permUserService;

    /**
     * 查询用户租户权限绑定列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户租户权限列表
     */
    @GetMapping
    public SaResult getPermUserList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<PermUserVO> result = permUserService.getPermUserList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户租户权限绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户租户权限绑定列表成功").setData(result);
    }

    /**
     * 根据ID批量查询绑定关系 [分页]
     * @param ids 用户租户权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户租户权限信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getPermUsersByIds(@RequestParam List<Long> ids,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<PermUserVO> result = permUserService.getPermUsersByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户租户权限绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户租户权限绑定信息成功").setData(result);
    }

    /**
     * 根据租户用户ID查询绑定关系 [分页] - 查看某用户拥有的租户权限
     * @param tUserId 租户用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户租户权限列表
     */
    @GetMapping("/by-tuser/{tUserId}")
    public SaResult getPermUsersByTUserId(@PathVariable Long tUserId,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<PermUserVO> result = permUserService.getPermUsersByTUserId(tUserId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该用户暂无租户权限绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户租户权限列表成功").setData(result);
    }

    /**
     * 根据租户用户ID批量查询绑定关系 [分页] - 返回分组结果
     * @param tUserIds 租户用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户用户ID分组的用户租户权限分页列表
     */
    @GetMapping("/by-tusers")
    public SaResult getPermUsersByTUserIds(@RequestParam List<Long> tUserIds,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<Map<Long, List<PermUserVO>>> result = permUserService.getPermUsersByTUserIds(tUserIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些用户暂无租户权限绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户租户权限列表成功").setData(result);
    }

    /**
     * 根据租户权限ID查询绑定关系 [分页] - 查看某租户权限被哪些用户拥有
     * @param tPermId 租户权限ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户租户权限列表
     */
    @GetMapping("/by-tperm/{tPermId}")
    public SaResult getPermUsersByTPermId(@PathVariable Long tPermId,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<PermUserVO> result = permUserService.getPermUsersByTPermId(tPermId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户权限暂未分配给任何用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户权限用户列表成功").setData(result);
    }

    /**
     * 根据租户权限ID批量查询绑定关系 [分页] - 返回分组结果
     * @param tPermIds 租户权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户权限ID分组的用户租户权限分页列表
     */
    @GetMapping("/by-tperms")
    public SaResult getPermUsersByTPermIds(@RequestParam List<Long> tPermIds,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermUser> page = new Page<>(current, size);
        IPage<Map<Long, List<PermUserVO>>> result = permUserService.getPermUsersByTPermIds(tPermIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户权限暂未分配给任何用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户权限用户列表成功").setData(result);
    }

    /**
     * 新增用户租户权限绑定
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createPermUser(@RequestBody PermUserRTO permUserRTO) {
        boolean result = permUserService.createPermUser(permUserRTO);
        if (result) {
            return SaResult.ok("创建用户租户权限绑定成功");
        }
        return SaResult.error("创建用户租户权限绑定失败");
    }

    /**
     * 更新用户租户权限绑定
     * @param id 用户租户权限ID
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updatePermUser(@PathVariable Long id, @RequestBody PermUserRTO permUserRTO) {
        permUserRTO.setId(id);
        boolean result = permUserService.updatePermUser(permUserRTO);
        if (result) {
            return SaResult.ok("更新用户租户权限绑定成功");
        }
        return SaResult.error("更新用户租户权限绑定失败");
    }

    /**
     * 更新绑定状态（启用/禁用该权限在用户中的生效）
     * @param id 用户租户权限ID
     * @param status 状态
     * @return 操作结果
     */
    @PatchMapping("/{id}/status")
    public SaResult updatePermUserStatus(@PathVariable Long id, @RequestParam Boolean status) {
        boolean result = permUserService.updatePermUserStatus(id, status);
        if (result) {
            return SaResult.ok("更新用户租户权限状态成功");
        }
        return SaResult.error("更新用户租户权限状态失败");
    }

    /**
     * 删除绑定关系（真删）
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deletePermUser(@PathVariable Long id) {
        boolean result = permUserService.deletePermUser(id);
        if (result) {
            return SaResult.ok("删除用户租户权限绑定成功");
        }
        return SaResult.error("删除用户租户权限绑定失败");
    }

    /**
     * 软删除绑定关系
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeletePermUser(@PathVariable Long id) {
        boolean result = permUserService.softDeletePermUser(id);
        if (result) {
            return SaResult.ok("软删除用户租户权限绑定成功");
        }
        return SaResult.error("软删除用户租户权限绑定失败");
    }

    /**
     * 批量删除绑定关系（真删）
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeletePermUsers(@RequestParam List<Long> ids) {
        boolean result = permUserService.batchDeletePermUsers(ids);
        if (result) {
            return SaResult.ok("批量删除用户租户权限绑定成功");
        }
        return SaResult.error("批量删除用户租户权限绑定失败");
    }

    /**
     * 批量软删除绑定关系
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeletePermUsers(@RequestParam List<Long> ids) {
        boolean result = permUserService.batchSoftDeletePermUsers(ids);
        if (result) {
            return SaResult.ok("批量软删除用户租户权限绑定成功");
        }
        return SaResult.error("批量软删除用户租户权限绑定失败");
    }

}
