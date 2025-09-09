package com.cloudvalley.nebula.monolith.business.log.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaChangeLog;
import com.cloudvalley.nebula.monolith.business.log.model.rto.QuotaChangeLogRTO;
import com.cloudvalley.nebula.monolith.business.log.service.IQuotaChangeLogService;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaChangeLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额变更日志 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/quota-change-log")
public class QuotaChangeLogController {

    @Autowired
    private IQuotaChangeLogService quotaChangeLogService;

    /**
     * 查询配额变更日志列表 [分页]
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额变更日志列表
     */
    @GetMapping
    public SaResult getQuotaChangeLogList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<QuotaChangeLogVO> result = quotaChangeLogService.getQuotaChangeLogList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无配额变更日志数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取配额变更日志列表成功").setData(result);
    }

    /**
     * 根据ID批量查询变更日志 [分页]
     * @param ids 配额变更日志ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额变更日志信息分页列表
     */
    @GetMapping("/batch")
    public SaResult getQuotaChangeLogsByIds(@RequestParam List<Long> ids,
                                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<QuotaChangeLogVO> result = quotaChangeLogService.getQuotaChangeLogsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无配额变更日志数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取配额变更日志信息成功").setData(result);
    }

    /**
     * 根据租户配额ID查询变更日志 [分页] - 查看某配额的变更记录
     * @param tQuotaId 租户配额ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额变更日志列表
     */
    @GetMapping("/by-tquota/{tQuotaId}")
    public SaResult getQuotaChangeLogsByTQuotaId(@PathVariable Long tQuotaId,
                                                 @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<QuotaChangeLogVO> result = quotaChangeLogService.getQuotaChangeLogsByTQuotaId(tQuotaId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该配额暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取配额变更记录成功").setData(result);
    }

    /**
     * 根据租户配额ID批量查询变更日志 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按租户配额ID分组的配额变更日志分页列表
     */
    @GetMapping("/by-tquotas")
    public SaResult getQuotaChangeLogsByTQuotaIds(@RequestParam List<Long> tQuotaIds,
                                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaChangeLogVO>>> result = quotaChangeLogService.getQuotaChangeLogsByTQuotaIds(tQuotaIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些配额暂无变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取配额变更记录成功").setData(result);
    }

    /**
     * 根据申请人系统用户ID查询变更日志 [分页]
     * @param applicantSUserId 申请人系统用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额变更日志列表
     */
    @GetMapping("/by-applicant/{applicantSUserId}")
    public SaResult getQuotaChangeLogsByApplicantSUserId(@PathVariable Long applicantSUserId,
                                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<QuotaChangeLogVO> result = quotaChangeLogService.getQuotaChangeLogsByApplicantSUserId(applicantSUserId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该申请人暂无配额变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取申请人配额变更记录成功").setData(result);
    }

    /**
     * 根据申请人系统用户ID批量查询变更日志 [分页] - 返回分组结果
     * @param applicantSUserIds 申请人系统用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按申请人系统用户ID分组的配额变更日志分页列表
     */
    @GetMapping("/by-applicants")
    public SaResult getQuotaChangeLogsByApplicantSUserIds(@RequestParam List<Long> applicantSUserIds,
                                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaChangeLogVO>>> result = quotaChangeLogService.getQuotaChangeLogsByApplicantSUserIds(applicantSUserIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些申请人暂无配额变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取申请人配额变更记录成功").setData(result);
    }

    /**
     * 根据审批人系统用户ID查询变更日志 [分页]
     * @param approverSUserId 审批人系统用户ID
     * @param current 当前页码
     * @param size 每页大小
     * @return 配额变更日志列表
     */
    @GetMapping("/by-approver/{approverSUserId}")
    public SaResult getQuotaChangeLogsByApproverSUserId(@PathVariable Long approverSUserId,
                                                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<QuotaChangeLogVO> result = quotaChangeLogService.getQuotaChangeLogsByApproverSUserId(approverSUserId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该审批人暂无配额变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取审批人配额变更记录成功").setData(result);
    }

    /**
     * 根据审批人系统用户ID批量查询变更日志 [分页] - 返回分组结果
     * @param approverSUserIds 审批人系统用户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 按审批人系统用户ID分组的配额变更日志分页列表
     */
    @GetMapping("/by-approvers")
    public SaResult getQuotaChangeLogsByApproverSUserIds(@RequestParam List<Long> approverSUserIds,
                                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaChangeLog> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaChangeLogVO>>> result = quotaChangeLogService.getQuotaChangeLogsByApproverSUserIds(approverSUserIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些审批人暂无配额变更记录").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取审批人配额变更记录成功").setData(result);
    }

    /**
     * 新增配额变更日志
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    @PostMapping
    public SaResult createQuotaChangeLog(@RequestBody QuotaChangeLogRTO quotaChangeLogRTO) {
        boolean result = quotaChangeLogService.createQuotaChangeLog(quotaChangeLogRTO);
        if (result) {
            return SaResult.ok("创建配额变更日志成功");
        }
        return SaResult.error("创建配额变更日志失败");
    }

    /**
     * 更新配额变更日志
     * @param id 配额变更日志ID
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public SaResult updateQuotaChangeLog(@PathVariable Long id, @RequestBody QuotaChangeLogRTO quotaChangeLogRTO) {
        quotaChangeLogRTO.setId(id);
        boolean result = quotaChangeLogService.updateQuotaChangeLog(quotaChangeLogRTO);
        if (result) {
            return SaResult.ok("更新配额变更日志成功");
        }
        return SaResult.error("更新配额变更日志失败");
    }

    /**
     * 更新审批状态及审批人信息
     * @param id 配额变更日志ID
     * @param approvalStatus 审批状态
     * @param approverSUserId 审批人系统用户ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/approval")
    public SaResult updateApprovalInfo(@PathVariable Long id,
                                       @RequestParam String approvalStatus,
                                       @RequestParam Long approverSUserId) {
        boolean result = quotaChangeLogService.updateApprovalInfo(id, approvalStatus, approverSUserId);
        if (result) {
            return SaResult.ok("更新审批信息成功");
        }
        return SaResult.error("更新审批信息失败");
    }

    /**
     * 更新日志备注
     * @param id 配额变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    @PatchMapping("/{id}/remark")
    public SaResult updateQuotaChangeLogRemark(@PathVariable Long id, @RequestParam String remark) {
        boolean result = quotaChangeLogService.updateQuotaChangeLogRemark(id, remark);
        if (result) {
            return SaResult.ok("更新配额变更日志备注成功");
        }
        return SaResult.error("更新配额变更日志备注失败");
    }

    /**
     * 删除变更日志（真删）
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public SaResult deleteQuotaChangeLog(@PathVariable Long id) {
        boolean result = quotaChangeLogService.deleteQuotaChangeLog(id);
        if (result) {
            return SaResult.ok("删除配额变更日志成功");
        }
        return SaResult.error("删除配额变更日志失败");
    }

    /**
     * 软删除变更日志
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteQuotaChangeLog(@PathVariable Long id) {
        boolean result = quotaChangeLogService.softDeleteQuotaChangeLog(id);
        if (result) {
            return SaResult.ok("软删除配额变更日志成功");
        }
        return SaResult.error("软删除配额变更日志失败");
    }

    /**
     * 批量删除变更日志（真删）
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteQuotaChangeLogs(@RequestParam List<Long> ids) {
        boolean result = quotaChangeLogService.batchDeleteQuotaChangeLogs(ids);
        if (result) {
            return SaResult.ok("批量删除配额变更日志成功");
        }
        return SaResult.error("批量删除配额变更日志失败");
    }

    /**
     * 批量软删除变更日志
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteQuotaChangeLogs(@RequestParam List<Long> ids) {
        boolean result = quotaChangeLogService.batchSoftDeleteQuotaChangeLogs(ids);
        if (result) {
            return SaResult.ok("批量软删除配额变更日志成功");
        }
        return SaResult.error("批量软删除配额变更日志失败");
    }

}
