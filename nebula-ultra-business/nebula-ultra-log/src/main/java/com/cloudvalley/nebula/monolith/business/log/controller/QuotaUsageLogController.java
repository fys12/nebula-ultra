package com.cloudvalley.nebula.monolith.business.log.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaUsageLog;
import com.cloudvalley.nebula.monolith.business.log.model.rto.QuotaUsageLogRTO;
import com.cloudvalley.nebula.monolith.business.log.service.IQuotaUsageLogService;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaUsageLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额使用流水 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/quota-usage-log")
public class QuotaUsageLogController {

    @Autowired
    private IQuotaUsageLogService quotaUsageLogService;

    /**
     * 查询配额使用流水列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额使用流水列表
     */
    @GetMapping
    public SaResult getQuotaUsageLogList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<QuotaUsageLogVO> result = quotaUsageLogService.getQuotaUsageLogList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无配额使用流水数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取配额使用流水列表成功").setData(result);
    }

    /**
     * 根据ID批量查询使用流水 [分页]
     * @param ids 配额使用流水ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额使用流水信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getQuotaUsageLogsByIds(@RequestParam List<Long> ids,
                                           @RequestParam(value = "current", defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<QuotaUsageLogVO> result = quotaUsageLogService.getQuotaUsageLogsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无配额使用流水数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取配额使用流水信息成功").setData(result);
    }

    /**
     * 根据租户配额ID查询使用流水 [分页] - 查看某配额的使用记录
     * @param tQuotaId 租户配额ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额使用流水列表
     */
    @GetMapping("/by-tquota/{tQuotaId}")
    public SaResult getQuotaUsageLogsByTQuotaId(@PathVariable Long tQuotaId,
                                                @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<QuotaUsageLogVO> result = quotaUsageLogService.getQuotaUsageLogsByTQuotaId(tQuotaId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该配额暂无使用记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取配额使用记录成功").setData(result);
    }

    /**
     * 根据租户配额ID批量查询使用流水 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户配额ID分组的配额使用流水分页列表
     */
    @GetMapping("/by-tquotas")
    public SaResult getQuotaUsageLogsByTQuotaIds(@RequestParam List<Long> tQuotaIds,
                                                 @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaUsageLogVO>>> result = quotaUsageLogService.getQuotaUsageLogsByTQuotaIds(tQuotaIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些配额暂无使用记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取配额使用记录成功").setData(result);
    }

    /**
     * 根据操作人用户ID查询使用流水 [分页]
     * @param userId 操作人用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额使用流水列表
     */
    @GetMapping("/by-user/{userId}")
    public SaResult getQuotaUsageLogsByUserId(@PathVariable Long userId,
                                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<QuotaUsageLogVO> result = quotaUsageLogService.getQuotaUsageLogsByUserId(userId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该用户暂无配额使用记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户配额使用记录成功").setData(result);
    }

    /**
     * 根据操作人用户ID批量查询使用流水 [分页] - 返回分组结果
     * @param userIds 操作人用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按操作人用户ID分组的配额使用流水分页列表
     */
    @GetMapping("/by-users")
    public SaResult getQuotaUsageLogsByUserIds(@RequestParam List<Long> userIds,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaUsageLog> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaUsageLogVO>>> result = quotaUsageLogService.getQuotaUsageLogsByUserIds(userIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些用户暂无配额使用记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户配额使用记录成功").setData(result);
    }

    /**
     * 新增配额使用流水
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createQuotaUsageLog(@RequestBody QuotaUsageLogRTO quotaUsageLogRTO) {
        boolean result = quotaUsageLogService.createQuotaUsageLog(quotaUsageLogRTO);
        if (result) {
            return SaResult.ok("创建配额使用流水成功");
        }
        return SaResult.error("创建配额使用流水失败");
    }

    /**
     * 更新配额使用流水
     * @param id 配额使用流水ID
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateQuotaUsageLog(@PathVariable Long id, @RequestBody QuotaUsageLogRTO quotaUsageLogRTO) {
        quotaUsageLogRTO.setId(id);
        boolean result = quotaUsageLogService.updateQuotaUsageLog(quotaUsageLogRTO);
        if (result) {
            return SaResult.ok("更新配额使用流水成功");
        }
        return SaResult.error("更新配额使用流水失败");
    }

    /**
     * 更新流水备注
     * @param id 配额使用流水ID
     * @param remark 备注
     * @return 操作结果
     */
    @PatchMapping("/{id}/remark")
    public SaResult updateQuotaUsageLogRemark(@PathVariable Long id, @RequestParam String remark) {
        boolean result = quotaUsageLogService.updateQuotaUsageLogRemark(id, remark);
        if (result) {
            return SaResult.ok("更新配额使用流水备注成功");
        }
        return SaResult.error("更新配额使用流水备注失败");
    }

    /**
     * 删除使用流水（真删）
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteQuotaUsageLog(@PathVariable Long id) {
        boolean result = quotaUsageLogService.deleteQuotaUsageLog(id);
        if (result) {
            return SaResult.ok("删除配额使用流水成功");
        }
        return SaResult.error("删除配额使用流水失败");
    }

    /**
     * 软删除使用流水
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteQuotaUsageLog(@PathVariable Long id) {
        boolean result = quotaUsageLogService.softDeleteQuotaUsageLog(id);
        if (result) {
            return SaResult.ok("软删除配额使用流水成功");
        }
        return SaResult.error("软删除配额使用流水失败");
    }

    /**
     * 批量删除使用流水（真删）
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteQuotaUsageLogs(@RequestParam List<Long> ids) {
        boolean result = quotaUsageLogService.batchDeleteQuotaUsageLogs(ids);
        if (result) {
            return SaResult.ok("批量删除配额使用流水成功");
        }
        return SaResult.error("批量删除配额使用流水失败");
    }

    /**
     * 批量软删除使用流水
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteQuotaUsageLogs(@RequestParam List<Long> ids) {
        boolean result = quotaUsageLogService.batchSoftDeleteQuotaUsageLogs(ids);
        if (result) {
            return SaResult.ok("批量软删除配额使用流水成功");
        }
        return SaResult.error("批量软删除配额使用流水失败");
    }

}
