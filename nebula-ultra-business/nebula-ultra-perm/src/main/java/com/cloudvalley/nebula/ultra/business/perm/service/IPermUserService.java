package com.cloudvalley.nebula.ultra.business.perm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.PermUserRTO;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-租户权限绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IPermUserService extends IService<PermUser> {

    /**
     * 查询用户租户权限绑定列表 [分页]
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    IPage<PermUserVO> getPermUserList(Page<PermUser> page);

    /**
     * 根据用户租户权限ID批量查询用户租户权限 [分页]
     * @param ids 用户租户权限ID列表
     * @param page 分页参数
     * @return 用户租户权限信息分页列表
     */
    IPage<PermUserVO> getPermUsersByIds(List<Long> ids, Page<PermUser> page);

    /**
     * 根据租户用户ID查询用户租户权限 [分页]
     * @param tUserId 租户用户ID
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    IPage<PermUserVO> getPermUsersByTUserId(Long tUserId, Page<PermUser> page);

    /**
     * 根据租户用户ID批量查询用户租户权限 [分页] - 返回分组结果
     * @param tUserIds 租户用户ID列表
     * @param page 分页参数
     * @return 按租户用户ID分组的用户租户权限分页列表
     */
    IPage<Map<Long, List<PermUserVO>>> getPermUsersByTUserIds(List<Long> tUserIds, Page<PermUser> page);

    /**
     * 根据租户权限ID查询用户租户权限 [分页]
     * @param tPermId 租户权限ID
     * @param page 分页参数
     * @return 用户租户权限列表
     */
    IPage<PermUserVO> getPermUsersByTPermId(Long tPermId, Page<PermUser> page);

    /**
     * 根据租户权限ID批量查询用户租户权限 [分页] - 返回分组结果
     * @param tPermIds 租户权限ID列表
     * @param page 分页参数
     * @return 按租户权限ID分组的用户租户权限分页列表
     */
    IPage<Map<Long, List<PermUserVO>>> getPermUsersByTPermIds(List<Long> tPermIds, Page<PermUser> page);

    /**
     * 新增用户租户权限绑定
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    boolean createPermUser(PermUserRTO permUserRTO);

    /**
     * 更新用户租户权限绑定
     * @param permUserRTO 用户租户权限信息
     * @return 操作结果
     */
    boolean updatePermUser(PermUserRTO permUserRTO);

    /**
     * 更新用户租户权限状态
     * @param id 用户租户权限ID
     * @param status 状态
     * @return 操作结果
     */
    boolean updatePermUserStatus(Long id, Boolean status);

    /**
     * 删除用户租户权限绑定（真删除）
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    boolean deletePermUser(Long id);

    /**
     * 软删除用户租户权限绑定
     * @param id 用户租户权限ID
     * @return 操作结果
     */
    boolean softDeletePermUser(Long id);

    /**
     * 批量删除用户租户权限绑定（真删除）
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    boolean batchDeletePermUsers(List<Long> ids);

    /**
     * 批量软删除用户租户权限绑定
     * @param ids 用户租户权限ID列表
     * @return 操作结果
     */
    boolean batchSoftDeletePermUsers(List<Long> ids);

}
