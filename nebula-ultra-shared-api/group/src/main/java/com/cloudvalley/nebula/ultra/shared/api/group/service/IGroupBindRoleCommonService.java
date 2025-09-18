package com.cloudvalley.nebula.ultra.shared.api.group.service;


import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindRoleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGroupBindRoleCommonService {

    /**
     * 根据组租户角色id查询组租户角色
     * @param id 组租户角色ID
     * @return 组租户角色信息
     */
    GroupBindRoleVO getGroupBindRoleById(Long id);

    /**
     * 根据组-租户角色绑定ID列表查询绑定信息 [全量]
     *
     * @param ids 组-租户角色绑定记录ID列表
     * @return 匹配的 GroupBindRoleVO 列表
     */
    List<GroupBindRoleVO> getGroupBindRolesByIds(List<Long> ids);

    /**
     * 根据系统组ID查询其绑定的所有租户角色 [全量]
     *
     * @param groupId 系统组ID
     * @return 该组绑定的所有 GroupBindRoleVO 列表
     */
    List<GroupBindRoleVO> getGroupBindRolesByGroupId(Long groupId);

    /**
     * 根据系统组ID列表批量查询绑定的租户角色，并按组ID分组返回 [全量]
     *
     * @param groupIds 系统组ID列表
     * @return 分组结果列表，每个元素为 Map<groupId, VO列表>
     */
    Map<Long, List<GroupBindRoleVO>> getGroupBindRolesByGroupIds(List<Long> groupIds);

    /**
     * 根据租户角色ID查询所有绑定该角色的系统组 [全量]
     *
     * @param roleId 租户角色ID
     * @return 绑定该角色的所有 GroupBindRoleVO 列表
     */
    List<GroupBindRoleVO> getGroupBindRolesByRoleId(Long roleId);

    /**
     * 根据租户角色ID列表批量查询绑定的系统组，并按角色ID分组返回 [全量]
     *
     * @param roleIds 租户角色ID列表
     * @return 分组结果列表，每个元素为 Map<roleId, VO列表>
     */
    Map<Long, List<GroupBindRoleVO>> getGroupBindRolesByRoleIds(List<Long> roleIds);

    /**
     * 根据系统组ID查询租户角色ID列表
     * @param groupId 系统组ID
     * @return 租户角色ID集合
     */
    Set<Long> getRoleIdsByGroupId(Long groupId);

    /**
     * 根据租户角色ID查询系统组ID列表
     * @param roleId 租户角色ID
     * @return 系统组ID集合
     */
    Set<Long> getGroupIdsByRoleId(Long roleId);


}
