package com.cloudvalley.nebula.monolith.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.log.converter.QuotaUsageLogConverter;
import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaUsageLog;
import com.cloudvalley.nebula.monolith.business.log.mapper.QuotaUsageLogMapper;
import com.cloudvalley.nebula.monolith.business.log.model.rto.QuotaUsageLogRTO;
import com.cloudvalley.nebula.monolith.business.log.service.IQuotaUsageLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaUsageLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配额使用流水 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class QuotaUsageLogServiceImpl extends ServiceImpl<QuotaUsageLogMapper, QuotaUsageLog> implements IQuotaUsageLogService {

    @Autowired
    private QuotaUsageLogConverter quotaUsageLogConverter;

    /**
     * 查询配额使用流水列表 [分页]
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    @Override
    public IPage<QuotaUsageLogVO> getQuotaUsageLogList(Page<QuotaUsageLog> page) {
        // 构建查询条件，按使用时间倒序排列
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<QuotaUsageLog> quotaUsageLogPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(quotaUsageLogPage.getRecords());

        // 创建新的 IPage<QuotaUsageLogVO>，复用原分页参数
        return new Page<QuotaUsageLogVO>()
                .setRecords(voList)
                .setTotal(quotaUsageLogPage.getTotal())
                .setCurrent(quotaUsageLogPage.getCurrent())
                .setSize(quotaUsageLogPage.getSize());
    }

    /**
     * 根据配额使用流水ID批量查询配额使用流水 [分页]
     * @param ids 配额使用流水ID列表
     * @param page 分页参数
     * @return 配额使用流水信息分页列表
     */
    @Override
    public IPage<QuotaUsageLogVO> getQuotaUsageLogsByIds(List<Long> ids, Page<QuotaUsageLog> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaUsageLog::getId, ids)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);

        IPage<QuotaUsageLog> quotaUsageLogPage = this.page(page, queryWrapper);
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(quotaUsageLogPage.getRecords());

        return new Page<QuotaUsageLogVO>()
                .setRecords(voList)
                .setTotal(quotaUsageLogPage.getTotal())
                .setCurrent(quotaUsageLogPage.getCurrent())
                .setSize(quotaUsageLogPage.getSize());
    }

    /**
     * 根据租户配额ID查询配额使用流水 [分页]
     * @param tQuotaId 租户配额ID
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    @Override
    public IPage<QuotaUsageLogVO> getQuotaUsageLogsByTQuotaId(Long tQuotaId, Page<QuotaUsageLog> page) {
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getTQuotaId, tQuotaId)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);

        IPage<QuotaUsageLog> quotaUsageLogPage = this.page(page, queryWrapper);
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(quotaUsageLogPage.getRecords());

        return new Page<QuotaUsageLogVO>()
                .setRecords(voList)
                .setTotal(quotaUsageLogPage.getTotal())
                .setCurrent(quotaUsageLogPage.getCurrent())
                .setSize(quotaUsageLogPage.getSize());
    }

    /**
     * 根据租户配额ID批量查询配额使用流水 [分页] - 返回分组结果
     * @param tQuotaIds 租户配额ID列表
     * @param page 分页参数
     * @return 按租户配额ID分组的配额使用流水分页列表
     */
    @Override
    public IPage<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByTQuotaIds(List<Long> tQuotaIds, Page<QuotaUsageLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tQuotaIds,
                // 分页参数
                page,
                // 查询条件：按租户配额ID批量查询，且未删除，按使用时间倒序
                wrapper -> wrapper.in(QuotaUsageLog::getTQuotaId, tQuotaIds)
                        .eq(QuotaUsageLog::getDeleted, false)
                        .orderByDesc(QuotaUsageLog::getUsedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                quotaUsageLogConverter::EnListToVOList,
                // 按租户配额ID分组
                QuotaUsageLogVO::getTQuotaId
        );
    }

    /**
     * 根据操作人用户ID查询配额使用流水 [分页]
     * @param userId 操作人用户ID
     * @param page 分页参数
     * @return 配额使用流水列表
     */
    @Override
    public IPage<QuotaUsageLogVO> getQuotaUsageLogsByUserId(Long userId, Page<QuotaUsageLog> page) {
        LambdaQueryWrapper<QuotaUsageLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaUsageLog::getUserId, userId)
                .eq(QuotaUsageLog::getDeleted, false)
                .orderByDesc(QuotaUsageLog::getUsedAt);

        IPage<QuotaUsageLog> quotaUsageLogPage = this.page(page, queryWrapper);
        List<QuotaUsageLogVO> voList = quotaUsageLogConverter.EnListToVOList(quotaUsageLogPage.getRecords());

        return new Page<QuotaUsageLogVO>()
                .setRecords(voList)
                .setTotal(quotaUsageLogPage.getTotal())
                .setCurrent(quotaUsageLogPage.getCurrent())
                .setSize(quotaUsageLogPage.getSize());
    }

    /**
     * 根据操作人用户ID批量查询配额使用流水 [分页] - 返回分组结果
     * @param userIds 操作人用户ID列表
     * @param page 分页参数
     * @return 按操作人用户ID分组的配额使用流水分页列表
     */
    @Override
    public IPage<Map<Long, List<QuotaUsageLogVO>>> getQuotaUsageLogsByUserIds(List<Long> userIds, Page<QuotaUsageLog> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                userIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(QuotaUsageLog::getUserId, userIds)
                        .eq(QuotaUsageLog::getDeleted, false)
                        .orderByDesc(QuotaUsageLog::getUsedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                quotaUsageLogConverter::EnListToVOList,
                // 按操作人用户ID分组
                QuotaUsageLogVO::getUserId
        );
    }

    /**
     * 新增配额使用流水
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createQuotaUsageLog(QuotaUsageLogRTO quotaUsageLogRTO) {
        QuotaUsageLog quotaUsageLog = new QuotaUsageLog();
        BeanUtils.copyProperties(quotaUsageLogRTO, quotaUsageLog);

        // 设置默认值
        quotaUsageLog.setId(GeneratorUtils.generateId());
        if (quotaUsageLog.getUsedAt() == null) {
            quotaUsageLog.setUsedAt(GeneratorUtils.generateCurrentTime());
        }
        if (quotaUsageLog.getUpdatedAt() == null) {
            quotaUsageLog.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        }
        if (quotaUsageLog.getDeleted() == null) {
            quotaUsageLog.setDeleted(false);
        }

        return this.save(quotaUsageLog);
    }

    /**
     * 更新配额使用流水
     * @param quotaUsageLogRTO 配额使用流水信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotaUsageLog(QuotaUsageLogRTO quotaUsageLogRTO) {
        if (quotaUsageLogRTO.getId() == null) {
            return false;
        }

        QuotaUsageLog quotaUsageLog = new QuotaUsageLog();
        BeanUtils.copyProperties(quotaUsageLogRTO, quotaUsageLog);
        quotaUsageLog.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<QuotaUsageLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaUsageLog::getId, quotaUsageLogRTO.getId())
                .eq(QuotaUsageLog::getDeleted, false);

        return this.update(quotaUsageLog, updateWrapper);
    }

    /**
     * 更新流水备注
     * @param id 配额使用流水ID
     * @param remark 备注
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotaUsageLogRemark(Long id, String remark) {
        LambdaUpdateWrapper<QuotaUsageLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaUsageLog::getId, id)
                .eq(QuotaUsageLog::getDeleted, false)
                .set(QuotaUsageLog::getRemark, remark)
                .set(QuotaUsageLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除配额使用流水（真删除）
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuotaUsageLog(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除配额使用流水
     * @param id 配额使用流水ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteQuotaUsageLog(Long id) {
        LambdaUpdateWrapper<QuotaUsageLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaUsageLog::getId, id)
                .eq(QuotaUsageLog::getDeleted, false)
                .set(QuotaUsageLog::getDeleted, true)
                .set(QuotaUsageLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除配额使用流水（真删除）
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteQuotaUsageLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除配额使用流水
     * @param ids 配额使用流水ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteQuotaUsageLogs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<QuotaUsageLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(QuotaUsageLog::getId, ids)
                .eq(QuotaUsageLog::getDeleted, false)
                .set(QuotaUsageLog::getDeleted, true)
                .set(QuotaUsageLog::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
