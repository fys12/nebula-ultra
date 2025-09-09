package com.cloudvalley.nebula.monolith.business.perm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermRoleRTO;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermRoleVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户角色-租户权限关联 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IPermRoleService extends IService<PermRole> {

    /**
     * 查询权限角色列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<PermRoleVO> getPermRoleList(Page<PermRole> page);

    /**
     * 根据权限角色id查询权限角色(可查询单个或多个，传入的id可以单个或多个) [通过权限角色id获取一个或多个「权限-角色」绑定关系的完整信息][分页]
     * @param ids 权限角色ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<PermRoleVO> getPermRolesByIds(List<Long> ids, Page<PermRole> page);

    /**
     * 根据租户权限id查询权限角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）租户权限有哪些租户角色绑定][分页]
     * @param permId 租户权限ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<PermRoleVO> getPermRolesByPermId(Long permId, Page<PermRole> page);

    /**
     * 根据租户权限id查询权限角色(批量查询多个权限) [查看某个（或多个）租户权限有哪些租户角色绑定][分页]
     * @param permIds 租户权限ID列表
     * @param page 分页参数
     * @return 分页结果，按权限ID分组
     */
    IPage<Map<Long, List<PermRoleVO>>> getPermRolesByPermIds(List<Long> permIds, Page<PermRole> page);

    /**
     * 根据租户角色id查询权限角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）租户角色有哪些租户权限绑定][分页]
     * @param roleId 租户角色ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<PermRoleVO> getPermRolesByRoleId(Long roleId, Page<PermRole> page);

    /**
     * 根据租户角色id查询权限角色(批量查询多个角色) [查看某个（或多个）租户角色有哪些租户权限绑定][分页]
     * @param roleIds 租户角色ID列表
     * @param page 分页参数
     * @return 分页结果，按角色ID分组
     */
    IPage<Map<Long, List<PermRoleVO>>> getPermRolesByRoleIds(List<Long> roleIds, Page<PermRole> page);

    /**
     * 新增权限角色
     * @param permRoleRTO 权限角色信息
     * @return 是否成功
     */
    boolean createPermRole(PermRoleRTO permRoleRTO);

    /**
     * 更新权限角色
     * @param permRoleRTO 权限角色信息
     * @return 是否成功
     */
    boolean updatePermRole(PermRoleRTO permRoleRTO);

    /**
     * 更新权限角色状态 [该权限在该角色中的状态]
     * @param id 权限角色ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updatePermRoleState(Long id, Boolean state);

    /**
     * 删除权限角色(真)
     * @param id 权限角色ID
     * @return 是否成功
     */
    boolean deletePermRole(Long id);

    /**
     * 软删除权限角色
     * @param id 权限角色ID
     * @return 是否成功
     */
    boolean softDeletePermRole(Long id);

    /**
     * 批量删除权限角色(真)
     * @param ids 权限角色ID列表
     * @return 是否成功
     */
    boolean batchDeletePermRoles(List<Long> ids);

    /**
     * 批量软删除权限角色
     * @param ids 权限角色ID列表
     * @return 是否成功
     */
    boolean batchSoftDeletePermRoles(List<Long> ids);


}
