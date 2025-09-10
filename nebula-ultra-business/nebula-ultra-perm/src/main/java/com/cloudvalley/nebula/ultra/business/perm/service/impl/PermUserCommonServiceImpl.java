package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.perm.converter.PermUserConverter;
import com.cloudvalley.nebula.ultra.business.perm.mapper.PermUserMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermUser;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermUserVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.IPermUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PermUserCommonServiceImpl extends ServiceImpl<PermUserMapper, PermUser> implements IPermUserCommonService {

    @Autowired
    private PermUserConverter permUserConverter;

    /**
     * 根据用户租户权限ID查询单个绑定信息
     * @param id 用户租户权限关联的唯一标识ID
     * @return 对应的 PermUserVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public PermUserVO getPermUserById(Long id) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getId, id)
                .eq(PermUser::getDeleted, false);

        PermUser permUser = this.getOne(queryWrapper);
        return permUser != null ? permUserConverter.EnToVO(permUser) : null;
    }

    /**
     * 根据多个用户租户权限ID全量查询绑定信息（不分页）
     * @param ids 用户租户权限ID列表
     * @return 所有匹配的 PermUserVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<PermUserVO> getPermUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermUser::getId, ids)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);
        List<PermUser> list = this.list(queryWrapper);
        return permUserConverter.EnListToVOList(list);
    }

    /**
     * 根据租户用户ID全量查询其绑定的权限列表（查看某用户拥有哪些权限）
     * @param tUserId 租户用户ID
     * @return 该用户绑定的所有 PermUserVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermUserVO> getPermUsersByTUserId(Long tUserId) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTUserId, tUserId)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);
        List<PermUser> list = this.list(queryWrapper);
        return permUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户用户ID全量查询绑定权限，并按用户ID分组返回结果
     * @param tUserIds 租户用户ID列表
     * @return 包含一个 Map 的列表，键为 tUserId，值为对应用户的 PermUserVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<PermUserVO>>> getPermUsersByTUserIds(List<Long> tUserIds) {
        if (tUserIds == null || tUserIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermUser::getTUserId, tUserIds)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);
        List<PermUser> entities = this.list(queryWrapper);
        List<PermUserVO> voList = permUserConverter.EnListToVOList(entities);
        Map<Long, List<PermUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(PermUserVO::getTUserId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户权限ID全量查询其绑定的用户列表（查看某权限被哪些用户使用）
     * @param tPermId 租户权限ID
     * @return 该权限绑定的所有 PermUserVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermUserVO> getPermUsersByTPermId(Long tPermId) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTPermId, tPermId)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);
        List<PermUser> list = this.list(queryWrapper);
        return permUserConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户权限ID全量查询绑定用户，并按权限ID分组返回结果
     * @param tPermIds 租户权限ID列表
     * @return 包含一个 Map 的列表，键为 tPermId，值为对应权限的 PermUserVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<PermUserVO>>> getPermUsersByTPermIds(List<Long> tPermIds) {
        if (tPermIds == null || tPermIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermUser::getTPermId, tPermIds)
                .eq(PermUser::getDeleted, false)
                .orderByDesc(PermUser::getCreatedAt);
        List<PermUser> entities = this.list(queryWrapper);
        List<PermUserVO> voList = permUserConverter.EnListToVOList(entities);
        Map<Long, List<PermUserVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(PermUserVO::getTPermId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户用户ID查询其拥有的所有租户权限ID列表
     * @param tUserId 租户用户ID
     * @return 该用户绑定的租户权限ID去重集合（仅包含未软删记录）
     */
    @Override
    public Set<Long> getTPermIdsByTUserId(Long tUserId) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTUserId, tUserId)
                .eq(PermUser::getDeleted, false)
                .select(PermUser::getTPermId);

        List<PermUser> permUsers = this.list(queryWrapper);
        return permUsers.stream()
                .map(PermUser::getTPermId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据租户权限ID查询所有绑定该权限的租户用户ID列表
     * @param tPermId 租户权限ID
     * @return 绑定该权限的租户用户ID去重集合（仅包含未软删记录）
     */
    @Override
    public Set<Long> getTUserIdsByTPermId(Long tPermId) {
        LambdaQueryWrapper<PermUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermUser::getTPermId, tPermId)
                .eq(PermUser::getDeleted, false)
                .select(PermUser::getTUserId);

        List<PermUser> permUsers = this.list(queryWrapper);
        return permUsers.stream()
                .map(PermUser::getTUserId)
                .collect(Collectors.toSet());
    }

}
