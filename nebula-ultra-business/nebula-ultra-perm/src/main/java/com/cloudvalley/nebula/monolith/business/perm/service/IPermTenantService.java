package com.cloudvalley.nebula.monolith.business.perm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermTenantRTO;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限-租户绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IPermTenantService extends IService<PermTenant> {

    /**
     * 查询权限租户列表 [分页]
     * @param page 分页参数
     * @return 权限租户列表
     */
    IPage<PermTenantVO> getPermTenantList(Page<PermTenant> page);

    /**
     * 根据权限租户ID批量查询权限租户 [分页]
     * @param ids 权限租户ID列表
     * @param page 分页参数
     * @return 权限租户信息分页列表
     */
    IPage<PermTenantVO> getPermTenantsByIds(List<Long> ids, Page<PermTenant> page);

    /**
     * 根据租户ID查询权限租户 [分页]
     * @param tenantId 租户ID
     * @param page 分页参数
     * @return 权限租户列表
     */
    IPage<PermTenantVO> getPermTenantsByTenantId(Long tenantId, Page<PermTenant> page);

    /**
     * 根据租户ID批量查询权限租户 [分页] - 返回分组结果
     * @param tenantIds 租户ID列表
     * @param page 分页参数
     * @return 按租户ID分组的权限租户分页列表
     */
    IPage<Map<Long, List<PermTenantVO>>> getPermTenantsByTenantIds(List<Long> tenantIds, Page<PermTenant> page);

    /**
     * 根据权限ID查询权限租户 [分页]
     * @param permId 权限ID
     * @param page 分页参数
     * @return 权限租户列表
     */
    IPage<PermTenantVO> getPermTenantsByPermId(Long permId, Page<PermTenant> page);

    /**
     * 根据权限ID批量查询权限租户 [分页] - 返回分组结果
     * @param permIds 权限ID列表
     * @param page 分页参数
     * @return 按权限ID分组的权限租户分页列表
     */
    IPage<Map<Long, List<PermTenantVO>>> getPermTenantsByPermIds(List<Long> permIds, Page<PermTenant> page);

    /**
     * 新增权限租户
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    boolean createPermTenant(PermTenantRTO permTenantRTO);

    /**
     * 更新权限租户
     * @param permTenantRTO 权限租户信息
     * @return 操作结果
     */
    boolean updatePermTenant(PermTenantRTO permTenantRTO);

    /**
     * 更新权限租户状态
     * @param id 权限租户ID
     * @param state 状态
     * @return 操作结果
     */
    boolean updatePermTenantState(Long id, Boolean state);

    /**
     * 删除权限租户（真删除）
     * @param id 权限租户ID
     * @return 操作结果
     */
    boolean deletePermTenant(Long id);

    /**
     * 软删除权限租户
     * @param id 权限租户ID
     * @return 操作结果
     */
    boolean softDeletePermTenant(Long id);

    /**
     * 批量删除权限租户（真删除）
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    boolean batchDeletePermTenants(List<Long> ids);

    /**
     * 批量软删除权限租户
     * @param ids 权限租户ID列表
     * @return 操作结果
     */
    boolean batchSoftDeletePermTenants(List<Long> ids);

}
