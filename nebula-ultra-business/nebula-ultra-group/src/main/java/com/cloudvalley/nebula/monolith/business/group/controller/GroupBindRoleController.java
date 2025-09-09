package com.cloudvalley.nebula.monolith.business.group.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupBindRole;
import com.cloudvalley.nebula.monolith.business.group.model.rto.GroupBindRoleRTO;
import com.cloudvalley.nebula.monolith.business.group.service.IGroupBindRoleService;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupBindRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户角色绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/group-bind-role")
public class GroupBindRoleController {

    @Autowired
    private IGroupBindRoleService groupBindRoleService;

    /**
     * 查询组-租户角色绑定列表 [分页]
     *
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindRoleVO 列表
     */
    @GetMapping
    public SaResult getGroupBindRoleList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<GroupBindRoleVO> result = groupBindRoleService.getGroupBindRoleList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无组租户角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取组租户角色绑定列表成功").setData(result);
    }

    /**
     * 根据ID列表批量查询组-租户角色绑定 [分页]
     *
     * @param ids 组-租户角色绑定记录ID列表
     * @param current 当前页码，从1开始，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindRoleVO 列表
     */
    @GetMapping("/batch")
    public SaResult getGroupBindRolesByIds(@RequestParam List<Long> ids,
                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<GroupBindRoleVO> result = groupBindRoleService.getGroupBindRolesByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无组租户角色绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取组租户角色绑定信息成功").setData(result);
    }

    /**
     * 根据系统组ID查询其绑定的租户角色 [分页]
     *
     * @param groupId 系统组ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回该组绑定的 GroupBindRoleVO 分页列表
     */
    @GetMapping("/by-group/{groupId}")
    public SaResult getGroupBindRolesByGroupId(@PathVariable Long groupId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<GroupBindRoleVO> result = groupBindRoleService.getGroupBindRolesByGroupId(groupId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该系统组暂未绑定任何租户角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统组租户角色绑定列表成功").setData(result);
    }
    
    /**
     * 根据多个系统组ID批量查询绑定的租户角色 [分页]
     *
     * @param groupIds 系统组ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 groupId，值为对应 VO 列表
     */
    @GetMapping("/by-groups")
    public SaResult getGroupBindRolesByGroupIds(@RequestParam List<Long> groupIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupBindRoleVO>>> result = groupBindRoleService.getGroupBindRolesByGroupIds(groupIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些系统组暂未绑定任何租户角色").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统组租户角色绑定列表成功").setData(result);
    }

    /**
     * 根据租户角色ID查询绑定的系统组 [分页]
     *
     * @param roleId 租户角色ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回绑定该角色的 GroupBindRoleVO 分页列表
     */
    @GetMapping("/by-role/{roleId}")
    public SaResult getGroupBindRolesByRoleId(@PathVariable Long roleId,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<GroupBindRoleVO> result = groupBindRoleService.getGroupBindRolesByRoleId(roleId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户角色暂未绑定任何系统组").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户角色系统组绑定列表成功").setData(result);
    }

    /**
     * 根据多个租户角色ID批量查询绑定的系统组 [分页]
     *
     * @param roleIds 租户角色ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 roleId，值为对应 VO 列表
     */
    @GetMapping("/by-roles")
    public SaResult getGroupBindRolesByRoleIds(@RequestParam List<Long> roleIds,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindRole> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupBindRoleVO>>> result = groupBindRoleService.getGroupBindRolesByRoleIds(roleIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户角色暂未绑定任何系统组").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户角色系统组绑定列表成功").setData(result);
    }

    /**
     * 新增组-租户角色绑定
     *
     * @param groupBindRoleRTO 请求传输对象，包含绑定信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createGroupBindRole(@RequestBody GroupBindRoleRTO groupBindRoleRTO) {
        boolean result = groupBindRoleService.createGroupBindRole(groupBindRoleRTO);
        if (result) {
            return SaResult.ok("创建组租户角色绑定成功");
        }
        return SaResult.error("创建组租户角色绑定失败");
    }

    /**
     * 更新组-租户角色绑定
     *
     * @param id 绑定记录ID
     * @param groupBindRoleRTO 请求传输对象，包含更新信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateGroupBindRole(@PathVariable Long id, @RequestBody GroupBindRoleRTO groupBindRoleRTO) {
        groupBindRoleRTO.setId(id);
        boolean result = groupBindRoleService.updateGroupBindRole(groupBindRoleRTO);
        if (result) {
            return SaResult.ok("更新组租户角色绑定成功");
        }
        return SaResult.error("更新组租户角色绑定失败");
    }

    /**
     * 删除组-租户角色绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteGroupBindRole(@PathVariable Long id) {
        boolean result = groupBindRoleService.deleteGroupBindRole(id);
        if (result) {
            return SaResult.ok("删除组租户角色绑定成功");
        }
        return SaResult.error("删除组租户角色绑定失败");
    }

    /**
     * 软删除组-租户角色绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteGroupBindRole(@PathVariable Long id) {
        boolean result = groupBindRoleService.softDeleteGroupBindRole(id);
        if (result) {
            return SaResult.ok("软删除组租户角色绑定成功");
        }
        return SaResult.error("软删除组租户角色绑定失败");
    }

    /**
     * 批量删除组-租户角色绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteGroupBindRoles(@RequestParam List<Long> ids) {
        boolean result = groupBindRoleService.batchDeleteGroupBindRoles(ids);
        if (result) {
            return SaResult.ok("批量删除组租户角色绑定成功");
        }
        return SaResult.error("批量删除组租户角色绑定失败");
    }

    /**
     * 批量软删除组-租户角色绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部软删除成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteGroupBindRoles(@RequestParam List<Long> ids) {
        boolean result = groupBindRoleService.batchSoftDeleteGroupBindRoles(ids);
        if (result) {
            return SaResult.ok("批量软删除组租户角色绑定成功");
        }
        return SaResult.error("批量软删除组租户角色绑定失败");
    }

}
