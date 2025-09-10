package com.cloudvalley.nebula.ultra.shared.api.perm.service;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPermTenantCommonService {

    /**
     * 根据权限租户ID查询单个权限租户绑定信息
     * @param id 权限租户关联的唯一标识ID
     * @return 对应的 PermTenantVO 对象，若不存在或已软删则返回 null
     */
    PermTenantVO getPermTenantById(Long id);

    /**
     * 根据多个权限租户ID全量查询绑定信息（不分页）
     * @param ids 权限租户关联ID列表
     * @return 所有匹配的 PermTenantVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<PermTenantVO> getPermTenantsByIds(List<Long> ids);

    /**
     * 根据租户ID全量查询其绑定的权限列表（查看某租户拥有哪些权限）
     * @param tenantId 租户ID（系统租户ID）
     * @return 该租户绑定的所有 PermTenantVO 列表，按创建时间倒序排列
     */
    List<PermTenantVO> getPermTenantsByTenantId(Long tenantId);

    /**
     * 根据多个租户ID全量查询绑定权限，并按租户ID分组返回结果
     * @param tenantIds 租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 PermTenantVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<PermTenantVO>>> getPermTenantsByTenantIds(List<Long> tenantIds);

    /**
     * 根据权限ID全量查询其绑定的租户列表（查看某权限被哪些租户使用）
     * @param permId 权限ID（系统权限ID）
     * @return 该权限绑定的所有 PermTenantVO 列表，按创建时间倒序排列
     */
    List<PermTenantVO> getPermTenantsByPermId(Long permId);

    /**
     * 根据多个权限ID全量查询绑定租户，并按权限ID分组返回结果
     * @param permIds 权限ID列表
     * @return 包含一个 Map 的列表，键为 sPermId，值为对应权限的 PermTenantVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<PermTenantVO>>> getPermTenantsByPermIds(List<Long> permIds);

    /**
     * 根据租户ID查询其拥有的所有权限ID列表
     * @param tenantId 租户ID（系统租户ID）
     * @return 该租户绑定的权限ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getPermIdsByTenantId(Long tenantId);

    /**
     * 根据权限ID查询所有绑定该权限的租户ID列表
     * @param permId 权限ID（系统权限ID）
     * @return 绑定该权限的租户ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getTenantIdsByPermId(Long permId);

    /**
     * 根据多个租户ID查询其拥有的所有权限ID列表
     * @param tenantIds 租户ID列表（系统租户ID）
     * @return Map结构，键为租户ID，值为该租户关联的权限ID集合
     */
    Map<Long, Set<Long>> getPermIdsByTenantIds(List<Long> tenantIds);

    /**
     * 根据多个权限ID查询所有拥有这些权限的租户ID列表
     * @param permIds 权限ID列表（系统权限ID）
     * @return Map结构，键为权限ID，值为拥有该权限的所有租户ID集合
     */
    Map<Long, Set<Long>> getTenantIdsByPermIds(List<Long> permIds);
}
