package com.cloudvalley.nebula.monolith.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.role.converter.RoleUserConverter;
import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleUser;
import com.cloudvalley.nebula.monolith.business.role.mapper.RoleUserMapper;
import com.cloudvalley.nebula.monolith.business.role.model.rto.RoleUserRTO;
import com.cloudvalley.nebula.monolith.business.role.service.IRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户角色分配 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

    @Autowired
    private RoleUserConverter roleUserConverter;

    /**
     * 分页查询「租户用户-租户角色」分配列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 RoleUserVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<RoleUserVO> getRoleUserList(Page<RoleUser> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<RoleUser> roleUserPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(roleUserPage.getRecords());

        // 创建新的 IPage<RoleUserVO>，复用原分页参数
        return new Page<RoleUserVO>()
                .setRecords(voList)
                .setTotal(roleUserPage.getTotal())
                .setCurrent(roleUserPage.getCurrent())
                .setSize(roleUserPage.getSize());
    }

    /**
     * 根据多个用户角色分配ID分页批量查询绑定信息
     * @param ids 绑定关系ID列表
     * @param page 分页参数对象
     * @return 分页的 RoleUserVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<RoleUserVO> getRoleUsersByIds(List<Long> ids, Page<RoleUser> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getId, ids)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);

        IPage<RoleUser> roleUserPage = this.page(page, queryWrapper);
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(roleUserPage.getRecords());

        return new Page<RoleUserVO>()
                .setRecords(voList)
                .setTotal(roleUserPage.getTotal())
                .setCurrent(roleUserPage.getCurrent())
                .setSize(roleUserPage.getSize());
    }

    /**
     * 根据租户用户ID分页查询其绑定的角色列表（查看某用户拥有哪些租户角色）
     * @param userId 租户用户ID（tUserId）
     * @param page 分页参数对象
     * @return 分页的 RoleUserVO 列表，表示该用户已分配的角色信息；若无数据返回空分页
     */
    @Override
    public IPage<RoleUserVO> getRoleUsersByUserId(Long userId, Page<RoleUser> page) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTUserId, userId)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);

        IPage<RoleUser> roleUserPage = this.page(page, queryWrapper);
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(roleUserPage.getRecords());

        return new Page<RoleUserVO>()
                .setRecords(voList)
                .setTotal(roleUserPage.getTotal())
                .setCurrent(roleUserPage.getCurrent())
                .setSize(roleUserPage.getSize());
    }

    /**
     * 根据多个租户用户ID分页批量查询绑定角色，并按用户ID分组返回结果
     * @param userIds 租户用户ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tUserId，值为对应用户的 RoleUserVO 列表
     */
    @Override
    public IPage<Map<Long, List<RoleUserVO>>> getRoleUsersByUserIds(List<Long> userIds, Page<RoleUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                userIds,
                // 分页参数
                page,
                // 查询条件：按用户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(RoleUser::getTUserId, userIds)
                        .eq(RoleUser::getDeleted, false)
                        .orderByDesc(RoleUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                roleUserConverter::EnListToVOList,
                // 按用户ID分组
                RoleUserVO::getTUserId
        );
    }

    /**
     * 根据租户角色ID分页查询其绑定的用户列表（查看某角色被哪些用户拥有）
     * @param roleId 租户角色ID（tRoleId）
     * @param page 分页参数对象
     * @return 分页的 RoleUserVO 列表，表示分配了该角色的用户信息；若无数据返回空分页
     */
    @Override
    public IPage<RoleUserVO> getRoleUsersByRoleId(Long roleId, Page<RoleUser> page) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTRoleId, roleId)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);

        IPage<RoleUser> roleUserPage = this.page(page, queryWrapper);
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(roleUserPage.getRecords());

        return new Page<RoleUserVO>()
                .setRecords(voList)
                .setTotal(roleUserPage.getTotal())
                .setCurrent(roleUserPage.getCurrent())
                .setSize(roleUserPage.getSize());
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定用户，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tRoleId，值为对应角色的 RoleUserVO 列表
     */
    @Override
    public IPage<Map<Long, List<RoleUserVO>>> getRoleUsersByRoleIds(List<Long> roleIds, Page<RoleUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                roleIds,
                // 分页参数
                page,
                // 查询条件：按角色ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(RoleUser::getTRoleId, roleIds)
                        .eq(RoleUser::getDeleted, false)
                        .orderByDesc(RoleUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                roleUserConverter::EnListToVOList,
                // 按角色ID分组
                RoleUserVO::getTRoleId
        );
    }

    /**
     * 新增「租户用户-租户角色」分配关系
     * @param roleUserRTO 请求传输对象，包含 tUserId 和 tRoleId
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRoleUser(RoleUserRTO roleUserRTO) {
        RoleUser roleUser = new RoleUser();
        BeanUtils.copyProperties(roleUserRTO, roleUser);

        // 设置默认值
        roleUser.setId(GeneratorUtils.generateId());
        roleUser.setCreatedAt(GeneratorUtils.generateCurrentTime());
        roleUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        roleUser.setCreatedById(FetchUtils.getCurrentUserId());
        if (roleUserRTO.getState() == null && roleUser.getDeleted() == null) {
            roleUser.setState(true);
            roleUser.setDeleted(false);
        }

        return this.save(roleUser);
    }

    /**
     * 更新「租户用户-租户角色」分配信息
     * @param roleUserRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleUser(RoleUserRTO roleUserRTO) {
        if (roleUserRTO.getId() == null) {
            return false;
        }

        RoleUser roleUser = new RoleUser();
        BeanUtils.copyProperties(roleUserRTO, roleUser);
        roleUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<RoleUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleUser::getId, roleUserRTO.getId())
                .eq(RoleUser::getDeleted, false);

        return this.update(roleUser, updateWrapper);
    }

    /**
     * 更新用户角色分配状态（启用/禁用该角色在用户中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleUserState(Long id, Boolean state) {
        LambdaUpdateWrapper<RoleUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleUser::getId, id)
                .eq(RoleUser::getDeleted, false)
                .set(RoleUser::getState, state)
                .set(RoleUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除用户角色分配（物理删除）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleUser(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除用户角色分配（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteRoleUser(Long id) {
        LambdaUpdateWrapper<RoleUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleUser::getId, id)
                .eq(RoleUser::getDeleted, false)
                .set(RoleUser::getDeleted, true)
                .set(RoleUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个用户角色分配（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRoleUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个用户角色分配（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteRoleUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<RoleUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(RoleUser::getId, ids)
                .eq(RoleUser::getDeleted, false)
                .set(RoleUser::getDeleted, true)
                .set(RoleUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
