package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupPermConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupPerm;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupPermMapper;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupPermVO;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupPermCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupPermCommonServiceImpl extends ServiceImpl<GroupPermMapper, GroupPerm> implements IGroupPermCommonService {

    @Autowired
    private GroupPermConverter groupPermConverter;

    /**
     * 根据ID查询单个权限组-权限绑定信息
     *
     * @param id 绑定记录ID
     * @return 对应的 GroupPermVO，若不存在则返回 null
     */
    @Override
    public GroupPermVO getGroupPermById(Long id) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getId, id)
                .eq(GroupPerm::getDeleted, false);

        GroupPerm groupPerm = this.getOne(queryWrapper);
        return groupPerm != null ? groupPermConverter.EnToVO(groupPerm) : null;
    }


    /**
     * 根据ID列表批量查询权限组-权限绑定 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 所有匹配的 GroupPermVO 列表
     */
    @Override
    public List<GroupPermVO> getGroupPermsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupPerm::getId, ids)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);
        List<GroupPerm> list = this.list(queryWrapper);
        return groupPermConverter.EnListToVOList(list);
    }

    /**
     * 根据系统组ID查询其绑定的所有权限 [全量]
     *
     * @param groupId 系统组ID
     * @return 该组绑定的所有 GroupPermVO 列表
     */
    @Override
    public List<GroupPermVO> getGroupPermsByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getSGroupId, groupId)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);
        List<GroupPerm> list = this.list(queryWrapper);
        return groupPermConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统组ID批量查询权限绑定关系，并按组ID分组返回 [全量]
     *
     * @param groupIds 系统组ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public Map<Long, List<GroupPermVO>> getGroupPermsByGroupIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupPerm::getSGroupId, groupIds)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);
        List<GroupPerm> entities = this.list(queryWrapper);
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(entities);
        Map<Long, List<GroupPermVO>> grouped = voList.stream().collect(Collectors.groupingBy(GroupPermVO::getSGroupId));
        return grouped;
    }

    /**
     * 根据租户权限ID查询所有绑定该权限的系统组 [全量]
     *
     * @param permId 租户权限ID
     * @return 该权限绑定的所有 GroupPermVO 列表
     */
    @Override
    public List<GroupPermVO> getGroupPermsByPermId(Long permId) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getTPermId, permId)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);
        List<GroupPerm> list = this.list(queryWrapper);
        return groupPermConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户权限ID批量查询绑定关系，并按权限ID分组返回 [全量]
     *
     * @param permIds 租户权限ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public Map<Long, List<GroupPermVO>> getGroupPermsByPermIds(List<Long> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupPerm::getTPermId, permIds)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);
        List<GroupPerm> entities = this.list(queryWrapper);
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(entities);
        Map<Long, List<GroupPermVO>> grouped = voList.stream().collect(Collectors.groupingBy(GroupPermVO::getTPermId));
        return grouped;
    }

    /**
     * 根据系统组ID查询其绑定的所有租户权限ID
     *
     * @param groupId 系统组ID
     * @return 绑定的租户权限ID集合
     */
    @Override
    public Set<Long> getPermIdsByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getSGroupId, groupId)
                .eq(GroupPerm::getDeleted, false)
                .select(GroupPerm::getTPermId);

        List<GroupPerm> groupPerms = this.list(queryWrapper);
        return groupPerms.stream()
                .map(GroupPerm::getTPermId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户权限ID查询所有绑定该权限的系统组ID
     *
     * @param permId 租户权限ID
     * @return 绑定该权限的系统组ID集合
     */
    @Override
    public Set<Long> getGroupIdsByPermId(Long permId) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getTPermId, permId)
                .eq(GroupPerm::getDeleted, false)
                .select(GroupPerm::getSGroupId);

        List<GroupPerm> groupPerms = this.list(queryWrapper);
        return groupPerms.stream()
                .map(GroupPerm::getSGroupId)
                .collect(Collectors.toSet());
    }

}
