package com.cloudvalley.nebula.monolith.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.combo.converter.ComboRoleConverter;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboRole;
import com.cloudvalley.nebula.monolith.business.combo.model.rto.ComboRoleRTO;
import com.cloudvalley.nebula.monolith.business.combo.mapper.ComboRoleMapper;
import com.cloudvalley.nebula.monolith.business.combo.service.IComboRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboRoleVO;
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
 * 套餐-角色绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class ComboRoleServiceImpl extends ServiceImpl<ComboRoleMapper, ComboRole> implements IComboRoleService {

    @Autowired
    private ComboRoleConverter comboRoleConverter;

    /**
     * 查询套餐-角色绑定列表 [分页]
     *
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的套餐-角色绑定关系的VO列表及分页信息
     */
    @Override
    public IPage<ComboRoleVO> getComboRoleList(Page<ComboRole> page) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        IPage<ComboRole> dataPage = this.page(page, queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboRoleVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据绑定ID列表分页查询套餐-角色绑定关系
     *
     * @param ids 绑定关系的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的套餐-角色绑定关系的VO列表及分页信息，若ID列表为空则返回空分页结果
     */
    @Override
    public IPage<ComboRoleVO> getComboRolesByIds(List<Long> ids, Page<ComboRole> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboRole::getId, ids)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        IPage<ComboRole> dataPage = this.page(page, queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboRoleVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐ID分页查询套餐-角色绑定关系
     *
     * @param sComboId 系统套餐的ID
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的套餐-角色绑定关系的VO列表及分页信息
     */
    @Override
    public IPage<ComboRoleVO> getComboRolesBySComboId(Long sComboId, Page<ComboRole> page) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSComboId, sComboId)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        IPage<ComboRole> dataPage = this.page(page, queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboRoleVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统套餐ID列表分页查询套餐-角色绑定关系（返回分组结果）
     *
     * @param sComboIds 系统套餐的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含按系统套餐ID分组的套餐-角色绑定关系VO列表及分页信息
     */
    @Override
    public IPage<Map<Long, List<ComboRoleVO>>> getComboRolesBySComboIds(List<Long> sComboIds, Page<ComboRole> page) {
        return FetchUtils.pageGroupQuery(
                sComboIds,
                page,
                wrapper -> wrapper.in(ComboRole::getSComboId, sComboIds)
                        .eq(ComboRole::getDeleted, false)
                        .orderByDesc(ComboRole::getCreatedAt),
                this::page,
                comboRoleConverter::EnListToVOList,
                ComboRoleVO::getSComboId
        );
    }

    /**
     * 根据系统角色ID分页查询套餐-角色绑定关系
     *
     * @param sRoleId 系统角色的ID
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的套餐-角色绑定关系的VO列表及分页信息
     */
    @Override
    public IPage<ComboRoleVO> getComboRolesBySRoleId(Long sRoleId, Page<ComboRole> page) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSRoleId, sRoleId)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        IPage<ComboRole> dataPage = this.page(page, queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(dataPage.getRecords());
        return new Page<ComboRoleVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统角色ID列表分页查询套餐-角色绑定关系（返回分组结果）
     *
     * @param sRoleIds 系统角色的ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含按系统角色ID分组的套餐-角色绑定关系VO列表及分页信息
     */
    @Override
    public IPage<Map<Long, List<ComboRoleVO>>> getComboRolesBySRoleIds(List<Long> sRoleIds, Page<ComboRole> page) {
        return FetchUtils.pageGroupQuery(
                sRoleIds,
                page,
                wrapper -> wrapper.in(ComboRole::getSRoleId, sRoleIds)
                        .eq(ComboRole::getDeleted, false)
                        .orderByDesc(ComboRole::getCreatedAt),
                this::page,
                comboRoleConverter::EnListToVOList,
                ComboRoleVO::getSRoleId
        );
    }

    /**
     * 新增套餐-角色绑定关系
     *
     * @param comboRoleRTO 套餐-角色绑定关系的请求传输对象，包含新增所需的配置信息
     * @return 新增操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createComboRole(ComboRoleRTO comboRoleRTO) {
        ComboRole entity = new ComboRole();
        BeanUtils.copyProperties(comboRoleRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新套餐-角色绑定关系
     *
     * @param comboRoleRTO 套餐-角色绑定关系的请求传输对象，包含更新所需的配置信息（需包含绑定ID）
     * @return 更新操作是否成功，成功返回true，失败（如ID为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComboRole(ComboRoleRTO comboRoleRTO) {
        if (comboRoleRTO.getId() == null) {
            return false;
        }
        ComboRole entity = new ComboRole();
        BeanUtils.copyProperties(comboRoleRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<ComboRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboRole::getId, comboRoleRTO.getId())
                .eq(ComboRole::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 物理删除套餐-角色绑定关系（真删）
     *
     * @param id 套餐-角色绑定关系的ID
     * @return 删除操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComboRole(Long id) {
        return this.removeById(id);
    }

    /**
     * 逻辑删除套餐-角色绑定关系（软删）
     *
     * @param id 套餐-角色绑定关系的ID
     * @return 软删除操作是否成功，成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteComboRole(Long id) {
        LambdaUpdateWrapper<ComboRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ComboRole::getId, id)
                .eq(ComboRole::getDeleted, false)
                .set(ComboRole::getDeleted, true)
                .set(ComboRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量物理删除套餐-角色绑定关系（真删）
     *
     * @param ids 套餐-角色绑定关系的ID列表
     * @return 批量删除操作是否成功，成功返回true，失败（如ID列表为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteComboRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量逻辑删除套餐-角色绑定关系（软删）
     *
     * @param ids 套餐-角色绑定关系的ID列表
     * @return 批量软删除操作是否成功，成功返回true，失败（如ID列表为空）返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteComboRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<ComboRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ComboRole::getId, ids)
                .eq(ComboRole::getDeleted, false)
                .set(ComboRole::getDeleted, true)
                .set(ComboRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}
