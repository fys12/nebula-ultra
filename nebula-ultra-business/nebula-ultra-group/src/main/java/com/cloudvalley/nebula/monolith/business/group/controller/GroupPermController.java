package com.cloudvalley.nebula.monolith.business.group.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupPerm;
import com.cloudvalley.nebula.monolith.business.group.model.rto.GroupPermRTO;
import com.cloudvalley.nebula.monolith.business.group.service.IGroupPermService;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupPermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限组-权限关联表 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/group-perm")
public class GroupPermController {

    @Autowired
    private IGroupPermService groupPermService;

    /**
     * 查询权限组-权限关联列表 [分页]
     *
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupPermVO 列表
     */
    @GetMapping
    public SaResult getGroupPermList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<GroupPermVO> result = groupPermService.getGroupPermList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限组关联数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限组关联列表成功").setData(result);
    }

    /**
     * 根据ID列表批量查询权限组-权限关联 [分页]
     *
     * @param ids 关联记录ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupPermVO 列表
     */
    @GetMapping("/batch")
    public SaResult getGroupPermsByIds(@RequestParam List<Long> ids,
                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<GroupPermVO> result = groupPermService.getGroupPermsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无权限组关联数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限组关联信息成功").setData(result);
    }

    /**
     * 根据系统组ID查询其绑定的权限 [分页]
     *
     * @param groupId 系统组ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回该组绑定的 GroupPermVO 分页列表
     */
    @GetMapping("/by-group/{groupId}")
    public SaResult getGroupPermsByGroupId(@PathVariable Long groupId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<GroupPermVO> result = groupPermService.getGroupPermsByGroupId(groupId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该系统组暂无权限关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统组权限关联列表成功").setData(result);
    }

    /**
     * 根据多个系统组ID批量查询权限关联 [分页]，并按组ID分组返回
     *
     * @param groupIds 系统组ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 groupId，值为对应 VO 列表
     */
    @GetMapping("/by-groups")
    public SaResult getGroupPermsByGroupIds(@RequestParam List<Long> groupIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupPermVO>>> result = groupPermService.getGroupPermsByGroupIds(groupIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些系统组暂无权限关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统组权限关联列表成功").setData(result);
    }

    /**
     * 根据租户权限ID查询绑定的系统组 [分页]
     *
     * @param permId 租户权限ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回绑定该权限的 GroupPermVO 分页列表
     */
    @GetMapping("/by-perm/{permId}")
    public SaResult getGroupPermsByPermId(@PathVariable Long permId,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<GroupPermVO> result = groupPermService.getGroupPermsByPermId(permId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该权限暂未被任何组关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取权限组关联列表成功").setData(result);
    }

    /**
     * 根据多个租户权限ID批量查询绑定的系统组 [分页]，并按权限ID分组返回
     *
     * @param permIds 租户权限ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 permId，值为对应 VO 列表
     */
    @GetMapping("/by-perms")
    public SaResult getGroupPermsByPermIds(@RequestParam List<Long> permIds,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupPerm> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupPermVO>>> result = groupPermService.getGroupPermsByPermIds(permIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些权限暂未被任何组关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取权限组关联列表成功").setData(result);
    }

    /**
     * 新增权限组-权限绑定关系
     *
     * @param groupPermRTO 请求传输对象，包含绑定信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createGroupPerm(@RequestBody GroupPermRTO groupPermRTO) {
        boolean result = groupPermService.createGroupPerm(groupPermRTO);
        if (result) {
            return SaResult.ok("创建权限组关联成功");
        }
        return SaResult.error("创建权限组关联失败");
    }

    /**
     * 更新权限组-权限绑定关系
     *
     * @param id 绑定记录ID
     * @param groupPermRTO 请求传输对象，包含更新信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateGroupPerm(@PathVariable Long id, @RequestBody GroupPermRTO groupPermRTO) {
        groupPermRTO.setId(id);
        boolean result = groupPermService.updateGroupPerm(groupPermRTO);
        if (result) {
            return SaResult.ok("更新权限组关联成功");
        }
        return SaResult.error("更新权限组关联失败");
    }

    /**
     * 删除权限组-权限绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteGroupPerm(@PathVariable Long id) {
        boolean result = groupPermService.deleteGroupPerm(id);
        if (result) {
            return SaResult.ok("删除权限组关联成功");
        }
        return SaResult.error("删除权限组关联失败");
    }

    /**
     * 软删除权限组-权限绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteGroupPerm(@PathVariable Long id) {
        boolean result = groupPermService.softDeleteGroupPerm(id);
        if (result) {
            return SaResult.ok("软删除权限组关联成功");
        }
        return SaResult.error("软删除权限组关联失败");
    }

    /**
     * 批量删除权限组-权限绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteGroupPerms(@RequestParam List<Long> ids) {
        boolean result = groupPermService.batchDeleteGroupPerms(ids);
        if (result) {
            return SaResult.ok("批量删除权限组关联成功");
        }
        return SaResult.error("批量删除权限组关联失败");
    }

    /**
     * 批量软删除权限组-权限绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部软删除成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteGroupPerms(@RequestParam List<Long> ids) {
        boolean result = groupPermService.batchSoftDeleteGroupPerms(ids);
        if (result) {
            return SaResult.ok("批量软删除权限组关联成功");
        }
        return SaResult.error("批量软删除权限组关联失败");
    }

}
