package com.cloudvalley.nebula.ultra.shared.api.perm.service;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermRoleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPermRoleCommonService {

    /**
     * 根据权限角色ID查询单个权限角色绑定信息
     * @param id 权限角色关联的唯一标识ID
     * @return 对应的 PermRoleVO 对象，若不存在或已软删则返回 null
     */
    PermRoleVO getPermRoleById(Long id);

    /**
     * 根据多个权限角色ID全量查询绑定信息（不分页）
     * @param ids 权限角色关联ID列表
     * @return 所有匹配的 PermRoleVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<PermRoleVO> getPermRolesByIds(List<Long> ids);

    /**
     * 根据租户权限ID全量查询其绑定的角色列表（查看某权限被哪些角色使用）
     * @param permId 租户权限ID
     * @return 该权限绑定的所有 PermRoleVO 列表，按创建时间倒序排列
     */
    List<PermRoleVO> getPermRolesByPermId(Long permId);

    /**
     * 根据多个租户权限ID全量查询绑定角色，并按权限ID分组返回结果
     * @param permIds 租户权限ID列表
     * @return 包含一个 Map 的列表，键为 tPermId，值为对应权限绑定的角色VO列表；输入为空时返回空列表
     */
    List<Map<Long, List<PermRoleVO>>> getPermRolesByPermIds(List<Long> permIds);

    /**
     * 根据租户角色ID全量查询其绑定的权限列表（查看某角色拥有哪些权限）
     * @param roleId 租户角色ID
     * @return 该角色绑定的所有 PermRoleVO 列表，按创建时间倒序排列
     */
    List<PermRoleVO> getPermRolesByRoleId(Long roleId);

    /**
     * 根据多个租户角色ID全量查询绑定权限，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @return 包含一个 Map 的列表，键为 tRoleId，值为对应角色绑定的权限VO列表；输入为空时返回空列表
     */
    List<Map<Long, List<PermRoleVO>>> getPermRolesByRoleIds(List<Long> roleIds);

    /**
     * 根据租户权限ID查询所有绑定的租户角色ID列表
     * @param permId 租户权限ID
     * @return 该权限绑定的租户角色ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getRoleIdsByPermId(Long permId);

    /**
     * 根据租户角色ID查询所有绑定的租户权限ID列表
     * @param roleId 租户角色ID
     * @return 该角色绑定的租户权限ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getPermIdsByRoleId(Long roleId);


}
