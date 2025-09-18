package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.perm.converter.PermRoleConverter;
import com.cloudvalley.nebula.ultra.business.perm.mapper.PermRoleMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermRole;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.IPermRoleCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermRoleCommonServiceImpl extends ServiceImpl<PermRoleMapper, PermRole> implements IPermRoleCommonService {

    @Autowired
    private PermRoleConverter permRoleConverter;

    /**
     * 根据权限角色ID查询单个绑定信息
     * @param id 权限角色关联的唯一标识ID
     * @return 对应的 PermRoleVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public PermRoleVO getPermRoleById(Long id) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getId, id)
                .eq(PermRole::getDeleted, false);

        PermRole permRole = this.getOne(queryWrapper);
        return permRole != null ? permRoleConverter.EnToVO(permRole) : null;
    }

    /**
     * 根据多个权限角色ID全量查询绑定信息（不分页）
     * @param ids 权限角色ID列表
     * @return 所有匹配的 PermRoleVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<PermRoleVO> getPermRolesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getId, ids)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);
        List<PermRole> list = this.list(queryWrapper);
        return permRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据租户权限ID全量查询其绑定的角色列表
     * @param permId 租户权限ID
     * @return 该权限绑定的所有 PermRoleVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermRoleVO> getPermRolesByPermId(Long permId) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTPermId, permId)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);
        List<PermRole> list = this.list(queryWrapper);
        return permRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户权限ID全量查询绑定角色，并按权限ID分组返回结果
     * @param permIds 租户权限ID列表
     * @return 包含一个 Map 的列表，键为 tPermId，值为对应权限的 PermRoleVO 列表；输入为空时返回空列表
     */
    @Override
    public Map<Long, List<PermRoleVO>> getPermRolesByPermIds(List<Long> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getTPermId, permIds)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);
        List<PermRole> entities = this.list(queryWrapper);
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(entities);
        Map<Long, List<PermRoleVO>> grouped = voList.stream().collect(Collectors.groupingBy(PermRoleVO::getTPermId));
        return grouped;
    }

    /**
     * 根据租户角色ID全量查询其绑定的权限列表
     * @param roleId 租户角色ID
     * @return 该角色绑定的所有 PermRoleVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermRoleVO> getPermRolesByRoleId(Long roleId) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTRoleId, roleId)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);
        List<PermRole> list = this.list(queryWrapper);
        return permRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户角色ID全量查询绑定权限，并按角色ID分组返回结果
     * @param roleIds 租户角色ID列表
     * @return 包含一个 Map 的列表，键为 tRoleId，值为对应角色的 PermRoleVO 列表；输入为空时返回空列表
     */
    @Override
    public Map<Long, List<PermRoleVO>> getPermRolesByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getTRoleId, roleIds)
                .eq(PermRole::getDeleted, false)
                .orderByDesc(PermRole::getCreatedAt);
        List<PermRole> entities = this.list(queryWrapper);
        List<PermRoleVO> voList = permRoleConverter.EnListToVOList(entities);
        return voList.stream().collect(Collectors.groupingBy(PermRoleVO::getTRoleId));
    }

    /**
     * 根据租户权限ID查询所有绑定的租户角色ID列表
     * @param permId 租户权限ID
     * @return 该权限绑定的租户角色ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getRoleIdsByPermId(Long permId) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTPermId, permId)
                .eq(PermRole::getDeleted, false)
                .select(PermRole::getTRoleId);

        List<PermRole> permRoles = this.list(queryWrapper);
        return permRoles.stream()
                .map(PermRole::getTRoleId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户角色ID查询所有绑定的租户权限ID列表
     * @param roleId 租户角色ID
     * @return 该角色绑定的租户权限ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getPermIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRole::getTRoleId, roleId)
                .eq(PermRole::getDeleted, false)
                .select(PermRole::getTPermId);

        List<PermRole> permRoles = this.list(queryWrapper);
        return permRoles.stream()
                .map(PermRole::getTPermId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据多个租户角色ID查询所有绑定的租户权限ID列表
     * @param roleIds 租户角色ID列表
     * @return Map结构，键为租户角色ID，值为该角色绑定的租户权限ID集合
     */
    @Override
    public Map<Long, Set<Long>> getPermIdsByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getTRoleId, roleIds)
                .eq(PermRole::getDeleted, false)
                .select(PermRole::getTRoleId, PermRole::getTPermId);

        List<PermRole> permRoles = this.list(queryWrapper);
        return permRoles.stream()
                .collect(Collectors.groupingBy(
                        PermRole::getTRoleId,
                        Collectors.mapping(PermRole::getTPermId, Collectors.toSet())
                ));
    }

    /**
     * 根据多个租户权限ID查询所有绑定的租户角色ID列表
     * @param permIds 租户权限ID列表
     * @return Map结构，键为租户权限ID，值为该权限绑定的租户角色ID集合
     */
    @Override
    public Map<Long, Set<Long>> getRoleIdsByPermIds(List<Long> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<PermRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermRole::getTPermId, permIds)
                .eq(PermRole::getDeleted, false)
                .select(PermRole::getTPermId, PermRole::getTRoleId);

        List<PermRole> permRoles = this.list(queryWrapper);
        return permRoles.stream()
                .collect(Collectors.groupingBy(
                        PermRole::getTPermId,
                        Collectors.mapping(PermRole::getTRoleId, Collectors.toSet())
                ));
    }

}
