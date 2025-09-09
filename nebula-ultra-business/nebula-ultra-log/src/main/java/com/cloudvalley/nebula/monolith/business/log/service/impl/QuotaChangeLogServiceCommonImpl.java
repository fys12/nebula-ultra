package com.cloudvalley.nebula.monolith.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.log.converter.QuotaChangeLogConverter;
import com.cloudvalley.nebula.monolith.business.log.mapper.QuotaChangeLogMapper;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaChangeLog;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaChangeLogVO;
import com.cloudvalley.nebula.monolith.shared.api.log.service.IQuotaChangeLogCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuotaChangeLogServiceCommonImpl extends ServiceImpl<QuotaChangeLogMapper, QuotaChangeLog> implements IQuotaChangeLogCommonService {

    @Autowired
    private QuotaChangeLogConverter quotaChangeLogConverter;

    /**
     * 根据配额变更日志ID查询单个配额变更日志信息
     * @param id 配额变更日志的唯一标识ID
     * @return 对应的配额变更日志VO对象，若未找到或已删除则返回null
     */
    @Override
    public QuotaChangeLogVO getQuotaChangeLogById(Long id) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getId, id)
                .eq(QuotaChangeLog::getDeleted, false);

        QuotaChangeLog quotaChangeLog = this.getOne(queryWrapper);
        return quotaChangeLog != null ? quotaChangeLogConverter.EnToVO(quotaChangeLog) : null;
    }

    /**
     * 根据配额变更日志ID列表批量查询配额变更日志（全量数据）
     * @param ids 配额变更日志ID列表
     * @return 匹配的配额变更日志VO列表，按申请时间倒序排列，忽略已删除记录；若ids为空则返回空列表
     */
    @Override
    public List<QuotaChangeLogVO> getQuotaChangeLogsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaChangeLog::getId, ids)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> list = this.list(queryWrapper);
        return quotaChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据租户配额ID查询该配额下的所有配额变更日志（全量数据）
     * @param tQuotaId 租户配额ID
     * @return 与该配额关联的配额变更日志VO列表，按申请时间倒序排列
     */
    @Override
    public List<QuotaChangeLogVO> getQuotaChangeLogsByTQuotaId(Long tQuotaId) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getTQuotaId, tQuotaId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> list = this.list(queryWrapper);
        return quotaChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据租户配额ID列表批量查询配额变更日志，并按配额ID分组返回结果（全量数据）
     * @param tQuotaIds 租户配额ID列表
     * @return 包含一个Map的列表，Map的键为tQuotaId，值为对应配额的变更日志VO列表，按申请时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByTQuotaIds(List<Long> tQuotaIds) {
        if (tQuotaIds == null || tQuotaIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaChangeLog::getTQuotaId, tQuotaIds)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> entities = this.list(queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<QuotaChangeLogVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaChangeLogVO::getTQuotaId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据申请人系统用户ID查询其申请的所有配额变更日志（全量数据）
     * @param applicantSUserId 申请人系统用户ID
     * @return 该用户作为申请人发起的配额变更日志VO列表，按申请时间倒序排列
     */
    @Override
    public List<QuotaChangeLogVO> getQuotaChangeLogsByApplicantSUserId(Long applicantSUserId) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getApplicantSUserId, applicantSUserId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> list = this.list(queryWrapper);
        return quotaChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个申请人系统用户ID批量查询配额变更日志，并按申请人ID分组返回结果（全量数据）
     * @param applicantSUserIds 申请人系统用户ID列表
     * @return 包含一个Map的列表，Map的键为applicantSUserId，值为对应用户的申请日志VO列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApplicantSUserIds(List<Long> applicantSUserIds) {
        if (applicantSUserIds == null || applicantSUserIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaChangeLog::getApplicantSUserId, applicantSUserIds)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> entities = this.list(queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<QuotaChangeLogVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaChangeLogVO::getApplicantSUserId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据审批人系统用户ID查询其审批相关的所有配额变更日志（全量数据）
     * @param approverSUserId 审批人系统用户ID
     * @return 该用户作为审批人参与的配额变更日志VO列表，按申请时间倒序排列
     */
    @Override
    public List<QuotaChangeLogVO> getQuotaChangeLogsByApproverSUserId(Long approverSUserId) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getApproverSUserId, approverSUserId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> list = this.list(queryWrapper);
        return quotaChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个审批人系统用户ID批量查询配额变更日志，并按审批人ID分组返回结果（全量数据）
     * @param approverSUserIds 审批人系统用户ID列表
     * @return 包含一个Map的列表，Map的键为approverSUserId，值为对应用户的审批日志VO列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApproverSUserIds(List<Long> approverSUserIds) {
        if (approverSUserIds == null || approverSUserIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaChangeLog::getApproverSUserId, approverSUserIds)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);
        List<QuotaChangeLog> entities = this.list(queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<QuotaChangeLogVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaChangeLogVO::getApproverSUserId));
        return java.util.Collections.singletonList(grouped);
    }

}
