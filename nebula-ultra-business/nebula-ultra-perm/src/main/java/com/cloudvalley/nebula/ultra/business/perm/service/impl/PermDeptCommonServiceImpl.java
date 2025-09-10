package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.perm.converter.PermDeptConverter;
import com.cloudvalley.nebula.ultra.business.perm.mapper.PermDeptMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermDept;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.IPermDeptCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermDeptCommonServiceImpl extends ServiceImpl<PermDeptMapper, PermDept> implements IPermDeptCommonService {

    @Autowired
    private PermDeptConverter permDeptConverter;

    /**
     * 根据主键ID查询单个租户部门-租户权限关联
     * @param id 关联关系的唯一标识ID
     * @return 对应的 PermDeptVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public PermDeptVO getPermDeptById(Long id) {
        // 主键过滤 + 仅查未软删
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getId, id)
                .eq(PermDept::getDeleted, false);
        PermDept entity = this.getOne(queryWrapper);
        return entity != null ? permDeptConverter.EnToVO(entity) : null;
    }

    /**
     * 根据多个ID全量查询租户部门-租户权限关联（不分页）
     * @param ids 关联ID列表
     * @return 所有匹配的 PermDeptVO 列表，忽略已软删记录；输入为空时返回空列表
     */
    @Override
    public List<PermDeptVO> getPermDeptsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getId, ids)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        List<PermDept> list = this.list(queryWrapper);
        return permDeptConverter.EnListToVOList(list);
    }

    /**
     * 根据租户部门ID全量查询其关联的权限列表
     * @param tDeptId 租户部门ID
     * @return 该部门拥有的所有 PermDeptVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermDeptVO> getPermDeptsByTDeptId(Long tDeptId) {
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTDeptId, tDeptId)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        List<PermDept> list = this.list(queryWrapper);
        return permDeptConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户部门ID全量查询关联权限，并按部门ID分组返回
     * @param tDeptIds 租户部门ID列表
     * @return 包含一个 Map 的列表，键为 tDeptId，值为对应部门的 PermDeptVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<PermDeptVO>>> getPermDeptsByTDeptIds(List<Long> tDeptIds) {
        if (tDeptIds == null || tDeptIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getTDeptId, tDeptIds)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        List<PermDept> entities = this.list(queryWrapper);
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(entities);
        Map<Long, List<PermDeptVO>> grouped = voList.stream().collect(Collectors.groupingBy(PermDeptVO::getTDeptId));
        return Collections.singletonList(grouped);
    }

    /**
     * 根据租户权限ID全量查询使用该权限的部门列表
     * @param tPermId 租户权限ID
     * @return 所有拥有该权限的部门对应的 PermDeptVO 列表，按创建时间倒序排列
     */
    @Override
    public List<PermDeptVO> getPermDeptsByTPermId(Long tPermId) {
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTPermId, tPermId)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        List<PermDept> list = this.list(queryWrapper);
        return permDeptConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户权限ID全量查询使用部门，并按权限ID分组返回
     * @param tPermIds 租户权限ID列表
     * @return 包含一个 Map 的列表，键为 tPermId，值为对应权限的部门VO列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<PermDeptVO>>> getPermDeptsByTPermIds(List<Long> tPermIds) {
        if (tPermIds == null || tPermIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getTPermId, tPermIds)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        List<PermDept> entities = this.list(queryWrapper);
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(entities);
        Map<Long, List<PermDeptVO>> grouped = voList.stream().collect(Collectors.groupingBy(PermDeptVO::getTPermId));
        return Collections.singletonList(grouped);
    }

    /**
     * 根据租户部门ID查询其拥有的租户权限ID列表
     * @param tDeptId 租户部门ID
     * @return 该部门拥有的未软删且启用状态的租户权限ID去重集合
     */
    @Override
    public Set<Long> getTPermIdsByTDeptId(Long tDeptId) {
        // 仅选择 tPermId 字段，避免无关数据传输
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTDeptId, tDeptId)
                .eq(PermDept::getDeleted, false)
                .select(PermDept::getTPermId);
        List<PermDept> list = this.list(queryWrapper);
        return list.stream().map(PermDept::getTPermId).collect(Collectors.toSet());
    }

    /**
     * 根据租户权限ID查询所有拥有该权限的租户部门ID列表
     * @param tPermId 租户权限ID
     * @return 拥有该权限且未软删、状态启用的租户部门ID去重集合
     */
    @Override
    public Set<Long> getTDeptIdsByTPermId(Long tPermId) {
        // 仅选择 tDeptId 字段，避免无关数据传输
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTPermId, tPermId)
                .eq(PermDept::getDeleted, false)
                .select(PermDept::getTDeptId);
        List<PermDept> list = this.list(queryWrapper);
        return list.stream().map(PermDept::getTDeptId).collect(Collectors.toSet());
    }

    /**
     * 根据多个租户部门ID查询其拥有的所有租户权限ID列表
     * @param tDeptIds 租户部门ID列表
     * @return Map结构，键为租户部门ID，值为该部门关联的租户权限ID集合；输入为空时返回空Map
     */
    @Override
    public Map<Long, Set<Long>> getTPermIdsByTDeptIds(List<Long> tDeptIds) {
        if (tDeptIds == null || tDeptIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getTDeptId, tDeptIds)
                .eq(PermDept::getDeleted, false)
                .select(PermDept::getTDeptId, PermDept::getTPermId);

        List<PermDept> list = this.list(queryWrapper);

        return list.stream()
                .collect(Collectors.groupingBy(
                        PermDept::getTDeptId,
                        Collectors.mapping(PermDept::getTPermId, Collectors.toSet())
                ));
    }

    /**
     * 根据多个租户权限ID查询所有拥有这些权限的租户部门ID列表
     * @param tPermIds 租户权限ID列表
     * @return Map结构，键为租户权限ID，值为拥有该权限的所有部门ID集合；输入为空时返回空Map
     */
    @Override
    public Map<Long, Set<Long>> getTDeptIdsByTPermIds(List<Long> tPermIds) {
        if (tPermIds == null || tPermIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getTPermId, tPermIds)
                .eq(PermDept::getDeleted, false)
                .select(PermDept::getTPermId, PermDept::getTDeptId);

        List<PermDept> list = this.list(queryWrapper);

        return list.stream()
                .collect(Collectors.groupingBy(
                        PermDept::getTPermId,
                        Collectors.mapping(PermDept::getTDeptId, Collectors.toSet())
                ));
    }

}
