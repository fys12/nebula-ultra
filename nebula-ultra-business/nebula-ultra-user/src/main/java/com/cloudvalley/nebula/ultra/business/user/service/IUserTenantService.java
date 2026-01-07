package com.cloudvalley.nebula.ultra.business.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.model.entity.UserTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.user.model.rto.UserTenantRTO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-用户关联 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IUserTenantService extends IService<UserTenant> {

    /**
     * 查询租户用户列表 [分页]
     * @param page 分页参数
     * @return 租户用户列表
     */
    IPage<UserTenantVO> getUserTenantList(Page<UserTenant> page);

    /**
     * 根据租户用户ID批量查询租户用户 [分页]
     * @param ids 租户用户ID列表
     * @param page 分页参数
     * @return 租户用户信息分页列表
     */
    IPage<UserTenantVO> getUserTenantsByIds(List<Long> ids, Page<UserTenant> page);

    /**
     * 根据租户ID查询租户用户 [分页]
     * @param tenantId 租户ID
     * @param page 分页参数
     * @return 租户用户列表
     */
    IPage<UserTenantVO> getUserTenantsByTenantId(Long tenantId, Page<UserTenant> page);


    /**
     * 根据租户ID批量查询租户用户 [分页] - 返回分组结果
     * @param tenantIds 租户ID列表
     * @param page 分页参数
     * @return 按租户ID分组的租户用户分页列表
     */
    IPage<Map<Long, List<UserTenantVO>>> getUserTenantsByTenantIds(List<Long> tenantIds, Page<UserTenant> page);

    /**
     * 根据用户ID查询租户用户 [分页]
     * @param userId 用户ID
     * @param page 分页参数
     * @return 租户用户列表
     */
    IPage<UserTenantVO> getUserTenantsByUserId(Long userId, Page<UserTenant> page);

    /**
     * 根据用户ID批量查询租户用户 [分页] - 返回分组结果
     * @param userIds 用户ID列表
     * @param page 分页参数
     * @return 按用户ID分组的租户用户分页列表
     */
    IPage<Map<Long, List<UserTenantVO>>> getUserTenantsByUserIds(List<Long> userIds, Page<UserTenant> page);

    /**
     * 新增租户用户
     * @param tenantUserRTO 租户用户信息
     * @return 操作结果
     */
    boolean createUserTenant(UserTenantRTO tenantUserRTO);

    /**
     * 更新租户用户
     * @param tenantUserRTO 租户用户信息
     * @return 操作结果
     */
    boolean updateUserTenant(UserTenantRTO tenantUserRTO);

    /**
     * 更新租户用户状态
     * @param id 租户用户ID
     * @param state 状态
     * @return 操作结果
     */
    boolean updateUserTenantState(Long id, Boolean state);

    /**
     * 删除租户用户（真删除）
     * @param id 租户用户ID
     * @return 操作结果
     */
    boolean deleteUserTenant(Long id);

    /**
     * 软删除租户用户
     * @param id 租户用户ID
     * @return 操作结果
     */
    boolean softDeleteUserTenant(Long id);

    /**
     * 批量删除租户用户（真删除）
     * @param ids 租户用户ID列表
     * @return 操作结果
     */
    boolean batchDeleteUserTenants(List<Long> ids);

    /**
     * 批量软删除租户用户
     * @param ids 租户用户ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteUserTenants(List<Long> ids);
    
}
