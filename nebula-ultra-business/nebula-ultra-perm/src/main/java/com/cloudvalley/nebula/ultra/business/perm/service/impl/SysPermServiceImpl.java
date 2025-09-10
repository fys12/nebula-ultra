package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.converter.SysPermConverter;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.SysPerm;
import com.cloudvalley.nebula.ultra.business.perm.mapper.SysPermMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.SysPermRTO;
import com.cloudvalley.nebula.ultra.business.perm.service.ISysPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysPermServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements ISysPermService {

    @Autowired
    private SysPermConverter sysPermConverter;

    /**
     * 分页查询系统权限列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 SysPermVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<SysPermVO> getSysPermList(Page<SysPerm> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPerm::getDeleted, false)
                .orderByDesc(SysPerm::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<SysPerm> sysPermPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysPermVO> voList = sysPermConverter.EnListToVOList(sysPermPage.getRecords());

        // 创建新的 IPage<SysPermVO>，复用原分页参数
        return new Page<SysPermVO>()
                .setRecords(voList)
                .setTotal(sysPermPage.getTotal())
                .setCurrent(sysPermPage.getCurrent())
                .setSize(sysPermPage.getSize());
    }

    /**
     * 根据多个ID分页批量查询系统权限
     * @param ids 系统权限ID列表
     * @param page 分页参数对象
     * @return 分页的 SysPermVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<SysPermVO> getSysPermsByIds(List<Long> ids, Page<SysPerm> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysPerm::getId, ids)
                .eq(SysPerm::getDeleted, false)
                .orderByDesc(SysPerm::getCreatedAt);

        IPage<SysPerm> sysPermPage = this.page(page, queryWrapper);
        List<SysPermVO> voList = sysPermConverter.EnListToVOList(sysPermPage.getRecords());

        return new Page<SysPermVO>()
                .setRecords(voList)
                .setTotal(sysPermPage.getTotal())
                .setCurrent(sysPermPage.getCurrent())
                .setSize(sysPermPage.getSize());
    }

    /**
     * 新增系统权限
     * @param sysPermRTO 请求传输对象，包含权限编码、名称、描述等信息
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置创建人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysPerm(SysPermRTO sysPermRTO) {
        SysPerm sysPerm = new SysPerm();
        BeanUtils.copyProperties(sysPermRTO, sysPerm);

        // 设置默认值
        sysPerm.setId(GeneratorUtils.generateId());
        sysPerm.setCreatedAt(GeneratorUtils.generateCurrentTime());
        sysPerm.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysPerm.setCreatedById(FetchUtils.getCurrentUserId());
        sysPerm.setUpdatedById(FetchUtils.getCurrentUserId());
        if (sysPermRTO.getState() == null) {
            sysPerm.setState(true);
        }
        if (sysPermRTO.getDeleted() == null) {
            sysPerm.setDeleted(false);
        }

        return this.save(sysPerm);
    }

    /**
     * 更新系统权限信息
     * @param sysPermRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间与操作人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysPerm(SysPermRTO sysPermRTO) {
        if (sysPermRTO.getId() == null) {
            return false;
        }

        SysPerm sysPerm = new SysPerm();
        BeanUtils.copyProperties(sysPermRTO, sysPerm);
        sysPerm.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysPerm.setUpdatedById(FetchUtils.getCurrentUserId());

        LambdaUpdateWrapper<SysPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysPerm::getId, sysPermRTO.getId())
                .eq(SysPerm::getDeleted, false);

        return this.update(sysPerm, updateWrapper);
    }

    /**
     * 更新系统权限状态（启用/禁用）
     * @param id 系统权限ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysPermState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysPerm::getId, id)
                .eq(SysPerm::getDeleted, false)
                .set(SysPerm::getState, state)
                .set(SysPerm::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysPerm::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

    /**
     * 删除系统权限（物理删除）
     * @param id 系统权限ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysPerm(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统权限（标记 deleted = true）
     * @param id 系统权限ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysPerm(Long id) {
        LambdaUpdateWrapper<SysPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysPerm::getId, id)
                .eq(SysPerm::getDeleted, false)
                .set(SysPerm::getDeleted, true)
                .set(SysPerm::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysPerm::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个系统权限（物理删除）
     * @param ids 系统权限ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个系统权限（标记 deleted = true）
     * @param ids 系统权限ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysPerm::getId, ids)
                .eq(SysPerm::getDeleted, false)
                .set(SysPerm::getDeleted, true)
                .set(SysPerm::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysPerm::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

}
