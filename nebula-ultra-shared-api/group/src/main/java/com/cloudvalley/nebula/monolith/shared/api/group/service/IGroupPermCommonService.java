package com.cloudvalley.nebula.monolith.shared.api.group.service;


import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupPermVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGroupPermCommonService {

    /**
     * 根据ID查询权限组关联（单个）
     * @param id 关联ID
     * @return 权限组关联信息
     */
    GroupPermVO getGroupPermById(Long id);

    /**
     * 根据ID列表批量查询权限组-权限绑定关系 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 匹配的 GroupPermVO 列表
     */
    List<GroupPermVO> getGroupPermsByIds(List<Long> ids);

    /**
     * 根据系统组ID查询其绑定的所有权限 [全量]
     *
     * @param groupId 系统组ID
     * @return 该组绑定的 GroupPermVO 列表
     */
    List<GroupPermVO> getGroupPermsByGroupId(Long groupId);

    /**
     * 根据多个系统组ID批量查询权限绑定关系，并按组ID分组返回 [全量]
     *
     * @param groupIds 系统组ID列表
     * @return 分组结果列表，每个元素为 Map<groupId, VO列表>
     */
    List<Map<Long, List<GroupPermVO>>> getGroupPermsByGroupIds(List<Long> groupIds);

    /**
     * 根据租户权限ID查询所有绑定该权限的系统组 [全量]
     *
     * @param permId 租户权限ID
     * @return 绑定该权限的 GroupPermVO 列表
     */
    List<GroupPermVO> getGroupPermsByPermId(Long permId);

    /**
     * 根据多个租户权限ID批量查询绑定关系，并按权限ID分组返回 [全量]
     *
     * @param permIds 租户权限ID列表
     * @return 分组结果列表，每个元素为 Map<permId, VO列表>
     */
    List<Map<Long, List<GroupPermVO>>> getGroupPermsByPermIds(List<Long> permIds);

    /**
     * 根据系统组ID查询租户权限ID列表
     * @param groupId 系统组ID
     * @return 租户权限ID列表
     */
    Set<Long> getPermIdsByGroupId(Long groupId);

    /**
     * 根据租户权限ID查询系统组ID列表
     * @param permId 租户权限ID
     * @return 系统组ID列表
     */
    Set<Long> getGroupIdsByPermId(Long permId);


}
