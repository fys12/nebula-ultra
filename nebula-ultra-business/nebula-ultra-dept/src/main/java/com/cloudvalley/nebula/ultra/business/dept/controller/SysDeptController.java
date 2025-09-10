package com.cloudvalley.nebula.ultra.business.dept.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.SysDept;
import com.cloudvalley.nebula.ultra.business.dept.model.rto.SysDeptRTO;
import com.cloudvalley.nebula.ultra.business.dept.service.ISysDeptService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/sys-dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 分页查询系统部门列表。
     *
     * @param current 当前页码，默认为 1
     * @param size    每页显示数量，默认为 10
     * @return SaResult 封装响应结果，包含分页的 SysDeptVO 数据；
     *         若无数据则返回提示信息和空分页对象
     */
    @GetMapping
    public SaResult getSysDeptList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysDept> page = new Page<>(current, size);
        IPage<SysDeptVO> result = sysDeptService.getSysDeptList(page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统部门数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统部门列表成功").setData(result);
    }

    /**
     * 根据多个部门 ID 分页批量查询系统部门信息。
     *
     * @param ids    要查询的系统部门 ID 列表
     * @param current 当前页码，默认为 1
     * @param size    每页显示数量，默认为 10
     * @return SaResult 封装响应结果，包含匹配的分页 SysDeptVO 数据；
     *         若无匹配数据则返回提示信息和空分页对象
     */
    @GetMapping("/batch")
    public SaResult getSysDeptsByIds(@RequestParam List<Long> ids,
                                     @RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysDept> page = new Page<>(current, size);
        IPage<SysDeptVO> result = sysDeptService.getSysDeptsByIds(ids, page);
        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统部门数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取系统部门信息成功").setData(result);
    }

    /**
     * 根据多个部门 ID 查询所有匹配的系统部门信息（全量，无分页）。
     *
     * @param ids 要查询的系统部门 ID 列表
     * @return SaResult 封装响应结果，包含全部匹配的 SysDeptVO 列表；
     *         若无数据则返回空列表及提示信息
     */
    @GetMapping("/batch/all")
    public SaResult getSysDeptsByIdsAll(@RequestParam List<Long> ids) {
        List<SysDeptVO> result = sysDeptService.getSysDeptsByIds(ids);
        if (result.isEmpty()) {
            return SaResult.ok("暂无系统部门数据").setData(result);
        }
        return SaResult.ok("批量获取系统部门信息成功").setData(result);
    }

    /**
     * 新增一个系统部门。
     *
     * @param sysDeptRTO 请求传输对象，包含创建所需字段（如部门名、编码、父级ID等）
     * @return SaResult 封装操作结果；
     *         创建成功返回成功提示，失败则返回错误信息
     */
    @PostMapping
    public SaResult createSysDept(@RequestBody SysDeptRTO sysDeptRTO) {
        boolean result = sysDeptService.createSysDept(sysDeptRTO);
        if (result) {
            return SaResult.ok("创建系统部门成功");
        }
        return SaResult.error("创建系统部门失败");
    }

    /**
     * 更新指定 ID 的系统部门信息。
     *
     * @param id           系统部门唯一标识符（路径参数）
     * @param sysDeptRTO   请求传输对象，包含更新字段；方法内部会设置 ID
     * @return SaResult 封装操作结果；
     *         更新成功返回成功提示，失败或记录不存在则返回错误信息
     */
    @PutMapping("/{id}")
    public SaResult updateSysDept(@PathVariable Long id, @RequestBody SysDeptRTO sysDeptRTO) {
        sysDeptRTO.setId(id);
        boolean result = sysDeptService.updateSysDept(sysDeptRTO);
        if (result) {
            return SaResult.ok("更新系统部门成功");
        }
        return SaResult.error("更新系统部门失败");
    }

    /**
     * 更新系统部门的启用状态（启用/禁用）。
     *
     * @param id    系统部门唯一标识符
     * @param state 目标状态值：true 表示启用，false 表示禁用
     * @return SaResult 封装状态更新结果；
     *         成功则返回成功提示，失败则返回错误信息
     */
    @PatchMapping("/{id}/state")
    public SaResult updateSysDeptState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = sysDeptService.updateSysDeptState(id, state);
        if (result) {
            return SaResult.ok("更新系统部门状态成功");
        }
        return SaResult.error("更新系统部门状态失败");
    }

    /**
     * 物理删除一个系统部门（不可恢复）。
     *
     * @param id 系统部门唯一标识符
     * @return SaResult 封装删除结果；
     *         成功则返回成功提示，失败（如记录不存在）则返回错误信息
     */
    @DeleteMapping("/{id}")
    public SaResult deleteSysDept(@PathVariable Long id) {
        boolean result = sysDeptService.deleteSysDept(id);
        if (result) {
            return SaResult.ok("删除系统部门成功");
        }
        return SaResult.error("删除系统部门失败");
    }

    /**
     * 软删除一个系统部门（标记 deleted = true，数据保留）。
     *
     * @param id 系统部门唯一标识符
     * @return SaResult 封装软删除结果；
     *         成功则返回成功提示，失败则返回错误信息
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteSysDept(@PathVariable Long id) {
        boolean result = sysDeptService.softDeleteSysDept(id);
        if (result) {
            return SaResult.ok("软删除系统部门成功");
        }
        return SaResult.error("软删除系统部门失败");
    }

    /**
     * 批量物理删除多个系统部门（不可恢复）。
     *
     * @param ids 要删除的系统部门 ID 列表
     * @return SaResult 封装批量删除结果；
     *         全部删除成功返回成功提示，否则返回错误信息（不支持部分成功提示）
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteSysDepts(@RequestParam List<Long> ids) {
        boolean result = sysDeptService.batchDeleteSysDepts(ids);
        if (result) {
            return SaResult.ok("批量删除系统部门成功");
        }
        return SaResult.error("批量删除系统部门失败");
    }

    /**
     * 批量软删除多个系统部门（标记 deleted = true，数据保留）。
     *
     * @param ids 要软删除的系统部门 ID 列表
     * @return SaResult 封装批量软删除结果；
     *         成功则返回成功提示，失败则返回错误信息
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteSysDepts(@RequestParam List<Long> ids) {
        boolean result = sysDeptService.batchSoftDeleteSysDepts(ids);
        if (result) {
            return SaResult.ok("批量软删除系统部门成功");
        }
        return SaResult.error("批量软删除系统部门失败");
    }

}
