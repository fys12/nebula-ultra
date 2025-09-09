package com.cloudvalley.nebula.monolith.business.quota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.quota.converter.QuotaTenantConverter;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.QuotaTenant;
import com.cloudvalley.nebula.monolith.business.quota.mapper.QuotaTenantMapper;
import com.cloudvalley.nebula.monolith.business.quota.model.rto.QuotaTenantRTO;
import com.cloudvalley.nebula.monolith.business.quota.service.IQuotaTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.QuotaTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户配额总览 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class QuotaTenantServiceImpl extends ServiceImpl<QuotaTenantMapper, QuotaTenant> implements IQuotaTenantService {

    @Autowired
    private QuotaTenantConverter quotaTenantConverter;

    /**
     * 分页查询「系统租户-系统配额」绑定列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 QuotaTenantVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<QuotaTenantVO> getQuotaTenantList(Page<QuotaTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<QuotaTenant> quotaTenantPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(quotaTenantPage.getRecords());

        // 创建新的 IPage<QuotaTenantVO>，复用原分页参数
        return new Page<QuotaTenantVO>()
                .setRecords(voList)
                .setTotal(quotaTenantPage.getTotal())
                .setCurrent(quotaTenantPage.getCurrent())
                .setSize(quotaTenantPage.getSize());
    }

    /**
     * 根据多个租户配额ID分页批量查询绑定信息
     * @param ids 租户配额关联ID列表
     * @param page 分页参数对象
     * @return 分页的 QuotaTenantVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<QuotaTenantVO> getQuotaTenantsByIds(List<Long> ids, Page<QuotaTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaTenant::getId, ids)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);

        IPage<QuotaTenant> quotaTenantPage = this.page(page, queryWrapper);
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(quotaTenantPage.getRecords());

        return new Page<QuotaTenantVO>()
                .setRecords(voList)
                .setTotal(quotaTenantPage.getTotal())
                .setCurrent(quotaTenantPage.getCurrent())
                .setSize(quotaTenantPage.getSize());
    }

    /**
     * 根据系统租户ID分页查询其绑定的配额列表（查看某租户拥有哪些系统配额）
     * @param tenantId 系统租户ID（sTenantId）
     * @param page 分页参数对象
     * @return 分页的 QuotaTenantVO 列表，表示该租户绑定的配额信息；若无数据返回空分页
     */
    @Override
    public IPage<QuotaTenantVO> getQuotaTenantsByTenantId(Long tenantId, Page<QuotaTenant> page) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSTenantId, tenantId)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);

        IPage<QuotaTenant> quotaTenantPage = this.page(page, queryWrapper);
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(quotaTenantPage.getRecords());

        return new Page<QuotaTenantVO>()
                .setRecords(voList)
                .setTotal(quotaTenantPage.getTotal())
                .setCurrent(quotaTenantPage.getCurrent())
                .setSize(quotaTenantPage.getSize());
    }

    /**
     * 根据多个系统租户ID分页批量查询绑定配额，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sTenantId，值为对应租户的 QuotaTenantVO 列表
     */
    @Override
    public IPage<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByTenantIds(List<Long> tenantIds, Page<QuotaTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tenantIds,
                // 分页参数
                page,
                // 查询条件：按租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(QuotaTenant::getSTenantId, tenantIds)
                        .eq(QuotaTenant::getDeleted, false)
                        .orderByDesc(QuotaTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                quotaTenantConverter::EnListToVOList,
                // 按租户ID分组
                QuotaTenantVO::getSTenantId
        );
    }

    /**
     * 根据系统配额ID分页查询使用该配额的租户列表（查看某配额被哪些租户使用）
     * @param quotaId 系统配额ID（sQuotaId）
     * @param page 分页参数对象
     * @return 分页的 QuotaTenantVO 列表，表示使用该配额的租户信息；若无数据返回空分页
     */
    @Override
    public IPage<QuotaTenantVO> getQuotaTenantsByQuotaId(Long quotaId, Page<QuotaTenant> page) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSQuotaId, quotaId)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);

        IPage<QuotaTenant> quotaTenantPage = this.page(page, queryWrapper);
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(quotaTenantPage.getRecords());

        return new Page<QuotaTenantVO>()
                .setRecords(voList)
                .setTotal(quotaTenantPage.getTotal())
                .setCurrent(quotaTenantPage.getCurrent())
                .setSize(quotaTenantPage.getSize());
    }

    /**
     * 根据多个系统配额ID分页批量查询使用租户，并按配额ID分组返回结果
     * @param quotaIds 系统配额ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sQuotaId，值为对应配额的 QuotaTenantVO 列表
     */
    @Override
    public IPage<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByQuotaIds(List<Long> quotaIds, Page<QuotaTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                quotaIds,
                // 分页参数
                page,
                // 查询条件：按配额ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(QuotaTenant::getSQuotaId, quotaIds)
                        .eq(QuotaTenant::getDeleted, false)
                        .orderByDesc(QuotaTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                quotaTenantConverter::EnListToVOList,
                // 按配额ID分组
                QuotaTenantVO::getSQuotaId
        );
    }

    /**
     * 新增「系统租户-系统配额」绑定关系
     * @param quotaTenantRTO 请求传输对象，包含 sTenantId、sQuotaId 及配额总量、已用量等信息
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，自动计算剩余配额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createQuotaTenant(QuotaTenantRTO quotaTenantRTO) {
        QuotaTenant quotaTenant = new QuotaTenant();
        BeanUtils.copyProperties(quotaTenantRTO, quotaTenant);

        // 设置默认值
        quotaTenant.setId(GeneratorUtils.generateId());
        quotaTenant.setCreatedAt(GeneratorUtils.generateCurrentTime());
        quotaTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (quotaTenantRTO.getDeleted() == null) {
            quotaTenant.setDeleted(false);
        }

        // 计算剩余配额
        if (quotaTenant.getTotal() != null && quotaTenant.getUsed() != null) {
            quotaTenant.setRemain(quotaTenant.getTotal() - quotaTenant.getUsed());
        }

        return this.save(quotaTenant);
    }

    /**
     * 更新「系统租户-系统配额」绑定信息
     * @param quotaTenantRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间并重新计算剩余配额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotaTenant(QuotaTenantRTO quotaTenantRTO) {
        if (quotaTenantRTO.getId() == null) {
            return false;
        }

        QuotaTenant quotaTenant = new QuotaTenant();
        BeanUtils.copyProperties(quotaTenantRTO, quotaTenant);
        quotaTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        // 重新计算剩余配额
        if (quotaTenant.getTotal() != null && quotaTenant.getUsed() != null) {
            quotaTenant.setRemain(quotaTenant.getTotal() - quotaTenant.getUsed());
        }

        LambdaUpdateWrapper<QuotaTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaTenant::getId, quotaTenantRTO.getId())
                .eq(QuotaTenant::getDeleted, false);

        return this.update(quotaTenant, updateWrapper);
    }

    /**
     * 删除租户配额绑定（物理删除）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuotaTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户配额绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteQuotaTenant(Long id) {
        LambdaUpdateWrapper<QuotaTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(QuotaTenant::getId, id)
                .eq(QuotaTenant::getDeleted, false)
                .set(QuotaTenant::getDeleted, true)
                .set(QuotaTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个租户配额绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteQuotaTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个租户配额绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteQuotaTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<QuotaTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(QuotaTenant::getId, ids)
                .eq(QuotaTenant::getDeleted, false)
                .set(QuotaTenant::getDeleted, true)
                .set(QuotaTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }
    
}
