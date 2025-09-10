package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.converter.PermRoleConverter;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermRole;
import com.cloudvalley.nebula.ultra.business.perm.mapper.PermRoleMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.PermRoleRTO;
import com.cloudvalley.nebula.ultra.business.perm.service.IPermRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermRoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户角色-租户权限关联 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class PermRoleServiceImpl extends ServiceImpl<PermRoleMapper, PermRole> implements IPermRoleService {

    @Autowired
    private PermRoleConverter permRoleConverter;

    /**
     * 分页查询「租户权限-租户角色」绑定列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 PermRoleVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<PermRoleVO> getPermRoleList(Page<PermRole> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<PermRole> permRolePage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(permRolePage.getRecords());

        // 创建新的 IPage<PermRoleVO>，复用原分页参数
        return new Page<PermRoleVO>()
                .setRecords(voList)
                .setTotal(permRolePage.getTotal())
                .setCurrent(permRolePage.getCurrent())
                .setSize(permRolePage.getSize());
    }

    /**
     * 根据多个权限角色ID分页批量查询绑定信息
     * @param ids 权限角色ID列表
     * @param page 分页参数对象
     * @return 分页的 PermRoleVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<PermRoleVO> getPermRolesByIds(List<Long> ids, Page<PermRole> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getId, ids)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);

        IPage<PermRole> permRolePage = this.page(page, queryWrapper);
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(permRolePage.getRecords());

        return new Page<PermRoleVO>()
                .setRecords(voList)
                .setTotal(permRolePage.getTotal())
                .setCurrent(permRolePage.getCurrent())
                .setSize(permRolePage.getSize());
    }

    /**
     * 根据租户权限ID分页查询其绑定的角色列表
     * @param permId 租户权限ID
     * @param page 分页参数对象
     * @return 分页的 PermRoleVO 列表，表示绑定该权限的租户角色；若无数据返回空分页
     */
    @Override
    public IPage<PermRoleVO> getPermRolesByPermId(Long permId, Page<PermRole> page) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTPermId, permId)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);

        IPage<PermRole> permRolePage = this.page(page, queryWrapper);
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(permRolePage.getRecords());

        return new Page<PermRoleVO>()
                .setRecords(voList)
                .setTotal(permRolePage.getTotal())
                .setCurrent(permRolePage.getCurrent())
                .setSize(permRolePage.getSize());
    }

    /**
     * 根据多个租户权限ID分页批量查询绑定角色，并按权限ID分组返回
     * @param permIds 租户权限ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tPermId，值为对应权限绑定的角色VO列表
     */
    @Override
    public IPage<Map<Long, List<PermRoleVO>>> getPermRolesByPermIds(List<Long> permIds, Page<PermRole> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                permIds,
                // 分页参数
                page,
                // 查询条件：按权限ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(PermRole::getTPermId, permIds)
                        .eq(PermRole::getDeleted, false)
                        .orderByDesc(PermRole::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                permRoleConverter::EnListToVOList,
                // 按权限ID分组
                PermRoleVO::getTPermId
        );
    }

    /**
     * 根据租户角色ID分页查询其绑定的权限列表
     * @param roleId 租户角色ID
     * @param page 分页参数对象
     * @return 分页的 PermRoleVO 列表，表示该角色绑定的权限；若无数据返回空分页
     */
    @Override
    public IPage<PermRoleVO> getPermRolesByRoleId(Long roleId, Page<PermRole> page) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTRoleId, roleId)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);

        IPage<PermRole> permRolePage = this.page(page, queryWrapper);
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(permRolePage.getRecords());

        return new Page<PermRoleVO>()
                .setRecords(voList)
                .setTotal(permRolePage.getTotal())
                .setCurrent(permRolePage.getCurrent())
                .setSize(permRolePage.getSize());
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定权限，并按角色ID分组返回
     * @param roleIds 租户角色ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tRoleId，值为对应角色绑定的权限VO列表
     */
    @Override
    public IPage<Map<Long, List<PermRoleVO>>> getPermRolesByRoleIds(List<Long> roleIds, Page<PermRole> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                roleIds,
                // 分页参数
                page,
                // 查询条件：按角色ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(PermRole::getTRoleId, roleIds)
                        .eq(PermRole::getDeleted, false)
                        .orderByDesc(PermRole::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                permRoleConverter::EnListToVOList,
                // 按角色ID分组
                PermRoleVO::getTRoleId
        );
    }

    /**
     * 新增「租户权限-租户角色」绑定关系
     * @param permRoleRTO 请求传输对象，包含 tPermId 和 tRoleId
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermRole(PermRoleRTO permRoleRTO) {
        PermRole permRole = new PermRole();
        BeanUtils.copyProperties(permRoleRTO, permRole);

        // 设置默认值
        permRole.setId(GeneratorUtils.generateId());
        permRole.setCreatedAt(GeneratorUtils.generateCurrentTime());
        permRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (permRoleRTO.getState() == null && permRole.getDeleted() == null) {
            permRole.setState(true);
            permRole.setDeleted(false);
        }

        return this.save(permRole);
    }

    /**
     * 更新「租户权限-租户角色」绑定信息
     * @param permRoleRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermRole(PermRoleRTO permRoleRTO) {
        if (permRoleRTO.getId() == null) {
            return false;
        }

        PermRole permRole = new PermRole();
        BeanUtils.copyProperties(permRoleRTO, permRole);
        permRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<PermRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermRole::getId, permRoleRTO.getId())
                .eq(PermRole::getDeleted, false);

        return this.update(permRole, updateWrapper);
    }

    /**
     * 更新权限角色绑定状态（启用/禁用该权限在角色中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermRoleState(Long id, Boolean state) {
        LambdaUpdateWrapper<PermRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermRole::getId, id)
                .eq(PermRole::getDeleted, false)
                .set(PermRole::getState, state)
                .set(PermRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除权限角色绑定（物理删除）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermRole(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除权限角色绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeletePermRole(Long id) {
        LambdaUpdateWrapper<PermRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermRole::getId, id)
                .eq(PermRole::getDeleted, false)
                .set(PermRole::getDeleted, true)
                .set(PermRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个权限角色绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeletePermRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个权限角色绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeletePermRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<PermRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PermRole::getId, ids)
                .eq(PermRole::getDeleted, false)
                .set(PermRole::getDeleted, true)
                .set(PermRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
