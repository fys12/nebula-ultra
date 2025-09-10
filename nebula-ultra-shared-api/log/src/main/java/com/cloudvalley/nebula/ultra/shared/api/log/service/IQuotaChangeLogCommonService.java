package com.cloudvalley.nebula.ultra.shared.api.log.service;

import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.QuotaChangeLogVO;

import java.util.List;
import java.util.Map;

public interface IQuotaChangeLogCommonService {

    /**
     * 根据配额变更日志ID查询单个配额变更日志信息
     * @param id 配额变更日志的唯一标识ID
     * @return 对应的配额变更日志VO对象，若不存在则返回null
     */
    QuotaChangeLogVO getQuotaChangeLogById(Long id);

    /**
     * 根据配额变更日志ID列表批量查询配额变更日志（全量数据）
     * @param ids 配额变更日志ID列表
     * @return 匹配的配额变更日志VO列表，按创建时间倒序排列，忽略已删除记录
     */
    List<QuotaChangeLogVO> getQuotaChangeLogsByIds(List<Long> ids);

    /**
     * 根据租户配额ID查询该配额下的所有配额变更日志（全量数据）
     * @param tQuotaId 租户配额ID
     * @return 与该配额关联的配额变更日志VO列表，按变更时间倒序排列
     */
    List<QuotaChangeLogVO> getQuotaChangeLogsByTQuotaId(Long tQuotaId);

    /**
     * 根据租户配额ID列表批量查询配额变更日志，并按配额ID分组返回结果（全量数据）
     * @param tQuotaIds 租户配额ID列表
     * @return 包含一个Map的列表，Map的键为tQuotaId，值为对应配额的变更日志VO列表
     */
    List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByTQuotaIds(List<Long> tQuotaIds);

    /**
     * 根据申请人系统用户ID查询其申请的所有配额变更日志（全量数据）
     * @param applicantSUserId 申请人系统用户ID
     * @return 该用户作为申请人发起的配额变更日志VO列表，按变更时间倒序排列
     */
    List<QuotaChangeLogVO> getQuotaChangeLogsByApplicantSUserId(Long applicantSUserId);

    /**
     * 根据多个申请人系统用户ID批量查询配额变更日志，并按申请人ID分组返回结果（全量数据）
     * @param applicantSUserIds 申请人系统用户ID列表
     * @return 包含一个Map的列表，Map的键为applicantSUserId，值为对应用户的申请日志VO列表
     */
    List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApplicantSUserIds(List<Long> applicantSUserIds);

    /**
     * 根据审批人系统用户ID查询其审批相关的所有配额变更日志（全量数据）
     * @param approverSUserId 审批人系统用户ID
     * @return 该用户作为审批人参与的配额变更日志VO列表，按变更时间倒序排列
     */
    List<QuotaChangeLogVO> getQuotaChangeLogsByApproverSUserId(Long approverSUserId);

    /**
     * 根据多个审批人系统用户ID批量查询配额变更日志，并按审批人ID分组返回结果（全量数据）
     * @param approverSUserIds 审批人系统用户ID列表
     * @return 包含一个Map的列表，Map的键为approverSUserId，值为对应用户的审批日志VO列表
     */
    List<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApproverSUserIds(List<Long> approverSUserIds);

}
