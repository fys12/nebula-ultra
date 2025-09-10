package com.cloudvalley.nebula.ultra.shared.api.group.service;


import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGroupTenantCommonService {
    
    /**
     * 根据ID查询单个租户-组绑定信息
     *
     * @param id 绑定记录ID
     * @return 对应的 GroupTenantVO，若不存在则返回 null
     */
    GroupTenantVO getGroupTenantById(Long id);

    /**
     * 根据ID列表批量查询租户-组绑定 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 所有匹配的 GroupTenantVO 列表
     */
    List<GroupTenantVO> getGroupTenantsByIds(List<Long> ids);

    /**
     * 根据系统租户ID查询其绑定的所有系统组 [全量]
     *
     * @param sTenantId 系统租户ID
     * @return 该租户绑定的所有 GroupTenantVO 列表
     */
    List<GroupTenantVO> getGroupTenantsBySTenantId(Long sTenantId);

    /**
     * 根据多个系统租户ID批量查询绑定关系，并按租户ID分组返回 [全量]
     *
     * @param sTenantIds 系统租户ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    List<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySTenantIds(List<Long> sTenantIds);

    /**
     * 根据系统组ID查询所有绑定该组的系统租户 [全量]
     *
     * @param sGroupId 系统组ID
     * @return 该组绑定的所有 GroupTenantVO 列表
     */
    List<GroupTenantVO> getGroupTenantsBySGroupId(Long sGroupId);

    /**
     * 根据多个系统组ID批量查询绑定关系，并按组ID分组返回 [全量]
     *
     * @param sGroupIds 系统组ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    List<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySGroupIds(List<Long> sGroupIds);

    /**
     * 根据系统租户ID查询其绑定的所有系统组ID
     *
     * @param sTenantId 系统租户ID
     * @return 绑定的系统组ID集合
     */
    Set<Long> getSGroupIdsBySTenantId(Long sTenantId);

    /**
     * 根据系统组ID查询所有绑定该组的系统租户ID
     *
     * @param sGroupId 系统组ID
     * @return 绑定的系统租户ID集合
     */
    Set<Long> getSTenantIdsBySGroupId(Long sGroupId);


}
