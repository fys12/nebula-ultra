package com.cloudvalley.nebula.ultra.shared.api.perm.service;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermUserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPermUserCommonService {

    /**
     * 根据用户租户权限ID查询单个绑定信息
     * @param id 用户租户权限关联的唯一标识ID
     * @return 对应的 PermUserVO 对象，若不存在或已软删则返回 null
     */
    PermUserVO getPermUserById(Long id);

    /**
     * 根据多个用户租户权限ID全量查询绑定信息（不分页）
     * @param ids 用户租户权限关联ID列表
     * @return 所有匹配的 PermUserVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<PermUserVO> getPermUsersByIds(List<Long> ids);

    /**
     * 根据租户用户ID全量查询其绑定的权限列表（查看某用户拥有哪些权限）
     * @param tUserId 租户用户ID
     * @return 该用户绑定的所有 PermUserVO 列表，按创建时间倒序排列
     */
    List<PermUserVO> getPermUsersByTUserId(Long tUserId);

    /**
     * 根据多个租户用户ID全量查询绑定权限，并按用户ID分组返回结果
     * @param tUserIds 租户用户ID列表
     * @return 包含一个 Map 的列表，键为 tUserId，值为对应用户的 PermUserVO 列表；输入为空时返回空列表
     */
    Map<Long, List<PermUserVO>> getPermUsersByTUserIds(List<Long> tUserIds);

    /**
     * 根据租户权限ID全量查询其绑定的用户列表（查看某权限被哪些用户使用）
     * @param tPermId 租户权限ID
     * @return 该权限绑定的所有 PermUserVO 列表，按创建时间倒序排列
     */
    List<PermUserVO> getPermUsersByTPermId(Long tPermId);

    /**
     * 根据多个租户权限ID全量查询绑定用户，并按权限ID分组返回结果
     * @param tPermIds
     * @return 包含一个 Map 的列表，键为 tPermId，值为对应用户的 PermUserVO 列表；输入为空时返回空列表
     */
    Map<Long, List<PermUserVO>> getPermUsersByTPermIds(List<Long> tPermIds);

    /**
     * 根据租户用户ID查询其拥有的所有租户权限ID列表
     * @param tUserId 租户用户ID
     * @return 该用户绑定的租户权限ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getTPermIdsByTUserId(Long tUserId);

    /**
     * 根据租户权限ID查询所有绑定该权限的租户用户ID列表
     * @param tPermId 租户权限ID
     * @return 绑定该权限的租户用户ID去重集合（仅包含未软删且启用的记录）
     */
    Set<Long> getTUserIdsByTPermId(Long tPermId);


}
