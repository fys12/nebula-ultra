package com.cloudvalley.nebula.monolith.business.user.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.user.model.entity.SysUser;
import com.cloudvalley.nebula.monolith.business.user.model.rto.SysUserRTO;
import com.cloudvalley.nebula.monolith.business.user.service.ISysUserService;
import com.cloudvalley.nebula.monolith.shared.api.user.model.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询系统用户列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 系统用户列表
     */
    @GetMapping
    public SaResult getUserList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysUser> page = new Page<>(current, size);
        IPage<SysUserVO> result = sysUserService.getUserList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统用户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统用户列表成功").setData(result);
    }

    /**
     * 根据ID批量查询系统用户 [分页]
     * @param ids 用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getUsersByIds(@RequestParam List<Long> ids,
                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysUser> page = new Page<>(current, size);
        IPage<SysUserVO> result = sysUserService.getUsersByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户信息成功").setData(result);
    }

    /**
     * 根据用户名和密码哈希值查询系统用户
     * @param username 用户名
     * @param passwordHash 密码哈希值
     * @return 用户信息
     */
    @GetMapping("/login")
    public SaResult getUserByUsernameAndPasswordHash(@RequestParam String username,
                                                     @RequestParam String passwordHash) {
        SysUserVO result = sysUserService.getUserByUsernameAndPasswordHash(username, passwordHash);

        if (result == null) {
            return SaResult.ok("未找到匹配的用户").setData(null);
        }
        return SaResult.ok("查询用户成功").setData(result);
    }

    /**
     * 新增系统用户
     * @param userRTO 用户信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createUser(@RequestBody SysUserRTO userRTO) {
        boolean result = sysUserService.createUser(userRTO);
        if (result) {
            return SaResult.ok("创建用户成功");
        }
        return SaResult.error("创建用户失败");
    }

    /**
     * 更新系统用户
     * @param id 用户ID
     * @param userRTO 用户信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateUser(@PathVariable Long id, @RequestBody SysUserRTO userRTO) {
        userRTO.setId(id);
        boolean result = sysUserService.updateUser(userRTO);
        if (result) {
            return SaResult.ok("更新用户成功");
        }
        return SaResult.error("更新用户失败");
    }

    /**
     * 更新系统用户状态
     * @param id 用户ID
     * @param state 状态
     * @return 操作结果
     */
    @PatchMapping("/{id}/state")
    public SaResult updateUserState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysUserService.updateUserState(id, state);
        if (result) {
            return SaResult.ok("更新用户状态成功");
        }
        return SaResult.error("更新用户状态失败");
    }

    /**
     * 删除系统用户（真删除）
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteUser(@PathVariable Long id) {
        boolean result = sysUserService.deleteUser(id);
        if (result) {
            return SaResult.ok("删除用户成功");
        }
        return SaResult.error("删除用户失败");
    }

    /**
     * 软删除系统用户
     * @param id 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteUser(@PathVariable Long id) {
        boolean result = sysUserService.softDeleteUser(id);
        if (result) {
            return SaResult.ok("软删除用户成功");
        }
        return SaResult.error("软删除用户失败");
    }

    /**
     * 批量删除系统用户（真删除）
     * @param ids 用户ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteUsers(@RequestParam List<Long> ids) {
        boolean result = sysUserService.batchDeleteUsers(ids);
        if (result) {
            return SaResult.ok("批量删除用户成功");
        }
        return SaResult.error("批量删除用户失败");
    }

    /**
     * 批量软删除系统用户
     * @param ids 用户ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteUsers(@RequestParam List<Long> ids) {
        boolean result = sysUserService.batchSoftDeleteUsers(ids);
        if (result) {
            return SaResult.ok("批量软删除用户成功");
        }
        return SaResult.error("批量软删除用户失败");
    }

}
