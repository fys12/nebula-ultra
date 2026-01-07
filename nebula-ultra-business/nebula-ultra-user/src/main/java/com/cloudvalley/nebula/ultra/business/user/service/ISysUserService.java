package com.cloudvalley.nebula.ultra.business.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.user.model.rto.SysUserRTO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询系统用户列表 [分页]
     * @param page 分页参数
     * @return 系统用户列表
     */
    IPage<SysUserVO> getUserList(Page<SysUser> page);

    /**
     * 根据ID批量查询系统用户 [分页]
     * @param ids 用户ID列表
     * @param page 分页参数
     * @return 用户信息分页列表
     */
    IPage<SysUserVO> getUsersByIds(List<Long> ids, Page<SysUser> page);

    /**
     * 根据 用户名 模糊查询 系统用户信息
     * @param username 用户名
     * @return 用户信息
     */
    List<SysUserVO> getUserByUsername(String username);

    /**
     * 根据用户名 和 密码 查询系统用户
     * @param username 用户名
     * @param passwordHash 密码(hash加密后)
     * @return 用户信息
     */
    SysUserVO getUserByUsernameAndPasswordHash(String username, String passwordHash);

    /**
     * 新增系统用户
     * @param userRTO 用户信息
     * @return 操作结果
     */
    boolean createUser(SysUserRTO userRTO);

    /**
     * 更新系统用户
     * @param userRTO 用户信息
     * @return 操作结果
     */
    boolean updateUser(SysUserRTO userRTO);

    /**
     * 更新系统用户状态
     * @param id 用户ID
     * @param state 状态
     * @return 操作结果
     */
    boolean updateUserState(Long id, Boolean state);

    /**
     * 删除系统用户（真删除）
     * @param id 用户ID
     * @return 操作结果
     */
    boolean deleteUser(Long id);

    /**
     * 软删除系统用户
     * @param id 用户ID
     * @return 操作结果
     */
    boolean softDeleteUser(Long id);

    /**
     * 批量删除系统用户（真删除）
     * @param ids 用户ID列表
     * @return 操作结果
     */
    boolean batchDeleteUsers(List<Long> ids);

    /**
     * 批量软删除系统用户
     * @param ids 用户ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteUsers(List<Long> ids);
}
