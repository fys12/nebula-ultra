package com.cloudvalley.nebula.ultra.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.log.converter.QuotaUsageLogConverter;
import com.cloudvalley.nebula.ultra.business.log.mapper.QuotaUsageLogMapper;
import com.cloudvalley.nebula.ultra.business.log.model.entity.QuotaUsageLog;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.QuotaUsageLogVO;
import com.cloudvalley.nebula.ultra.shared.api.log.service.IQuotaUsageLogCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuotaUsageLogServiceCommonImpl extends ServiceImpl<QuotaUsageLogMapper, QuotaUsageLog> implements IQuotaUsageLogCommonService {

    @Autowired
    private QuotaUsageLogConverter quotaUsageLogConverter;

    /**
     * 根据配额使用流水ID查询单个配额使用流水信息
     * @param id 配额使用流水的唯一标识ID
     * @return 对应的配额使用流水VO对象，若未找到或已删除则返回null
     */
    @Override
    public QuotaUsageLogVO getQuotaUsageLogById(Long id) {
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getId, id)
                .eq(QuotaUsageLog::getDeleted, false);

        QuotaUsageLog quotaUsageLog = this.getOne(queryWrapper);
        return quotaUsageLog != null ? quotaUsageLogConverter.EnToVO(quotaUsageLog) : null;
    }

    /**
     * 根据配额使用流水ID列表批量查询配额使用流水（全量数据）
     * @param ids 配额使用流水ID列表
     * @return 匹配的配额使用流水VO列表，按使用时间倒序排列，忽略已删除记录；若ids为空则返回空列表
     */
    @Override
    public List<QuotaUsageLogVO> getQuotaUsageLogsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaUsageLog::getId, ids)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);
        List<QuotaUsageLog> list = this.list(queryWrapper);
        return quotaUsageLogConverter.EnListToVOList(list);
    }

    /**
     * 根据租户配额ID查询该配额下的所有配额使用流水（全量数据）
     * @param tQuotaId 租户配额ID
     * @return 与该配额关联的配额使用流水VO列表，按使用时间倒序排列
     */
    @Override
    public List<QuotaUsageLogVO> getQuotaUsageLogsByTQuotaId(Long tQuotaId) {
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getTQuotaId, tQuotaId)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);
        List<QuotaUsageLog> list = this.list(queryWrapper);
        return quotaUsageLogConverter.EnListToVOList(list);
    }

    /**
     * 根据租户配额ID列表批量查询配额使用流水，并按配额ID分组返回结果（全量数据）
     * @param tQuotaIds 租户配额ID列表
     * @return 包含一个Map的列表，Map的键为tQuotaId，值为对应配额的使用流水VO列表，按使用时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByTQuotaIds(List<Long> tQuotaIds) {
        if (tQuotaIds == null || tQuotaIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaUsageLog::getTQuotaId, tQuotaIds)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);
        List<QuotaUsageLog> entities = this.list(queryWrapper);
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(entities);
        Map<Long, List<QuotaUsageLogVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaUsageLogVO::getTQuotaId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据操作人用户ID查询其操作的所有配额使用流水（全量数据）
     * @param userId 操作人用户ID
     * @return 该用户相关的配额使用流水VO列表，按使用时间倒序排列
     */
    @Override
    public List<QuotaUsageLogVO> getQuotaUsageLogsByUserId(Long userId) {
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getUserId, userId)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);
        List<QuotaUsageLog> list = this.list(queryWrapper);
        return quotaUsageLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个操作人用户ID批量查询配额使用流水，并按用户ID分组返回结果（全量数据）
     * @param userIds 操作人用户ID列表
     * @return 包含一个Map的列表，Map的键为userId，值为对应用户的使用流水VO列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaUsageLog::getUserId, userIds)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);
        List<QuotaUsageLog> entities = this.list(queryWrapper);
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(entities);
        Map<Long, List<QuotaUsageLogVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaUsageLogVO::getUserId));
        return java.util.Collections.singletonList(grouped);
    }

}
