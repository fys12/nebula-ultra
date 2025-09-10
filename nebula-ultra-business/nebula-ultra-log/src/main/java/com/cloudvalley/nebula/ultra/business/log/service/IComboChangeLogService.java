package com.cloudvalley.nebula.ultra.business.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.log.model.entity.ComboChangeLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.log.model.rto.ComboChangeLogRTO;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.ComboChangeLogVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐变更日志 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IComboChangeLogService extends IService<ComboChangeLog> {

    /**
     * 查询套餐变更日志列表 [分页]
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogList(Page<ComboChangeLog> page);

    /**
     * 根据套餐变更日志ID批量查询套餐变更日志 [分页]
     * @param ids 套餐变更日志ID列表
     * @param page 分页参数
     * @return 套餐变更日志信息分页列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogsByIds(List<Long> ids, Page<ComboChangeLog> page);

    /**
     * 根据系统租户ID查询套餐变更日志 [分页]
     * @param sTenantId 系统租户ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogsBySTenantId(Long sTenantId, Page<ComboChangeLog> page);

    /**
     * 根据系统租户ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 按系统租户ID分组的套餐变更日志分页列表
     */
    IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsBySTenantIds(List<Long> sTenantIds, Page<ComboChangeLog> page);

    /**
     * 根据旧系统套餐ID查询套餐变更日志 [分页]
     * @param oldSComboId 旧系统套餐ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogsByOldSComboId(Long oldSComboId, Page<ComboChangeLog> page);

    /**
     * 根据旧系统套餐ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param oldSComboIds 旧系统套餐ID列表
     * @param page 分页参数
     * @return 按旧系统套餐ID分组的套餐变更日志分页列表
     */
    IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOldSComboIds(List<Long> oldSComboIds, Page<ComboChangeLog> page);

    /**
     * 根据新系统套餐ID查询套餐变更日志 [分页]
     * @param newSComboId 新系统套餐ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogsByNewSComboId(Long newSComboId, Page<ComboChangeLog> page);

    /**
     * 根据新系统套餐ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param newSComboIds 新系统套餐ID列表
     * @param page 分页参数
     * @return 按新系统套餐ID分组的套餐变更日志分页列表
     */
    IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByNewSComboIds(List<Long> newSComboIds, Page<ComboChangeLog> page);

    /**
     * 根据操作人用户ID查询套餐变更日志 [分页]
     * @param operatorUserId 操作人用户ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    IPage<ComboChangeLogVO> getComboChangeLogsByOperatorUserId(Long operatorUserId, Page<ComboChangeLog> page);

    /**
     * 根据操作人用户ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param operatorUserIds 操作人用户ID列表
     * @param page 分页参数
     * @return 按操作人用户ID分组的套餐变更日志分页列表
     */
    IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOperatorUserIds(List<Long> operatorUserIds, Page<ComboChangeLog> page);

    /**
     * 新增套餐变更日志
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    boolean createComboChangeLog(ComboChangeLogRTO comboChangeLogRTO);

    /**
     * 更新套餐变更日志
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    boolean updateComboChangeLog(ComboChangeLogRTO comboChangeLogRTO);

    /**
     * 更新套餐变更日志备注
     * @param id 套餐变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    boolean updateComboChangeLogRemark(Long id, String remark);

    /**
     * 删除套餐变更日志（真删除）
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    boolean deleteComboChangeLog(Long id);

    /**
     * 软删除套餐变更日志
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    boolean softDeleteComboChangeLog(Long id);

    /**
     * 批量删除套餐变更日志（真删除）
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    boolean batchDeleteComboChangeLogs(List<Long> ids);

    /**
     * 批量软删除套餐变更日志
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteComboChangeLogs(List<Long> ids);

}
