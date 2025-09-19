package com.cloudvalley.nebula.ultra.business.tenant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantDetailsVO;
import com.cloudvalley.nebula.ultra.business.tenant.service.ISysTenantService;
import com.cloudvalley.nebula.ultra.business.tenant.service.ITenantAggregatorService;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.service.ISysTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TenantAggregatorServiceImpl implements ITenantAggregatorService {

    @Autowired
    private ISysTenantService iSysTenantService;

    @Autowired
    private ISysTenantCommonService iSysTenantCommonService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    /**
     * 获取租户详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户详情信息
     */
    @Override
    public List<TenantDetailsVO> getTenantInfo(Integer current, Integer size) {
        // 1. 查询 系统租户列表 基本信息（分页）
        IPage<SysTenantVO> sysTenantList = iSysTenantService.getSysTenantList(new Page<>(current, size));

        // 2. 获取所有租户信息，构建ID到租户的映射
        List<SysTenantVO> allTenants = iSysTenantCommonService.getSysTenants();
        Map<Long, SysTenantVO> tenantMap = allTenants.stream()
                .collect(Collectors.toMap(SysTenantVO::getId, tenant -> tenant));

        // 3. 构建父ID到子租户列表的映射
        Map<Long, List<SysTenantVO>> parentToChildrenMap = allTenants.stream()
                .filter(tenant -> tenant.getParentId() != null)
                .collect(Collectors.groupingBy(SysTenantVO::getParentId));

        // 4. 对分页结果中的每个租户构建完整的树形结构
        return sysTenantList.getRecords().stream()
                .map(tenant -> buildTenantTree(tenant, tenantMap, parentToChildrenMap))
                .collect(Collectors.toList());
    }

    /**
     * 递归构建租户树
     * @param tenant 当前租户
     * @param tenantMap 租户ID到租户的映射
     * @param parentToChildrenMap 父ID到子租户列表的映射
     * @return 租户树
     */
    private TenantDetailsVO buildTenantTree(SysTenantVO tenant, Map<Long, SysTenantVO> tenantMap,
                                            Map<Long, List<SysTenantVO>> parentToChildrenMap) {
        // 1. 获取当前租户的子租户列表
        List<SysTenantVO> childTenants = parentToChildrenMap.getOrDefault(tenant.getId(), new ArrayList<>());

        // 2. 递归构建子租户树
        List<TenantDetailsVO> childTenantVOs = childTenants.stream()
                .map(childTenant -> buildTenantTree(childTenant, tenantMap, parentToChildrenMap))
                .collect(Collectors.toList());

        // 3. 获取父租户名称
        String parentTenantName = null;
        if (tenant.getParentId() != null) {
            SysTenantVO parentTenant = tenantMap.get(tenant.getParentId());
            if (parentTenant != null) {
                parentTenantName = parentTenant.getName();
            }
        }

        // 4. 获取负责人用户信息
        String ownerUser = null;
        if (tenant.getOwnerUserId() != null) {
            SysUserVO userVO = iSysUserCommonService.getUserById(tenant.getOwnerUserId());
            if (userVO != null) {
                ownerUser = userVO.getUsername();
            }
        }

        // 5. 获取创建人信息
        String createdByUser = null;
        if (tenant.getCreatedById() != null) {
            SysUserVO userVO = iSysUserCommonService.getUserById(tenant.getCreatedById());
            if (userVO != null) {
                createdByUser = userVO.getUsername();
            }
        }

        // 6. 构建当前租户的TenantDetailsVO
        return new TenantDetailsVO(
                tenant.getId(),
                parentTenantName,
                tenant.getName(),
                tenant.getDesc(),
                tenant.getPhone(),
                tenant.getAddress(),
                ownerUser,
                tenant.getLicenseStartAt(),
                tenant.getLicenseEndAt(),
                tenant.getCreatedAt(),
                tenant.getUpdatedAt(),
                createdByUser,
                tenant.getColor(),
                tenant.getState(),
                childTenantVOs
        );
    }
}
