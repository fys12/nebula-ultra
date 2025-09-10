package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.converter.SysGroupConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.SysGroup;
import com.cloudvalley.nebula.ultra.business.group.model.rto.SysGroupRTO;
import com.cloudvalley.nebula.ultra.business.group.mapper.SysGroupMapper;
import com.cloudvalley.nebula.ultra.business.group.service.ISysGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 组 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements ISysGroupService {

    @Autowired
    private SysGroupConverter sysGroupConverter;

    /**
     * 查询系统组列表 [分页]
     * @param page 分页参数
     * @return 系统组列表
     */
    @Override
    public IPage<SysGroupVO> getSysGroupList(Page<SysGroup> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysGroup::getDeleted, false)
                .orderByDesc(SysGroup::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<SysGroup> sysGroupPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysGroupVO> voList = sysGroupConverter.EnListToVOList(sysGroupPage.getRecords());

        // 创建新的 IPage<SysGroupVO>，复用原分页参数
        return new Page<SysGroupVO>()
                .setRecords(voList)
                .setTotal(sysGroupPage.getTotal())
                .setCurrent(sysGroupPage.getCurrent())
                .setSize(sysGroupPage.getSize());
    }

    /**
     * 根据系统组ID批量查询系统组 [分页]
     * @param ids 系统组ID列表
     * @param page 分页参数
     * @return 系统组信息分页列表
     */
    @Override
    public IPage<SysGroupVO> getSysGroupsByIds(List<Long> ids, Page<SysGroup> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysGroup::getId, ids)
                .eq(SysGroup::getDeleted, false)
                .orderByDesc(SysGroup::getCreatedAt);

        IPage<SysGroup> sysGroupPage = this.page(page, queryWrapper);
        List<SysGroupVO> voList = sysGroupConverter.EnListToVOList(sysGroupPage.getRecords());

        return new Page<SysGroupVO>()
                .setRecords(voList)
                .setTotal(sysGroupPage.getTotal())
                .setCurrent(sysGroupPage.getCurrent())
                .setSize(sysGroupPage.getSize());
    }

    /**
     * 根据系统组ID批量查询系统组 [全量]
     * @param ids 系统组ID列表
     * @return 系统组信息
     */
    @Override
    public List<SysGroupVO> getSysGroupsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<SysGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysGroup::getId, ids)
                .eq(SysGroup::getDeleted, false)
                .orderByDesc(SysGroup::getCreatedAt);
        List<SysGroup> list = this.list(queryWrapper);
        return sysGroupConverter.EnListToVOList(list);
    }

    /**
     * 新增系统组
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysGroup(SysGroupRTO sysGroupRTO) {
        SysGroup sysGroup = new SysGroup();
        BeanUtils.copyProperties(sysGroupRTO, sysGroup);

        // 设置默认值
        sysGroup.setId(GeneratorUtils.generateId());
        sysGroup.setCreatedAt(GeneratorUtils.generateCurrentTime());
        sysGroup.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysGroup.setCreatorById(FetchUtils.getCurrentUserId());
        if (sysGroup.getDeleted() == null) {
            sysGroup.setDeleted(false);
        }

        return this.save(sysGroup);
    }

    /**
     * 更新系统组
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysGroup(SysGroupRTO sysGroupRTO) {
        if (sysGroupRTO.getId() == null) {
            return false;
        }

        SysGroup sysGroup = new SysGroup();
        BeanUtils.copyProperties(sysGroupRTO, sysGroup);
        sysGroup.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysGroup::getId, sysGroupRTO.getId())
                .eq(SysGroup::getDeleted, false);

        return this.update(sysGroup, updateWrapper);
    }

    /**
     * 删除系统组（真删除）
     * @param id 系统组ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysGroup(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统组
     * @param id 系统组ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysGroup(Long id) {
        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysGroup::getId, id)
                .eq(SysGroup::getDeleted, false)
                .set(SysGroup::getDeleted, true)
                .set(SysGroup::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除系统组（真删除）
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysGroups(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除系统组
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysGroups(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysGroup::getId, ids)
                .eq(SysGroup::getDeleted, false)
                .set(SysGroup::getDeleted, true)
                .set(SysGroup::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
