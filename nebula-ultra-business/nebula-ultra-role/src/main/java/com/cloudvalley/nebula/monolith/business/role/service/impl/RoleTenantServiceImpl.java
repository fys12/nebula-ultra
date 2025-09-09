package com.cloudvalley.nebula.monolith.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.role.converter.RoleTenantConverter;
import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleTenant;
import com.cloudvalley.nebula.monolith.business.role.mapper.RoleTenantMapper;
import com.cloudvalley.nebula.monolith.business.role.model.rto.RoleTenantRTO;
import com.cloudvalley.nebula.monolith.business.role.service.IRoleTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-角色绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class RoleTenantServiceImpl extends ServiceImpl<RoleTenantMapper, RoleTenant> implements IRoleTenantService {

    @Autowired
    private RoleTenantConverter roleTenantConverter;

    /**
     * 分页查询「系统租户-系统角色」绑定列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 RoleTenantVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<RoleTenantVO> getRoleTenantList(Page<RoleTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<RoleTenant> roleTenantPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(roleTenantPage.getRecords());

        // 创建新的 IPage<RoleTenantVO>，复用原分页参数
        return new Page<RoleTenantVO>()
                .setRecords(voList)
                .setTotal(roleTenantPage.getTotal())
                .setCurrent(roleTenantPage.getCurrent())
                .setSize(roleTenantPage.getSize());
    }

    /**
     * 根据多个租户角色ID分页批量查询绑定信息
     * @param ids 绑定关系ID列表
     * @param page 分页参数对象
     * @return 分页的 RoleTenantVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<RoleTenantVO> getRoleTenantsByIds(List<Long> ids, Page<RoleTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleTenant::getId, ids)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);

        IPage<RoleTenant> roleTenantPage = this.page(page, queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(roleTenantPage.getRecords());

        return new Page<RoleTenantVO>()
                .setRecords(voList)
                .setTotal(roleTenantPage.getTotal())
                .setCurrent(roleTenantPage.getCurrent())
                .setSize(roleTenantPage.getSize());
    }

    /**
     * 根据系统租户ID分页查询其绑定的角色列表（查看某租户拥有哪些系统角色）
     * @param tenantId 系统租户ID（sTenantId）
     * @param page 分页参数对象
     * @return 分页的 RoleTenantVO 列表，表示该租户绑定的角色信息；若无数据返回空分页
     */
    @Override
    public IPage<RoleTenantVO> getRoleTenantsByTenantId(Long tenantId, Page<RoleTenant> page) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSTenantId, tenantId)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);

        IPage<RoleTenant> roleTenantPage = this.page(page, queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(roleTenantPage.getRecords());

        return new Page<RoleTenantVO>()
                .setRecords(voList)
                .setTotal(roleTenantPage.getTotal())
                .setCurrent(roleTenantPage.getCurrent())
                .setSize(roleTenantPage.getSize());
    }

    /**
     * 根据多个系统租户ID分页批量查询绑定角色，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sTenantId，值为对应租户的 RoleTenantVO 列表
     */
    @Override
    public IPage<Map<Long, List<RoleTenantVO>>> getRoleTenantsByTenantIds(List<Long> tenantIds, Page<RoleTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                tenantIds,
                // 分页参数
                page,
                // 查询条件：按租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(RoleTenant::getSTenantId, tenantIds)
                        .eq(RoleTenant::getDeleted, false)
                        .orderByDesc(RoleTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                roleTenantConverter::EnListToVOList,
                // 按租户ID分组
                RoleTenantVO::getSTenantId
        );
    }

    /**
     * 根据系统角色ID分页查询其绑定的租户列表（查看某角色被哪些租户使用）
     * @param roleId 系统角色ID（sRoleId）
     * @param page 分页参数对象
     * @return 分页的 RoleTenantVO 列表，表示绑定该角色的租户信息；若无数据返回空分页
     */
    @Override
    public IPage<RoleTenantVO> getRoleTenantsByRoleId(Long roleId, Page<RoleTenant> page) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSRoleId, roleId)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);

        IPage<RoleTenant> roleTenantPage = this.page(page, queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(roleTenantPage.getRecords());

        return new Page<RoleTenantVO>()
                .setRecords(voList)
                .setTotal(roleTenantPage.getTotal())
                .setCurrent(roleTenantPage.getCurrent())
                .setSize(roleTenantPage.getSize());
    }

    /**
     * 根据多个系统角色ID分页批量查询绑定租户，并按角色ID分组返回结果
     * @param roleIds 系统角色ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 sRoleId，值为对应角色的 RoleTenantVO 列表
     */
    @Override
    public IPage<Map<Long, List<RoleTenantVO>>> getRoleTenantsByRoleIds(List<Long> roleIds, Page<RoleTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                roleIds,
                // 分页参数
                page,
                // 查询条件：按角色ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(RoleTenant::getSRoleId, roleIds)
                        .eq(RoleTenant::getDeleted, false)
                        .orderByDesc(RoleTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                roleTenantConverter::EnListToVOList,
                // 按角色ID分组
                RoleTenantVO::getSRoleId
        );
    }

    /**
     * 新增「系统租户-系统角色」绑定关系
     * @param roleTenantRTO 请求传输对象，包含 sTenantId 和 sRoleId
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRoleTenant(RoleTenantRTO roleTenantRTO) {
        RoleTenant roleTenant = new RoleTenant();
        BeanUtils.copyProperties(roleTenantRTO, roleTenant);

        // 设置默认值
        roleTenant.setId(GeneratorUtils.generateId());
        roleTenant.setCreatedAt(GeneratorUtils.generateCurrentTime());
        roleTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        roleTenant.setCreatedById(FetchUtils.getCurrentUserId());
        if (roleTenantRTO.getState() == null && roleTenant.getDeleted() == null) {
            roleTenant.setState(true);
            roleTenant.setDeleted(false);
        }

        return this.save(roleTenant);
    }

    /**
     * 更新「系统租户-系统角色」绑定信息
     * @param roleTenantRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleTenant(RoleTenantRTO roleTenantRTO) {
        if (roleTenantRTO.getId() == null) {
            return false;
        }

        RoleTenant roleTenant = new RoleTenant();
        BeanUtils.copyProperties(roleTenantRTO, roleTenant);
        roleTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<RoleTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleTenant::getId, roleTenantRTO.getId())
                .eq(RoleTenant::getDeleted, false);

        return this.update(roleTenant, updateWrapper);
    }

    /**
     * 更新租户角色绑定状态（启用/禁用该角色在租户中的生效）
     * @param id 绑定关系ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleTenantState(Long id, Boolean state) {
        LambdaUpdateWrapper<RoleTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleTenant::getId, id)
                .eq(RoleTenant::getDeleted, false)
                .set(RoleTenant::getState, state)
                .set(RoleTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除租户角色绑定（物理删除）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户角色绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteRoleTenant(Long id) {
        LambdaUpdateWrapper<RoleTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleTenant::getId, id)
                .eq(RoleTenant::getDeleted, false)
                .set(RoleTenant::getDeleted, true)
                .set(RoleTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个租户角色绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRoleTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个租户角色绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteRoleTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<RoleTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(RoleTenant::getId, ids)
                .eq(RoleTenant::getDeleted, false)
                .set(RoleTenant::getDeleted, true)
                .set(RoleTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }
    
}
