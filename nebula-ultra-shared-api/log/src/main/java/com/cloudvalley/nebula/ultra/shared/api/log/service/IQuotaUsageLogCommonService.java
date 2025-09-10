package com.cloudvalley.nebula.ultra.shared.api.log.service;

import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.QuotaUsageLogVO;

import java.util.List;
import java.util.Map;

public interface IQuotaUsageLogCommonService {

    /**
     * 根据配额使用流水ID查询单个配额使用流水信息
     * @param id 配额使用流水的唯一标识ID
     * @return 对应的配额使用流水VO对象，若未找到则返回null
     */
    QuotaUsageLogVO getQuotaUsageLogById(Long id);

    /**
     * 根据配额使用流水ID列表批量查询配额使用流水（全量数据）
     * @param ids 配额使用流水ID列表
     * @return 匹配的配额使用流水VO列表，按使用时间倒序排列，忽略已删除记录
     */
    List<QuotaUsageLogVO> getQuotaUsageLogsByIds(List<Long> ids);

    /**
     * 根据租户配额ID查询该配额下的所有使用流水（全量数据）
     * @param tQuotaId 租户配额ID
     * @return 与该配额关联的配额使用流水VO列表，按使用时间倒序排列
     */
    List<QuotaUsageLogVO> getQuotaUsageLogsByTQuotaId(Long tQuotaId);

    /**
     * 根据租户配额ID列表批量查询使用流水，并按配额ID分组返回结果（全量数据）
     * @param tQuotaIds 租户配额ID列表
     * @return 包含一个Map的列表，Map的键为tQuotaId，值为对应配额的使用流水VO列表
     */
    List<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByTQuotaIds(List<Long> tQuotaIds);

    /**
     * 根据操作人用户ID查询其操作的所有配额使用流水（全量数据）
     * @param userId 操作人用户ID
     * @return 该用户相关的配额使用流水VO列表，按使用时间倒序排列
     */
    List<QuotaUsageLogVO> getQuotaUsageLogsByUserId(Long userId);

    /**
     * 根据多个操作人用户ID批量查询配额使用流水，并按用户ID分组返回结果（全量数据）
     * @param userIds 操作人用户ID列表
     * @return 包含一个Map的列表，Map的键为userId，值为对应用户的使用流水VO列表
     */
    List<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByUserIds(List<Long> userIds);

}
