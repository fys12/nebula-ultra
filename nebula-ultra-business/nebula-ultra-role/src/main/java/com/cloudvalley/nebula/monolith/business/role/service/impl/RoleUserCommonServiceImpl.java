package com.cloudvalley.nebula.monolith.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.role.converter.RoleUserConverter;
import com.cloudvalley.nebula.monolith.business.role.mapper.RoleUserMapper;
import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleUser;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleUserVO;
import com.cloudvalley.nebula.monolith.shared.api.role.service.IRoleUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleUserCommonServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserCommonService {

    @Autowired
    private RoleUserConverter roleUserConverter;

    /**
     * 根据用户角色分配ID查询单个绑定信息
     * @param id 绑定关系的唯一标识ID
     * @return 对应的 RoleUserVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public RoleUserVO getRoleUserById(Long id) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getId, id)
                .eq(RoleUser::getDeleted, false);

        RoleUser roleUser = this.getOne(queryWrapper);
        return roleUser != null ? roleUserConverter.EnToVO(roleUser) : null;
    }

    /**
     * 根据多个用户角色分配ID全量查询绑定信息（不分页）
     * @param ids 绑定关系ID列表
     * @return 所有匹配的 RoleUserVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<RoleUserVO> getRoleUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getId, ids)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);
        List<RoleUser> list = this.list(queryWrapper);
        return roleUserConverter.EnListToVOList(list);
    }

    /**
     * 根据租户用户ID全量查询其绑定的角色列表
     * @param userId 租户用户ID（tUserId）
     * @return 该用户绑定的所有 RoleUserVO 列表，按创建时间倒序排列
     */
    @Override
    public List<RoleUserVO> getRoleUsersByUserId(Long userId) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTUserId, userId)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);
        List<RoleUser> list = this.list(queryWrapper);
        return roleUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户用户ID全量查询绑定角色，并按用户ID分组返回结果
     * @param userIds 租户用户ID列表
     * @return 包含一个 Map 的列表，键为 tUserId，值为对应用户的 RoleUserVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<RoleUserVO>>> getRoleUsersByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getTUserId, userIds)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);
        List<RoleUser> entities = this.list(queryWrapper);
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(entities);
        Map<Long, List<RoleUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(RoleUserVO::getTUserId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户角色ID全量查询其绑定的用户列表
     * @param roleId 租户角色ID（tRoleId）
     * @return 该角色绑定的所有 RoleUserVO 列表，按创建时间倒序排列
     */
    @Override
    public List<RoleUserVO> getRoleUsersByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTRoleId, roleId)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);
        List<RoleUser> list = this.list(queryWrapper);
        return roleUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户角色ID全量查询绑定用户，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @return 包含一个 Map 的列表，键为 tRoleId，值为对应角色的 RoleUserVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<RoleUserVO>>> getRoleUsersByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getTRoleId, roleIds)
                .eq(RoleUser::getDeleted, false)
                .orderByDesc(RoleUser::getCreatedAt);
        List<RoleUser> entities = this.list(queryWrapper);
        List<RoleUserVO> voList = roleUserConverter.EnListToVOList(entities);
        Map<Long, List<RoleUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(RoleUserVO::getTRoleId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户用户ID查询其拥有的所有租户角色ID列表
     * @param userId 租户用户ID（tUserId）
     * @return 该用户绑定的租户角色ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTUserId, userId)
                .eq(RoleUser::getDeleted, false)
                .eq(RoleUser::getState, true)
                .select(RoleUser::getTRoleId);

        List<RoleUser> roleUsers = this.list(queryWrapper);
        return roleUsers.stream()
                .map(RoleUser::getTRoleId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户角色ID查询所有绑定该角色的租户用户ID列表
     * @param roleId 租户角色ID（tRoleId）
     * @return 绑定该角色的租户用户ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getUserIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getTRoleId, roleId)
                .eq(RoleUser::getDeleted, false)
                .eq(RoleUser::getState, true)
                .select(RoleUser::getTUserId);

        List<RoleUser> roleUsers = this.list(queryWrapper);
        return roleUsers.stream()
                .map(RoleUser::getTUserId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户用户ID列表查询其拥有的所有租户角色ID列表
     * @param userIds 租户用户ID列表（tUserId）
     * @return 租户角色ID列表 Map 键-用户 值-角色
     */
    @Override
    public Map<Long, Set<Long>> getRoleIdsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getTUserId, userIds)
                .eq(RoleUser::getDeleted, false)
                .eq(RoleUser::getState, true)
                .select(RoleUser::getTUserId, RoleUser::getTRoleId);

        List<RoleUser> roleUsers = this.list(queryWrapper);
        return roleUsers.stream()
                .collect(Collectors.groupingBy(
                        RoleUser::getTUserId,
                        Collectors.mapping(RoleUser::getTRoleId, Collectors.toSet())
                ));
    }

    /**
     * 根据租户角色ID列表查询所有绑定该角色的租户用户ID列表
     * @param roleIds 租户角色ID列表（tRoleId）
     * @return 租户用户ID列表 Map 键-角色 值-用户
     */
    @Override
    public Map<Long, Set<Long>> getUserIdsByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getTRoleId, roleIds)
                .eq(RoleUser::getDeleted, false)
                .eq(RoleUser::getState, true)
                .select(RoleUser::getTRoleId, RoleUser::getTUserId);

        List<RoleUser> roleUsers = this.list(queryWrapper);
        return roleUsers.stream()
                .collect(Collectors.groupingBy(
                        RoleUser::getTRoleId,
                        Collectors.mapping(RoleUser::getTUserId, Collectors.toSet())
                ));
    }

}
