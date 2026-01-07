package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.converter.ComboPermConverter;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboPerm;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboPermRTO;
import com.cloudvalley.nebula.ultra.business.combo.mapper.ComboPermMapper;
import com.cloudvalley.nebula.ultra.business.combo.service.IComboPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboPermVO;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐-权限绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class ComboPermServiceImpl extends ServiceImpl<ComboPermMapper, ComboPerm> implements IComboPermService {

    @Autowired
    private ComboPermConverter comboPermConverter;

    /**
     * 分页查询套餐-权限绑定关系列表
     *
     * @param page 分页参数对象，包含页码、每页条数等信息
     * @return 分页结果对象，包含套餐-权限绑定关系的VO列表及分页信息
     */
    @Override
    public IPage<ComboPermVO> getComboPermList(Page<ComboPerm> page) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        IPage<ComboPerm> dataPage = this.page(page, queryWrapper);
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboPermVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据绑定ID列表分页查询套餐-权限绑定关系
     *
     * @param ids 绑定关系的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的套餐-权限绑定关系的VO列表及分页信息
     */
    @Override
    public IPage<ComboPermVO> getComboPermsByIds(List<Long> ids, Page<ComboPerm> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboPerm::getId, ids)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        IPage<ComboPerm> dataPage = this.page(page, queryWrapper);
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboPermVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐ID分页查询相关的套餐-权限绑定关系
     *
     * @param sComboId 系统套餐的唯一标识ID
     * @param page 分页参数对象
     * @return 分页结果对象，包含该系统套餐相关的绑定关系VO列表及分页信息
     */
    @Override
    public IPage<ComboPermVO> getComboPermsBySComboId(Long sComboId, Page<ComboPerm> page) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSComboId, sComboId)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        IPage<ComboPerm> dataPage = this.page(page, queryWrapper);
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboPermVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐ID列表分页查询绑定关系，并按系统套餐ID分组返回
     *
     * @param sComboIds 系统套餐的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，records中包含一个Map，key为系统套餐ID，value为对应的绑定关系VO列表
     */
    @Override
    public IPage<Map<Long, List<ComboPermVO>>> getComboPermsBySComboIds(List<Long> sComboIds, Page<ComboPerm> page) {
        return FetchUtils.pageGroupQuery(
                sComboIds,
                page,
                wrapper -> wrapper.in(ComboPerm::getSComboId, sComboIds)
                        .eq(ComboPerm::getDeleted, false)
                        .orderByDesc(ComboPerm::getCreatedAt),
                this::page,
                comboPermConverter::EnListToVOList,
                ComboPermVO::getSComboId
        );
    }

    /**
     * 根据系统权限ID分页查询相关的套餐-权限绑定关系
     *
     * @param sPermId 系统权限的唯一标识ID
     * @param page 分页参数对象
     * @return 分页结果对象，包含该系统权限相关的绑定关系VO列表及分页信息
     */
    @Override
    public IPage<ComboPermVO> getComboPermsBySPermId(Long sPermId, Page<ComboPerm> page) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSPermId, sPermId)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        IPage<ComboPerm> dataPage = this.page(page, queryWrapper);
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboPermVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统权限ID列表分页查询绑定关系，并按系统权限ID分组返回
     *
     * @param sPermIds 系统权限的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，records中包含一个Map，key为系统权限ID，value为对应的绑定关系VO列表
     */
    @Override
    public IPage<Map<Long, List<ComboPermVO>>> getComboPermsBySPermIds(List<Long> sPermIds, Page<ComboPerm> page) {
        return FetchUtils.pageGroupQuery(
                sPermIds,
                page,
                wrapper -> wrapper.in(ComboPerm::getSPermId, sPermIds)
                        .eq(ComboPerm::getDeleted, false)
                        .orderByDesc(ComboPerm::getCreatedAt),
                this::page,
                comboPermConverter::EnListToVOList,
                ComboPermVO::getSPermId
        );
    }

    /**
     * 新增套餐-权限绑定关系
     *
     * @param comboPermRTO 套餐-权限绑定关系的请求传输对象，包含新增所需的信息
     * @return 操作结果：true表示新增成功，false表示新增失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createComboPerm(ComboPermRTO comboPermRTO) {
        ComboPerm entity = new ComboPerm();
        BeanUtils.copyProperties(comboPermRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新套餐-权限绑定关系
     *
     * @param comboPermRTO 套餐-权限绑定关系的请求传输对象，包含更新所需的信息（必须包含ID）
     * @return 操作结果：true表示更新成功，false表示更新失败（若ID为空则直接返回false）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComboPerm(ComboPermRTO comboPermRTO) {
        if (comboPermRTO.getId() == null) {
            return false;
        }
        ComboPerm entity = new ComboPerm();
        BeanUtils.copyProperties(comboPermRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<ComboPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboPerm::getId, comboPermRTO.getId())
                .eq(ComboPerm::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 物理删除套餐-权限绑定关系（从数据库中彻底删除）
     *
     * @param id 绑定关系的唯一标识ID
     * @return 操作结果：true表示删除成功，false表示删除失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComboPerm(Long id) {
        return this.removeById(id);
    }

    /**
     * 逻辑删除套餐-权限绑定关系（仅更新删除标识，不实际删除数据）
     *
     * @param id 绑定关系的唯一标识ID
     * @return 操作结果：true表示删除成功，false表示删除失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteComboPerm(Long id) {
        LambdaUpdateWrapper<ComboPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboPerm::getId, id)
                .eq(ComboPerm::getDeleted, false)
                .set(ComboPerm::getDeleted, true)
                .set(ComboPerm::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量物理删除套餐-权限绑定关系（从数据库中彻底删除）
     *
     * @param ids 绑定关系的ID列表
     * @return 操作结果：true表示批量删除成功，false表示批量删除失败（若参数为空则返回false）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteComboPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量逻辑删除套餐-权限绑定关系（仅更新删除标识，不实际删除数据）
     *
     * @param ids 绑定关系的ID列表
     * @return 操作结果：true表示批量删除成功，false表示批量删除失败（若参数为空则返回false）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteComboPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<ComboPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ComboPerm::getId, ids)
                .eq(ComboPerm::getDeleted, false)
                .set(ComboPerm::getDeleted, true)
                .set(ComboPerm::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}