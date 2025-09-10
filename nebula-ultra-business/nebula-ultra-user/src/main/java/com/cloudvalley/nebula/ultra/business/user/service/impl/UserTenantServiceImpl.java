package com.cloudvalley.nebula.ultra.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.converter.UserTenantConverter;
import com.cloudvalley.nebula.ultra.business.user.model.entity.UserTenant;
import com.cloudvalley.nebula.ultra.business.user.model.rto.UserTenantRTO;
import com.cloudvalley.nebula.ultra.business.user.mapper.UserTenantMapper;
import com.cloudvalley.nebula.ultra.business.user.service.IUserTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-用户关联 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class UserTenantServiceImpl extends ServiceImpl<UserTenantMapper, UserTenant> implements IUserTenantService {

    @Autowired
    private UserTenantConverter userTenantConverter;

    /**
     * 分页查询所有有效的租户用户（未删除），按创建时间倒序排列。
     *
     * @param page 分页参数对象
     * @return 分页封装的 UserTenantVO 列表
     */
    @Override
    public IPage<UserTenantVO> getUserTenantList(Page<UserTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<UserTenant> tenantUserPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(tenantUserPage.getRecords());

        // 创建新的 IPage<UserTenantVO>，复用原分页参数
        return new Page<UserTenantVO>()
                .setRecords(voList)
                .setTotal(tenantUserPage.getTotal())
                .setCurrent(tenantUserPage.getCurrent())
                .setSize(tenantUserPage.getSize());
    }

    /**
     * 根据多个租户用户ID分页查询租户用户信息。
     *
     * @param ids  租户用户ID列表
     * @param page 分页参数
     * @return 分页的 UserTenantVO 列表；若ids为空则返回空分页
     */
    @Override
    public IPage<UserTenantVO> getUserTenantsByIds(List<Long> ids, Page<UserTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getId, ids)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);

        IPage<UserTenant> tenantUserPage = this.page(page, queryWrapper);
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(tenantUserPage.getRecords());

        return new Page<UserTenantVO>()
                .setRecords(voList)
                .setTotal(tenantUserPage.getTotal())
                .setCurrent(tenantUserPage.getCurrent())
                .setSize(tenantUserPage.getSize());
    }

    /**
     * 根据租户ID分页查询该租户下的所有用户。
     *
     * @param tenantId 租户唯一标识符
     * @param page     分页参数
     * @return 分页的租户用户VO列表
     */
    @Override
    public IPage<UserTenantVO> getUserTenantsByTenantId(Long tenantId, Page<UserTenant> page) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSTenantId, tenantId)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);

        IPage<UserTenant> tenantUserPage = this.page(page, queryWrapper);
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(tenantUserPage.getRecords());

        return new Page<UserTenantVO>()
                .setRecords(voList)
                .setTotal(tenantUserPage.getTotal())
                .setCurrent(tenantUserPage.getCurrent())
                .setSize(tenantUserPage.getSize());
    }

    /**
     * 根据多个租户ID分页查询租户用户，并按租户ID分组返回。
     *
     * @param tenantIds 租户ID列表
     * @param page      分页参数
     * @return 分页的 Map<租户ID, 用户VO列表>，用于批量关联场景
     */
    @Override
    public IPage<Map<Long, List<UserTenantVO>>> getUserTenantsByTenantIds(List<Long> tenantIds, Page<UserTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tenantIds,
                // 分页参数
                page,
                // 查询条件：按租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(UserTenant::getSTenantId, tenantIds)
                        .eq(UserTenant::getDeleted, false)
                        .orderByDesc(UserTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                userTenantConverter::EnListToVOList,
                // 按租户ID分组
                UserTenantVO::getSTenantId
        );
    }

    /**
     * 根据用户ID分页查询其所属的所有租户用户记录。
     *
     * @param userId 用户唯一标识符
     * @param page   分页参数
     * @return 分页的租户用户VO列表
     */
    @Override
    public IPage<UserTenantVO> getUserTenantsByUserId(Long userId, Page<UserTenant> page) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSUserId, userId)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);

        IPage<UserTenant> tenantUserPage = this.page(page, queryWrapper);
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(tenantUserPage.getRecords());

        return new Page<UserTenantVO>()
                .setRecords(voList)
                .setTotal(tenantUserPage.getTotal())
                .setCurrent(tenantUserPage.getCurrent())
                .setSize(tenantUserPage.getSize());
    }

    /**
     * 根据多个用户ID分页查询租户用户，并按用户ID分组返回。
     *
     * @param userIds 用户ID列表
     * @param page    分页参数
     * @return 分页的 Map<用户ID, 租户用户VO列表>
     */
    @Override
    public IPage<Map<Long, List<UserTenantVO>>> getUserTenantsByUserIds(List<Long> userIds, Page<UserTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                userIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(UserTenant::getSUserId, userIds)
                        .eq(UserTenant::getDeleted, false)
                        .orderByDesc(UserTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                userTenantConverter::EnListToVOList,
                // 按用户ID分组
                UserTenantVO::getSUserId
        );
    }

    /**
     * 新增租户用户记录。
     *
     * @param tenantUserRTO 请求传输对象，包含租户、用户等关联信息
     * @return 是否创建成功
     * @note 自动生成ID、创建时间、创建人，默认启用且未删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUserTenant(UserTenantRTO tenantUserRTO) {
        UserTenant tenantUser = new UserTenant();
        BeanUtils.copyProperties(tenantUserRTO, tenantUser);

        // 设置默认值
        tenantUser.setId(GeneratorUtils.generateId());
        tenantUser.setCreatedAt(GeneratorUtils.generateCurrentTime());
        tenantUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        tenantUser.setCreatedById(FetchUtils.getCurrentUserId());
        if (tenantUserRTO.getState() == null && tenantUser.getDeleted() == null) {
            tenantUser.setState(true);
            tenantUser.setDeleted(false);
        }

        return this.save(tenantUser);
    }

    /**
     * 更新租户用户记录。
     *
     * @param tenantUserRTO 包含ID的更新数据
     * @return 是否更新成功；ID为空或记录不存在时返回 false
     * @note 仅更新未删除的记录，自动更新修改时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserTenant(UserTenantRTO tenantUserRTO) {
        if (tenantUserRTO.getId() == null) {
            return false;
        }

        UserTenant tenantUser = new UserTenant();
        BeanUtils.copyProperties(tenantUserRTO, tenantUser);
        tenantUser.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<UserTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserTenant::getId, tenantUserRTO.getId())
                .eq(UserTenant::getDeleted, false);

        return this.update(tenantUser, updateWrapper);
    }

    /**
     * 更新租户用户的状态（启用/禁用）。
     *
     * @param id    租户用户ID
     * @param state 目标状态值
     * @return 是否更新成功
     * @note 仅对未删除的记录生效，同时更新修改时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserTenantState(Long id, Boolean state) {
        LambdaUpdateWrapper<UserTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserTenant::getId, id)
                .eq(UserTenant::getDeleted, false)
                .set(UserTenant::getState, state)
                .set(UserTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 物理删除租户用户（不可恢复）。
     *
     * @param id 租户用户ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户用户（标记 deleted = true）。
     *
     * @param id 租户用户ID
     * @return 是否软删除成功
     * @note 仅对未删除的记录执行操作
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteUserTenant(Long id) {
        LambdaUpdateWrapper<UserTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserTenant::getId, id)
                .eq(UserTenant::getDeleted, false)
                .set(UserTenant::getDeleted, true)
                .set(UserTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量物理删除多个租户用户（不可恢复）。
     *
     * @param ids 租户用户ID列表
     * @return 是否全部删除成功；若ids为空则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUserTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个租户用户（标记 deleted = true）。
     *
     * @param ids 租户用户ID列表
     * @return 是否批量软删除成功；若ids为空则返回 false
     * @note 仅对未删除的记录进行标记
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteUserTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<UserTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(UserTenant::getId, ids)
                .eq(UserTenant::getDeleted, false)
                .set(UserTenant::getDeleted, true)
                .set(UserTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
