package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupBindRoleConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindRole;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupBindRoleMapper;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupBindRoleCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupBindRoleCommonServiceImpl extends ServiceImpl<GroupBindRoleMapper, GroupBindRole> implements IGroupBindRoleCommonService {

    @Autowired
    private GroupBindRoleConverter groupBindRoleConverter;

    /**
     * 根据ID查询单个组-租户角色绑定信息
     *
     * @param id 绑定记录ID
     * @return 对应的 GroupBindRoleVO，若不存在则返回 null
     */
    @Override
    public GroupBindRoleVO getGroupBindRoleById(Long id) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getId, id)
                .eq(GroupBindRole::getDeleted, false);

        GroupBindRole groupBindRole = this.getOne(queryWrapper);
        return groupBindRole != null ? groupBindRoleConverter.EnToVO(groupBindRole) : null;
    }
    
    /**
     * 根据ID列表批量查询组-租户角色绑定 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 所有匹配的 GroupBindRoleVO 列表
     */
    @Override
    public List<GroupBindRoleVO> getGroupBindRolesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindRole::getId, ids)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);
        List<GroupBindRole> list = this.list(queryWrapper);
        return groupBindRoleConverter.EnListToVOList(list);
    }
    
    /**
     * 根据系统组ID查询其绑定的所有租户角色 [全量]
     *
     * @param groupId 系统组ID
     * @return 该组绑定的所有 GroupBindRoleVO 列表
     */
    @Override
    public List<GroupBindRoleVO> getGroupBindRolesByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getSGroupId, groupId)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);
        List<GroupBindRole> list = this.list(queryWrapper);
        return groupBindRoleConverter.EnListToVOList(list);
    }
    
    /**
     * 根据多个系统组ID批量查询绑定关系，并按组ID分组返回 [全量]
     *
     * @param groupIds 系统组ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public List<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByGroupIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindRole::getSGroupId, groupIds)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);
        List<GroupBindRole> entities = this.list(queryWrapper);
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(entities);
        Map<Long, List<GroupBindRoleVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(GroupBindRoleVO::getSGroupId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户角色ID查询绑定的所有系统组 [全量]
     *
     * @param roleId 租户角色ID
     * @return 该角色绑定的所有 GroupBindRoleVO 列表
     */
    @Override
    public List<GroupBindRoleVO> getGroupBindRolesByRoleId(Long roleId) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getTRoleId, roleId)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);
        List<GroupBindRole> list = this.list(queryWrapper);
        return groupBindRoleConverter.EnListToVOList(list);
    }


    /**
     * 根据多个租户角色ID批量查询绑定关系，并按角色ID分组返回 [全量]
     *
     * @param roleIds 租户角色ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public List<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindRole::getTRoleId, roleIds)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);
        List<GroupBindRole> entities = this.list(queryWrapper);
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(entities);
        Map<Long, List<GroupBindRoleVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(GroupBindRoleVO::getTRoleId));
        return java.util.Collections.singletonList(grouped);
    }
    
    /**
     * 根据系统组ID查询其绑定的所有租户角色ID
     *
     * @param groupId 系统组ID
     * @return 绑定的租户角色ID集合
     */
    @Override
    public Set<Long> getRoleIdsByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getSGroupId, groupId)
                .eq(GroupBindRole::getDeleted, false)
                .select(GroupBindRole::getTRoleId);

        List<GroupBindRole> groupBindRoles = this.list(queryWrapper);
        return groupBindRoles.stream()
                .map(GroupBindRole::getTRoleId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户角色ID查询所有绑定该角色的系统组ID
     *
     * @param roleId 租户角色ID
     * @return 绑定该角色的系统组ID集合
     */
    @Override
    public Set<Long> getGroupIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getTRoleId, roleId)
                .eq(GroupBindRole::getDeleted, false)
                .select(GroupBindRole::getSGroupId);

        List<GroupBindRole> groupBindRoles = this.list(queryWrapper);
        return groupBindRoles.stream()
                .map(GroupBindRole::getSGroupId)
                .collect(Collectors.toSet());
    }
    
}
