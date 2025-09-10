package com.cloudvalley.nebula.ultra.business.group.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindDept;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupBindDeptRTO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupBindDeptService;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户部门绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/group-bind-dept")
public class GroupBindDeptController {

    @Autowired
    private IGroupBindDeptService groupBindDeptService;

    /**
     * 查询组-租户部门绑定列表 [分页]
     *
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindDeptVO 列表，封装在 SaResult 中
     */
    @GetMapping
    public SaResult getGroupBindDeptList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<GroupBindDeptVO> result = groupBindDeptService.getGroupBindDeptList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无组-租户部门绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取组-租户部门绑定列表成功").setData(result);
    }

    /**
     * 根据ID批量查询绑定关系 [分页]
     *
     * @param ids 组-租户部门绑定记录ID列表
     * @param current 当前页码，从1开始，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindDeptVO 列表，封装在 SaResult 中
     */
    @GetMapping("/batch")
    public SaResult getGroupBindDeptsByIds(@RequestParam List<Long> ids,
                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<GroupBindDeptVO> result = groupBindDeptService.getGroupBindDeptsByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无组-租户部门绑定数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取组-租户部门绑定信息成功").setData(result);
    }

    /**
     * 根据系统组ID查询绑定关系 [分页] - 查看某组关联的租户部门
     *
     * @param sGroupId 系统组ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindDeptVO 列表，表示该组绑定的租户部门，封装在 SaResult 中
     */
    @GetMapping("/by-sgroup/{sGroupId}")
    public SaResult getGroupBindDeptsBySGroupId(@PathVariable Long sGroupId,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<GroupBindDeptVO> result = groupBindDeptService.getGroupBindDeptsBySGroupId(sGroupId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该组暂无绑定的租户部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取组绑定的租户部门列表成功").setData(result);
    }

    /**
     * 根据系统组ID批量查询绑定关系 [分页] - 返回分组结果
     *
     * @param sGroupIds 系统组ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为系统组ID，值为对应绑定的 GroupBindDeptVO 列表，封装在 SaResult 中
     */
    @GetMapping("/by-sgroups")
    public SaResult getGroupBindDeptsBySGroupIds(@RequestParam List<Long> sGroupIds,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupBindDeptVO>>> result = groupBindDeptService.getGroupBindDeptsBySGroupIds(sGroupIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些组暂无绑定的租户部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取组绑定的租户部门列表成功").setData(result);
    }

    /**
     * 根据租户部门ID查询绑定关系 [分页] - 查看某租户部门被哪些组关联
     *
     * @param tDeptId 租户部门ID
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 GroupBindDeptVO 列表，表示关联该租户部门的组信息，封装在 SaResult 中
     */
    @GetMapping("/by-tdept/{tDeptId}")
    public SaResult getGroupBindDeptsByTDeptId(@PathVariable Long tDeptId,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<GroupBindDeptVO> result = groupBindDeptService.getGroupBindDeptsByTDeptId(tDeptId, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户部门暂未被组关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户部门关联的组列表成功").setData(result);
    }

    /**
     * 根据租户部门ID批量查询绑定关系 [分页] - 返回分组结果
     *
     * @param tDeptIds 租户部门ID列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return SaResult 返回分页的 Map，键为租户部门ID，值为对应关联的 GroupBindDeptVO 列表，封装在 SaResult 中
     */
    @GetMapping("/by-tdepts")
    public SaResult getGroupBindDeptsByTDeptIds(@RequestParam List<Long> tDeptIds,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GroupBindDept> page = new Page<>(current, size);
        IPage<Map<Long, List<GroupBindDeptVO>>> result = groupBindDeptService.getGroupBindDeptsByTDeptIds(tDeptIds, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户部门暂未被组关联").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户部门关联的组列表成功").setData(result);
    }

    /**
     * 新增组-租户部门绑定
     *
     * @param groupDeptRTO 请求传输对象，包含绑定所需信息
     * @return SaResult 返回操作结果布尔值，true表示创建成功，false表示失败，封装在 SaResult 中
     */
    @PostMapping
    public SaResult createGroupBindDept(@RequestBody GroupBindDeptRTO groupDeptRTO) {
        boolean result = groupBindDeptService.createGroupBindDept(groupDeptRTO);
        if (result) {
            return SaResult.ok("创建组-租户部门绑定成功");
        }
        return SaResult.error("创建组-租户部门绑定失败");
    }

    /**
     * 更新组-租户部门绑定
     *
     * @param id 组-租户部门绑定记录的唯一标识
     * @param groupDeptRTO 请求传输对象，包含更新信息
     * @return SaResult 返回操作结果布尔值，true表示更新成功，false表示失败，封装在 SaResult 中
     */
    @PutMapping("/{id}")
    public SaResult updateGroupBindDept(@PathVariable Long id, @RequestBody GroupBindDeptRTO groupDeptRTO) {
        groupDeptRTO.setId(id);
        boolean result = groupBindDeptService.updateGroupBindDept(groupDeptRTO);
        if (result) {
            return SaResult.ok("更新组-租户部门绑定成功");
        }
        return SaResult.error("更新组-租户部门绑定失败");
    }

    /**
     * 删除组-租户部门绑定（真删）
     *
     * @param id 组-租户部门绑定记录的唯一标识
     * @return SaResult 返回操作结果布尔值，true表示删除成功，false表示失败，封装在 SaResult 中
     */
    @DeleteMapping("/{id}")
    public SaResult deleteGroupBindDept(@PathVariable Long id) {
        boolean result = groupBindDeptService.deleteGroupBindDept(id);
        if (result) {
            return SaResult.ok("删除组-租户部门绑定成功");
        }
        return SaResult.error("删除组-租户部门绑定失败");
    }

    /**
     * 软删除组-租户部门绑定
     *
     * @param id 组-租户部门绑定记录的唯一标识
     * @return SaResult 返回操作结果布尔值，true表示软删除成功，false表示失败，封装在 SaResult 中
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteGroupBindDept(@PathVariable Long id) {
        boolean result = groupBindDeptService.softDeleteGroupBindDept(id);
        if (result) {
            return SaResult.ok("软删除组-租户部门绑定成功");
        }
        return SaResult.error("软删除组-租户部门绑定失败");
    }

    /**
     * 批量删除组-租户部门绑定（真删）
     *
     * @param ids 组-租户部门绑定记录ID列表
     * @return SaResult 返回操作结果布尔值，true表示批量删除成功，false表示失败，封装在 SaResult 中
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteGroupBindDepts(@RequestParam List<Long> ids) {
        boolean result = groupBindDeptService.batchDeleteGroupBindDepts(ids);
        if (result) {
            return SaResult.ok("批量删除组-租户部门绑定成功");
        }
        return SaResult.error("批量删除组-租户部门绑定失败");
    }

    /**
     * 批量软删除组-租户部门绑定
     *
     * @param ids 组-租户部门绑定记录ID列表
     * @return SaResult 返回操作结果布尔值，true表示批量软删除成功，false表示失败，封装在 SaResult 中
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteGroupBindDepts(@RequestParam List<Long> ids) {
        boolean result = groupBindDeptService.batchSoftDeleteGroupBindDepts(ids);
        if (result) {
            return SaResult.ok("批量软删除组-租户部门绑定成功");
        }
        return SaResult.error("批量软删除组-租户部门绑定失败");
    }

}
