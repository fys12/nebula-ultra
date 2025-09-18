package com.cloudvalley.nebula.ultra.shared.api.log.service;

import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.ComboChangeLogVO;

import java.util.List;
import java.util.Map;

public interface IComboChangeLogCommonService {

    /**
     * 根据套餐变更日志ID查询单个套餐变更日志信息
     * @param id 套餐变更日志的唯一标识ID
     * @return 对应的套餐变更日志VO对象，若不存在则返回null
     */
    ComboChangeLogVO getComboChangeLogById(Long id);

    /**
     * 根据套餐变更日志ID列表批量查询套餐变更日志（全量数据）
     * @param ids 套餐变更日志ID列表
     * @return 匹配的套餐变更日志VO列表，按查询顺序返回，未匹配的ID将被忽略
     */
    List<ComboChangeLogVO> getComboChangeLogsByIds(List<Long> ids);

    /**
     * 根据系统租户ID查询该租户下的所有套餐变更日志（全量数据）
     * @param sTenantId 系统租户ID
     * @return 该租户相关的套餐变更日志VO列表
     */
    List<ComboChangeLogVO> getComboChangeLogsBySTenantId(Long sTenantId);

    /**
     * 根据系统租户ID列表批量查询套餐变更日志，并按租户ID分组返回（全量数据）
     * @param sTenantIds 系统租户ID列表
     * @return 分组结果列表，每个元素是一个Map，键为sTenantId，值为对应的日志VO列表
     */
    Map<Long, List<ComboChangeLogVO>> getComboChangeLogsBySTenantIds(List<Long> sTenantIds);

    /**
     * 根据旧系统套餐ID查询所有关联的套餐变更日志（全量数据）
     * @param oldSComboId 旧系统套餐ID
     * @return 匹配的套餐变更日志VO列表
     */
    List<ComboChangeLogVO> getComboChangeLogsByOldSComboId(Long oldSComboId);

    /**
     * 根据多个旧系统套餐ID批量查询套餐变更日志，并按旧套餐ID分组返回（全量数据）
     * @param oldSComboIds 旧系统套餐ID列表
     * @return 分组结果列表，每个元素是一个Map，键为oldSComboId，值为对应的日志VO列表
     */
    Map<Long, List<ComboChangeLogVO>> getComboChangeLogsByOldSComboIds(List<Long> oldSComboIds);

    /**
     * 根据新系统套餐ID查询所有关联的套餐变更日志（全量数据）
     * @param newSComboId 新系统套餐ID
     * @return 匹配的套餐变更日志VO列表
     */
    List<ComboChangeLogVO> getComboChangeLogsByNewSComboId(Long newSComboId);

    /**
     * 根据多个新系统套餐ID批量查询套餐变更日志，并按新套餐ID分组返回（全量数据）
     * @param newSComboIds 新系统套餐ID列表
     * @return 分组结果列表，每个元素是一个Map，键为newSComboId，值为对应的日志VO列表
     */
    List<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByNewSComboIds(List<Long> newSComboIds);

    /**
     * 根据操作人用户ID查询其操作的所有套餐变更日志（全量数据）
     * @param operatorUserId 操作人用户ID
     * @return 该用户操作的套餐变更日志VO列表
     */
    List<ComboChangeLogVO> getComboChangeLogsByOperatorUserId(Long operatorUserId);

    /**
     * 根据多个操作人用户ID批量查询套餐变更日志，并按用户ID分组返回（全量数据）
     * @param operatorUserIds 操作人用户ID列表
     * @return 分组结果列表，每个元素是一个Map，键为operatorUserId，值为对应的日志VO列表
     */
    List<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOperatorUserIds(List<Long> operatorUserIds);
}
