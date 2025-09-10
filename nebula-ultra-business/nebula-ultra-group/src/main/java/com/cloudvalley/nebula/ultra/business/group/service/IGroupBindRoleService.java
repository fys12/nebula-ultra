package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupBindRoleRTO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindRoleVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户角色绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IGroupBindRoleService extends IService<GroupBindRole> {

    /**
     * 查询组租户角色列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<GroupBindRoleVO> getGroupBindRoleList(Page<GroupBindRole> page);

    /**
     * 根据组租户角色id查询组租户角色(可查询单个或多个，传入的id可以单个或多个) [通过组租户角色id获取一个或多个「组-租户角色」绑定关系的完整信息][分页]
     * @param ids 组租户角色ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<GroupBindRoleVO> getGroupBindRolesByIds(List<Long> ids, Page<GroupBindRole> page);

    /**
     * 根据系统组id查询组租户角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）系统组有哪些租户角色绑定][分页]
     * @param groupId 系统组ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<GroupBindRoleVO> getGroupBindRolesByGroupId(Long groupId, Page<GroupBindRole> page);

    /**
     * 根据系统组id查询组租户角色(批量查询多个组) [查看某个（或多个）系统组有哪些租户角色绑定][分页]
     * @param groupIds 系统组ID列表
     * @param page 分页参数
     * @return 分页结果，按组ID分组
     */
    IPage<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByGroupIds(List<Long> groupIds, Page<GroupBindRole> page);

    /**
     * 根据租户角色id查询组租户角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）租户角色有哪些系统组绑定][分页]
     * @param roleId 租户角色ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<GroupBindRoleVO> getGroupBindRolesByRoleId(Long roleId, Page<GroupBindRole> page);

    /**
     * 根据租户角色id查询组租户角色(批量查询多个角色) [查看某个（或多个）租户角色有哪些系统组绑定][分页]
     * @param roleIds 租户角色ID列表
     * @param page 分页参数
     * @return 分页结果，按角色ID分组
     */
    IPage<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByRoleIds(List<Long> roleIds, Page<GroupBindRole> page);

    /**
     * 新增组租户角色
     * @param groupBindRoleRTO 组租户角色信息
     * @return 是否成功
     */
    boolean createGroupBindRole(GroupBindRoleRTO groupBindRoleRTO);

    /**
     * 更新组租户角色
     * @param groupBindRoleRTO 组租户角色信息
     * @return 是否成功
     */
    boolean updateGroupBindRole(GroupBindRoleRTO groupBindRoleRTO);

    /**
     * 删除组租户角色(真)
     * @param id 组租户角色ID
     * @return 是否成功
     */
    boolean deleteGroupBindRole(Long id);

    /**
     * 软删除组租户角色
     * @param id 组租户角色ID
     * @return 是否成功
     */
    boolean softDeleteGroupBindRole(Long id);

    /**
     * 批量删除组租户角色(真)
     * @param ids 组租户角色ID列表
     * @return 是否成功
     */
    boolean batchDeleteGroupBindRoles(List<Long> ids);

    /**
     * 批量软删除组租户角色
     * @param ids 组租户角色ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteGroupBindRoles(List<Long> ids);
    
}
