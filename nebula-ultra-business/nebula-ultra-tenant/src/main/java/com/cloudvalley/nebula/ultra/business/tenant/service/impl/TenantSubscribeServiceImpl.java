package com.cloudvalley.nebula.ultra.business.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.converter.TenantSubscribeConverter;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.TenantSubscribe;
import com.cloudvalley.nebula.ultra.business.tenant.mapper.TenantSubscribeMapper;
import com.cloudvalley.nebula.ultra.business.tenant.model.rto.TenantSubscribeRTO;
import com.cloudvalley.nebula.ultra.business.tenant.service.ITenantSubscribeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.TenantSubscribeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户订阅的套餐 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class TenantSubscribeServiceImpl extends ServiceImpl<TenantSubscribeMapper, TenantSubscribe> implements ITenantSubscribeService {

    @Autowired
    private TenantSubscribeConverter tenantSubscribeConverter;

    /**
     * 分页查询租户订阅列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 TenantSubscribeVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<TenantSubscribeVO> getTenantSubscribeList(Page<TenantSubscribe> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<TenantSubscribe> tenantSubscribePage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(tenantSubscribePage.getRecords());

        // 创建新的 IPage<TenantSubscribeVO>，复用原分页参数
        return new Page<TenantSubscribeVO>()
                .setRecords(voList)
                .setTotal(tenantSubscribePage.getTotal())
                .setCurrent(tenantSubscribePage.getCurrent())
                .setSize(tenantSubscribePage.getSize());
    }

    /**
     * 根据多个订阅ID分页批量查询订阅信息
     * @param ids 订阅记录ID列表
     * @param page 分页参数对象
     * @return 分页的 TenantSubscribeVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<TenantSubscribeVO> getTenantSubscribesByIds(List<Long> ids, Page<TenantSubscribe> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TenantSubscribe::getId, ids)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);

        IPage<TenantSubscribe> tenantSubscribePage = this.page(page, queryWrapper);
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(tenantSubscribePage.getRecords());

        return new Page<TenantSubscribeVO>()
                .setRecords(voList)
                .setTotal(tenantSubscribePage.getTotal())
                .setCurrent(tenantSubscribePage.getCurrent())
                .setSize(tenantSubscribePage.getSize());
    }

    /**
     * 根据租户ID分页查询其订阅的套餐列表
     * @param tenantId 租户ID（sTenantId）
     * @param page 分页参数对象
     * @return 分页的 TenantSubscribeVO 列表，表示该租户的订阅信息；若无数据返回空分页
     */
    @Override
    public IPage<TenantSubscribeVO> getTenantSubscribesByTenantId(Long tenantId, Page<TenantSubscribe> page) {
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getSTenantId, tenantId)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);

        IPage<TenantSubscribe> tenantSubscribePage = this.page(page, queryWrapper);
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(tenantSubscribePage.getRecords());

        return new Page<TenantSubscribeVO>()
                .setRecords(voList)
                .setTotal(tenantSubscribePage.getTotal())
                .setCurrent(tenantSubscribePage.getCurrent())
                .setSize(tenantSubscribePage.getSize());
    }

    /**
     * 根据多个租户ID分页批量查询订阅信息，并按租户ID分组返回结果
     * @param tenantIds 租户ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sTenantId，值为对应租户的 TenantSubscribeVO 列表
     */
    @Override
    public IPage<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByTenantIds(List<Long> tenantIds, Page<TenantSubscribe> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tenantIds,
                // 分页参数
                page,
                // 查询条件：按租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(TenantSubscribe::getSTenantId, tenantIds)
                        .eq(TenantSubscribe::getDeleted, false)
                        .orderByDesc(TenantSubscribe::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                tenantSubscribeConverter::EnListToVOList,
                // 按租户ID分组
                TenantSubscribeVO::getSTenantId
        );
    }

    /**
     * 根据套餐ID分页查询其被订阅情况（查看某套餐被哪些租户使用）
     * @param comboId 套餐ID（sComboId）
     * @param page 分页参数对象
     * @return 分页的 TenantSubscribeVO 列表，表示订阅了该套餐的租户信息；若无数据返回空分页
     */
    @Override
    public IPage<TenantSubscribeVO> getTenantSubscribesByComboId(Long comboId, Page<TenantSubscribe> page) {
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getSComboId, comboId)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);

        IPage<TenantSubscribe> tenantSubscribePage = this.page(page, queryWrapper);
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(tenantSubscribePage.getRecords());

        return new Page<TenantSubscribeVO>()
                .setRecords(voList)
                .setTotal(tenantSubscribePage.getTotal())
                .setCurrent(tenantSubscribePage.getCurrent())
                .setSize(tenantSubscribePage.getSize());
    }

    /**
     * 根据多个套餐ID分页批量查询订阅租户，并按套餐ID分组返回结果
     * @param comboIds 套餐ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sComboId，值为对应套餐的 TenantSubscribeVO 列表
     */
    @Override
    public IPage<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByComboIds(List<Long> comboIds, Page<TenantSubscribe> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                comboIds,
                // 分页参数
                page,
                // 查询条件：按套餐ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(TenantSubscribe::getSComboId, comboIds)
                        .eq(TenantSubscribe::getDeleted, false)
                        .orderByDesc(TenantSubscribe::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                tenantSubscribeConverter::EnListToVOList,
                // 按套餐ID分组
                TenantSubscribeVO::getSComboId
        );
    }

    /**
     * 新增租户订阅关系（绑定租户与套餐）
     * @param tenantSubscribeRTO 请求传输对象，包含 sTenantId 和 sComboId
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态和创建人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTenantSubscribe(TenantSubscribeRTO tenantSubscribeRTO) {
        TenantSubscribe tenantSubscribe = new TenantSubscribe();
        BeanUtils.copyProperties(tenantSubscribeRTO, tenantSubscribe);

        // 设置默认值
        tenantSubscribe.setId(GeneratorUtils.generateId());
        tenantSubscribe.setCreatedAt(GeneratorUtils.generateCurrentTime());
        tenantSubscribe.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        tenantSubscribe.setCreatedById(FetchUtils.getCurrentUserId());
        if (tenantSubscribeRTO.getDeleted() == null) {
            tenantSubscribe.setDeleted(false);
        }
        if (tenantSubscribeRTO.getStatus() == null) {
            tenantSubscribe.setState(true);
        }

        return this.save(tenantSubscribe);
    }

    /**
     * 更新租户订阅信息
     * @param tenantSubscribeRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantSubscribe(TenantSubscribeRTO tenantSubscribeRTO) {
        if (tenantSubscribeRTO.getId() == null) {
            return false;
        }

        TenantSubscribe tenantSubscribe = new TenantSubscribe();
        BeanUtils.copyProperties(tenantSubscribeRTO, tenantSubscribe);
        tenantSubscribe.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<TenantSubscribe> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TenantSubscribe::getId, tenantSubscribeRTO.getId())
                .eq(TenantSubscribe::getDeleted, false);

        return this.update(tenantSubscribe, updateWrapper);
    }

    /**
     * 更新租户订阅状态（如：active、expired、suspended）
     * @param id 订阅记录ID
     * @param status 目标状态字符串（需符合业务枚举）
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantSubscribeState(Long id, Boolean status) {
        LambdaUpdateWrapper<TenantSubscribe> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TenantSubscribe::getId, id)
                .eq(TenantSubscribe::getDeleted, false)
                .set(TenantSubscribe::getState, status)
                .set(TenantSubscribe::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除租户订阅（物理删除）
     * @param id 订阅记录ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTenantSubscribe(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户订阅（标记 deleted = true）
     * @param id 订阅记录ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteTenantSubscribe(Long id) {
        LambdaUpdateWrapper<TenantSubscribe> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TenantSubscribe::getId, id)
                .eq(TenantSubscribe::getDeleted, false)
                .set(TenantSubscribe::getDeleted, true)
                .set(TenantSubscribe::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个租户订阅（物理删除）
     * @param ids 订阅记录ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTenantSubscribes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个租户订阅（标记 deleted = true）
     * @param ids 订阅记录ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteTenantSubscribes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<TenantSubscribe> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(TenantSubscribe::getId, ids)
                .eq(TenantSubscribe::getDeleted, false)
                .set(TenantSubscribe::getDeleted, true)
                .set(TenantSubscribe::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
