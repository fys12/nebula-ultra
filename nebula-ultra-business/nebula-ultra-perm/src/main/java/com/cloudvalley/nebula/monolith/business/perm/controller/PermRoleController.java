package com.cloudvalley.nebula.monolith.business.perm.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermRole;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermRoleRTO;
import com.cloudvalley.nebula.monolith.business.perm.service.IPermRoleService;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户角色-租户权限关联 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/perm-role")
public class PermRoleController {

    @Autowired
    private IPermRoleService permRoleService;

    /**
     * 分页查询权限角色绑定列表（所有未软删的「权限-角色」关联）
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 PermRoleVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getPermRoleList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<PermRoleVO> result = permRoleService.getPermRoleList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限角色绑定列表成功").setData(result);
    }

    /**
     * 根据多个权限角色ID分页批量查询绑定信息
     * @param ids 权限角色关联的主键ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermRoleVO 列表，表示匹配的「权限-角色」绑定关系
     */
    @GetMapping("/batch")
    public SaResult getPermRolesByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<PermRoleVO> result = permRoleService.getPermRolesByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限角色绑定信息成功").setData(result);
    }

    /**
     * 根据租户权限ID分页查询其绑定的角色列表（查看某权限被哪些角色使用）
     * @param permId 租户权限ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermRoleVO 列表，表示绑定该权限的角色信息
     */
    @GetMapping("/by-perm/{permId}")
    public SaResult getPermRolesByPermId(
            @PathVariable Long permId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<PermRoleVO> result = permRoleService.getPermRolesByPermId(permId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该权限暂未绑定任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限角色绑定列表成功").setData(result);
    }

    /**
     * 根据多个租户权限ID分页批量查询绑定角色，并按权限ID分组返回
     * @param permIds 租户权限ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tPermId，值为对应权限绑定的角色VO列表
     */
    @GetMapping("/by-perms")
    public SaResult getPermRolesByPermIds(
            @RequestParam List<Long> permIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<Map<Long, List<PermRoleVO>>> result = permRoleService.getPermRolesByPermIds(permIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些权限暂未绑定任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限角色绑定列表成功").setData(result);
    }

    /**
     * 根据租户角色ID分页查询其绑定的权限列表（查看某角色拥有哪些权限）
     * @param roleId 租户角色ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 PermRoleVO 列表，表示该角色绑定的权限信息
     */
    @GetMapping("/by-role/{roleId}")
    public SaResult getPermRolesByRoleId(
            @PathVariable Long roleId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<PermRoleVO> result = permRoleService.getPermRolesByRoleId(roleId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该角色暂未绑定任何权限").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取角色权限绑定列表成功").setData(result);
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定权限，并按角色ID分组返回
     * @param roleIds 租户角色ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tRoleId，值为对应角色绑定的权限VO列表
     */
    @GetMapping("/by-roles")
    public SaResult getPermRolesByRoleIds(
            @RequestParam List<Long> roleIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<PermRole> page = new Page<>(current, size);
        IPage<Map<Long, List<PermRoleVO>>> result = permRoleService.getPermRolesByRoleIds(roleIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些角色暂未绑定任何权限").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取角色权限绑定列表成功").setData(result);
    }

    /**
     * 新增「租户权限-租户角色」绑定关系
     * @param permRoleRTO 请求传输对象，包含 tPermId 和 tRoleId
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createPermRole(@RequestBody PermRoleRTO permRoleRTO) {
        boolean result = permRoleService.createPermRole(permRoleRTO);
        if (result) {
            return SaResult.ok("创建权限角色绑定成功");
        }
        return SaResult.error("创建权限角色绑定失败");
    }

    /**
     * 更新「租户权限-租户角色」绑定信息
     * @param id 绑定关系的唯一标识ID
     * @param permRoleRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updatePermRole(@PathVariable Long id, @RequestBody PermRoleRTO permRoleRTO) {
        permRoleRTO.setId(id);
        boolean result = permRoleService.updatePermRole(permRoleRTO);
        if (result) {
            return SaResult.ok("更新权限角色绑定成功");
        }
        return SaResult.error("更新权限角色绑定失败");
    }

    /**
     * 更新权限角色绑定状态（启用/禁用该权限在角色中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updatePermRoleState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = permRoleService.updatePermRoleState(id, state);
        if (result) {
            return SaResult.ok("更新权限角色状态成功");
        }
        return SaResult.error("更新权限角色状态失败");
    }

    /**
     * 删除权限角色绑定（物理删除）
     * @param id 绑定关系ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deletePermRole(@PathVariable Long id) {
        boolean result = permRoleService.deletePermRole(id);
        if (result) {
            return SaResult.ok("删除权限角色绑定成功");
        }
        return SaResult.error("删除权限角色绑定失败");
    }

    /**
     * 软删除权限角色绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeletePermRole(@PathVariable Long id) {
        boolean result = permRoleService.softDeletePermRole(id);
        if (result) {
            return SaResult.ok("软删除权限角色绑定成功");
        }
        return SaResult.error("软删除权限角色绑定失败");
    }

    /**
     * 批量删除多个权限角色绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeletePermRoles(@RequestParam List<Long> ids) {
        boolean result = permRoleService.batchDeletePermRoles(ids);
        if (result) {
            return SaResult.ok("批量删除权限角色绑定成功");
        }
        return SaResult.error("批量删除权限角色绑定失败");
    }

    /**
     * 批量软删除多个权限角色绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeletePermRoles(@RequestParam List<Long> ids) {
        boolean result = permRoleService.batchSoftDeletePermRoles(ids);
        if (result) {
            return SaResult.ok("批量软删除权限角色绑定成功");
        }
        return SaResult.error("批量软删除权限角色绑定失败");
    }

}
