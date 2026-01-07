package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.dept.converter.DeptTenantConverter;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.DeptTenant;
import com.cloudvalley.nebula.ultra.business.dept.mapper.DeptTenantMapper;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeptTenantCommonServiceImpl extends ServiceImpl<DeptTenantMapper, DeptTenant> implements IDeptTenantCommonService {

    @Autowired
    private DeptTenantConverter deptTenantConverter;
    
    /**
     * 根据租户 - 部门绑定 id 查询绑定关系（单个）
     * @param id 租户部门绑定主键ID（雪花算法ID）
     * @return DeptTenantVO 租户部门绑定VO对象（查询不到时返回null）
     */
    @Override
    public DeptTenantVO getDeptTenantById(Long id) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getId, id)
                .eq(DeptTenant::getDeleted, false);
        DeptTenant entity = this.getOne(queryWrapper);
        return entity != null ? deptTenantConverter.EnToVO(entity) : null;
    }

    /**
     * 根据租户 - 部门绑定 id 批量查询绑定关系 [全量]
     * @param ids 租户部门绑定主键ID列表（雪花算法ID）
     * @return List<DeptTenantVO> 租户部门绑定VO列表（ids为空时返回空列表）
     */
    @Override
    public List<DeptTenantVO> getDeptTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getId, ids)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        List<DeptTenant> list = this.list(queryWrapper);
        return deptTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户 id 查询绑定关系 [全量]
     * @param sTenantId 系统租户主键ID（雪花算法ID）
     * @return List<DeptTenantVO> 租户部门绑定VO列表
     */
    @Override
    public List<DeptTenantVO> getDeptTenantsBySTenantId(Long sTenantId) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSTenantId, sTenantId)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        List<DeptTenant> list = this.list(queryWrapper);
        return deptTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sTenantIds 系统租户主键ID列表（雪花算法ID）
     * @return List<Map<Long, List<DeptTenantVO>>> 分组结果列表（单个Map，key：系统租户ID，value：对应租户的部门绑定VO列表；sTenantIds为空时返回空列表）
     */
    @Override
    public Map<Long, List<DeptTenantVO>> getDeptTenantsBySTenantIds(List<Long> sTenantIds) {
        if (sTenantIds == null || sTenantIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getSTenantId, sTenantIds)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        List<DeptTenant> entities = this.list(queryWrapper);
        List<DeptTenantVO> voList = deptTenantConverter.EnListToVOList(entities);
        return voList.stream().collect(Collectors.groupingBy(DeptTenantVO::getSTenantId));
    }

    /**
     * 根据系统部门 id 查询绑定关系 [全量]
     * @param sDeptId 系统部门主键ID（雪花算法ID）
     * @return List<DeptTenantVO> 租户部门绑定VO列表
     */
    @Override
    public List<DeptTenantVO> getDeptTenantsBySDeptId(Long sDeptId) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSDeptId, sDeptId)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        List<DeptTenant> list = this.list(queryWrapper);
        return deptTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统部门 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sDeptIds 系统部门主键ID列表（雪花算法ID）
     * @return List<Map<Long, List<DeptTenantVO>>> 分组结果列表（单个Map，key：系统部门ID，value：对应部门的租户绑定VO列表；sDeptIds为空时返回空列表）
     */
    @Override
    public Map<Long, List<DeptTenantVO>> getDeptTenantsBySDeptIds(List<Long> sDeptIds) {
        if (sDeptIds == null || sDeptIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getSDeptId, sDeptIds)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        List<DeptTenant> entities = this.list(queryWrapper);
        List<DeptTenantVO> voList = deptTenantConverter.EnListToVOList(entities);
        return voList.stream().collect(Collectors.groupingBy(DeptTenantVO::getSDeptId));
    }

    /**
     * 根据系统租户 ID 查询系统部门 ID 列表
     * @param sTenantId 系统租户主键ID（雪花算法ID）
     * @return Set<Long> 系统部门ID集合（仅包含启用状态且未删除的绑定关系对应的部门ID）
     */
    @Override
    public Set<Long> getSDeptIdsBySTenantId(Long sTenantId) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSTenantId, sTenantId)
                .eq(DeptTenant::getDeleted, false)
                .select(DeptTenant::getSDeptId);
        List<DeptTenant> list = this.list(queryWrapper);
        return list.stream().map(DeptTenant::getSDeptId).collect(Collectors.toSet());
    }

    /**
     * 根据系统部门 ID 查询系统租户 ID 列表
     * @param sDeptId 系统部门主键ID（雪花算法ID）
     * @return Set<Long> 系统租户ID集合（仅包含启用状态且未删除的绑定关系对应的租户ID）
     */
    @Override
    public Set<Long> getSTenantIdsBySDeptId(Long sDeptId) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSDeptId, sDeptId)
                .eq(DeptTenant::getDeleted, false)
                .select(DeptTenant::getSTenantId);
        List<DeptTenant> list = this.list(queryWrapper);
        return list.stream().map(DeptTenant::getSTenantId).collect(Collectors.toSet());
    }

    /**
     * 根据多个系统租户 ID 查询系统部门 ID 列表
     * @param sTenantIds 系统租户ID列表
     * @return 按系统租户ID分组的系统部门ID集合映射
     */
    @Override
    public Map<Long, Set<Long>> getSDeptIdsBySTenantIds(List<Long> sTenantIds) {
        if (sTenantIds == null || sTenantIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getSTenantId, sTenantIds)
                .eq(DeptTenant::getDeleted, false)
                .select(DeptTenant::getSTenantId, DeptTenant::getSDeptId);

        List<DeptTenant> list = this.list(queryWrapper);

        return list.stream()
                .collect(Collectors.groupingBy(
                        DeptTenant::getSTenantId,
                        Collectors.mapping(DeptTenant::getSDeptId, Collectors.toSet())
                ));
    }

    /**
     * 根据多个系统部门 ID 查询系统租户 ID 列表
     * @param sDeptIds 系统部门ID列表
     * @return 按系统部门ID分组的系统租户ID集合映射
     */
    @Override
    public Map<Long, Set<Long>> getSTenantIdsBySDeptIds(List<Long> sDeptIds) {
        if (sDeptIds == null || sDeptIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getSDeptId, sDeptIds)
                .eq(DeptTenant::getDeleted, false)
                .select(DeptTenant::getSDeptId, DeptTenant::getSTenantId);

        List<DeptTenant> list = this.list(queryWrapper);

        return list.stream()
                .collect(Collectors.groupingBy(
                        DeptTenant::getSDeptId,
                        Collectors.mapping(DeptTenant::getSTenantId, Collectors.toSet())
                ));
    }

    /**
     * 根据 租户Id列表 和 部门Id列表 查询 租户部门信息
     * @param sTenantIds 租户Id列表
     * @param sDeptIds 部门Id列表
     * @return 按租户Id分组的绑定关系映射列表
     */
    @Override
    public Map<Long, List<DeptTenantVO>> getDeptTenantsBySTenantIdsAndSDeptIds(List<Long> sTenantIds, List<Long> sDeptIds) {
        if (sTenantIds == null || sTenantIds.isEmpty() || sDeptIds == null || sDeptIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getSTenantId, sTenantIds)
                .in(DeptTenant::getSDeptId, sDeptIds)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);

        List<DeptTenant> entities = this.list(queryWrapper);
        List<DeptTenantVO> voList = deptTenantConverter.EnListToVOList(entities);

        return voList.stream()
                .collect(Collectors.groupingBy(DeptTenantVO::getSTenantId));
    }

}
