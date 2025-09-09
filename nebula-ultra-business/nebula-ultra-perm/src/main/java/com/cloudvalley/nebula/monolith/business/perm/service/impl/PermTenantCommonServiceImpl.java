package com.cloudvalley.nebula.monolith.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.perm.converter.PermTenantConverter;
import com.cloudvalley.nebula.monolith.business.perm.mapper.PermTenantMapper;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermTenant;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermTenantVO;
import com.cloudvalley.nebula.monolith.shared.api.perm.service.IPermTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermTenantCommonServiceImpl extends ServiceImpl<PermTenantMapper, PermTenant> implements IPermTenantCommonService {

    @Autowired
    private PermTenantConverter permTenantConverter;

    /**
     * 根据权限租户ID查询权限租户（单个）
     * @param id 权限租户ID
     * @return 权限租户信息
     */
    @Override
    public PermTenantVO getPermTenantById(Long id) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getId, id)
                .eq(PermTenant::getDeleted, false);

        PermTenant permTenant = this.getOne(queryWrapper);
        return permTenant != null ? permTenantConverter.EnToVO(permTenant) : null;
    }

    /** 根据权限租户ID批量查询权限租户 [全量] */
    @Override
    public List<PermTenantVO> getPermTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermTenant::getId, ids)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);
        List<PermTenant> list = this.list(queryWrapper);
        return permTenantConverter.EnListToVOList(list);
    }

    /** 根据租户ID查询权限租户 [全量] */
    @Override
    public List<PermTenantVO> getPermTenantsByTenantId(Long tenantId) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSTenantId, tenantId)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);
        List<PermTenant> list = this.list(queryWrapper);
        return permTenantConverter.EnListToVOList(list);
    }

    /** 根据租户ID批量查询权限租户 [全量] - 批量返回分组结果 */
    @Override
    public List<Map<Long, List<PermTenantVO>>> getPermTenantsByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermTenant::getSTenantId, tenantIds)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);
        List<PermTenant> entities = this.list(queryWrapper);
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(entities);
        Map<Long, List<PermTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(PermTenantVO::getSTenantId));
        return java.util.Collections.singletonList(grouped);
    }

    /** 根据权限ID查询权限租户 [全量] */
    @Override
    public List<PermTenantVO> getPermTenantsByPermId(Long permId) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSPermId, permId)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);
        List<PermTenant> list = this.list(queryWrapper);
        return permTenantConverter.EnListToVOList(list);
    }

    /** 根据权限ID批量查询权限租户 [全量] - 批量返回分组结果 */
    @Override
    public List<Map<Long, List<PermTenantVO>>> getPermTenantsByPermIds(List<Long> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermTenant::getSPermId, permIds)
                .eq(PermTenant::getDeleted, false)
                .orderByDesc(PermTenant::getCreatedAt);
        List<PermTenant> entities = this.list(queryWrapper);
        List<PermTenantVO> voList = permTenantConverter.EnListToVOList(entities);
        Map<Long, List<PermTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(PermTenantVO::getSPermId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据租户ID查询权限ID列表
     * @param tenantId 租户ID
     * @return 权限ID列表
     */
    @Override
    public Set<Long> getPermIdsByTenantId(Long tenantId) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSTenantId, tenantId)
                .eq(PermTenant::getDeleted, false)
                .select(PermTenant::getSPermId);

        List<PermTenant> permTenants = this.list(queryWrapper);
        return permTenants.stream()
                .map(PermTenant::getSPermId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据权限ID查询租户ID列表
     * @param permId 权限ID
     * @return 租户ID列表
     */
    @Override
    public Set<Long> getTenantIdsByPermId(Long permId) {
        LambdaQueryWrapper<PermTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermTenant::getSPermId, permId)
                .eq(PermTenant::getDeleted, false)
                .select(PermTenant::getSTenantId);

        List<PermTenant> permTenants = this.list(queryWrapper);
        return permTenants.stream()
                .map(PermTenant::getSTenantId)
                .collect(Collectors.toSet());
    }


}
