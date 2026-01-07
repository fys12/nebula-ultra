package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.dept.converter.SysDeptConverter;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.SysDept;
import com.cloudvalley.nebula.ultra.business.dept.model.rto.SysDeptRTO;
import com.cloudvalley.nebula.ultra.business.dept.mapper.SysDeptMapper;
import com.cloudvalley.nebula.ultra.business.dept.service.ISysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptConverter sysDeptConverter;

    /**
     * 分页查询所有未删除的系统部门，按创建时间倒序排列。
     *
     * @param page 分页参数
     * @return 分页封装的 SysDeptVO 列表
     */
    @Override
    public IPage<SysDeptVO> getSysDeptList(Page<SysDept> page) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeleted, false)
                .orderByDesc(SysDept::getCreatedAt);
        IPage<SysDept> dataPage = this.page(page, queryWrapper);
        List<SysDeptVO> voList = sysDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<SysDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据多个部门ID分页查询未删除的系统部门，按创建时间倒序排列。
     *
     * @param ids  部门ID列表
     * @param page 分页参数
     * @return 分页的 SysDeptVO 列表；若ids为空则返回空分页
     */
    @Override
    public IPage<SysDeptVO> getSysDeptsByIds(List<Long> ids, Page<SysDept> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDept::getId, ids)
                .eq(SysDept::getDeleted, false)
                .orderByDesc(SysDept::getCreatedAt);
        IPage<SysDept> dataPage = this.page(page, queryWrapper);
        List<SysDeptVO> voList = sysDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<SysDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据多个部门ID查询所有匹配的系统部门（全量数据，无分页）。
     *
     * @param ids 部门ID列表
     * @return 所有未删除的 SysDeptVO 列表；若ids为空则返回空列表
     */
    @Override
    public List<SysDeptVO> getSysDeptsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDept::getId, ids)
                .eq(SysDept::getDeleted, false)
                .orderByDesc(SysDept::getCreatedAt);
        List<SysDept> list = this.list(queryWrapper);
        return sysDeptConverter.EnListToVOList(list);
    }

    /**
     * 新增一个系统部门记录。
     *
     * @param sysDeptRTO 请求数据对象
     * @return 是否保存成功
     * @note 自动生成ID、创建/更新时间，默认启用且未删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysDept(SysDeptRTO sysDeptRTO) {
        SysDept entity = new SysDept();
        BeanUtils.copyProperties(sysDeptRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getState() == null && entity.getDeleted() == null) {
            entity.setState(true);
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新指定ID的系统部门信息。
     *
     * @param sysDeptRTO 包含ID和更新字段的请求对象
     * @return 是否更新成功；ID为空或记录不存在时返回false
     * @note 仅更新未删除的记录，并自动更新修改时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysDept(SysDeptRTO sysDeptRTO) {
        if (sysDeptRTO.getId() == null) {
            return false;
        }
        SysDept entity = new SysDept();
        BeanUtils.copyProperties(sysDeptRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDept::getId, sysDeptRTO.getId())
                .eq(SysDept::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 更新系统部门的启用状态（启用/禁用）。
     *
     * @param id    部门ID
     * @param state 目标状态值
     * @return 是否更新成功
     * @note 仅对未删除的记录生效，同时更新修改时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysDeptState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDept::getId, id)
                .eq(SysDept::getDeleted, false)
                .set(SysDept::getState, state)
                .set(SysDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 物理删除一个系统部门（不可恢复）。
     *
     * @param id 部门ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysDept(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除一个系统部门（标记 deleted = true）。
     *
     * @param id 部门ID
     * @return 是否软删除成功
     * @note 仅对未删除的记录执行操作
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysDept(Long id) {
        LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDept::getId, id)
                .eq(SysDept::getDeleted, false)
                .set(SysDept::getDeleted, true)
                .set(SysDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量物理删除多个系统部门（不可恢复）。
     *
     * @param ids 部门ID列表
     * @return 是否全部删除成功；若ids为空则返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysDepts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个系统部门（标记 deleted = true）。
     *
     * @param ids 部门ID列表
     * @return 是否批量更新成功；若ids为空则返回false
     * @note 仅对未删除的记录进行标记
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysDepts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysDept::getId, ids)
                .eq(SysDept::getDeleted, false)
                .set(SysDept::getDeleted, true)
                .set(SysDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}
