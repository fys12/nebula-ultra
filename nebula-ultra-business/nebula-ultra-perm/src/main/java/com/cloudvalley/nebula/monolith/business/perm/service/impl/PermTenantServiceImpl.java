package com.cloudvalley.nebula.monolith.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.converter.PermTenantConverter;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermTenant;
import com.cloudvalley.nebula.monolith.business.perm.mapper.PermTenantMapper;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermTenantRTO;
import com.cloudvalley.nebula.monolith.business.perm.service.IPermTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限-租户绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class PermTenantServiceImpl extends ServiceImpl<PermTenantMapper, PermTenant> implements IPermTenantService {

    @Autowired
    private PermTenantConverter permTenantConverter;

    /**
     * 查询权限租户列表 [分页]
     * @param page 分页参数
     * @return 权限租户列表
     */
    @Override
    public IPage<PermTenantVO> getPermTenantList(Page<PermTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<PermTenant> permTenantPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(permTenantPage.getRecords());

        // 创建新的 IPage<PermTenantVO>，复用原分页参数
        return new Page<PermTenantVO>()
                .setRecords(voList)
                .setTotal(permTenantPage.getTotal())
                .setCurrent(permTenantPage.getCurrent())
                .setSize(permTenantPage.getSize());
    }

    /**
     * 根据权限租户ID批量查询权限租户 [分页]
     * @param ids 权限租户ID列表
     * @param page 分页参数
     * @return 权限租户信息分页列表
     */
    @Override
    public IPage<PermTenantVO> getPermTenantsByIds(List<Long> ids, Page<PermTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermTenant::getId, ids)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);

        IPage<PermTenant> permTenantPage = this.page(page, queryWrapper);
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(permTenantPage.getRecords());

        return new Page<PermTenantVO>()
                .setRecords(voList)
                .setTotal(permTenantPage.getTotal())
                .setCurrent(permTenantPage.getCurrent())
                .setSize(permTenantPage.getSize());
    }

    /**
     * 根据租户ID查询权限租户 [分页]
     * @param tenantId 租户ID
     * @param page 分页参数
     * @return 权限租户列表
     */
    @Override
    public IPage<PermTenantVO> getPermTenantsByTenantId(Long tenantId, Page<PermTenant> page) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSTenantId, tenantId)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);

        IPage<PermTenant> permTenantPage = this.page(page, queryWrapper);
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(permTenantPage.getRecords());

        return new Page<PermTenantVO>()
                .setRecords(voList)
                .setTotal(permTenantPage.getTotal())
                .setCurrent(permTenantPage.getCurrent())
                .setSize(permTenantPage.getSize());
    }

    /**
     * 根据租户ID批量查询权限租户 [分页] - 返回分组结果
     * @param tenantIds 租户ID列表
     * @param page 分页参数
     * @return 按租户ID分组的权限租户分页列表
     */
    @Override
    public IPage<Map<Long, List<PermTenantVO>>> getPermTenantsByTenantIds(List<Long> tenantIds, Page<PermTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tenantIds,
                // 分页参数
                page,
                // 查询条件：按租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(PermTenant::getSTenantId, tenantIds)
                        .eq(PermTenant::getDeleted, false)
                        .orderByDesc(PermTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                permTenantConverter::EnListToVOList,
                // 按租户ID分组
                PermTenantVO::getSTenantId
        );
    }

    /**
     * 根据权限ID查询权限租户 [分页]
     * @param permId 权限ID
     * @param page 分页参数
     * @return 权限租户列表
     */
    @Override
    public IPage<PermTenantVO> getPermTenantsByPermId(Long permId, Page<PermTenant> page) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSPermId, permId)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);

        IPage<PermTenant> permTenantPage = this.page(page, queryWrapper);
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(permTenantPage.getRecords());

        return new Page<PermTenantVO>()
                .setRecords(voList)
                .setTotal(permTenantPage.getTotal())
                .setCurrent(permTenantPage.getCurrent())
                .setSize(permTenantPage.getSize());
    }

    /**
     * 根据权限ID批量查询权限租户 [分页] - 返回分组结果
     * @param permIds 权限ID列表
     * @param page 分页参数
     * @return 按权限ID分组的权限租户分页列表
     */
    @Override
    public IPage<Map<Long, List<PermTenantVO>>> getPermTenantsByPermIds(List<Long> permIds, Page<PermTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                permIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(PermTenant::getSPermId, permIds)
                        .eq(PermTenant::getDeleted, false)
                        .orderByDesc(PermTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                permTenantConverter::EnListToVOList,
                // 按权限ID分组
                PermTenantVO::getSPermId
        );
    }

    /**
     * 新增权限租户
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermTenant(PermTenantRTO permTenantRTO) {
        PermTenant permTenant = new PermTenant();
        BeanUtils.copyProperties(permTenantRTO, permTenant);

        // 设置默认值
        permTenant.setId(GeneratorUtils.generateId());
        permTenant.setCreatedAt(GeneratorUtils.generateCurrentTime());
        permTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        permTenant.setCreatedById(FetchUtils.getCurrentUserId());
        if (permTenantRTO.getState() == null && permTenant.getDeleted() == null) {
            permTenant.setState(true);
            permTenant.setDeleted(false);
        }

        return this.save(permTenant);
    }

    /**
     * 更新权限租户
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermTenant(PermTenantRTO permTenantRTO) {
        if (permTenantRTO.getId() == null) {
            return false;
        }

        PermTenant permTenant = new PermTenant();
        BeanUtils.copyProperties(permTenantRTO, permTenant);
        permTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<PermTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermTenant::getId, permTenantRTO.getId())
                .eq(PermTenant::getDeleted, false);

        return this.update(permTenant, updateWrapper);
    }

    /**
     * 更新权限租户状态
     * @param id 权限租户ID
     * @param state 状态
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermTenantState(Long id, Boolean state) {
        LambdaUpdateWrapper<PermTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermTenant::getId, id)
                .eq(PermTenant::getDeleted, false)
                .set(PermTenant::getState, state)
                .set(PermTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除权限租户（真删除）
     * @param id 权限租户ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除权限租户
     * @param id 权限租户ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeletePermTenant(Long id) {
        LambdaUpdateWrapper<PermTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermTenant::getId, id)
                .eq(PermTenant::getDeleted, false)
                .set(PermTenant::getDeleted, true)
                .set(PermTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除权限租户（真删除）
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeletePermTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除权限租户
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeletePermTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<PermTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PermTenant::getId, ids)
                .eq(PermTenant::getDeleted, false)
                .set(PermTenant::getDeleted, true)
                .set(PermTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

}
