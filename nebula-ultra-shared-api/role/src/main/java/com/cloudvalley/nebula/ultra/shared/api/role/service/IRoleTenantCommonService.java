package com.cloudvalley.nebula.ultra.shared.api.role.service;

import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoleTenantCommonService {

    /**
     * 根据租户角色绑定ID查询单个绑定信息
     * @param id 租户角色关联的唯一标识ID
     * @return 对应的 RoleTenantVO 对象，若不存在或已软删则返回 null
     */
    RoleTenantVO getRoleTenantById(Long id);

    /**
     * 根据多个租户角色ID全量查询绑定信息（不分页）
     * @param ids 租户角色关联ID列表
     * @return 所有匹配的 RoleTenantVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<RoleTenantVO> getRoleTenantsByIds(List<Long> ids);

    /**
     * 根据系统租户ID全量查询其绑定的角色列表（查看某租户拥有哪些系统角色）
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的所有 RoleTenantVO 列表，按创建时间倒序排列
     */
    List<RoleTenantVO> getRoleTenantsByTenantId(Long tenantId);

    /**
     * 根据多个系统租户ID全量查询绑定角色，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 RoleTenantVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<RoleTenantVO>>> getRoleTenantsByTenantIds(List<Long> tenantIds);

    /**
     * 根据系统角色ID全量查询其绑定的租户列表（查看某角色被哪些租户使用）
     * @param roleId 系统角色ID（sRoleId）
     * @return 该角色绑定的所有 RoleTenantVO 列表，按创建时间倒序排列
     */
    List<RoleTenantVO> getRoleTenantsByRoleId(Long roleId);

    /**
     * 根据多个系统角色ID全量查询使用租户，并按角色ID分组返回结果
     * @param roleIds 系统角色ID列表
     * @return 包含一个 Map 的列表，键为 sRoleId，值为对应角色的 RoleTenantVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<RoleTenantVO>>> getRoleTenantsByRoleIds(List<Long> roleIds);

    /**
     * 根据系统租户ID查询其拥有的所有系统角色ID列表
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的系统角色ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getRoleIdsByTenantId(Long tenantId);

    /**
     * 根据系统角色ID查询所有绑定该角色的系统租户ID列表
     * @param roleId 系统角色ID（sRoleId）
     * @return 绑定该角色的系统租户ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getTenantIdsByRoleId(Long roleId);

}
