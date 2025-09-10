package com.cloudvalley.nebula.ultra.core.auth;

import com.cloudvalley.nebula.ultra.core.model.vo.CheckDeptVO;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptUserVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
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
    private ISysTenantCommonService iSysTenantCommonService;

    @Autowired
    private IDeptTenantCommonService iDeptTenantCommonService;

    @Autowired
    private ISysDeptCommonService iSysDeptCommonService;

    @Autowired
    private IDeptUserCommonService iDeptUserCommonService;

    /**
     * 权限认证
     * @param tUserId 租户用户ID
     * @param sTenantIds 租户ID 列表
     * @return 权限认证结果 Map<层级 Map<权限状态 Map<系统权限Id 系统权限VO>>>
     */
    public Map<String, Object> checkPerm(Long tUserId, List<Long> sTenantIds) {

        // 有效权限Id [ 系统级 Id->TId ]
        List<Long> validSysTPermId = new ArrayList<>();

        // 有效权限Id [ 租户级 Id->TId ]
        List<Long> validTenantTPermId = new ArrayList<>();

        // 有效权限Id [ 部门级 Id->TId ]
        List<Long> validDeptTPermId = new ArrayList<>();

        // 有效权限Id [ 角色级 Id->TId ]
        List<Long> validRoleTPermId = new ArrayList<>();

        // 有效权限Id [ 用户级 Id->TId ]
        List<Long> validUserTPermId = new ArrayList<>();

        // 禁用权限Id [ 系统级 Id->TId ]
        List<Long> disabledSysTPermId = new ArrayList<>();

        // 禁用权限Id [ 租户级 Id->TId ]
        List<Long> disabledTenantTPermId = new ArrayList<>();

        // 禁用权限Id [ 部门级 Id->TId ]
        List<Long> disabledDeptTPermId = new ArrayList<>();

        // 禁用权限Id [ 角色级 Id->TId ]
        List<Long> disabledRoleTPermId = new ArrayList<>();

        // 禁用权限Id [ 用户级 Id->TId ]
        List<Long> disabledUserTPermId = new ArrayList<>();

        // 1. 查询 所有系统权限

        // 1.1 获取 禁用的 系统权限Id [ 禁用权限 系统级 ]

        // 2. 根据 系统租户Id列表 查询 租户权限信息

        // 2.1 获取 禁用 系统权限Id [ 禁用权限 租户级 ]

        // 2.2 调用 租户认证 获取 禁用的 租户Id

        // 2.3 获取 禁用的 租户Id 对应的 系统权限Id [ 禁用权限 租户级 ]

        // 3. 根据 租户权限Id列表 查询 部门权限信息

        // 3.1 获取 禁用 租户权限Id [ 禁用权限 部门级 ]

        // 3.2 调用 部门认证 获取 禁用的 部门Id

        // 3.3 获取 禁用的 部门Id 对应的 租户权限Id [ 禁用权限 部门级 ]

        // 4. 根据 租户权限Id列表 查询 角色权限信息

        // 4.1 获取 禁用 租户权限Id [ 禁用权限 角色级 ]

        // 4.2 调用 角色认证 获取 禁用的 角色Id

        // 4.3 获取 禁用的 角色Id 对应的 租户权限Id [ 禁用权限 角色级 ]

        // 5. 根据 租户用户Id 查询 用户权限信息

        // 5.1 获取 禁用 租户权限Id [ 禁用权限 用户级 ]

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
        // 1. 根据 租户Id列表 查询 租户部门信息
        List<Map<Long, List<DeptTenantVO>>> deptTenantsBySTenantIds = iDeptTenantCommonService.getDeptTenantsBySTenantIds(sTenantIds);

        // 1.1 根据租户用户Id 查询 绑定 租户部门Id
        Set<Long> deptIdsByUserId = iDeptUserCommonService.getDeptIdsByUserId(tUserId);

        // 1.2 排除 其余租户部门信息 只保留 用户 绑定的部门信息
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

        // 2. 获取 所有系统部门Id
        List<Long> sysDeptIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(DeptTenantVO::getSDeptId)
                .distinct()
                .toList();

        // 2.1 根据 系统部门Id 获取 系统部门信息 Map<系统部门ID, 部门VO>
        Map<Long, SysDeptVO> sysDeptsByIds = iSysDeptCommonService.getSysDeptsByIds(sysDeptIds);

        // 2.2 获取 禁用系统部门Id [ 禁用部门 系统级 ]
        List<SysDeptVO> disabledSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> !dept.getState())
                .toList();

        // 2.3 获取 有效系统部门 [ 有效部门 系统级 ]
        List<SysDeptVO> validSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> dept.getState())
                .toList();

        // 2.4 建立 系统部门Id->租户部门Id 键值对
        Map<Long, List<Long>> sysDeptIdToTenantDeptIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        DeptTenantVO::getSDeptId,
                        Collectors.mapping(DeptTenantVO::getId, Collectors.toList())
                ));

        // 2.5 获取 因系统部门禁用 级联禁用的对应的租户部门Id
        List<Long> cascadeDisabledDeptBySys = disabledSysDept.stream()
                // 获取禁用的系统部门ID
                .map(SysDeptVO::getId)
                // 获取对应的租户部门ID
                .flatMap(sysDeptId -> sysDeptIdToTenantDeptIds.getOrDefault(sysDeptId, Collections.emptyList()).stream())
                .distinct()
                .toList();

        // 3. 获取 所有租户部门Id
        List<Long> tenantDeptIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 3.1 获取 禁用租户部门Id [ 禁用部门 租户级 ]
        List<Long> disabledTenantDeptTenantIds = deptTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .filter(deptTenant -> !deptTenant.getState())
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 3.2 获取 有效租户部门Id [ 有效部门 租户级 ] = tenantDeptIds - disabledTenantDeptTenantIds - cascadeDisabledDeptBySys
        List<Long> validTenantDeptSystemIds = tenantDeptIds.stream()
                .filter(tenantDeptId -> !disabledTenantDeptTenantIds.contains(tenantDeptId))
                .filter(tenantDeptId -> !cascadeDisabledDeptBySys.contains(tenantDeptId))
                .toList();

        // 3.3 根据 禁用租户部门Id 获取 租户部门信息 [ 禁用部门 租户级 ]
        // 通过 validTenantDeptSystemIds 反向查找对应的系统部门ID，然后获取系统部门信息
        List<SysDeptVO> disabledTenantDept = sysDeptIdToTenantDeptIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(disabledTenantDeptTenantIds::contains))
                .map(Map.Entry::getKey)
                .map(sysDeptsByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 3.4 根据 有效租户部门Id 获取 系统部门信息 [ 有效部门 租户级 ]
        // 通过 validTenantDeptSystemIds 反向查找对应的系统部门ID，然后获取系统部门信息
        List<SysDeptVO> validTenantDept = sysDeptIdToTenantDeptIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(validTenantDeptSystemIds::contains))
                .map(Map.Entry::getKey)
                .map(sysDeptsByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 3.5 获取 因租户部门禁用 级联禁用的对应的租户部门Id = disabledTenantDeptTenantIds + cascadeDisabledDeptBySys
        List<Long> cascadeDisabledDeptByTenant = Stream.concat(
                disabledTenantDeptTenantIds.stream(),
                cascadeDisabledDeptBySys.stream()
        ).distinct().toList();

        // 4. 根据 租户用户Id 查询 绑定 租户部门信息
        List<DeptUserVO> deptUsersByUserId = iDeptUserCommonService.getDeptUsersByUserId(tUserId);

        // 4.1 获取 禁用的 用户租户部门Id [ 禁用部门 用户级 ]
        List<Long> disabledUserDeptIds = deptUsersByUserId.stream()
                .filter(deptUser -> !deptUser.getState())
                .map(DeptUserVO::getTDeptId)
                .toList();

        // 4.2 获取 有效部门Id [ 有效部门 用户级 ] = tenantDeptIds - disabledUserDeptIds - cascadeDisabledDeptByTenant
        List<Long> validUserDeptIds = tenantDeptIds.stream()
                .filter(tenantDeptId -> !disabledUserDeptIds.contains(tenantDeptId))
                .filter(tenantDeptId -> !cascadeDisabledDeptByTenant.contains(tenantDeptId))
                .toList();

        // 4.3 根据 禁用部门Id 获取 对应 系统部门信息 [ 禁用部门 用户级 ]
        // 通过 validUserDeptIds 反向查找对应的系统部门ID，然后获取系统部门信息
        List<SysDeptVO> disabledUserDept = sysDeptIdToTenantDeptIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(disabledUserDeptIds::contains))
                .map(Map.Entry::getKey)
                .map(sysDeptsByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 4.4 根据 有效部门Id 获取 对应 系统部门信息 [ 有效部门 用户级 ]
        // 通过 validUserDeptIds 反向查找对应的系统部门ID，然后获取系统部门信息
        List<SysDeptVO> validUserDept = sysDeptIdToTenantDeptIds.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(validUserDeptIds::contains))
                .map(Map.Entry::getKey)
                .map(sysDeptsByIds::get)
                .filter(Objects::nonNull)
                .toList();

        // 5. 组装数据
        return new CheckDeptVO(
                validSysDept,
                validTenantDept,
                validUserDept,
                disabledSysDept,
                disabledTenantDept,
                disabledUserDept
        );
    }

    /**
     * 角色认证
     * @param tUserId 租户用户ID
     * @param sTenantIds 租户ID 列表
     * @return 角色认证结果 Map<角色状态:层级 角色VO>
     */
    public Map<String, Object> checkRole(Long tUserId, List<Long> sTenantIds) {
        // 1. 根据 租户Id 获取 查询 租户角色信息

        // 2. 获取 所有系统角色Id

        // 2.1 根据 系统角色Id 获取 系统角色信息

        // 2.2 获取 禁用系统角色Id [ 禁用角色 系统级 ]

        // 3. 获取 禁用租户角色Id [ 禁用角色 租户级 ]

        // 4. 根据 租户用户Id 查询 绑定 租户角色信息

        // 4.1 获取 禁用的 用户租户角色Id [ 禁用角色 用户级 ]

        return null;

    }

}
