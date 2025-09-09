package com.cloudvalley.nebula.monolith.business.group.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupTenant;
import com.cloudvalley.nebula.monolith.business.group.model.rto.GroupTenantRTO;
import com.cloudvalley.nebula.monolith.business.group.service.IGroupTenantService;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/group-tenant")
public class GroupTenantController {

    @Autowired
    private IGroupTenantService groupTenantService;

    /**
     * 分页查询租户-组绑定列表
     *
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupTenantVO 列表
     */
    @GetMapping
    public SaResult getGroupTenantList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<GroupTenantVO> result = groupTenantService.getGroupTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户组绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户组绑定列表成功").setData(result);
    }

    /**
     * 根据ID列表批量查询租户-组绑定 [分页]
     *
     * @param ids 绑定记录ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupTenantVO 列表
     */
    @GetMapping("/batch")
    public SaResult getGroupTenantsByIds(@RequestParam List<Long> ids,
                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<GroupTenantVO> result = groupTenantService.getGroupTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户组绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户组绑定信息成功").setData(result);
    }

    /**
     * 根据系统租户ID查询其绑定的系统组 [分页]
     *
     * @param sTenantId 系统租户ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回该租户绑定的 GroupTenantVO 分页列表
     */
    @GetMapping("/by-stenant/{sTenantId}")
    public SaResult getGroupTenantsBySTenantId(@PathVariable Long sTenantId,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<GroupTenantVO> result = groupTenantService.getGroupTenantsBySTenantId(sTenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂未绑定任何组").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户绑定组列表成功").setData(result);
    }

    /**
     * 根据多个系统租户ID批量查询绑定关系 [分页]，并按租户ID分组返回
     *
     * @param sTenantIds 系统租户ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 sTenantId，值为对应 VO 列表
     */
    @GetMapping("/by-stenants")
    public SaResult getGroupTenantsBySTenantIds(@RequestParam List<Long> sTenantIds,
                                                @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupTenantVO>>> result = groupTenantService.getGroupTenantsBySTenantIds(sTenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂未绑定任何组").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户绑定组列表成功").setData(result);
    }

    /**
     * 根据系统组ID查询绑定该组的系统租户 [分页]
     *
     * @param sGroupId 系统组ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回绑定该组的 GroupTenantVO 分页列表
     */
    @GetMapping("/by-sgroup/{sGroupId}")
    public SaResult getGroupTenantsBySGroupId(@PathVariable Long sGroupId,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<GroupTenantVO> result = groupTenantService.getGroupTenantsBySGroupId(sGroupId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该组暂未被任何租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取组绑定租户列表成功").setData(result);
    }

    /**
     * 根据多个系统组ID批量查询绑定关系 [分页]，并按组ID分组返回
     *
     * @param sGroupIds 系统组ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为 sGroupId，值为对应 VO 列表
     */
    @GetMapping("/by-sgroups")
    public SaResult getGroupTenantsBySGroupIds(@RequestParam List<Long> sGroupIds,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupTenantVO>>> result = groupTenantService.getGroupTenantsBySGroupIds(sGroupIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些组暂未被任何租户绑定").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取组绑定租户列表成功").setData(result);
    }

    /**
     * 新增租户-组绑定关系
     *
     * @param groupTenantRTO 请求传输对象，包含绑定信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createGroupTenant(@RequestBody GroupTenantRTO groupTenantRTO) {
        boolean result = groupTenantService.createGroupTenant(groupTenantRTO);
        if (result) {
            return SaResult.ok("创建组租户绑定成功");
        }
        return SaResult.error("创建组租户绑定失败");
    }

    /**
     * 更新租户-组绑定关系
     *
     * @param id 绑定记录ID
     * @param groupTenantRTO 请求传输对象，包含更新信息
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateGroupTenant(@PathVariable Long id, @RequestBody GroupTenantRTO groupTenantRTO) {
        groupTenantRTO.setId(id);
        boolean result = groupTenantService.updateGroupTenant(groupTenantRTO);
        if (result) {
            return SaResult.ok("更新组租户绑定成功");
        }
        return SaResult.error("更新组租户绑定失败");
    }

    /**
     * 删除租户-组绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteGroupTenant(@PathVariable Long id) {
        boolean result = groupTenantService.deleteGroupTenant(id);
        if (result) {
            return SaResult.ok("删除组租户绑定成功");
        }
        return SaResult.error("删除组租户绑定失败");
    }

    /**
     * 软删除租户-组绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return SaResult 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteGroupTenant(@PathVariable Long id) {
        boolean result = groupTenantService.softDeleteGroupTenant(id);
        if (result) {
            return SaResult.ok("软删除组租户绑定成功");
        }
        return SaResult.error("软删除组租户绑定失败");
    }

    /**
     * 批量删除租户-组绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteGroupTenants(@RequestParam List<Long> ids) {
        boolean result = groupTenantService.batchDeleteGroupTenants(ids);
        if (result) {
            return SaResult.ok("批量删除组租户绑定成功");
        }
        return SaResult.error("批量删除组租户绑定失败");
    }

    /**
     * 批量软删除租户-组绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return SaResult 全部软删除成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteGroupTenants(@RequestParam List<Long> ids) {
        boolean result = groupTenantService.batchSoftDeleteGroupTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除组租户绑定成功");
        }
        return SaResult.error("批量软删除组租户绑定失败");
    }
    
}
