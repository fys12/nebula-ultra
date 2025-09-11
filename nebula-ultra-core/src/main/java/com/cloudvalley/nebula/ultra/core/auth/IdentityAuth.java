package com.cloudvalley.nebula.ultra.core.auth;

import com.cloudvalley.nebula.ultra.core.model.vo.CheckDeptVO;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckPermVO;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckRoleVO;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptUserVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.*;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.*;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleUserVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.service.ISysTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IdentityAuth {

    @Autowired
    private IPermTenantCommonService iPermTenantCommonService;

    @Autowired
    private IPermDeptCommonService iPermDeptCommonService;

    @Autowired
    private IPermRoleCommonService iPermRoleCommonService;

    @Autowired
    private IPermUserCommonService iPermUserCommonService;

    @Autowired
    private ISysTenantCommonService iSysTenantCommonService;

    @Autowired
    private ISysDeptCommonService iSysDeptCommonService;

    @Autowired
    private IDeptTenantCommonService iDeptTenantCommonService;

    @Autowired
    private IDeptUserCommonService iDeptUserCommonService;

    @Autowired
    private ISysRoleCommonService iSysRoleCommonService;

    @Autowired
    private IRoleTenantCommonService iRoleTenantCommonService;

    @Autowired
    private IRoleUserCommonService iRoleUserCommonService;

    @Autowired
    private ISysPermCommonService iSysPermCommonService;

    /**
     * 权限认证
     * @param tUserId 租户用户ID
     * @return 权限认证结果
     */
    public CheckPermVO checkPerm(Long tUserId) {
        // 1. 调用 租户认证

        // 1.1 获取 用户 绑定的有效租户Id

        // 1.2 获取 用户 绑定的禁用租户Id

        // 1.3 根据 禁用租户Id 获取 绑定的权限 [ 因禁用的租户Id 级联禁用的 权限 ]

        // 2. 调用 部门认证

        // 2.1 获取 用户 绑定的有效部门Id

        // 2.2 获取 用户 绑定的禁用部门Id

        // 2.3 根据 禁用部门Id 查询 对应权限 [ 因禁用的部门Id 级联禁用的 权限 ]

        // 3. 调用 角色认证

        // 3.1 获取 用户 绑定的有效角色Id

        // 3.2 获取 用户 绑定的禁用角色Id

        // 3.3 根据 禁用角色Id 获取 绑定的权限 [ 因禁用的角色Id 级联禁用的 权限 ]

        // 4. 根据 用户有效部门Id 查询 绑定的租户权限

        // 5. 根据 用户有效角色Id 获取 绑定的租户权限

        // 6. 根据 组合用户Id 查询 直接绑定的组合权限

        // 7. 组合用户 绑定的租户权限 [ 所属 有效部门权限 + 有效角色权限 + 本身权限 ]

        // 8. 根据 用户 绑定的租户权限Id 查询 绑定的 系统权限信息

        // 8.1 获取 禁用系统权限Id [ 禁用权限 系统级 ]

        // 8.2 获取 有效系统权限Id [ 有效权限 系统级 ]

        // 8.3 获取 因系统权限禁用 级联禁用的对应的租户权限Id

        // 9 获取 禁用租户权限Id [ 禁用权限 租户级 ]

        // 9.1 获取 有效租户权限Id [ 有效权限 租户级 ]

        // 10. 获取 禁用租户权限Id [ 禁用权限 部门级 ]

        // 10.1 获取 有效租户权限Id [ 有效权限 部门级 ]

        // 11. 获取 禁用租户权限Id [ 禁用权限 角色级 ]

        // 11.1 获取 有效租户权限Id [ 有效权限 角色级 ]

        // 12. 获取 禁用租户权限Id [ 禁用权限 用户级 ]

        // 12.1 获取 有效租户权限Id [ 有效权限 用户级 ]

        // 13. 组装 权限认证结果

        return null;

    }

    /**
     * 租户认证
     * @param sTenantIds 租户ID 列表
     * @return 租户认证结果
     */
    public CheckTenantVO checkTenant(List<Long> sTenantIds) {
        // 1. 根据 系统租户Id列表 查询 系统租户信息
        Map<Long, SysTenantVO> sysTenantsByIds = iSysTenantCommonService.getSysTenantsByIds(sTenantIds);

        // 2. 获取 禁用的 系统租户Id [ 禁用租户 系统级 ]
        List<SysTenantVO> disabledSysTenant = sysTenantsByIds.values().stream()
                .filter(tenant -> !tenant.getState())
                .toList();

        // 3. 获取 有效的 系统租户Id [ 有效租户 系统级 ]
        List<SysTenantVO> validSysTenant = sysTenantsByIds.values().stream()
                .filter(tenant -> tenant.getState())
                .toList();

        // 4. 构建并返回结果
        return new CheckTenantVO(validSysTenant, disabledSysTenant);

    }

    /**
     * 部门认证
     * @param tUserId 租户用户ID
     * @param sTenantIds 租户ID 列表
     * @return 部门认证结果
     */
    public CheckDeptVO checkDept(Long tUserId, List<Long> sTenantIds) {
        // 1. 调用 租户认证
        CheckTenantVO checkTenantVO = checkTenant(sTenantIds);

        // 1.1 获取 有效租户Id列表
        List<Long> validTenantIds = checkTenantVO.getValidSysTenant().stream()
                .map(SysTenantVO::getId)
                .toList();

        // 1.2 获取 禁用租户Id列表
        List<Long> disabledTenantIds = checkTenantVO.getDisabledSysTenant().stream()
                .map(SysTenantVO::getId)
                .toList();

        // 2. 根据 所有租户Id列表（有效+禁用） 查询 租户部门信息
        List<Long> allTenantIds = Stream.concat(validTenantIds.stream(), disabledTenantIds.stream())
                .distinct()
                .toList();
        List<Map<Long, List<DeptTenantVO>>> deptTenantsBySTenantIds = iDeptTenantCommonService.getDeptTenantsBySTenantIds(allTenantIds);

        // 2.1 根据租户用户Id 查询 绑定 租户部门Id
        Set<Long> deptIdsByUserId = iDeptUserCommonService.getDeptIdsByUserId(tUserId);

        // 2.2 排除 其余租户部门信息 只保留 用户 绑定的部门信息（无论有效或禁用的租户）
        deptTenantsBySTenantIds = deptTenantsBySTenantIds.stream()
                .map(tenantDeptMap -> tenantDeptMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> deptIdsByUserId.contains(deptTenant.getId()))
                                        .collect(Collectors.toList())
                        )))
                .filter(map -> !map.isEmpty() || map.values().stream().anyMatch(list -> !list.isEmpty()))
                .toList();

        // 3. 获取 所有系统部门Id
        List<Long> sysDeptIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(DeptTenantVO::getSDeptId)
                .distinct()
                .toList();

        // 3.1 根据 系统部门Id 获取 系统部门信息 Map<系统部门ID, 部门VO>
        Map<Long, SysDeptVO> sysDeptsByIds = iSysDeptCommonService.getSysDeptsByIds(sysDeptIds);

        // 3.2 获取 禁用系统部门Id [ 禁用部门 系统级 ]
        List<SysDeptVO> disabledSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> dept.getState() == null || !dept.getState())
                .toList();

        // 3.3 获取 有效系统部门 [ 有效部门 系统级 ]
        List<SysDeptVO> validSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> dept.getState() != null && dept.getState())
                .toList();

        // 3.4 建立 系统部门Id->租户部门信息 键值对（包含租户ID信息）
        Map<Long, List<DeptTenantVO>> sysDeptIdToTenantDeptVOs = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(DeptTenantVO::getSDeptId));

        // 3.5 获取 因系统部门禁用 级联禁用的对应的租户部门Id
        List<Long> cascadeDisabledDeptBySys = disabledSysDept.stream()
                .map(SysDeptVO::getId)
                .flatMap(sysDeptId -> sysDeptIdToTenantDeptVOs.getOrDefault(sysDeptId, Collections.emptyList()).stream())
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 4. 获取 所有租户部门Id
        List<Long> tenantDeptIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 4.1 获取 禁用租户部门Id [ 禁用部门 租户级 ]
        List<Long> disabledTenantDeptTenantIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .filter(deptTenant -> deptTenant.getState() == null || !deptTenant.getState())
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 4.2 获取 有效租户部门Id [ 有效部门 租户级 ] = tenantDeptIds - disabledTenantDeptTenantIds - cascadeDisabledDeptBySys
        List<Long> validTenantDeptSystemIds = tenantDeptIds.stream()
                .filter(tenantDeptId -> !disabledTenantDeptTenantIds.contains(tenantDeptId))
                .filter(tenantDeptId -> !cascadeDisabledDeptBySys.contains(tenantDeptId))
                .toList();

        // 4.3 获取 因租户部门禁用 级联禁用的对应的租户部门Id = disabledTenantDeptTenantIds + cascadeDisabledDeptBySys
        List<Long> cascadeDisabledDeptByTenant = Stream.concat(
                disabledTenantDeptTenantIds.stream(),
                cascadeDisabledDeptBySys.stream()
        ).distinct().toList();

        // 5. 根据 租户用户Id 查询 绑定 租户部门信息
        List<DeptUserVO> deptUsersByUserId = iDeptUserCommonService.getDeptUsersByUserId(tUserId);

        // 5.1 获取 禁用的 用户租户部门Id [ 禁用部门 用户级 ]
        List<Long> disabledUserDeptIds = deptUsersByUserId.stream()
                .filter(deptUser -> deptUser.getState() == null || !deptUser.getState())
                .map(DeptUserVO::getTDeptId)
                .toList();

        // 5.2 获取 有效部门Id [ 有效部门 用户级 ] = tenantDeptIds - disabledUserDeptIds - cascadeDisabledDeptByTenant
        List<Long> validUserDeptIds = tenantDeptIds.stream()
                .filter(tenantDeptId -> !disabledUserDeptIds.contains(tenantDeptId))
                .filter(tenantDeptId -> !cascadeDisabledDeptByTenant.contains(tenantDeptId))
                .toList();

        // 6. 建立租户ID到系统部门的映射关系（只包含有效租户）
        Map<Long, List<SysDeptVO>> tenantIdToSysDeptVOs = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .map(deptTenant -> sysDeptsByIds.get(deptTenant.getSDeptId()))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7. 按租户分组组装各级有效部门数据时，需要设置 cascadeDisable 字段（只包含有效租户）
        // 7.1 系统级有效部门 - 按租户分组（无级联禁用，cascadeDisable = null）
        Map<Long, List<SysDeptVO>> validSysDeptByTenant = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> validSysDept.stream()
                                .filter(dept -> tenantIdToSysDeptVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysDept -> sysDept.getId().equals(dept.getId())))
                                // 系统级部门不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                .collect(Collectors.toList())
                ));

        // 7.2 租户级有效部门 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysDeptVO>> validTenantDeptByTenant = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> validTenantDeptSystemIds.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            // 租户级有效部门不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                            return originalDept;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7.3 用户级有效部门 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysDeptVO>> validUserDeptByTenant = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> validUserDeptIds.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            // 用户级有效部门不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                            return originalDept;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8. 按租户分组组装各级禁用部门数据时，需要设置 cascadeDisable 字段（只包含有效租户）
        // 8.1 系统级禁用部门 - 按租户分组（直接禁用，cascadeDisable = null）
        Map<Long, List<SysDeptVO>> disabledSysDeptByTenant = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> disabledSysDept.stream()
                                .filter(dept -> tenantIdToSysDeptVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysDept -> sysDept.getId().equals(dept.getId())))
                                // 系统级禁用部门是直接禁用，不是级联禁用，cascadeDisable 保持原值（通常为 null）
                                .collect(Collectors.toList())
                ));

        // 8.2 租户级禁用部门 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysDeptVO>> disabledTenantDeptByTenant = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> disabledTenantDeptTenantIds.contains(deptTenant.getId())
                                                || cascadeDisabledDeptBySys.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());

                                            // 判断是否为级联禁用
                                            if (cascadeDisabledDeptBySys.contains(deptTenant.getId())) {
                                                // 因系统级部门禁用而级联禁用的租户级部门
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "system", // 设置级联禁用标识为 "system"
                                                        originalDept.getDeleted()
                                                );
                                            } else {
                                                // 直接禁用的租户级部门，cascadeDisable 保持原值（通常为 null）
                                                return originalDept;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.3 用户级禁用部门 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysDeptVO>> disabledUserDeptByTenant = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> disabledUserDeptIds.contains(deptTenant.getId())
                                                || cascadeDisabledDeptByTenant.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());

                                            // 判断级联禁用的类型
                                            if (cascadeDisabledDeptBySys.contains(deptTenant.getId())) {
                                                // 因系统级部门禁用而级联禁用
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "system", // 因系统级禁用而级联
                                                        originalDept.getDeleted()
                                                );
                                            } else if (disabledTenantDeptTenantIds.contains(deptTenant.getId())) {
                                                // 因租户级部门禁用而级联禁用
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "tenant", // 因租户级禁用而级联
                                                        originalDept.getDeleted()
                                                );
                                            } else {
                                                // 直接禁用的用户级部门，cascadeDisable 保持原值（通常为 null）
                                                return originalDept;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.4 获取用户所绑定的禁用租户的部门，并将这些部门信息存储到disabledDeptByTenant
        Map<Long, List<SysDeptVO>> disabledDeptByTenant = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> disabledTenantIds.contains(entry.getKey())) // 只处理禁用租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        // 只包含用户绑定的部门
                                        .filter(deptTenant -> deptIdsByUserId.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            // 设置级联禁用标识为 "tenant"
                                            return new SysDeptVO(
                                                    originalDept.getId(),
                                                    originalDept.getName(),
                                                    originalDept.getDesc(),
                                                    originalDept.getCreatedAt(),
                                                    originalDept.getUpdatedAt(),
                                                    originalDept.getCreatedById(),
                                                    originalDept.getUpdatedById(),
                                                    originalDept.getColor(),
                                                    originalDept.getState(),
                                                    "tenant", // 因租户禁用而级联
                                                    originalDept.getDeleted()
                                            );
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 9. 确保每个租户都有对应的条目（有效租户在前6个字段中，所有租户在disabledDeptByTenant中）
        allTenantIds.forEach(tenantId -> {
            // 只有有效租户才需要在前6个字段中有条目
            if (validTenantIds.contains(tenantId)) {
                validSysDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validTenantDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validUserDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledSysDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledTenantDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledUserDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
            }
            // 所有租户（包括禁用租户）在disabledDeptByTenant中都需要有条目
            disabledDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
        });

        // 10. 组装并返回结果
        return new CheckDeptVO(
                validSysDeptByTenant,
                validTenantDeptByTenant,
                validUserDeptByTenant,
                disabledSysDeptByTenant,
                disabledTenantDeptByTenant,
                disabledUserDeptByTenant,
                disabledDeptByTenant // 添加这个字段
        );
    }

    /**
     * 角色认证
     * @param tUserId 租户用户ID
     * @param sTenantIds 有效租户ID 列表
     * @return 角色认证结果
     */
    public CheckRoleVO checkRole(Long tUserId, List<Long> sTenantIds) {
        // 1. 根据 租户Id列表 获取 查询 租户角色信息
        List<Map<Long, List<RoleTenantVO>>> roleTenantsByTenantIds = iRoleTenantCommonService.getRoleTenantsByTenantIds(sTenantIds);

        // 1.1 根据租户用户Id 查询 绑定 租户角色Id
        Set<Long> roleIdsByUserId = iRoleUserCommonService.getRoleIdsByUserId(tUserId);

        // 1.2 排除 其余租户角色信息 只保留 用户 绑定的角色信息
        roleTenantsByTenantIds = roleTenantsByTenantIds.stream()
                .map(tenantRoleMap -> tenantRoleMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> roleIdsByUserId.contains(roleTenant.getId()))
                                        .collect(Collectors.toList())
                        )))
                .filter(map -> map.values().stream().anyMatch(list -> !list.isEmpty()))
                .toList();

        // 2. 获取 所有系统角色Id
        List<Long> sysRoleIds = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(RoleTenantVO::getSRoleId)
                .distinct()
                .toList();

        // 2.1 根据 系统角色Id 获取 系统角色信息 Map<系统角色ID, 角色VO>
        Map<Long, SysRoleVO> sysRolesByIds = iSysRoleCommonService.getSysRolesByIds(sysRoleIds);

        // 2.2 获取 禁用系统角色Id [ 禁用角色 系统级 ]
        List<SysRoleVO> disabledSysRole = sysRolesByIds.values().stream()
                .filter(role -> !role.getState())
                .toList();

        // 2.3 获取 有效系统角色 [ 有效角色 系统级 ]
        List<SysRoleVO> validSysRole = sysRolesByIds.values().stream()
                .filter(role -> role.getState())
                .toList();

        // 2.4 建立 系统角色Id->租户角色Id 键值对
        Map<Long, List<Long>> sysRoleIdToTenantRoleIds = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        RoleTenantVO::getSRoleId,
                        Collectors.mapping(RoleTenantVO::getId, Collectors.toList())
                ));

        // 2.5 获取 因系统角色禁用 级联禁用的对应的租户角色Id
        List<Long> cascadeDisabledRoleBySys = disabledSysRole.stream()
                // 获取禁用的系统角色ID
                .map(SysRoleVO::getId)
                // 获取对应的租户角色ID
                .flatMap(sysRoleId -> sysRoleIdToTenantRoleIds.getOrDefault(sysRoleId, Collections.emptyList()).stream())
                .distinct()
                .toList();

        // 3. 获取 禁用租户角色Id [ 禁用角色 租户级 ]
        List<Long> tenantRoleIds = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 3.1 获取 禁用租户角色Id [ 禁用角色 租户级 ]
        List<Long> disabledTenantRoleTenantIds = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .filter(roleTenant -> !roleTenant.getState())
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 3.2 获取 有效租户角色Id [ 有效角色 租户级 ] = tenantRoleIds - disabledTenantRoleTenantIds - cascadeDisabledRoleBySys
        List<Long> validTenantRoleSystemIds = tenantRoleIds.stream()
                .filter(tenantRoleId -> !disabledTenantRoleTenantIds.contains(tenantRoleId))
                .filter(tenantRoleId -> !cascadeDisabledRoleBySys.contains(tenantRoleId))
                .toList();

        // 3.3 根据 禁用租户角色Id 获取 租户角色信息 [ 禁用角色 租户级 ]
        // 通过 disabledTenantRoleTenantIds 反向查找对应的系统角色ID，然后获取系统角色信息
        List<SysRoleVO> disabledTenantRole = sysRoleIdToTenantRoleIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(disabledTenantRoleTenantIds::contains))
                .map(Map.Entry::getKey)
                .map(sysRolesByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 3.4 根据 有效租户角色Id 获取 系统角色信息 [ 有效角色 租户级 ]
        // 通过 validTenantRoleSystemIds 反向查找对应的系统角色ID，然后获取系统角色信息
        List<SysRoleVO> validTenantRole = sysRoleIdToTenantRoleIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(validTenantRoleSystemIds::contains))
                .map(Map.Entry::getKey)
                .map(sysRolesByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 3.5 获取 因租户角色禁用 级联禁用的对应的租户角色Id = disabledTenantRoleTenantIds + cascadeDisabledRoleBySys
        List<Long> cascadeDisabledRoleByTenant = Stream.concat(
                disabledTenantRoleTenantIds.stream(),
                cascadeDisabledRoleBySys.stream()
        ).distinct().toList();

        // 4. 根据 租户用户Id 查询 绑定 租户角色信息
        List<RoleUserVO> roleUsersByUserId = iRoleUserCommonService.getRoleUsersByUserId(tUserId);

        // 4.1 获取 禁用的 用户租户角色Id [ 禁用角色 用户级 ]
        List<Long> disabledUserRoleIds = roleUsersByUserId.stream()
                .filter(roleUser -> !roleUser.getState())
                .map(RoleUserVO::getTRoleId)
                .toList();

        // 4.2 获取 有效角色Id [ 有效角色 用户级 ] = tenantRoleIds - disabledUserRoleIds - cascadeDisabledRoleByTenant
        List<Long> validUserRoleIds = tenantRoleIds.stream()
                .filter(tenantRoleId -> !disabledUserRoleIds.contains(tenantRoleId))
                .filter(tenantRoleId -> !cascadeDisabledRoleByTenant.contains(tenantRoleId))
                .toList();

        // 4.3 根据 禁用角色Id 获取 对应 系统角色信息 [ 禁用角色 用户级 ]
        // 通过 disabledUserRoleIds 反向查找对应的系统角色ID，然后获取系统角色信息
        List<SysRoleVO> disabledUserRole = sysRoleIdToTenantRoleIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(disabledUserRoleIds::contains))
                .map(Map.Entry::getKey)
                .map(sysRolesByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 4.4 根据 有效角色Id 获取 对应 系统角色信息 [ 有效角色 用户级 ]
        // 通过 validUserRoleIds 反向查找对应的系统角色ID，然后获取系统角色信息
        List<SysRoleVO> validUserRole = sysRoleIdToTenantRoleIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(validUserRoleIds::contains))
                .map(Map.Entry::getKey)
                .map(sysRolesByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 5. 建立租户ID到系统角色的映射关系
        Map<Long, List<SysRoleVO>> tenantIdToSysRoleVOs = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .map(roleTenant -> sysRolesByIds.get(roleTenant.getSRoleId()))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 6. 按租户分组组装各级有效角色数据

        // 6.1 系统级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> validSysRoleByTenant = sTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> validSysRole.stream()
                                .filter(role -> tenantIdToSysRoleVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysRole -> sysRole.getId().equals(role.getId())))
                                .collect(Collectors.toList())
                ));

        // 6.2 租户级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> validTenantRoleByTenant = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validTenantRoleSystemIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> sysRolesByIds.get(roleTenant.getSRoleId()))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 6.3 用户级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> validUserRoleByTenant = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validUserRoleIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> sysRolesByIds.get(roleTenant.getSRoleId()))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7. 按租户分组组装各级禁用角色数据

        // 7.1 系统级禁用角色 - 按租户分组（直接禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> disabledSysRoleByTenant = sTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> disabledSysRole.stream()
                                .filter(role -> tenantIdToSysRoleVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysRole -> sysRole.getId().equals(role.getId())))
                                .collect(Collectors.toList())
                ));

        // 7.2 租户级禁用角色 - 按租户分组（需要区分直接禁用和级联禁用）
        Map<Long, List<SysRoleVO>> disabledTenantRoleByTenant = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> disabledTenantRoleTenantIds.contains(roleTenant.getId())
                                                || cascadeDisabledRoleBySys.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());

                                            // 判断是否为级联禁用
                                            if (cascadeDisabledRoleBySys.contains(roleTenant.getId())) {
                                                // 因系统级角色禁用而级联禁用的租户级角色
                                                return new SysRoleVO(
                                                        originalRole.getId(),
                                                        originalRole.getName(),
                                                        originalRole.getAlias(),
                                                        originalRole.getDesc(),
                                                        originalRole.getCreatedAt(),
                                                        originalRole.getUpdatedAt(),
                                                        originalRole.getCreatedById(),
                                                        originalRole.getUpdatedById(),
                                                        originalRole.getColor(),
                                                        originalRole.getState(),
                                                        "system", // 设置级联禁用标识为 "system"
                                                        originalRole.getDeleted()
                                                );
                                            } else {
                                                // 直接禁用的租户级角色，cascadeDisable 保持原值（通常为 null）
                                                return originalRole;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7.3 用户级禁用角色 - 按租户分组（需要区分直接禁用和级联禁用）
        Map<Long, List<SysRoleVO>> disabledUserRoleByTenant = roleTenantsByTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> disabledUserRoleIds.contains(roleTenant.getId())
                                                || cascadeDisabledRoleByTenant.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());

                                            // 判断级联禁用的类型
                                            if (cascadeDisabledRoleBySys.contains(roleTenant.getId())) {
                                                // 因系统级角色禁用而级联禁用
                                                return new SysRoleVO(
                                                        originalRole.getId(),
                                                        originalRole.getName(),
                                                        originalRole.getAlias(),
                                                        originalRole.getDesc(),
                                                        originalRole.getCreatedAt(),
                                                        originalRole.getUpdatedAt(),
                                                        originalRole.getCreatedById(),
                                                        originalRole.getUpdatedById(),
                                                        originalRole.getColor(),
                                                        originalRole.getState(),
                                                        "system", // 因系统级禁用而级联
                                                        originalRole.getDeleted()
                                                );
                                            } else if (disabledTenantRoleTenantIds.contains(roleTenant.getId())) {
                                                // 因租户级角色禁用而级联禁用
                                                return new SysRoleVO(
                                                        originalRole.getId(),
                                                        originalRole.getName(),
                                                        originalRole.getAlias(),
                                                        originalRole.getDesc(),
                                                        originalRole.getCreatedAt(),
                                                        originalRole.getUpdatedAt(),
                                                        originalRole.getCreatedById(),
                                                        originalRole.getUpdatedById(),
                                                        originalRole.getColor(),
                                                        originalRole.getState(),
                                                        "tenant", // 因租户级禁用而级联
                                                        originalRole.getDeleted()
                                                );
                                            } else {
                                                // 直接禁用的用户级角色，cascadeDisable 保持原值（通常为 null）
                                                return originalRole;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8. 确保每个租户都有对应的条目（即使是空列表）
        sTenantIds.forEach(tenantId -> {
            validSysRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
            validTenantRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
            validUserRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
            disabledSysRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
            disabledTenantRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
            disabledUserRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
        });

        // 9. 组装并返回结果
        return new CheckRoleVO(
                validSysRoleByTenant,
                validTenantRoleByTenant,
                validUserRoleByTenant,
                disabledSysRoleByTenant,
                disabledTenantRoleByTenant,
                disabledUserRoleByTenant
        );

    }

}
