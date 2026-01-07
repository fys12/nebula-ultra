package com.cloudvalley.nebula.ultra.business.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.log.model.entity.QuotaUsageLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.log.model.rto.QuotaUsageLogRTO;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.QuotaUsageLogVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额使用流水 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IQuotaUsageLogService extends IService<QuotaUsageLog> {

    /**
     * 查询配额使用流水列表 [分页]
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    IPage<QuotaUsageLogVO> getQuotaUsageLogList(Page<QuotaUsageLog> page);

    /**
     * 根据配额使用流水ID批量查询配额使用流水 [分页]
     * @param ids 配额使用流水ID列表
     * @param page 分页参数
     * @return 配额使用流水信息分页列表
     */
    IPage<QuotaUsageLogVO> getQuotaUsageLogsByIds(List<Long> ids, Page<QuotaUsageLog> page);

    /**
     * 根据租户配额ID查询配额使用流水 [分页]
     * @param tQuotaId 租户配额ID
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    IPage<QuotaUsageLogVO> getQuotaUsageLogsByTQuotaId(Long tQuotaId, Page<QuotaUsageLog> page);

    /**
     * 根据租户配额ID批量查询配额使用流水 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param page 分页参数
     * @return 按租户配额ID分组的配额使用流水分页列表
     */
    IPage<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByTQuotaIds(List<Long> tQuotaIds, Page<QuotaUsageLog> page);

    /**
     * 根据操作人用户ID查询配额使用流水 [分页]
     * @param userId 操作人用户ID
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    IPage<QuotaUsageLogVO> getQuotaUsageLogsByUserId(Long userId, Page<QuotaUsageLog> page);

    /**
     * 根据操作人用户ID批量查询配额使用流水 [分页] - 返回分组结果
     * @param userIds 操作人用户ID列表
     * @param page 分页参数
     * @return 按操作人用户ID分组的配额使用流水分页列表
     */
    IPage<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByUserIds(List<Long> userIds, Page<QuotaUsageLog> page);

    /**
     * 新增配额使用流水
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    boolean createQuotaUsageLog(QuotaUsageLogRTO quotaUsageLogRTO);

    /**
     * 更新配额使用流水
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    boolean updateQuotaUsageLog(QuotaUsageLogRTO quotaUsageLogRTO);

    /**
     * 更新流水备注
     * @param id 配额使用流水ID
     * @param remark 备注
     * @return 操作结果
     */
    boolean updateQuotaUsageLogRemark(Long id, String remark);

    /**
     * 删除配额使用流水（真删除）
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    boolean deleteQuotaUsageLog(Long id);

    /**
     * 软删除配额使用流水
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    boolean softDeleteQuotaUsageLog(Long id);

    /**
     * 批量删除配额使用流水（真删除）
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    boolean batchDeleteQuotaUsageLogs(List<Long> ids);

    /**
     * 批量软删除配额使用流水
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteQuotaUsageLogs(List<Long> ids);

}
