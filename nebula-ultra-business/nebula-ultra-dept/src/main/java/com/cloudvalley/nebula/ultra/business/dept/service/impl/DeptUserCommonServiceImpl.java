package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.dept.converter.DeptUserConverter;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.DeptUser;
import com.cloudvalley.nebula.ultra.business.dept.mapper.DeptUserMapper;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptUserVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeptUserCommonServiceImpl extends ServiceImpl<DeptUserMapper, DeptUser> implements IDeptUserCommonService {

    @Autowired
    private DeptUserConverter deptUserConverter;

    /**
     * 根据主键 ID 查询单个用户-部门关联信息。
     *
     * @param id 用户-部门关联记录的唯一标识
     * @return 对应的 DeptUserVO，若不存在或已删除则返回 null
     */
    @Override
    public DeptUserVO getDeptUserById(Long id) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getId, id)
                .eq(DeptUser::getDeleted, false);

        DeptUser userDept = this.getOne(queryWrapper);
        return userDept != null ? deptUserConverter.EnToVO(userDept) : null;
    }

    /**
     * 根据多个 ID 查询用户-部门关联记录（全量，无分页）。
     *
     * @param ids 用户-部门关联 ID 列表
     * @return 所有匹配的 DeptUserVO 列表，按创建时间倒序排列
     */
    @Override
    public List<DeptUserVO> getDeptUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getId, ids)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);
        List<DeptUser> list = this.list(queryWrapper);
        return deptUserConverter.EnListToVOList(list);
    }

    /**
     * 根据用户 ID 查询其关联的所有部门（全量，无分页）。
     *
     * @param userId 用户 ID
     * @return 用户关联的所有部门 VO 列表，按创建时间倒序
     */
    @Override
    public List<DeptUserVO> getDeptUsersByUserId(Long userId) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTUserId, userId)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);
        List<DeptUser> list = this.list(queryWrapper);
        return deptUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个用户 ID 查询所有用户-部门关联记录，并按用户 ID 分组返回（全量）。
     *
     * @param userIds 用户 ID 列表
     * @return 单元素列表，包含一个 Map，key 为用户 ID，value 为该用户对应的部门 VO 列表
     */
    @Override
    public List<Map<Long, List<DeptUserVO>>> getDeptUsersByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getTUserId, userIds)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);
        List<DeptUser> entities = this.list(queryWrapper);
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(entities);
        Map<Long, List<DeptUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(DeptUserVO::getTUserId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据部门 ID 查询所有关联的用户（全量，无分页）。
     *
     * @param deptId 部门 ID
     * @return 该部门下所有用户-部门 VO 列表
     */
    @Override
    public List<DeptUserVO> getDeptUsersByDeptId(Long deptId) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTDeptId, deptId)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);
        List<DeptUser> list = this.list(queryWrapper);
        return deptUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个部门 ID 查询所有关联用户，并按部门 ID 分组返回（全量）。
     *
     * @param deptIds 部门 ID 列表
     * @return 单元素列表，包含一个 Map，key 为部门 ID，value 为该部门下的用户 VO 列表
     */
    @Override
    public List<Map<Long, List<DeptUserVO>>> getDeptUsersByDeptIds(List<Long> deptIds) {
        if (deptIds == null || deptIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getTDeptId, deptIds)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);
        List<DeptUser> entities = this.list(queryWrapper);
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(entities);
        Map<Long, List<DeptUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(DeptUserVO::getTDeptId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据用户 ID 查询其有效关联的所有部门 ID 集合（仅启用状态）。
     *
     * @param userId 用户 ID
     * @return 该用户关联的有效部门 ID Set
     */
    @Override
    public Set<Long> getDeptIdsByUserId(Long userId) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTUserId, userId)
                .eq(DeptUser::getDeleted, false)
                .select(DeptUser::getTDeptId);

        List<DeptUser> userDepts = this.list(queryWrapper);
        return userDepts.stream()
                .map(DeptUser::getTDeptId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据部门 ID 查询其下所有有效用户 ID 集合（仅启用状态）。
     *
     * @param deptId 部门 ID
     * @return 该部门下所有有效用户 ID Set
     */
    @Override
    public Set<Long> getUserIdsByDeptId(Long deptId) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTDeptId, deptId)
                .eq(DeptUser::getDeleted, false)
                .select(DeptUser::getTUserId);

        List<DeptUser> userDepts = this.list(queryWrapper);
        return userDepts.stream()
                .map(DeptUser::getTUserId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户用户ID列表查询租户部门ID列表
     * @param userIds 租户用户ID列表
     * @return 租户部门ID列表 Map 键-用户 值-部门
     */
    @Override
    public Map<Long, Set<Long>> getDeptIdsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getTUserId, userIds)
                .eq(DeptUser::getDeleted, false)
                .select(DeptUser::getTUserId, DeptUser::getTDeptId);

        List<DeptUser> userDepts = this.list(queryWrapper);
        return userDepts.stream()
                .collect(Collectors.groupingBy(
                        DeptUser::getTUserId,
                        Collectors.mapping(DeptUser::getTDeptId, Collectors.toSet())
                ));
    }

    /**
     * 根据租户部门ID列表查询租户用户ID列表
     * @param deptIds 租户部门ID列表
     * @return 租户用户ID列表 Map 键-部门 值-用户
     */
    @Override
    public Map<Long, Set<Long>> getUserIdsByDeptIds(List<Long> deptIds) {
        if (deptIds == null || deptIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getTDeptId, deptIds)
                .eq(DeptUser::getDeleted, false)
                .select(DeptUser::getTDeptId, DeptUser::getTUserId);

        List<DeptUser> userDepts = this.list(queryWrapper);
        return userDepts.stream()
                .collect(Collectors.groupingBy(
                        DeptUser::getTDeptId,
                        Collectors.mapping(DeptUser::getTUserId, Collectors.toSet())
                ));
    }

}
