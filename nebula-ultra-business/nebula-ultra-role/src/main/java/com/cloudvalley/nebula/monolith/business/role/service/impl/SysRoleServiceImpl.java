package com.cloudvalley.nebula.monolith.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.role.converter.SysRoleConverter;
import com.cloudvalley.nebula.monolith.business.role.model.entity.SysRole;
import com.cloudvalley.nebula.monolith.business.role.mapper.SysRoleMapper;
import com.cloudvalley.nebula.monolith.business.role.model.rto.SysRoleRTO;
import com.cloudvalley.nebula.monolith.business.role.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.SysRoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    /**
     * 分页查询系统角色列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 SysRoleVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<SysRoleVO> getSysRoleList(Page<SysRole> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getDeleted, false)
                .orderByDesc(SysRole::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<SysRole> sysRolePage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysRoleVO> voList = sysRoleConverter.EnListToVOList(sysRolePage.getRecords());

        // 创建新的 IPage<SysRoleVO>，复用原分页参数
        return new Page<SysRoleVO>()
                .setRecords(voList)
                .setTotal(sysRolePage.getTotal())
                .setCurrent(sysRolePage.getCurrent())
                .setSize(sysRolePage.getSize());
    }

    /**
     * 根据多个ID分页批量查询系统角色
     * @param ids 系统角色ID列表
     * @param page 分页参数对象
     * @return 分页的 SysRoleVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<SysRoleVO> getSysRolesByIds(List<Long> ids, Page<SysRole> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRole::getId, ids)
                .eq(SysRole::getDeleted, false)
                .orderByDesc(SysRole::getCreatedAt);

        IPage<SysRole> sysRolePage = this.page(page, queryWrapper);
        List<SysRoleVO> voList = sysRoleConverter.EnListToVOList(sysRolePage.getRecords());

        return new Page<SysRoleVO>()
                .setRecords(voList)
                .setTotal(sysRolePage.getTotal())
                .setCurrent(sysRolePage.getCurrent())
                .setSize(sysRolePage.getSize());
    }

    /**
     * 新增系统角色
     * @param sysRoleRTO 请求传输对象，包含角色编码、名称、描述等信息
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置创建人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysRole(SysRoleRTO sysRoleRTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleRTO, sysRole);

        // 设置默认值
        sysRole.setId(GeneratorUtils.generateId());
        sysRole.setCreatedAt(GeneratorUtils.generateCurrentTime());
        sysRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysRole.setCreatedById(FetchUtils.getCurrentUserId());
        sysRole.setUpdatedById(FetchUtils.getCurrentUserId());
        if (sysRoleRTO.getState() == null && sysRole.getDeleted() == null) {
            sysRole.setState(true);
            sysRole.setDeleted(false);
        }

        return this.save(sysRole);
    }

    /**
     * 更新系统角色信息
     * @param sysRoleRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间与操作人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysRole(SysRoleRTO sysRoleRTO) {
        if (sysRoleRTO.getId() == null) {
            return false;
        }

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleRTO, sysRole);
        sysRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysRole.setUpdatedById(FetchUtils.getCurrentUserId());

        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRole::getId, sysRoleRTO.getId())
                .eq(SysRole::getDeleted, false);

        return this.update(sysRole, updateWrapper);
    }

    /**
     * 更新系统角色状态（启用/禁用）
     * @param id 系统角色ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysRoleState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRole::getId, id)
                .eq(SysRole::getDeleted, false)
                .set(SysRole::getState, state)
                .set(SysRole::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysRole::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

    /**
     * 删除系统角色（物理删除）
     * @param id 系统角色ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysRole(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统角色（标记 deleted = true）
     * @param id 系统角色ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysRole(Long id) {
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRole::getId, id)
                .eq(SysRole::getDeleted, false)
                .set(SysRole::getDeleted, true)
                .set(SysRole::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysRole::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个系统角色（物理删除）
     * @param ids 系统角色ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个系统角色（标记 deleted = true）
     * @param ids 系统角色ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysRole::getId, ids)
                .eq(SysRole::getDeleted, false)
                .set(SysRole::getDeleted, true)
                .set(SysRole::getUpdatedAt, GeneratorUtils.generateCurrentTime())
                .set(SysRole::getUpdatedById, FetchUtils.getCurrentUserId());

        return this.update(updateWrapper);
    }

}
