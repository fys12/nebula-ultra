package com.cloudvalley.nebula.ultra.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.role.converter.RoleTenantConverter;
import com.cloudvalley.nebula.ultra.business.role.mapper.RoleTenantMapper;
import com.cloudvalley.nebula.ultra.business.role.model.entity.RoleTenant;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleTenantCommonServiceImpl extends ServiceImpl<RoleTenantMapper, RoleTenant> implements IRoleTenantCommonService {

    @Autowired
    private RoleTenantConverter roleTenantConverter;

    /**
     * 根据租户角色绑定ID查询单个信息
     * @param id 绑定关系的唯一标识ID
     * @return 对应的 RoleTenantVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public RoleTenantVO getRoleTenantById(Long id) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getId, id)
                .eq(RoleTenant::getDeleted, false);

        RoleTenant roleTenant = this.getOne(queryWrapper);
        return roleTenant != null ? roleTenantConverter.EnToVO(roleTenant) : null;
    }

    /**
     * 根据多个租户角色ID全量查询绑定信息（不分页）
     * @param ids 绑定关系ID列表
     * @return 所有匹配的 RoleTenantVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<RoleTenantVO> getRoleTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleTenant::getId, ids)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);
        List<RoleTenant> list = this.list(queryWrapper);
        return roleTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户ID全量查询其绑定的角色列表
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的所有 RoleTenantVO 列表，按创建时间倒序排列
     */
    @Override
    public List<RoleTenantVO> getRoleTenantsByTenantId(Long tenantId) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSTenantId, tenantId)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);
        List<RoleTenant> list = this.list(queryWrapper);
        return roleTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统租户ID全量查询绑定角色，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 RoleTenantVO 列表；输入为空时返回空列表
     */
    @Override
    public Map<Long, List<RoleTenantVO>> getRoleTenantsByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleTenant::getSTenantId, tenantIds)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);
        List<RoleTenant> entities = this.list(queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(entities);
        return voList.stream().collect(Collectors.groupingBy(RoleTenantVO::getSTenantId));
    }

    /**
     * 根据系统角色ID全量查询其绑定的租户列表
     * @param roleId 系统角色ID（sRoleId）
     * @return 该角色绑定的所有 RoleTenantVO 列表，按创建时间倒序排列
     */
    @Override
    public List<RoleTenantVO> getRoleTenantsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSRoleId, roleId)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);
        List<RoleTenant> list = this.list(queryWrapper);
        return roleTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统角色ID全量查询使用租户，并按角色ID分组返回结果
     * @param roleIds 系统角色ID列表
     * @return 包含一个 Map 的列表，键为 sRoleId，值为对应角色的 RoleTenantVO 列表；输入为空时返回空列表
     */
    @Override
    public Map<Long, List<RoleTenantVO>> getRoleTenantsByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleTenant::getSRoleId, roleIds)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);
        List<RoleTenant> entities = this.list(queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(entities);
        return voList.stream().collect(Collectors.groupingBy(RoleTenantVO::getSRoleId));
    }

    /**
     * 根据系统租户ID查询其拥有的所有系统角色ID列表
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的系统角色ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getRoleIdsByTenantId(Long tenantId) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSTenantId, tenantId)
                .eq(RoleTenant::getDeleted, false)
                .select(RoleTenant::getSRoleId);

        List<RoleTenant> roleTenants = this.list(queryWrapper);
        return roleTenants.stream()
                .map(RoleTenant::getSRoleId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据系统角色ID查询所有绑定该角色的系统租户ID列表
     * @param roleId 系统角色ID（sRoleId）
     * @return 绑定该角色的系统租户ID去重集合（仅包含未软删且启用的记录）
     */
    @Override
    public Set<Long> getTenantIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleTenant::getSRoleId, roleId)
                .eq(RoleTenant::getDeleted, false)
                .select(RoleTenant::getSTenantId);

        List<RoleTenant> roleTenants = this.list(queryWrapper);
        return roleTenants.stream()
                .map(RoleTenant::getSTenantId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据 有效租户Id列表 和 有效角色Id列表 查询 租户角色信息
     * @param sTenantIds 租户Id列表
     * @param sRoleIds 角色Id列表
     * @return 按租户Id分组的绑定关系映射列表
     */
    @Override
    public Map<Long, List<RoleTenantVO>> getRoleTenantsBySTenantIdsAndSRoleIds(List<Long> sTenantIds, List<Long> sRoleIds) {
        if (sTenantIds == null || sTenantIds.isEmpty() || sRoleIds == null || sRoleIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<RoleTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleTenant::getSTenantId, sTenantIds)
                .in(RoleTenant::getSRoleId, sRoleIds)
                .eq(RoleTenant::getDeleted, false)
                .orderByDesc(RoleTenant::getCreatedAt);

        List<RoleTenant> entities = this.list(queryWrapper);
        List<RoleTenantVO> voList = roleTenantConverter.EnListToVOList(entities);

        return voList.stream()
                .collect(Collectors.groupingBy(RoleTenantVO::getSTenantId));
    }

}
