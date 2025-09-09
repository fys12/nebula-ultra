package com.cloudvalley.nebula.monolith.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.group.converter.GroupTenantConverter;
import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupTenant;
import com.cloudvalley.nebula.monolith.business.group.mapper.GroupTenantMapper;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupTenantVO;
import com.cloudvalley.nebula.monolith.shared.api.group.service.IGroupTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupTenantCommonServiceImpl extends ServiceImpl<GroupTenantMapper, GroupTenant> implements IGroupTenantCommonService {

    @Autowired
    private GroupTenantConverter groupTenantConverter;

    /**
     * 根据ID查询单个租户-组绑定信息
     *
     * @param id 绑定记录ID
     * @return 对应的 GroupTenantVO，若不存在则返回 null
     */
    @Override
    public GroupTenantVO getGroupTenantById(Long id) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getId, id)
                .eq(GroupTenant::getDeleted, false);

        GroupTenant groupTenant = this.getOne(queryWrapper);
        return groupTenant != null ? groupTenantConverter.EnToVO(groupTenant) : null;
    }


    /**
     * 根据ID列表批量查询租户-组绑定 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 所有匹配的 GroupTenantVO 列表
     */
    @Override
    public List<GroupTenantVO> getGroupTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupTenant::getId, ids)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);
        List<GroupTenant> list = this.list(queryWrapper);
        return groupTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户ID查询其绑定的所有系统组 [全量]
     *
     * @param sTenantId 系统租户ID
     * @return 该租户绑定的所有 GroupTenantVO 列表
     */
    @Override
    public List<GroupTenantVO> getGroupTenantsBySTenantId(Long sTenantId) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSTenantId, sTenantId)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);
        List<GroupTenant> list = this.list(queryWrapper);
        return groupTenantConverter.EnListToVOList(list);
    }


    /**
     * 根据多个系统租户ID批量查询绑定关系，并按租户ID分组返回 [全量]
     *
     * @param sTenantIds 系统租户ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public List<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySTenantIds(List<Long> sTenantIds) {
        if (sTenantIds == null || sTenantIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupTenant::getSTenantId, sTenantIds)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);
        List<GroupTenant> entities = this.list(queryWrapper);
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(entities);
        Map<Long, List<GroupTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(GroupTenantVO::getSTenantId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据系统组ID查询所有绑定该组的系统租户 [全量]
     *
     * @param sGroupId 系统组ID
     * @return 该组绑定的所有 GroupTenantVO 列表
     */
    @Override
    public List<GroupTenantVO> getGroupTenantsBySGroupId(Long sGroupId) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSGroupId, sGroupId)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);
        List<GroupTenant> list = this.list(queryWrapper);
        return groupTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统组ID批量查询绑定关系，并按组ID分组返回 [全量]
     *
     * @param sGroupIds 系统组ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public List<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySGroupIds(List<Long> sGroupIds) {
        if (sGroupIds == null || sGroupIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupTenant::getSGroupId, sGroupIds)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);
        List<GroupTenant> entities = this.list(queryWrapper);
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(entities);
        Map<Long, List<GroupTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(GroupTenantVO::getSGroupId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据系统租户ID查询其绑定的所有系统组ID
     *
     * @param sTenantId 系统租户ID
     * @return 绑定的系统组ID集合
     */
    @Override
    public Set<Long> getSGroupIdsBySTenantId(Long sTenantId) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSTenantId, sTenantId)
                .eq(GroupTenant::getDeleted, false)
                .select(GroupTenant::getSGroupId);

        List<GroupTenant> groupTenants = this.list(queryWrapper);
        return groupTenants.stream()
                .map(GroupTenant::getSGroupId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据系统组ID查询所有绑定该组的系统租户ID
     *
     * @param sGroupId 系统组ID
     * @return 绑定的系统租户ID集合
     */
    @Override
    public Set<Long> getSTenantIdsBySGroupId(Long sGroupId) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSGroupId, sGroupId)
                .eq(GroupTenant::getDeleted, false)
                .select(GroupTenant::getSTenantId);

        List<GroupTenant> groupTenants = this.list(queryWrapper);
        return groupTenants.stream()
                .map(GroupTenant::getSTenantId)
                .collect(Collectors.toSet());
    }

}
