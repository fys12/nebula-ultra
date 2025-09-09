package com.cloudvalley.nebula.monolith.shared.api.role.service;

import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleUserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoleUserCommonService {

    /**
     * 根据用户角色分配ID查询单个绑定信息
     * @param id 租户用户-租户角色关联的唯一标识ID
     * @return 对应的 RoleUserVO 对象，若不存在或已软删则返回 null
     */
    RoleUserVO getRoleUserById(Long id);

    /**
     * 根据多个用户角色分配ID全量查询绑定信息（不分页）
     * @param ids 绑定关系ID列表
     * @return 所有匹配的 RoleUserVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<RoleUserVO> getRoleUsersByIds(List<Long> ids);

    /**
     * 根据租户用户ID全量查询其绑定的角色列表（查看某用户拥有哪些租户角色）
     * @param userId 租户用户ID（tUserId）
     * @return 该用户绑定的所有 RoleUserVO 列表，按创建时间倒序排列
     */
    List<RoleUserVO> getRoleUsersByUserId(Long userId);

    /**
     * 根据多个租户用户ID全量查询绑定角色，并按用户ID分组返回结果
     * @param userIds 租户用户ID列表
     * @return 包含一个 Map 的列表，键为 tUserId，值为对应用户的 RoleUserVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<RoleUserVO>>> getRoleUsersByUserIds(List<Long> userIds);

    /**
     * 根据租户角色ID全量查询其绑定的用户列表（查看某角色被哪些用户拥有）
     * @param roleId 租户角色ID（tRoleId）
     * @return 该角色绑定的所有 RoleUserVO 列表，按创建时间倒序排列
     */
    List<RoleUserVO> getRoleUsersByRoleId(Long roleId);

    /**
     * 根据多个租户角色ID全量查询绑定用户，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @return 包含一个 Map 的列表，键为 tRoleId，值为对应角色的 RoleUserVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<RoleUserVO>>> getRoleUsersByRoleIds(List<Long> roleIds);

    /**
     * 根据租户用户ID查询其拥有的所有租户角色ID列表
     * @param userId 租户用户ID（tUserId）
     * @return 该用户绑定的租户角色ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getRoleIdsByUserId(Long userId);

    /**
     * 根据租户角色ID查询所有绑定该角色的租户用户ID列表
     * @param roleId 租户角色ID（tRoleId）
     * @return 绑定该角色的租户用户ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getUserIdsByRoleId(Long roleId);

    /**
     * 根据租户用户ID列表查询其拥有的所有租户角色ID列表
     * @param userIds 租户用户ID列表（tUserId）
     * @return 租户角色ID列表 Map 键-用户 值-角色
     */
    Map<Long, Set<Long>> getRoleIdsByUserIds(List<Long> userIds);

    /**
     * 根据租户角色ID列表查询所有绑定该角色的租户用户ID列表
     * @param roleIds 租户角色ID列表（tRoleId）
     * @return 租户用户ID列表 Map 键-角色 值-用户
     */
    Map<Long, Set<Long>> getUserIdsByRoleIds(List<Long> roleIds);

}
