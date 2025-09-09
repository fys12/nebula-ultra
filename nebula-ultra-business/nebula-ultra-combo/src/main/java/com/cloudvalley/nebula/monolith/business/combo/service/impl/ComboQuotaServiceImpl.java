package com.cloudvalley.nebula.monolith.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.combo.converter.ComboQuotaConverter;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboQuota;
import com.cloudvalley.nebula.monolith.business.combo.model.rto.ComboQuotaRTO;
import com.cloudvalley.nebula.monolith.business.combo.mapper.ComboQuotaMapper;
import com.cloudvalley.nebula.monolith.business.combo.service.IComboQuotaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboQuotaVO;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐配额配置 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class ComboQuotaServiceImpl extends ServiceImpl<ComboQuotaMapper, ComboQuota> implements IComboQuotaService {

    @Autowired
    private ComboQuotaConverter comboQuotaConverter;

    /**
     * 查询套餐配额配置列表 [分页]
     * 分页查询未删除的套餐配额配置，按创建时间降序排列
     *
     * @param page 分页参数对象，包含页码、每页条数等信息
     * @return 分页的套餐配额VO列表
     */
    @Override
    public IPage<ComboQuotaVO> getComboQuotaList(Page<ComboQuota> page) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        IPage<ComboQuota> dataPage = this.page(page, queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboQuotaVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据id批量查询套餐配额配置 [分页]
     * 分页查询指定ID列表中未删除的套餐配额配置，按创建时间降序排列
     *
     * @param ids 套餐配额ID列表
     * @param page 分页参数对象
     * @return 分页的套餐配额VO列表，若ID列表为空则返回空分页结果
     */
    @Override
    public IPage<ComboQuotaVO> getComboQuotasByIds(List<Long> ids, Page<ComboQuota> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboQuota::getId, ids)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        IPage<ComboQuota> dataPage = this.page(page, queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboQuotaVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐id查询配额配置 [分页]
     * 分页查询指定系统套餐下未删除的配额配置，按创建时间降序排列
     *
     * @param sComboId 系统套餐ID
     * @param page 分页参数对象
     * @return 分页的套餐配额VO列表
     */
    @Override
    public IPage<ComboQuotaVO> getComboQuotasBySComboId(Long sComboId, Page<ComboQuota> page) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSComboId, sComboId)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        IPage<ComboQuota> dataPage = this.page(page, queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboQuotaVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐id批量查询配额配置 [分页] - 返回分组结果
     * 分页查询多个系统套餐下未删除的配额配置，并按系统套餐ID分组
     *
     * @param sComboIds 系统套餐ID列表
     * @param page 分页参数对象
     * @return 分页的分组结果，键为系统套餐ID，值为对应的配额VO列表
     */
    @Override
    public IPage<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySComboIds(List<Long> sComboIds, Page<ComboQuota> page) {
        return FetchUtils.pageGroupQuery(
                sComboIds,
                page,
                wrapper -> wrapper.in(ComboQuota::getSComboId, sComboIds)
                        .eq(ComboQuota::getDeleted, false)
                        .orderByDesc(ComboQuota::getCreatedAt),
                this::page,
                comboQuotaConverter::EnListToVOList,
                ComboQuotaVO::getSComboId
        );
    }

    /**
     * 根据系统配额id查询配额配置 [分页]
     * 分页查询指定系统配额关联的未删除套餐配额配置，按创建时间降序排列
     *
     * @param sQuotaId 系统配额ID
     * @param page 分页参数对象
     * @return 分页的套餐配额VO列表
     */
    @Override
    public IPage<ComboQuotaVO> getComboQuotasBySQuotaId(Long sQuotaId, Page<ComboQuota> page) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSQuotaId, sQuotaId)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        IPage<ComboQuota> dataPage = this.page(page, queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboQuotaVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统配额id批量查询配额配置 [分页] - 返回分组结果
     * 分页查询多个系统配额关联的未删除套餐配额配置，并按系统配额ID分组
     *
     * @param sQuotaIds 系统配额ID列表
     * @param page 分页参数对象
     * @return 分页的分组结果，键为系统配额ID，值为对应的配额VO列表
     */
    @Override
    public IPage<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySQuotaIds(List<Long> sQuotaIds, Page<ComboQuota> page) {
        return FetchUtils.pageGroupQuery(
                sQuotaIds,
                page,
                wrapper -> wrapper.in(ComboQuota::getSQuotaId, sQuotaIds)
                        .eq(ComboQuota::getDeleted, false)
                        .orderByDesc(ComboQuota::getCreatedAt),
                this::page,
                comboQuotaConverter::EnListToVOList,
                ComboQuotaVO::getSQuotaId
        );
    }

    /**
     * 新增套餐配额配置
     * 新建套餐配额记录，自动生成ID和时间戳，默认标记为未删除
     *
     * @param comboQuotaRTO 套餐配额请求传输对象，包含新增的配置信息
     * @return 新增操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createComboQuota(ComboQuotaRTO comboQuotaRTO) {
        ComboQuota entity = new ComboQuota();
        BeanUtils.copyProperties(comboQuotaRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新套餐配额配置
     * 更新指定ID的未删除套餐配额记录，更新时间戳
     *
     * @param comboQuotaRTO 套餐配额请求传输对象，包含更新的配置信息（必须包含ID）
     * @return 更新操作是否成功，若ID为空则返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComboQuota(ComboQuotaRTO comboQuotaRTO) {
        if (comboQuotaRTO.getId() == null) {
            return false;
        }
        ComboQuota entity = new ComboQuota();
        BeanUtils.copyProperties(comboQuotaRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<ComboQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboQuota::getId, comboQuotaRTO.getId())
                .eq(ComboQuota::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 删除套餐配额配置（真删）
     * 物理删除指定ID的套餐配额记录
     *
     * @param id 套餐配额ID
     * @return 删除操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComboQuota(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除套餐配额配置
     * 逻辑删除指定ID的套餐配额记录，标记删除状态并更新时间戳
     *
     * @param id 套餐配额ID
     * @return 软删除操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteComboQuota(Long id) {
        LambdaUpdateWrapper<ComboQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboQuota::getId, id)
                .eq(ComboQuota::getDeleted, false)
                .set(ComboQuota::getDeleted, true)
                .set(ComboQuota::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除套餐配额配置（真删）
     * 物理批量删除指定ID列表的套餐配额记录
     *
     * @param ids 套餐配额ID列表
     * @return 批量删除操作是否成功，若ID列表为空则返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteComboQuotas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除套餐配额配置
     * 逻辑批量删除指定ID列表的套餐配额记录，标记删除状态并更新时间戳
     *
     * @param ids 套餐配额ID列表
     * @return 批量软删除操作是否成功，若ID列表为空则返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteComboQuotas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<ComboQuota> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ComboQuota::getId, ids)
                .eq(ComboQuota::getDeleted, false)
                .set(ComboQuota::getDeleted, true)
                .set(ComboQuota::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}
