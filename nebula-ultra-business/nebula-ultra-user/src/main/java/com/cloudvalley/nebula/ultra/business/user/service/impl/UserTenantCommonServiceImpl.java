package com.cloudvalley.nebula.ultra.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.user.converter.UserTenantConverter;
import com.cloudvalley.nebula.ultra.business.user.model.entity.UserTenant;
import com.cloudvalley.nebula.ultra.business.user.mapper.UserTenantMapper;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.IUserTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTenantCommonServiceImpl extends ServiceImpl<UserTenantMapper, UserTenant> implements IUserTenantCommonService {

    @Autowired
    private UserTenantConverter userTenantConverter;

    /**
     * 根据租户用户ID查询单个租户用户信息。
     *
     * @param id 租户用户唯一标识符
     * @return 对应的 UserTenantVO；若不存在或已删除则返回 null
     */
    @Override
    public UserTenantVO getUserTenantById(Long id) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getId, id)
                .eq(UserTenant::getDeleted, false);

        UserTenant tenantUser = this.getOne(queryWrapper);
        return tenantUser != null ? userTenantConverter.EnToVO(tenantUser) : null;
    }

    /**
     * 根据多个租户用户ID查询所有匹配的租户用户信息（全量，无分页）。
     *
     * @param ids 租户用户ID列表
     * @return 所有匹配的 UserTenantVO 列表；若ids为空则返回空列表
     */
    @Override
    public List<UserTenantVO> getUserTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getId, ids)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);
        List<UserTenant> list = this.list(queryWrapper);
        return userTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据租户ID查询该租户下的所有用户（全量，无分页）。
     *
     * @param tenantId 租户唯一标识符
     * @return 该租户下所有租户用户VO列表
     */
    @Override
    public List<UserTenantVO> getUserTenantsByTenantId(Long tenantId) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSTenantId, tenantId)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);
        List<UserTenant> list = this.list(queryWrapper);
        return userTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户ID查询所有租户用户，并按租户ID分组（全量）。
     *
     * @param tenantIds 租户ID列表
     * @return 单元素列表，包含一个 Map<租户ID, 用户VO列表>
     */
    @Override
    public List<Map<Long, List<UserTenantVO>>> getUserTenantsByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getSTenantId, tenantIds)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);
        List<UserTenant> entities = this.list(queryWrapper);
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(entities);
        Map<Long, List<UserTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(UserTenantVO::getSTenantId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据用户ID查询其所属的所有租户用户记录（全量）。
     *
     * @param userId 用户唯一标识符
     * @return 该用户在所有租户中的租户用户VO列表
     */
    @Override
    public List<UserTenantVO> getUserTenantsByUserId(Long userId) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSUserId, userId)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);
        List<UserTenant> list = this.list(queryWrapper);
        return userTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个用户ID查询所有租户用户，并按用户ID分组（全量）。
     *
     * @param userIds 用户ID列表
     * @return 单元素列表，包含一个 Map<用户ID, 租户用户VO列表>
     */
    @Override
    public List<Map<Long, List<UserTenantVO>>> getUserTenantsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getSUserId, userIds)
                .eq(UserTenant::getDeleted, false)
                .orderByDesc(UserTenant::getCreatedAt);
        List<UserTenant> entities = this.list(queryWrapper);
        List<UserTenantVO> voList = userTenantConverter.EnListToVOList(entities);
        Map<Long, List<UserTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(UserTenantVO::getSUserId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户ID查询其下所有有效的用户ID集合（启用状态）。
     *
     * @param tenantId 租户唯一标识符
     * @return 该租户下所有有效用户ID的 Set 集合
     */
    @Override
    public Set<Long> getUserIdsByTenantId(Long tenantId) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSTenantId, tenantId)
                .eq(UserTenant::getDeleted, false)
                .select(UserTenant::getSUserId);

        List<UserTenant> tenantUsers = this.list(queryWrapper);
        return tenantUsers.stream()
                .map(UserTenant::getSUserId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据用户ID查询其所属的所有有效租户ID集合（启用状态）。
     *
     * @param userId 用户唯一标识符
     * @return 该用户所属的所有有效租户ID的 Set 集合
     */
    @Override
    public Set<Long> getTenantIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getSUserId, userId)
                .eq(UserTenant::getDeleted, false)
                .select(UserTenant::getSTenantId);

        List<UserTenant> tenantUsers = this.list(queryWrapper);
        return tenantUsers.stream()
                .map(UserTenant::getSTenantId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据 租户ID列表 查询 用户ID列表
     * @param tenantIds 租户ID列表
     * @return 用户ID列表 Map 键-租户 值-用户
     */
    @Override
    public Map<Long, Set<Long>> getUserIdsByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getSTenantId, tenantIds)
                .eq(UserTenant::getDeleted, false)
                .select(UserTenant::getSTenantId, UserTenant::getSUserId);

        List<UserTenant> tenantUsers = this.list(queryWrapper);
        return tenantUsers.stream()
                .collect(Collectors.groupingBy(
                        UserTenant::getSTenantId,
                        Collectors.mapping(UserTenant::getSUserId, Collectors.toSet())
                ));
    }

    /**
     * 根据用户ID列表查询租户ID列表
     * @param userIds 用户ID列表
     * @return 租户ID列表 Map 键-用户 值-租户
     */
    @Override
    public Map<Long, Set<Long>> getTenantIdsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserTenant::getSUserId, userIds)
                .eq(UserTenant::getDeleted, false)
                .select(UserTenant::getSUserId, UserTenant::getSTenantId);

        List<UserTenant> tenantUsers = this.list(queryWrapper);
        return tenantUsers.stream()
                .collect(Collectors.groupingBy(
                        UserTenant::getSUserId,
                        Collectors.mapping(UserTenant::getSTenantId, Collectors.toSet())
                ));
    }


}
