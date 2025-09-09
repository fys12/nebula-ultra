package com.cloudvalley.nebula.monolith.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.converter.QuotaChangeLogConverter;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaChangeLog;
import com.cloudvalley.nebula.monolith.business.log.mapper.QuotaChangeLogMapper;
import com.cloudvalley.nebula.monolith.business.log.model.rto.QuotaChangeLogRTO;
import com.cloudvalley.nebula.monolith.business.log.service.IQuotaChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaChangeLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额变更日志 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class QuotaChangeLogServiceImpl extends ServiceImpl<QuotaChangeLogMapper, QuotaChangeLog> implements IQuotaChangeLogService {

    @Autowired
    private QuotaChangeLogConverter quotaChangeLogConverter;

    /**
     * 查询配额变更日志列表 [分页]
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    @Override
    public IPage<QuotaChangeLogVO> getQuotaChangeLogList(Page<QuotaChangeLog> page) {
        // 构建查询条件，按申请时间倒序排列
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<QuotaChangeLog> quotaChangeLogPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(quotaChangeLogPage.getRecords());

        // 创建新的 IPage<QuotaChangeLogVO>，复用原分页参数
        return new Page<QuotaChangeLogVO>()
                .setRecords(voList)
                .setTotal(quotaChangeLogPage.getTotal())
                .setCurrent(quotaChangeLogPage.getCurrent())
                .setSize(quotaChangeLogPage.getSize());
    }

    /**
     * 根据配额变更日志ID批量查询配额变更日志 [分页]
     * @param ids 配额变更日志ID列表
     * @param page 分页参数
     * @return 配额变更日志信息分页列表
     */
    @Override
    public IPage<QuotaChangeLogVO> getQuotaChangeLogsByIds(List<Long> ids, Page<QuotaChangeLog> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaChangeLog::getId, ids)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);

        IPage<QuotaChangeLog> quotaChangeLogPage = this.page(page, queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(quotaChangeLogPage.getRecords());

        return new Page<QuotaChangeLogVO>()
                .setRecords(voList)
                .setTotal(quotaChangeLogPage.getTotal())
                .setCurrent(quotaChangeLogPage.getCurrent())
                .setSize(quotaChangeLogPage.getSize());
    }

    /**
     * 根据租户配额ID查询配额变更日志 [分页]
     * @param tQuotaId 租户配额ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    @Override
    public IPage<QuotaChangeLogVO> getQuotaChangeLogsByTQuotaId(Long tQuotaId, Page<QuotaChangeLog> page) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getTQuotaId, tQuotaId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);

        IPage<QuotaChangeLog> quotaChangeLogPage = this.page(page, queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(quotaChangeLogPage.getRecords());

        return new Page<QuotaChangeLogVO>()
                .setRecords(voList)
                .setTotal(quotaChangeLogPage.getTotal())
                .setCurrent(quotaChangeLogPage.getCurrent())
                .setSize(quotaChangeLogPage.getSize());
    }

    /**
     * 根据租户配额ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param page 分页参数
     * @return 按租户配额ID分组的配额变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByTQuotaIds(List<Long> tQuotaIds, Page<QuotaChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tQuotaIds,
                // 分页参数
                page,
                // 查询条件：按租户配额ID批量查询，且未删除，按申请时间倒序
                wrapper -> wrapper.in(QuotaChangeLog::getTQuotaId, tQuotaIds)
                        .eq(QuotaChangeLog::getDeleted, false)
                        .orderByDesc(QuotaChangeLog::getAppliedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                quotaChangeLogConverter::EnListToVOList,
                // 按租户配额ID分组
                QuotaChangeLogVO::getTQuotaId
        );
    }

    /**
     * 根据申请人系统用户ID查询配额变更日志 [分页]
     * @param applicantSUserId 申请人系统用户ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    @Override
    public IPage<QuotaChangeLogVO> getQuotaChangeLogsByApplicantSUserId(Long applicantSUserId, Page<QuotaChangeLog> page) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getApplicantSUserId, applicantSUserId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);

        IPage<QuotaChangeLog> quotaChangeLogPage = this.page(page, queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(quotaChangeLogPage.getRecords());

        return new Page<QuotaChangeLogVO>()
                .setRecords(voList)
                .setTotal(quotaChangeLogPage.getTotal())
                .setCurrent(quotaChangeLogPage.getCurrent())
                .setSize(quotaChangeLogPage.getSize());
    }

    /**
     * 根据申请人系统用户ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param applicantSUserIds 申请人系统用户ID列表
     * @param page 分页参数
     * @return 按申请人系统用户ID分组的配额变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApplicantSUserIds(List<Long> applicantSUserIds, Page<QuotaChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                applicantSUserIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(QuotaChangeLog::getApplicantSUserId, applicantSUserIds)
                        .eq(QuotaChangeLog::getDeleted, false)
                        .orderByDesc(QuotaChangeLog::getAppliedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                quotaChangeLogConverter::EnListToVOList,
                // 按申请人系统用户ID分组
                QuotaChangeLogVO::getApplicantSUserId
        );
    }

    /**
     * 根据审批人系统用户ID查询配额变更日志 [分页]
     * @param approverSUserId 审批人系统用户ID
     * @param page 分页参数
     * @return 配额变更日志列表
     */
    @Override
    public IPage<QuotaChangeLogVO> getQuotaChangeLogsByApproverSUserId(Long approverSUserId, Page<QuotaChangeLog> page) {
        LambdaQueryWrapper<QuotaChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaChangeLog::getApproverSUserId, approverSUserId)
                .eq(QuotaChangeLog::getDeleted, false)
                .orderByDesc(QuotaChangeLog::getAppliedAt);

        IPage<QuotaChangeLog> quotaChangeLogPage = this.page(page, queryWrapper);
        List<QuotaChangeLogVO> voList = quotaChangeLogConverter.EnListToVOList(quotaChangeLogPage.getRecords());

        return new Page<QuotaChangeLogVO>()
                .setRecords(voList)
                .setTotal(quotaChangeLogPage.getTotal())
                .setCurrent(quotaChangeLogPage.getCurrent())
                .setSize(quotaChangeLogPage.getSize());
    }

    /**
     * 根据审批人系统用户ID批量查询配额变更日志 [分页] - 返回分组结果
     * @param approverSUserIds 审批人系统用户ID列表
     * @param page 分页参数
     * @return 按审批人系统用户ID分组的配额变更日志分页列表
     */
    @Override
    public IPage<Map<Long, List<QuotaChangeLogVO>>> getQuotaChangeLogsByApproverSUserIds(List<Long> approverSUserIds, Page<QuotaChangeLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                approverSUserIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(QuotaChangeLog::getApproverSUserId, approverSUserIds)
                        .eq(QuotaChangeLog::getDeleted, false)
                        .orderByDesc(QuotaChangeLog::getAppliedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                quotaChangeLogConverter::EnListToVOList,
                // 按审批人系统用户ID分组
                QuotaChangeLogVO::getApproverSUserId
        );
    }

    /**
     * 新增配额变更日志
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createQuotaChangeLog(QuotaChangeLogRTO quotaChangeLogRTO) {
        QuotaChangeLog quotaChangeLog = new QuotaChangeLog();
        BeanUtils.copyProperties(quotaChangeLogRTO, quotaChangeLog);

        // 设置默认值
        quotaChangeLog.setId(GeneratorUtils.generateId());
        if (quotaChangeLog.getAppliedAt() == null) {
            quotaChangeLog.setAppliedAt(GeneratorUtils.generateCurrentTime());
        }
        if (quotaChangeLog.getUpdatedAt() == null) {
            quotaChangeLog.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        }
        if (quotaChangeLog.getApprovalStatus() == null) {
            quotaChangeLog.setApprovalStatus("pending");
        }
        if (quotaChangeLog.getState() == null) {
            quotaChangeLog.setState(true);
        }
        if (quotaChangeLog.getDeleted() == null) {
            quotaChangeLog.setDeleted(false);
        }

        return this.save(quotaChangeLog);
    }

    /**
     * 更新配额变更日志
     * @param quotaChangeLogRTO 配额变更日志信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotaChangeLog(QuotaChangeLogRTO quotaChangeLogRTO) {
        if (quotaChangeLogRTO.getId() == null) {
            return false;
        }

        QuotaChangeLog quotaChangeLog = new QuotaChangeLog();
        BeanUtils.copyProperties(quotaChangeLogRTO, quotaChangeLog);
        quotaChangeLog.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<QuotaChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaChangeLog::getId, quotaChangeLogRTO.getId())
                .eq(QuotaChangeLog::getDeleted, false);

        return this.update(quotaChangeLog, updateWrapper);
    }

    /**
     * 更新审批状态及审批人信息
     * @param id 配额变更日志ID
     * @param approvalStatus 审批状态
     * @param approverSUserId 审批人系统用户ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApprovalInfo(Long id, String approvalStatus, Long approverSUserId) {
        LambdaUpdateWrapper<QuotaChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaChangeLog::getId, id)
                .eq(QuotaChangeLog::getDeleted, false)
                .set(QuotaChangeLog::getApprovalStatus, approvalStatus)
                .set(QuotaChangeLog::getApproverSUserId, approverSUserId)
                .set(QuotaChangeLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 更新日志备注
     * @param id 配额变更日志ID
     * @param remark 备注
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotaChangeLogRemark(Long id, String remark) {
        LambdaUpdateWrapper<QuotaChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaChangeLog::getId, id)
                .eq(QuotaChangeLog::getDeleted, false)
                .set(QuotaChangeLog::getRemark, remark)
                .set(QuotaChangeLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除配额变更日志（真删除）
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuotaChangeLog(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除配额变更日志
     * @param id 配额变更日志ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteQuotaChangeLog(Long id) {
        LambdaUpdateWrapper<QuotaChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaChangeLog::getId, id)
                .eq(QuotaChangeLog::getDeleted, false)
                .set(QuotaChangeLog::getDeleted, true)
                .set(QuotaChangeLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除配额变更日志（真删除）
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteQuotaChangeLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除配额变更日志
     * @param ids 配额变更日志ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteQuotaChangeLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<QuotaChangeLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(QuotaChangeLog::getId, ids)
                .eq(QuotaChangeLog::getDeleted, false)
                .set(QuotaChangeLog::getDeleted, true)
                .set(QuotaChangeLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
