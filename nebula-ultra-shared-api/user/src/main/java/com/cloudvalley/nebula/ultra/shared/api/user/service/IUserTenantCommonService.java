package com.cloudvalley.nebula.ultra.shared.api.user.service;

import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserTenantCommonService {

    /**
     * 根据租户用户ID查询租户用户（单个）
     * @param id 租户用户ID
     * @return 租户用户信息
     */
    UserTenantVO getUserTenantById(Long id);

    /**
     * 根据多个租户用户ID查询对应的租户用户信息（全量数据，无分页）。
     *
     * @param ids 租户用户ID列表
     * @return 匹配的 UserTenantVO 列表；若 ids 为空或无匹配数据，则返回空列表
     */
    List<UserTenantVO> getUserTenantsByIds(List<Long> ids);

    /**
     * 根据租户ID查询该租户下的所有用户租户关联信息（全量数据）。
     *
     * @param tenantId 租户唯一标识符
     * @return 该租户下所有用户租户关联的 VO 列表
     */
    List<UserTenantVO> getUserTenantsByTenantId(Long tenantId);

    /**
     * 根据多个租户ID批量查询租户用户，并按租户ID分组返回结果（全量）。
     *
     * @param tenantIds 租户ID列表
     * @return 单元素列表，包含一个 Map<Long, List<UserTenantVO>>，键为租户ID，值为对应用户列表
     */
    List<Map<Long, List<UserTenantVO>>> getUserTenantsByTenantIds(List<Long> tenantIds);

    /**
     * 根据用户ID查询该用户所属的所有租户关联信息（全量数据）。
     *
     * @param userId 用户唯一标识符
     * @return 该用户在所有租户中的 UserTenantVO 列表
     */
    List<UserTenantVO> getUserTenantsByUserId(Long userId);

    /**
     * 根据多个用户ID批量查询租户用户，并按用户ID分组返回结果（全量）。
     *
     * @param userIds 用户ID列表
     * @return 单元素列表，包含一个 Map<Long, List<UserTenantVO>>，键为用户ID，值为对应租户列表
     */
    List<Map<Long, List<UserTenantVO>>> getUserTenantsByUserIds(List<Long> userIds);

    /**
     * 根据租户ID查询用户ID列表
     * @param tenantId 租户ID
     * @return 用户ID列表
     */
    Set<Long> getUserIdsByTenantId(Long tenantId);

    /**
     * 根据用户ID查询租户ID列表
     * @param userId 用户ID
     * @return 租户ID列表
     */
    Set<Long> getTenantIdsByUserId(Long userId);

    /**
     * 根据 租户ID列表 查询 用户ID列表
     * @param tenantIds 租户ID列表
     * @return 用户ID列表 Map 键-租户 值-用户
     */
    Map<Long, Set<Long>> getUserIdsByTenantIds(List<Long> tenantIds);

    /**
     * 根据 用户ID列表 查询 租户ID列表
     * @param userIds 用户ID列表
     * @return 租户ID列表 Map 键-用户 值-租户
     */
    Map<Long, Set<Long>> getTenantIdsByUserIds(List<Long> userIds);

}
