package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.converter.PermUserConverter;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermUser;
import com.cloudvalley.nebula.ultra.business.perm.mapper.PermUserMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.PermUserRTO;
import com.cloudvalley.nebula.ultra.business.perm.service.IPermUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户权限绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class PermUserServiceImpl extends ServiceImpl<PermUserMapper, PermUser> implements IPermUserService {

    @Autowired
    private PermUserConverter permUserConverter;

    /**
     * 查询用户租户权限绑定列表 [分页]
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    @Override
    public IPage<PermUserVO> getPermUserList(Page<PermUser> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<PermUser> permUserPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<PermUserVO> voList = permUserConverter.EnListToVOList(permUserPage.getRecords());

        // 创建新的 IPage<PermUserVO>，复用原分页参数
        return new Page<PermUserVO>()
                .setRecords(voList)
                .setTotal(permUserPage.getTotal())
                .setCurrent(permUserPage.getCurrent())
                .setSize(permUserPage.getSize());
    }

    /**
     * 根据用户租户权限ID批量查询用户租户权限 [分页]
     * @param ids 用户租户权限ID列表
     * @param page 分页参数
     * @return 用户租户权限信息分页列表
     */
    @Override
    public IPage<PermUserVO> getPermUsersByIds(List<Long> ids, Page<PermUser> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermUser::getId, ids)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);

        IPage<PermUser> permUserPage = this.page(page, queryWrapper);
        List<PermUserVO> voList = permUserConverter.EnListToVOList(permUserPage.getRecords());

        return new Page<PermUserVO>()
                .setRecords(voList)
                .setTotal(permUserPage.getTotal())
                .setCurrent(permUserPage.getCurrent())
                .setSize(permUserPage.getSize());
    }

    /**
     * 根据租户用户ID查询用户租户权限 [分页]
     * @param tUserId 租户用户ID
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    @Override
    public IPage<PermUserVO> getPermUsersByTUserId(Long tUserId, Page<PermUser> page) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTUserId, tUserId)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);

        IPage<PermUser> permUserPage = this.page(page, queryWrapper);
        List<PermUserVO> voList = permUserConverter.EnListToVOList(permUserPage.getRecords());

        return new Page<PermUserVO>()
                .setRecords(voList)
                .setTotal(permUserPage.getTotal())
                .setCurrent(permUserPage.getCurrent())
                .setSize(permUserPage.getSize());
    }

    /**
     * 根据租户用户ID批量查询用户租户权限 [分页] - 返回分组结果
     * @param tUserIds 租户用户ID列表
     * @param page 分页参数
     * @return 按租户用户ID分组的用户租户权限分页列表
     */
    @Override
    public IPage<Map<Long, List<PermUserVO>>> getPermUsersByTUserIds(List<Long> tUserIds, Page<PermUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tUserIds,
                // 分页参数
                page,
                // 查询条件：按租户用户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(PermUser::getTUserId, tUserIds)
                        .eq(PermUser::getDeleted, false)
                        .orderByDesc(PermUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                permUserConverter::EnListToVOList,
                // 按租户用户ID分组
                PermUserVO::getTUserId
        );
    }

    /**
     * 根据租户权限ID查询用户租户权限 [分页]
     * @param tPermId 租户权限ID
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    @Override
    public IPage<PermUserVO> getPermUsersByTPermId(Long tPermId, Page<PermUser> page) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTPermId, tPermId)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);

        IPage<PermUser> permUserPage = this.page(page, queryWrapper);
        List<PermUserVO> voList = permUserConverter.EnListToVOList(permUserPage.getRecords());

        return new Page<PermUserVO>()
                .setRecords(voList)
                .setTotal(permUserPage.getTotal())
                .setCurrent(permUserPage.getCurrent())
                .setSize(permUserPage.getSize());
    }

    /**
     * 根据租户权限ID批量查询用户租户权限 [分页] - 返回分组结果
     * @param tPermIds 租户权限ID列表
     * @param page 分页参数
     * @return 按租户权限ID分组的用户租户权限分页列表
     */
    @Override
    public IPage<Map<Long, List<PermUserVO>>> getPermUsersByTPermIds(List<Long> tPermIds, Page<PermUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tPermIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(PermUser::getTPermId, tPermIds)
                        .eq(PermUser::getDeleted, false)
                        .orderByDesc(PermUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                permUserConverter::EnListToVOList,
                // 按租户权限ID分组
                PermUserVO::getTPermId
        );
    }

    /**
     * 新增用户租户权限绑定
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermUser(PermUserRTO permUserRTO) {
        PermUser permUser = new PermUser();
        BeanUtils.copyProperties(permUserRTO, permUser);

        // 设置默认值
        permUser.setId(GeneratorUtils.generateId());
        permUser.setCreatedAt(GeneratorUtils.generateCurrentTime());
        permUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (permUser.getDeleted() == null) {
            permUser.setDeleted(false);
        }

        return this.save(permUser);
    }

    /**
     * 更新用户租户权限绑定
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermUser(PermUserRTO permUserRTO) {
        if (permUserRTO.getId() == null) {
            return false;
        }

        PermUser permUser = new PermUser();
        BeanUtils.copyProperties(permUserRTO, permUser);
        permUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<PermUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermUser::getId, permUserRTO.getId())
                .eq(PermUser::getDeleted, false);

        return this.update(permUser, updateWrapper);
    }

    /**
     * 更新用户租户权限状态
     * @param id 用户租户权限ID
     * @param status 状态
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermUserStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<PermUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermUser::getId, id)
                .eq(PermUser::getDeleted, false)
                .set(PermUser::getState, status)
                .set(PermUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除用户租户权限绑定（真删除）
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermUser(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除用户租户权限绑定
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeletePermUser(Long id) {
        LambdaUpdateWrapper<PermUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermUser::getId, id)
                .eq(PermUser::getDeleted, false)
                .set(PermUser::getDeleted, true)
                .set(PermUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除用户租户权限绑定（真删除）
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeletePermUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除用户租户权限绑定
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeletePermUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<PermUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PermUser::getId, ids)
                .eq(PermUser::getDeleted, false)
                .set(PermUser::getDeleted, true)
                .set(PermUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
