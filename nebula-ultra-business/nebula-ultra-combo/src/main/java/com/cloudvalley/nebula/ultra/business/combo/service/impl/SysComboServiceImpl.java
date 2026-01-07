package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.converter.SysComboConverter;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.SysCombo;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.SysComboRTO;
import com.cloudvalley.nebula.ultra.business.combo.mapper.SysComboMapper;
import com.cloudvalley.nebula.ultra.business.combo.service.ISysComboService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.SysComboVO;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysComboServiceImpl extends ServiceImpl<SysComboMapper, SysCombo> implements ISysComboService {

    /**
     * 自动注入系统套餐转换器，用于实体与VO之间的转换
     */
    @Autowired
    private SysComboConverter sysComboConverter;

    /**
     * 查询系统套餐列表 [分页]
     *
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的系统套餐VO列表及分页信息
     */
    @Override
    public IPage<SysComboVO> getSysComboList(Page<SysCombo> page) {
        LambdaQueryWrapper<SysCombo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCombo::getDeleted, false)
                .orderByDesc(SysCombo::getCreatedAt);
        IPage<SysCombo> dataPage = this.page(page, queryWrapper);
        List<SysComboVO> voList = sysComboConverter.EnListToVOList(dataPage.getRecords());
        return new Page<SysComboVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据ID批量查询系统套餐 [分页]
     *
     * @param ids 系统套餐的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的系统套餐VO列表及分页信息，若ID列表为空则返回空分页结果
     */
    @Override
    public IPage<SysComboVO> getSysCombosByIds(List<Long> ids, Page<SysCombo> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<SysCombo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysCombo::getId, ids)
                .eq(SysCombo::getDeleted, false)
                .orderByDesc(SysCombo::getCreatedAt);
        IPage<SysCombo> dataPage = this.page(page, queryWrapper);
        List<SysComboVO> voList = sysComboConverter.EnListToVOList(dataPage.getRecords());
        return new Page<SysComboVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 新增系统套餐
     *
     * @param sysComboRTO 系统套餐的请求传输对象，包含新增所需的配置信息
     * @return 新增操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysCombo(SysComboRTO sysComboRTO) {
        SysCombo entity = new SysCombo();
        BeanUtils.copyProperties(sysComboRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新系统套餐
     *
     * @param sysComboRTO 系统套餐的请求传输对象，包含更新所需的配置信息（需包含套餐ID）
     * @return 更新操作是否成功，成功返回true，失败（如ID为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysCombo(SysComboRTO sysComboRTO) {
        if (sysComboRTO.getId() == null) {
            return false;
        }
        SysCombo entity = new SysCombo();
        BeanUtils.copyProperties(sysComboRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<SysCombo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysCombo::getId, sysComboRTO.getId())
                .eq(SysCombo::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 删除系统套餐（真删）
     *
     * @param id 系统套餐的ID
     * @return 删除操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysCombo(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统套餐
     *
     * @param id 系统套餐的ID
     * @return 软删除操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysCombo(Long id) {
        LambdaUpdateWrapper<SysCombo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysCombo::getId, id)
                .eq(SysCombo::getDeleted, false)
                .set(SysCombo::getDeleted, true)
                .set(SysCombo::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除系统套餐（真删）
     *
     * @param ids 系统套餐的ID列表
     * @return 批量删除操作是否成功，成功返回true，失败（如ID列表为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysCombos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除系统套餐
     *
     * @param ids 系统套餐的ID列表
     * @return 批量软删除操作是否成功，成功返回true，失败（如ID列表为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysCombos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<SysCombo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysCombo::getId, ids)
                .eq(SysCombo::getDeleted, false)
                .set(SysCombo::getDeleted, true)
                .set(SysCombo::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}
