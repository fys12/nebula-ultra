package com.cloudvalley.nebula.ultra.business.role.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.role.model.entity.RoleTenant;
import com.cloudvalley.nebula.ultra.business.role.model.rto.RoleTenantRTO;
import com.cloudvalley.nebula.ultra.business.role.service.IRoleTenantService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-角色绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/role-tenant")
public class RoleTenantController {

    @Autowired
    private IRoleTenantService roleTenantService;

    /**
     * 分页查询「系统租户-系统角色」绑定列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 RoleTenantVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getRoleTenantList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<RoleTenantVO> result = roleTenantService.getRoleTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户角色绑定列表成功").setData(result);
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定信息
     * @param ids 租户角色关联ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleTenantVO 列表，表示匹配的「租户-角色」绑定关系
     */
    @GetMapping("/batch")
    public SaResult getRoleTenantsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<RoleTenantVO> result = roleTenantService.getRoleTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户角色绑定信息成功").setData(result);
    }

    /**
     * 根据系统租户ID分页查询其绑定的角色列表（查看某租户拥有哪些系统角色）
     * @param tenantId 系统租户ID（sTenantId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleTenantVO 列表，表示该租户绑定的角色信息
     */
    @GetMapping("/by-tenant/{tenantId}")
    public SaResult getRoleTenantsByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<RoleTenantVO> result = roleTenantService.getRoleTenantsByTenantId(tenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂未绑定任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户角色绑定列表成功").setData(result);
    }

    /**
     * 根据多个系统租户ID分页批量查询绑定角色，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 sTenantId，值为对应租户的 RoleTenantVO 列表
     */
    @GetMapping("/by-tenants")
    public SaResult getRoleTenantsByTenantIds(
            @RequestParam List<Long> tenantIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<RoleTenantVO>>> result = roleTenantService.getRoleTenantsByTenantIds(tenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂未绑定任何角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户角色绑定列表成功").setData(result);
    }

    /**
     * 根据系统角色ID分页查询其绑定的租户列表（查看某角色被哪些租户使用）
     * @param roleId 系统角色ID（sRoleId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 RoleTenantVO 列表，表示绑定该角色的租户信息
     */
    @GetMapping("/by-role/{roleId}")
    public SaResult getRoleTenantsByRoleId(
            @PathVariable Long roleId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<RoleTenantVO> result = roleTenantService.getRoleTenantsByRoleId(roleId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该角色暂未被任何租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取角色租户绑定列表成功").setData(result);
    }

    /**
     * 根据多个系统角色ID分页批量查询绑定租户，并按角色ID分组返回结果
     * @param roleIds 系统角色ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 sRoleId，值为对应角色的 RoleTenantVO 列表
     */
    @GetMapping("/by-roles")
    public SaResult getRoleTenantsByRoleIds(
            @RequestParam List<Long> roleIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<RoleTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<RoleTenantVO>>> result = roleTenantService.getRoleTenantsByRoleIds(roleIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些角色暂未被任何租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取角色租户绑定列表成功").setData(result);
    }

    /**
     * 新增「系统租户-系统角色」绑定关系
     * @param roleTenantRTO 请求传输对象，包含 sTenantId 和 sRoleId
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createRoleTenant(@RequestBody RoleTenantRTO roleTenantRTO) {
        boolean result = roleTenantService.createRoleTenant(roleTenantRTO);
        if (result) {
            return SaResult.ok("创建租户角色绑定成功");
        }
        return SaResult.error("创建租户角色绑定失败");
    }

    /**
     * 更新「系统租户-系统角色」绑定信息
     * @param id 绑定关系的唯一标识ID
     * @param roleTenantRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateRoleTenant(@PathVariable Long id, @RequestBody RoleTenantRTO roleTenantRTO) {
        roleTenantRTO.setId(id);
        boolean result = roleTenantService.updateRoleTenant(roleTenantRTO);
        if (result) {
            return SaResult.ok("更新租户角色绑定成功");
        }
        return SaResult.error("更新租户角色绑定失败");
    }

    /**
     * 更新租户角色绑定状态（启用/禁用该角色在租户中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/state")
    public SaResult updateRoleTenantState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = roleTenantService.updateRoleTenantState(id, state);
        if (result) {
            return SaResult.ok("更新租户角色状态成功");
        }
        return SaResult.error("更新租户角色状态失败");
    }

    /**
     * 删除租户角色绑定（物理删除）
     * @param id 绑定关系ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteRoleTenant(@PathVariable Long id) {
        boolean result = roleTenantService.deleteRoleTenant(id);
        if (result) {
            return SaResult.ok("删除租户角色绑定成功");
        }
        return SaResult.error("删除租户角色绑定失败");
    }

    /**
     * 软删除租户角色绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteRoleTenant(@PathVariable Long id) {
        boolean result = roleTenantService.softDeleteRoleTenant(id);
        if (result) {
            return SaResult.ok("软删除租户角色绑定成功");
        }
        return SaResult.error("软删除租户角色绑定失败");
    }

    /**
     * 批量删除多个租户角色绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteRoleTenants(@RequestParam List<Long> ids) {
        boolean result = roleTenantService.batchDeleteRoleTenants(ids);
        if (result) {
            return SaResult.ok("批量删除租户角色绑定成功");
        }
        return SaResult.error("批量删除租户角色绑定失败");
    }

    /**
     * 批量软删除多个租户角色绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteRoleTenants(@RequestParam List<Long> ids) {
        boolean result = roleTenantService.batchSoftDeleteRoleTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除租户角色绑定成功");
        }
        return SaResult.error("批量软删除租户角色绑定失败");
    }

}
