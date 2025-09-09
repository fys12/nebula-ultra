package com.cloudvalley.nebula.monolith.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.converter.ComboChangeLogConverter;
import com.cloudvalley.nebula.monolith.business.log.model.entity.ComboChangeLog;
import com.cloudvalley.nebula.monolith.business.log.model.rto.ComboChangeLogRTO;
import com.cloudvalley.nebula.monolith.business.log.mapper.ComboChangeLogMapper;
import com.cloudvalley.nebula.monolith.business.log.service.IComboChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.ComboChangeLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐变更日志 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class ComboChangeLogServiceImpl extends ServiceImpl<ComboChangeLogMapper, ComboChangeLog> implements IComboChangeLogService {

    @Autowired
    private ComboChangeLogConverter comboChangeLogConverter;

    /**
     * 查询套餐变更日志列表 [分页]
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogList(Page<ComboChangeLog> page) {
        // 构建查询条件，按变更时间倒序排列
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        // 创建新的 IPage<ComboChangeLogVO>，复用原分页参数
        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据套餐变更日志ID批量查询套餐变更日志 [分页]
     * @param ids 套餐变更日志ID列表
     * @param page 分页参数
     * @return 套餐变更日志信息分页列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogsByIds(List<Long> ids, Page<ComboChangeLog> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getId, ids)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据系统租户ID查询套餐变更日志 [分页]
     * @param sTenantId 系统租户ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogsBySTenantId(Long sTenantId, Page<ComboChangeLog> page) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getSTenantId, sTenantId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据系统租户ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 按系统租户ID分组的套餐变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsBySTenantIds(List<Long> sTenantIds, Page<ComboChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                sTenantIds,
                // 分页参数
                page,
                // 查询条件：按系统租户ID批量查询，且未删除，按变更时间倒序
                wrapper -> wrapper.in(ComboChangeLog::getSTenantId, sTenantIds)
                        .eq(ComboChangeLog::getDeleted, false)
                        .orderByDesc(ComboChangeLog::getChangedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                comboChangeLogConverter::EnListToVOList,
                // 按系统租户ID分组
                ComboChangeLogVO::getSTenantId
        );
    }

    /**
     * 根据旧系统套餐ID查询套餐变更日志 [分页]
     * @param oldSComboId 旧系统套餐ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogsByOldSComboId(Long oldSComboId, Page<ComboChangeLog> page) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getOldSComboId, oldSComboId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据旧系统套餐ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param oldSComboIds 旧系统套餐ID列表
     * @param page 分页参数
     * @return 按旧系统套餐ID分组的套餐变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOldSComboIds(List<Long> oldSComboIds, Page<ComboChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                oldSComboIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(ComboChangeLog::getOldSComboId, oldSComboIds)
                        .eq(ComboChangeLog::getDeleted, false)
                        .orderByDesc(ComboChangeLog::getChangedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                comboChangeLogConverter::EnListToVOList,
                // 按旧系统套餐ID分组
                ComboChangeLogVO::getOldSComboId
        );
    }

    /**
     * 根据新系统套餐ID查询套餐变更日志 [分页]
     * @param newSComboId 新系统套餐ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogsByNewSComboId(Long newSComboId, Page<ComboChangeLog> page) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getNewSComboId, newSComboId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据新系统套餐ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param newSComboIds 新系统套餐ID列表
     * @param page 分页参数
     * @return 按新系统套餐ID分组的套餐变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByNewSComboIds(List<Long> newSComboIds, Page<ComboChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                newSComboIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(ComboChangeLog::getNewSComboId, newSComboIds)
                        .eq(ComboChangeLog::getDeleted, false)
                        .orderByDesc(ComboChangeLog::getChangedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                comboChangeLogConverter::EnListToVOList,
                // 按新系统套餐ID分组
                ComboChangeLogVO::getNewSComboId
        );
    }

    /**
     * 根据操作人用户ID查询套餐变更日志 [分页]
     * @param operatorUserId 操作人用户ID
     * @param page 分页参数
     * @return 套餐变更日志列表
     */
    @Override
    public IPage<ComboChangeLogVO> getComboChangeLogsByOperatorUserId(Long operatorUserId, Page<ComboChangeLog> page) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getOperatorUserId, operatorUserId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);

        IPage<ComboChangeLog> comboChangeLogPage = this.page(page, queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(comboChangeLogPage.getRecords());

        return new Page<ComboChangeLogVO>()
                .setRecords(voList)
                .setTotal(comboChangeLogPage.getTotal())
                .setCurrent(comboChangeLogPage.getCurrent())
                .setSize(comboChangeLogPage.getSize());
    }

    /**
     * 根据操作人用户ID批量查询套餐变更日志 [分页] - 返回分组结果
     * @param operatorUserIds 操作人用户ID列表
     * @param page 分页参数
     * @return 按操作人用户ID分组的套餐变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOperatorUserIds(List<Long> operatorUserIds, Page<ComboChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                operatorUserIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(ComboChangeLog::getOperatorUserId, operatorUserIds)
                        .eq(ComboChangeLog::getDeleted, false)
                        .orderByDesc(ComboChangeLog::getChangedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                comboChangeLogConverter::EnListToVOList,
                // 按操作人用户ID分组
                ComboChangeLogVO::getOperatorUserId
        );
    }

    /**
     * 新增套餐变更日志
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createComboChangeLog(ComboChangeLogRTO comboChangeLogRTO) {
        ComboChangeLog comboChangeLog = new ComboChangeLog();
        BeanUtils.copyProperties(comboChangeLogRTO, comboChangeLog);

        // 设置默认值
        comboChangeLog.setId(GeneratorUtils.generateId());
        if (comboChangeLog.getChangedAt() == null) {
            comboChangeLog.setChangedAt(GeneratorUtils.generateCurrentTime());
        }
        if (comboChangeLog.getDeleted() == null) {
            comboChangeLog.setDeleted(false);
        }

        return this.save(comboChangeLog);
    }

    /**
     * 更新套餐变更日志
     * @param comboChangeLogRTO 套餐变更日志信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComboChangeLog(ComboChangeLogRTO comboChangeLogRTO) {
        if (comboChangeLogRTO.getId() == null) {
            return false;
        }

        ComboChangeLog comboChangeLog = new ComboChangeLog();
        BeanUtils.copyProperties(comboChangeLogRTO, comboChangeLog);

        LambdaUpdateWrapper<ComboChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboChangeLog::getId, comboChangeLogRTO.getId())
                .eq(ComboChangeLog::getDeleted, false);

        return this.update(comboChangeLog, updateWrapper);
    }

    /**
     * 更新套餐变更日志备注
     * @param id 套餐变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComboChangeLogRemark(Long id, String remark) {
        LambdaUpdateWrapper<ComboChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboChangeLog::getId, id)
                .eq(ComboChangeLog::getDeleted, false)
                .set(ComboChangeLog::getRemark, remark);

        return this.update(updateWrapper);
    }

    /**
     * 删除套餐变更日志（真删除）
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComboChangeLog(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除套餐变更日志
     * @param id 套餐变更日志ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteComboChangeLog(Long id) {
        LambdaUpdateWrapper<ComboChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboChangeLog::getId, id)
                .eq(ComboChangeLog::getDeleted, false)
                .set(ComboChangeLog::getDeleted, true);

        return this.update(updateWrapper);
    }

    /**
     * 批量删除套餐变更日志（真删除）
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteComboChangeLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除套餐变更日志
     * @param ids 套餐变更日志ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteComboChangeLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<ComboChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ComboChangeLog::getId, ids)
                .eq(ComboChangeLog::getDeleted, false)
                .set(ComboChangeLog::getDeleted, true);

        return this.update(updateWrapper);
    }

}
