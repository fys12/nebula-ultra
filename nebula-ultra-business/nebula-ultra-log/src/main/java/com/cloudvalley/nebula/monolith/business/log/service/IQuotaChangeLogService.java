package com.cloudvalley.nebula.monolith.business.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaChangeLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.log.model.rto.QuotaChangeLogRTO;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaChangeLogVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额变更日志 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IQuotaChangeLogService extends IService<QuotaChangeLog> {

    /**
     * 查询配额变更日志列表 [分页]
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    IPage<QuotaChangeLogVO> getQuotaChangeLogList(Page<QuotaChangeLog> page);

    /**
     * 根据配额变更日志ID批量查询配额变更日志 [分页]
     * @param ids 配额变更日志ID列表
     * @param page 分页参数
     * @return 配额变更日志信息分页列表
     */
    IPage<QuotaChangeLogVO> getQuotaChangeLogsByIds(List<Long> ids, Page<QuotaChangeLog> page);

    /**
     * 根据租户配额ID查询配额变更日志 [分页]
     * @param tQuotaId 租户配额ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    IPage<QuotaChangeLogVO> getQuotaChangeLogsByTQuotaId(Long tQuotaId, Page<QuotaChangeLog> page);

    /**
     * 根据租户配额ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param page 分页参数
     * @return 按租户配额ID分组的配额变更日志分页列表
     */
    IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByTQuotaIds(List<Long> tQuotaIds, Page<QuotaChangeLog> page);

    /**
     * 根据申请人系统用户ID查询配额变更日志 [分页]
     * @param applicantSUserId 申请人系统用户ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    IPage<QuotaChangeLogVO> getQuotaChangeLogsByApplicantSUserId(Long applicantSUserId, Page<QuotaChangeLog> page);

    /**
     * 根据申请人系统用户ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param applicantSUserIds 申请人系统用户ID列表
     * @param page 分页参数
     * @return 按申请人系统用户ID分组的配额变更日志分页列表
     */
    IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApplicantSUserIds(List<Long> applicantSUserIds, Page<QuotaChangeLog> page);

    /**
     * 根据审批人系统用户ID查询配额变更日志 [分页]
     * @param approverSUserId 审批人系统用户ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    IPage<QuotaChangeLogVO> getQuotaChangeLogsByApproverSUserId(Long approverSUserId, Page<QuotaChangeLog> page);

    /**
     * 根据审批人系统用户ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param approverSUserIds 审批人系统用户ID列表
     * @param page 分页参数
     * @return 按审批人系统用户ID分组的配额变更日志分页列表
     */
    IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApproverSUserIds(List<Long> approverSUserIds, Page<QuotaChangeLog> page);

    /**
     * 新增配额变更日志
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    boolean createQuotaChangeLog(QuotaChangeLogRTO quotaChangeLogRTO);

    /**
     * 更新配额变更日志
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    boolean updateQuotaChangeLog(QuotaChangeLogRTO quotaChangeLogRTO);

    /**
     * 更新审批状态及审批人信息
     * @param id 配额变更日志ID
     * @param approvalStatus 审批状态
     * @param approverSUserId 审批人系统用户ID
     * @return 操作结果
     */
    boolean updateApprovalInfo(Long id, String approvalStatus, Long approverSUserId);

    /**
     * 更新日志备注
     * @param id 配额变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    boolean updateQuotaChangeLogRemark(Long id, String remark);

    /**
     * 删除配额变更日志（真删除）
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    boolean deleteQuotaChangeLog(Long id);

    /**
     * 软删除配额变更日志
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    boolean softDeleteQuotaChangeLog(Long id);

    /**
     * 批量删除配额变更日志（真删除）
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    boolean batchDeleteQuotaChangeLogs(List<Long> ids);

    /**
     * 批量软删除配额变更日志
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteQuotaChangeLogs(List<Long> ids);


}
