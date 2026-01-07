package com.cloudvalley.nebula.ultra.business.log.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.log.model.entity.ComboChangeLog;
import com.cloudvalley.nebula.ultra.business.log.model.rto.ComboChangeLogRTO;
import com.cloudvalley.nebula.ultra.business.log.service.IComboChangeLogService;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.ComboChangeLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐变更日志 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/combo-change-log")
public class ComboChangeLogController {

    @Autowired
    private IComboChangeLogService comboChangeLogService;

    /**
     * 查询套餐变更日志列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志列表
     */
    @GetMapping
    public SaResult getComboChangeLogList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐变更日志数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐变更日志列表成功").setData(result);
    }

    /**
     * 根据ID批量查询变更日志 [分页]
     * @param ids 套餐变更日志ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getComboChangeLogsByIds(@RequestParam List<Long> ids,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无套餐变更日志数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐变更日志信息成功").setData(result);
    }

    /**
     * 根据系统租户ID查询变更日志 [分页] - 查看某租户的套餐变更记录
     * @param sTenantId 系统租户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志列表
     */
    @GetMapping("/by-stenant/{sTenantId}")
    public SaResult getComboChangeLogsBySTenantId(@PathVariable Long sTenantId,
                                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogsBySTenantId(sTenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂无套餐变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户套餐变更记录成功").setData(result);
    }

    /**
     * 根据系统租户ID批量查询变更日志 [分页] - 返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按系统租户ID分组的套餐变更日志分页列表
     */
    @GetMapping("/by-stenants")
    public SaResult getComboChangeLogsBySTenantIds(@RequestParam List<Long> sTenantIds,
                                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboChangeLogVO>>> result = comboChangeLogService.getComboChangeLogsBySTenantIds(sTenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂无套餐变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户套餐变更记录成功").setData(result);
    }

    /**
     * 根据旧系统套餐ID查询变更日志 [分页]
     * @param oldSComboId 旧系统套餐ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志列表
     */
    @GetMapping("/by-old-scombo/{oldSComboId}")
    public SaResult getComboChangeLogsByOldSComboId(@PathVariable Long oldSComboId,
                                                    @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogsByOldSComboId(oldSComboId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该旧套餐暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取旧套餐变更记录成功").setData(result);
    }

    /**
     * 根据旧系统套餐ID批量查询变更日志 [分页] - 返回分组结果
     * @param oldSComboIds 旧系统套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按旧系统套餐ID分组的套餐变更日志分页列表
     */
    @GetMapping("/by-old-scombos")
    public SaResult getComboChangeLogsByOldSComboIds(@RequestParam List<Long> oldSComboIds,
                                                     @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboChangeLogVO>>> result = comboChangeLogService.getComboChangeLogsByOldSComboIds(oldSComboIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些旧套餐暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取旧套餐变更记录成功").setData(result);
    }

    /**
     * 根据新系统套餐ID查询变更日志 [分页]
     * @param newSComboId 新系统套餐ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志列表
     */
    @GetMapping("/by-new-scombo/{newSComboId}")
    public SaResult getComboChangeLogsByNewSComboId(@PathVariable Long newSComboId,
                                                    @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogsByNewSComboId(newSComboId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该新套餐暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取新套餐变更记录成功").setData(result);
    }

    /**
     * 根据新系统套餐ID批量查询变更日志 [分页] - 返回分组结果
     * @param newSComboIds 新系统套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按新系统套餐ID分组的套餐变更日志分页列表
     */
    @GetMapping("/by-new-scombos")
    public SaResult getComboChangeLogsByNewSComboIds(@RequestParam List<Long> newSComboIds,
                                                     @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboChangeLogVO>>> result = comboChangeLogService.getComboChangeLogsByNewSComboIds(newSComboIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些新套餐暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取新套餐变更记录成功").setData(result);
    }

    /**
     * 根据操作人用户ID查询变更日志 [分页]
     * @param operatorUserId 操作人用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 套餐变更日志列表
     */
    @GetMapping("/by-operator/{operatorUserId}")
    public SaResult getComboChangeLogsByOperatorUserId(@PathVariable Long operatorUserId,
                                                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<ComboChangeLogVO> result = comboChangeLogService.getComboChangeLogsByOperatorUserId(operatorUserId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该操作人暂无套餐变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取操作人套餐变更记录成功").setData(result);
    }

    /**
     * 根据操作人用户ID批量查询变更日志 [分页] - 返回分组结果
     * @param operatorUserIds 操作人用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按操作人用户ID分组的套餐变更日志分页列表
     */
    @GetMapping("/by-operators")
    public SaResult getComboChangeLogsByOperatorUserIds(@RequestParam List<Long> operatorUserIds,
                                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<ComboChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<ComboChangeLogVO>>> result = comboChangeLogService.getComboChangeLogsByOperatorUserIds(operatorUserIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些操作人暂无套餐变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取操作人套餐变更记录成功").setData(result);
    }

    /**
     * 新增套餐变更日志
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createComboChangeLog(@RequestBody ComboChangeLogRTO comboChangeLogRTO) {
        boolean result = comboChangeLogService.createComboChangeLog(comboChangeLogRTO);
        if (result) {
            return SaResult.ok("创建套餐变更日志成功");
        }
        return SaResult.error("创建套餐变更日志失败");
    }

    /**
     * 更新套餐变更日志
     * @param id 套餐变更日志ID
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateComboChangeLog(@PathVariable Long id, @RequestBody ComboChangeLogRTO comboChangeLogRTO) {
        comboChangeLogRTO.setId(id);
        boolean result = comboChangeLogService.updateComboChangeLog(comboChangeLogRTO);
        if (result) {
            return SaResult.ok("更新套餐变更日志成功");
        }
        return SaResult.error("更新套餐变更日志失败");
    }

    /**
     * 更新套餐变更日志备注
     * @param id 套餐变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    @PatchMapping("/{id}/remark")
    public SaResult updateComboChangeLogRemark(@PathVariable Long id, @RequestParam String remark) {
        boolean result = comboChangeLogService.updateComboChangeLogRemark(id, remark);
        if (result) {
            return SaResult.ok("更新套餐变更日志备注成功");
        }
        return SaResult.error("更新套餐变更日志备注失败");
    }

    /**
     * 删除变更日志（真删）
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteComboChangeLog(@PathVariable Long id) {
        boolean result = comboChangeLogService.deleteComboChangeLog(id);
        if (result) {
            return SaResult.ok("删除套餐变更日志成功");
        }
        return SaResult.error("删除套餐变更日志失败");
    }

    /**
     * 软删除变更日志
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteComboChangeLog(@PathVariable Long id) {
        boolean result = comboChangeLogService.softDeleteComboChangeLog(id);
        if (result) {
            return SaResult.ok("软删除套餐变更日志成功");
        }
        return SaResult.error("软删除套餐变更日志失败");
    }

    /**
     * 批量删除变更日志（真删）
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteComboChangeLogs(@RequestParam List<Long> ids) {
        boolean result = comboChangeLogService.batchDeleteComboChangeLogs(ids);
        if (result) {
            return SaResult.ok("批量删除套餐变更日志成功");
        }
        return SaResult.error("批量删除套餐变更日志失败");
    }

    /**
     * 批量软删除变更日志
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteComboChangeLogs(@RequestParam List<Long> ids) {
        boolean result = comboChangeLogService.batchSoftDeleteComboChangeLogs(ids);
        if (result) {
            return SaResult.ok("批量软删除套餐变更日志成功");
        }
        return SaResult.error("批量软删除套餐变更日志失败");
    }

}
