package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupBindDeptConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindDept;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupBindDeptMapper;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.group.service.IGroupBindDeptCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupBindDeptCommonServiceImpl extends ServiceImpl<GroupBindDeptMapper, GroupBindDept> implements IGroupBindDeptCommonService {

    @Autowired
    private GroupBindDeptConverter groupBindDeptConverter;

    /**
     * 根据ID查询单个组-租户部门绑定
     *
     * @param id 绑定记录ID
     * @return 对应的 GroupBindDeptVO，若不存在则返回 null
     */
    @Override
    public GroupBindDeptVO getGroupBindDeptById(Long id) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getId, id)
                .eq(GroupBindDept::getDeleted, false);
        GroupBindDept entity = this.getOne(queryWrapper);
        return entity != null ? groupBindDeptConverter.EnToVO(entity) : null;
    }

    /**
     * 根据ID列表批量查询组-租户部门绑定 [全量]
     *
     * @param ids 绑定记录ID列表
     * @return 匹配的 GroupBindDeptVO 列表
     */
    @Override
    public List<GroupBindDeptVO> getGroupBindDeptsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindDept::getId, ids)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        List<GroupBindDept> list = this.list(queryWrapper);
        return groupBindDeptConverter.EnListToVOList(list);
    }

    /**
     * 根据租户组ID查询其绑定的租户部门 [全量]
     *
     * @param tGroupId 租户组ID
     * @return 该组绑定的所有 GroupBindDeptVO 列表
     */
    @Override
    public List<GroupBindDeptVO> getGroupBindDeptsBySGroupId(Long tGroupId) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getSGroupId, tGroupId)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        List<GroupBindDept> list = this.list(queryWrapper);
        return groupBindDeptConverter.EnListToVOList(list);
    }


    /**
     * 根据租户组ID列表批量查询绑定关系，并按组ID分组返回 [全量]
     *
     * @param tGroupIds 租户组ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public Map<Long, List<GroupBindDeptVO>> getGroupBindDeptsBySGroupIds(List<Long> tGroupIds) {
        if (tGroupIds == null || tGroupIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindDept::getSGroupId, tGroupIds)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        List<GroupBindDept> entities = this.list(queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(entities);
        Map<Long, List<GroupBindDeptVO>> grouped = voList.stream().collect(Collectors.groupingBy(GroupBindDeptVO::getTGroupId));
        return grouped;
    }


    /**
     * 根据租户部门ID查询关联的组 [全量]
     *
     * @param tDeptId 租户部门ID
     * @return 该部门关联的所有 GroupBindDeptVO 列表
     */
    @Override
    public List<GroupBindDeptVO> getGroupBindDeptsByTDeptId(Long tDeptId) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getTDeptId, tDeptId)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        List<GroupBindDept> list = this.list(queryWrapper);
        return groupBindDeptConverter.EnListToVOList(list);
    }


    /**
     * 根据租户部门ID列表批量查询绑定关系，并按部门ID分组返回 [全量]
     *
     * @param tDeptIds 租户部门ID列表
     * @return 包含分组结果的列表（单个 Map）
     */
    @Override
    public Map<Long, List<GroupBindDeptVO>> getGroupBindDeptsByTDeptIds(List<Long> tDeptIds) {
        if (tDeptIds == null || tDeptIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindDept::getTDeptId, tDeptIds)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        List<GroupBindDept> entities = this.list(queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(entities);
        Map<Long, List<GroupBindDeptVO>> grouped = voList.stream().collect(Collectors.groupingBy(GroupBindDeptVO::getTDeptId));
        return grouped;
    }


    /**
     * 根据租户组ID查询其绑定的所有租户部门ID
     *
     * @param tGroupId 租户组ID
     * @return 绑定的租户部门ID集合
     */
    @Override
    public Set<Long> getTDeptIdsBySGroupId(Long tGroupId) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getSGroupId, tGroupId)
                .eq(GroupBindDept::getDeleted, false)
                .select(GroupBindDept::getTDeptId);
        List<GroupBindDept> list = this.list(queryWrapper);
        return list.stream().map(GroupBindDept::getTDeptId).collect(Collectors.toSet());
    }

    /**
     * 根据租户部门ID查询其关联的所有系统组ID
     *
     * @param tDeptId 租户部门ID
     * @return 关联的系统组ID集合
     */
    @Override
    public Set<Long> getSGroupIdsByTDeptId(Long tDeptId) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getTDeptId, tDeptId)
                .eq(GroupBindDept::getDeleted, false)
                .select(GroupBindDept::getSGroupId);
        List<GroupBindDept> list = this.list(queryWrapper);
        return list.stream().map(GroupBindDept::getSGroupId).collect(Collectors.toSet());
    }

}
