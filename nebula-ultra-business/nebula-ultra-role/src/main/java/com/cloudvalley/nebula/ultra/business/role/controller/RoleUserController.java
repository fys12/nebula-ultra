package com.cloudvalley.nebula.ultra.business.role.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.role.model.entity.RoleUser;
import com.cloudvalley.nebula.ultra.business.role.model.rto.RoleUserRTO;
import com.cloudvalley.nebula.ultra.business.role.service.IRoleUserService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户角色分配 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/role-user")
public class RoleUserController {

    @Autowired
    private IRoleUserService roleUserService;

    /**
     * 分页查询「租户用户-租户角色」分配列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 RoleUserVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getRoleUserList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<RoleUserVO> result = roleUserService.getRoleUserList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户角色分配数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户角色分配列表成功").setData(result);
    }

    /**
     * 根据多个用户角色分配ID分页批量查询绑定信息
     * @param ids 绑定关系ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleUserVO 列表，表示匹配的「用户-角色」分配记录
     */
    @GetMapping("/batch")
    public SaResult getRoleUsersByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<RoleUserVO> result = roleUserService.getRoleUsersByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户角色分配数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户角色分配信息成功").setData(result);
    }

    /**
     * 根据租户用户ID分页查询其绑定的角色列表（查看某用户拥有哪些角色）
     * @param userId 租户用户ID（tUserId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleUserVO 列表，表示该用户已分配的角色信息
     */
    @GetMapping("/by-user/{userId}")
    public SaResult getRoleUsersByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<RoleUserVO> result = roleUserService.getRoleUsersByUserId(userId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该用户暂未分配任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户角色分配列表成功").setData(result);
    }

    /**
     * 根据多个租户用户ID分页批量查询绑定角色，并按用户ID分组返回结果
     * @param userIds 租户用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tUserId，值为对应用户的 RoleUserVO 列表
     */
    @GetMapping("/by-users")
    public SaResult getRoleUsersByUserIds(
            @RequestParam List<Long> userIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<Map<Long, List<RoleUserVO>>> result = roleUserService.getRoleUsersByUserIds(userIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些用户暂未分配任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户角色分配列表成功").setData(result);
    }

    /**
     * 根据租户角色ID分页查询其绑定的用户列表（查看某角色被哪些用户拥有）
     * @param roleId 租户角色ID（tRoleId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleUserVO 列表，表示分配了该角色的用户信息
     */
    @GetMapping("/by-role/{roleId}")
    public SaResult getRoleUsersByRoleId(
            @PathVariable Long roleId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<RoleUserVO> result = roleUserService.getRoleUsersByRoleId(roleId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该角色下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取角色用户分配列表成功").setData(result);
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定用户，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tRoleId，值为对应角色的 RoleUserVO 列表
     */
    @GetMapping("/by-roles")
    public SaResult getRoleUsersByRoleIds(
            @RequestParam List<Long> roleIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleUser> page = new Page<>(current, size);
        IPage<Map<Long, List<RoleUserVO>>> result = roleUserService.getRoleUsersByRoleIds(roleIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些角色下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取角色用户分配列表成功").setData(result);
    }

    /**
     * 新增「租户用户-租户角色」分配关系
     * @param roleUserRTO 请求传输对象，包含 tUserId 和 tRoleId
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createRoleUser(@RequestBody RoleUserRTO roleUserRTO) {
        boolean result = roleUserService.createRoleUser(roleUserRTO);
        if (result) {
            return SaResult.ok("创建用户角色分配成功");
        }
        return SaResult.error("创建用户角色分配失败");
    }

    /**
     * 更新「租户用户-租户角色」分配信息
     * @param id 绑定关系的唯一标识ID
     * @param roleUserRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateRoleUser(@PathVariable Long id, @RequestBody RoleUserRTO roleUserRTO) {
        roleUserRTO.setId(id);
        boolean result = roleUserService.updateRoleUser(roleUserRTO);
        if (result) {
            return SaResult.ok("更新用户角色分配成功");
        }
        return SaResult.error("更新用户角色分配失败");
    }

    /**
     * 更新用户角色分配状态（启用/禁用该角色在用户中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateRoleUserState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = roleUserService.updateRoleUserState(id, state);
        if (result) {
            return SaResult.ok("更新用户角色状态成功");
        }
        return SaResult.error("更新用户角色状态失败");
    }

    /**
     * 删除用户角色分配（物理删除）
     * @param id 绑定关系ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteRoleUser(@PathVariable Long id) {
        boolean result = roleUserService.deleteRoleUser(id);
        if (result) {
            return SaResult.ok("删除用户角色分配成功");
        }
        return SaResult.error("删除用户角色分配失败");
    }

    /**
     * 软删除用户角色分配（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteRoleUser(@PathVariable Long id) {
        boolean result = roleUserService.softDeleteRoleUser(id);
        if (result) {
            return SaResult.ok("软删除用户角色分配成功");
        }
        return SaResult.error("软删除用户角色分配失败");
    }

    /**
     * 批量删除多个用户角色分配（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteRoleUsers(@RequestParam List<Long> ids) {
        boolean result = roleUserService.batchDeleteRoleUsers(ids);
        if (result) {
            return SaResult.ok("批量删除用户角色分配成功");
        }
        return SaResult.error("批量删除用户角色分配失败");
    }

    /**
     * 批量软删除多个用户角色分配（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteRoleUsers(@RequestParam List<Long> ids) {
        boolean result = roleUserService.batchSoftDeleteRoleUsers(ids);
        if (result) {
            return SaResult.ok("批量软删除用户角色分配成功");
        }
        return SaResult.error("批量软删除用户角色分配失败");
    }

}
