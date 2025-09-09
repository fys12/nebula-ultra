package com.cloudvalley.nebula.monolith.business.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.role.model.rto.RoleUserRTO;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户角色分配 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IRoleUserService extends IService<RoleUser> {

    /**
     * 查询租户用户租户角色分配列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleUserVO> getRoleUserList(Page<RoleUser> page);

    /**
     * 根据租户用户租户角色id查询租户用户租户角色分配(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 租户用户租户角色ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleUserVO> getRoleUsersByIds(List<Long> ids, Page<RoleUser> page);

    /**
     * 根据租户用户id查询租户用户租户角色分配(可查询单个或多个，传入的id可以单个或多个) [查某个（或多个）用户有哪些角色][分页]
     * @param userId 租户用户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleUserVO> getRoleUsersByUserId(Long userId, Page<RoleUser> page);

    /**
     * 根据租户用户id查询租户用户租户角色分配(批量查询多个用户) [查某个（或多个）用户有哪些角色][分页]
     * @param userIds 租户用户ID列表
     * @param page 分页参数
     * @return 分页结果，按用户ID分组
     */
    IPage<Map<Long, List<RoleUserVO>>> getRoleUsersByUserIds(List<Long> userIds, Page<RoleUser> page);

    /**
     * 根据租户角色id查询租户用户租户角色分配(可查询单个或多个，传入的id可以单个或多个) [查某个（或多个）角色有哪些用户][分页]
     * @param roleId 租户角色ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleUserVO> getRoleUsersByRoleId(Long roleId, Page<RoleUser> page);

    /**
     * 根据租户角色id查询租户用户租户角色分配(批量查询多个角色) [查某个（或多个）角色有哪些用户][分页]
     * @param roleIds 租户角色ID列表
     * @param page 分页参数
     * @return 分页结果，按角色ID分组
     */
    IPage<Map<Long, List<RoleUserVO>>> getRoleUsersByRoleIds(List<Long> roleIds, Page<RoleUser> page);

    /**
     * 新增用户角色
     * @param roleUserRTO 用户角色信息
     * @return 是否成功
     */
    boolean createRoleUser(RoleUserRTO roleUserRTO);

    /**
     * 更新用户角色
     * @param roleUserRTO 用户角色信息
     * @return 是否成功
     */
    boolean updateRoleUser(RoleUserRTO roleUserRTO);

    /**
     * 更新用户角色状态 [该角色在该用户中的状态]
     * @param id 用户角色ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateRoleUserState(Long id, Boolean state);

    /**
     * 删除用户角色(真)
     * @param id 用户角色ID
     * @return 是否成功
     */
    boolean deleteRoleUser(Long id);

    /**
     * 软删除用户角色
     * @param id 用户角色ID
     * @return 是否成功
     */
    boolean softDeleteRoleUser(Long id);

    /**
     * 批量删除用户角色(真)
     * @param ids 用户角色ID列表
     * @return 是否成功
     */
    boolean batchDeleteRoleUsers(List<Long> ids);

    /**
     * 批量软删除用户角色
     * @param ids 用户角色ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteRoleUsers(List<Long> ids);
    
}
