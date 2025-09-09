package com.cloudvalley.nebula.monolith.business.quota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.quota.converter.SysQuotaConverter;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.SysQuota;
import com.cloudvalley.nebula.monolith.business.quota.mapper.SysQuotaMapper;
import com.cloudvalley.nebula.monolith.business.quota.model.rto.SysQuotaRTO;
import com.cloudvalley.nebula.monolith.business.quota.service.ISysQuotaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.SysQuotaVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统配额 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysQuotaServiceImpl extends ServiceImpl<SysQuotaMapper, SysQuota> implements ISysQuotaService {

    @Autowired
    private SysQuotaConverter sysQuotaConverter;

    /**
     * 分页查询系统配额列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 SysQuotaVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<SysQuotaVO> getSysQuotaList(Page<SysQuota> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysQuota::getDeleted, false)
                .orderByDesc(SysQuota::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<SysQuota> sysQuotaPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysQuotaVO> voList = sysQuotaConverter.EnListToVOList(sysQuotaPage.getRecords());

        // 创建新的 IPage<SysQuotaVO>，复用原分页参数
        return new Page<SysQuotaVO>()
                .setRecords(voList)
                .setTotal(sysQuotaPage.getTotal())
                .setCurrent(sysQuotaPage.getCurrent())
                .setSize(sysQuotaPage.getSize());
    }

    /**
     * 根据多个ID分页批量查询系统配额
     * @param ids 系统配额ID列表
     * @param page 分页参数对象
     * @return 分页的 SysQuotaVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<SysQuotaVO> getSysQuotasByIds(List<Long> ids, Page<SysQuota> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysQuota::getId, ids)
                .eq(SysQuota::getDeleted, false)
                .orderByDesc(SysQuota::getCreatedAt);

        IPage<SysQuota> sysQuotaPage = this.page(page, queryWrapper);
        List<SysQuotaVO> voList = sysQuotaConverter.EnListToVOList(sysQuotaPage.getRecords());

        return new Page<SysQuotaVO>()
                .setRecords(voList)
                .setTotal(sysQuotaPage.getTotal())
                .setCurrent(sysQuotaPage.getCurrent())
                .setSize(sysQuotaPage.getSize());
    }

    /**
     * 新增系统配额（定义配额类型，如存储、AI tokens）
     * @param sysQuotaRTO 请求传输对象，包含配额编码、名称、单位、描述等信息
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysQuota(SysQuotaRTO sysQuotaRTO) {
        SysQuota sysQuota = new SysQuota();
        BeanUtils.copyProperties(sysQuotaRTO, sysQuota);

        // 设置默认值
        sysQuota.setId(GeneratorUtils.generateId());
        sysQuota.setCreatedAt(GeneratorUtils.generateCurrentTime());
        sysQuota.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysQuota.setCreatedById(FetchUtils.getCurrentUserId());
        if (sysQuotaRTO.getState() == null && sysQuota.getDeleted() == null) {
            sysQuota.setState(true);
            sysQuota.setDeleted(false);
        }

        return this.save(sysQuota);
    }

    /**
     * 更新系统配额信息（如名称、单位、描述）
     * @param sysQuotaRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysQuota(SysQuotaRTO sysQuotaRTO) {
        if (sysQuotaRTO.getId() == null) {
            return false;
        }

        SysQuota sysQuota = new SysQuota();
        BeanUtils.copyProperties(sysQuotaRTO, sysQuota);
        sysQuota.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<SysQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysQuota::getId, sysQuotaRTO.getId())
                .eq(SysQuota::getDeleted, false);

        return this.update(sysQuota, updateWrapper);
    }

    /**
     * 更新系统配额状态（启用/禁用）
     * @param id 系统配额ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysQuotaState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysQuota::getId, id)
                .eq(SysQuota::getDeleted, false)
                .set(SysQuota::getState, state)
                .set(SysQuota::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除系统配额（物理删除）
     * @param id 系统配额ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysQuota(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统配额（标记 deleted = true）
     * @param id 系统配额ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysQuota(Long id) {
        LambdaUpdateWrapper<SysQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysQuota::getId, id)
                .eq(SysQuota::getDeleted, false)
                .set(SysQuota::getDeleted, true)
                .set(SysQuota::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个系统配额（物理删除）
     * @param ids 系统配额ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysQuotas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个系统配额（标记 deleted = true）
     * @param ids 系统配额ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysQuotas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysQuota::getId, ids)
                .eq(SysQuota::getDeleted, false)
                .set(SysQuota::getDeleted, true)
                .set(SysQuota::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
