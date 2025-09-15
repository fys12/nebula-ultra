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
     * @param tUserIds 租户用户ID 列表
     * @param sTenantIds 有效租户ID 列表
     * @return 权限认证结果
     */
     public CheckPermVO checkPerm(List<Long> tUserIds, List<Long> sTenantIds) {
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

         // 2. 调用 部门认证
         CheckDeptVO checkDeptVO = checkDept(tUserIds, sTenantIds);

         // 3. 调用 角色认证
         CheckRoleVO checkRoleVO = checkRole(tUserIds, sTenantIds);

         // 4. 根据 所有租户Id列表（有效+禁用） 查询 租户权限信息
         List<Long> allTenantIds = Stream.concat(validTenantIds.stream(), disabledTenantIds.stream())
                 .distinct()
                 .toList();
         List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIds = iPermTenantCommonService.getPermTenantsByTenantIds(allTenantIds);

         // 5. 根据租户用户Id 查询 绑定 租户权限Id
         List<Map<Long, List<PermUserVO>>> permUsersByTUserIds = iPermUserCommonService.getPermUsersByTUserIds(tUserIds);

         // 6. 获取 所有系统权限Id
         List<Long> sysPermIds = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .map(PermTenantVO::getSPermId)
                 .distinct()
                 .toList();

         // 6.1 根据 系统权限Id 获取 系统权限信息 Map<系统权限ID, 权限VO>
         List<SysPermVO> sysPermsByIds = iSysPermCommonService.getSysPermsByIds(sysPermIds);
         Map<Long, SysPermVO> sysPermMap = sysPermsByIds.stream()
                 .collect(Collectors.toMap(SysPermVO::getId, perm -> perm));

         // 6.2 获取 禁用系统权限Id [ 禁用权限 系统级 ]
         List<SysPermVO> disabledSysPerm = sysPermsByIds.stream()
                 .filter(perm -> perm.getState() == null || !perm.getState())
                 .toList();

         // 6.3 获取 有效系统权限 [ 有效权限 系统级 ]
         List<SysPermVO> validSysPerm = sysPermsByIds.stream()
                 .filter(perm -> perm.getState() != null && perm.getState())
                 .toList();

         // 6.4 建立 系统权限Id->租户权限信息 键值对（包含租户ID信息）
         Map<Long, List<PermTenantVO>> sysPermIdToTenantPermVOs = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .collect(Collectors.groupingBy(PermTenantVO::getSPermId));

         // 6.5 获取 因系统权限禁用 级联禁用的对应的租户权限Id
         List<Long> cascadeDisabledPermBySys = disabledSysPerm.stream()
                 .map(SysPermVO::getId)
                 .flatMap(sysPermId -> sysPermIdToTenantPermVOs.getOrDefault(sysPermId, Collections.emptyList()).stream())
                 .map(PermTenantVO::getId)
                 .distinct()
                 .toList();

         // 7. 获取 所有租户权限Id
         List<Long> tenantPermIds = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .map(PermTenantVO::getId)
                 .distinct()
                 .toList();

         // 7.1 获取 禁用租户权限Id [ 禁用权限 租户级 ]
         List<Long> disabledTenantPermTenantIds = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .filter(permTenant -> permTenant.getState() == null || !permTenant.getState())
                 .map(PermTenantVO::getId)
                 .distinct()
                 .toList();

         // 7.2 获取 有效租户权限Id [ 有效权限 租户级 ] = tenantPermIds - disabledTenantPermTenantIds - cascadeDisabledPermBySys
         List<Long> validTenantPermSystemIds = tenantPermIds.stream()
                 .filter(tenantPermId -> !disabledTenantPermTenantIds.contains(tenantPermId))
                 .filter(tenantPermId -> !cascadeDisabledPermBySys.contains(tenantPermId))
                 .toList();

         // 7.3 获取 因租户权限禁用 级联禁用的对应的租户权限Id = disabledTenantPermTenantIds + cascadeDisabledPermBySys
         List<Long> cascadeDisabledPermByTenant = Stream.concat(
                 disabledTenantPermTenantIds.stream(),
                 cascadeDisabledPermBySys.stream()
         ).distinct().toList();

         // 8. 根据 租户用户Id 查询 绑定 租户权限信息
         List<Map<Long, List<PermUserVO>>> permUsersByUserId = iPermUserCommonService.getPermUsersByTUserIds(tUserIds);

         // 8.1 获取 禁用的 用户租户权限Id [ 禁用权限 用户级 ]
         List<Long> disabledUserPermIds = permUsersByUserId.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .filter(permUser -> permUser.getStatus() == null || !permUser.getStatus())
                 .map(PermUserVO::getTPermId)
                 .toList();

         // 8.2 获取 有效权限Id [ 有效权限 用户级 ] = tenantPermIds - disabledUserPermIds - cascadeDisabledPermByTenant
         List<Long> validUserPermIds = tenantPermIds.stream()
                 .filter(tenantPermId -> !disabledUserPermIds.contains(tenantPermId))
                 .filter(tenantPermId -> !cascadeDisabledPermByTenant.contains(tenantPermId))
                 .toList();

         // 9. 根据租户部门Id 查询 绑定 租户权限信息
         // 9.1 获取用户在各租户下的部门Id（使用租户部门Id）
         Map<Long, Set<Long>> deptIdsByTenant = new HashMap<>();
         for (Long tenantId : validTenantIds) {
             Set<Long> deptIds = new HashSet<>();
             // 通过用户绑定的租户部门关系获取租户部门Id
             List<Map<Long, List<DeptUserVO>>> deptUsersByUserId = iDeptUserCommonService.getDeptUsersByUserIds(tUserIds);
             for (Map<Long, List<DeptUserVO>> deptUserMap : deptUsersByUserId) {
                 for (List<DeptUserVO> deptUsers : deptUserMap.values()) {
                     for (DeptUserVO deptUser : deptUsers) {
                         if (tUserIds.contains(deptUser.getTUserId())) {
                             deptIds.add(deptUser.getTDeptId()); // 使用租户部门Id
                         }
                     }
                 }
             }
             deptIdsByTenant.put(tenantId, deptIds);
         }

         // 9.2 根据部门Id获取部门权限
         List<Long> allDeptIds = deptIdsByTenant.values().stream()
                 .flatMap(Set::stream)
                 .distinct()
                 .toList();
         List<Map<Long, List<PermDeptVO>>> permDeptsByTDeptIds = iPermDeptCommonService.getPermDeptsByTDeptIds(allDeptIds);

         // 10. 根据租户角色Id 查询 绑定 租户权限信息
         // 10.1 获取用户在各租户下的角色Id（使用租户角色Id）
         Map<Long, Set<Long>> roleIdsByTenant = new HashMap<>();
         for (Long tenantId : validTenantIds) {
             Set<Long> roleIds = new HashSet<>();
             // 通过用户绑定的租户角色关系获取租户角色Id
             List<Map<Long, List<RoleUserVO>>> roleUsersByUserId = iRoleUserCommonService.getRoleUsersByUserIds(tUserIds);
             for (Map<Long, List<RoleUserVO>> roleUserMap : roleUsersByUserId) {
                 for (List<RoleUserVO> roleUsers : roleUserMap.values()) {
                     for (RoleUserVO roleUser : roleUsers) {
                         if (tUserIds.contains(roleUser.getTUserId())) {
                             roleIds.add(roleUser.getTRoleId()); // 使用租户角色Id
                         }
                     }
                 }
             }
             roleIdsByTenant.put(tenantId, roleIds);
         }

         // 10.2 根据角色Id获取角色权限
         List<Long> allRoleIds = roleIdsByTenant.values().stream()
                 .flatMap(Set::stream)
                 .distinct()
                 .toList();
         List<Map<Long, List<PermRoleVO>>> permRolesByTRoleIds = iPermRoleCommonService.getPermRolesByRoleIds(allRoleIds);

         // 10.3 获取角色权限Id
         Set<Long> rolePermIds = permRolesByTRoleIds.stream()
                 .flatMap(map -> map.values().stream())
                 .flatMap(List::stream)
                 .map(PermRoleVO::getTPermId)
                 .collect(Collectors.toSet());

         // 11. 按租户分组组装各级有效权限数据
         // 11.1 系统级有效权限 - 按租户分组（无级联禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> validSysPermByTenant = validTenantIds.stream()
                 .collect(Collectors.toMap(
                         tenantId -> tenantId,
                         tenantId -> validSysPerm.stream()
                                 .collect(Collectors.toList())
                 ));

         // 11.2 租户级有效权限 - 按租户分组（无级联禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> validTenantPermByTenant = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.entrySet().stream())
                 .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                 .collect(Collectors.groupingBy(
                         Map.Entry::getKey,
                         Collectors.mapping(
                                 entry -> entry.getValue().stream()
                                         .filter(permTenant -> validTenantPermSystemIds.contains(permTenant.getId()))
                                         .map(permTenant -> sysPermMap.get(permTenant.getSPermId()))
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toList()),
                                 Collectors.flatMapping(List::stream, Collectors.toList())
                         )
                 ));

         // 11.3 部门级有效权限 - 按租户分组（无级联禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> validDeptPermByTenant = new HashMap<>();
         for (Map.Entry<Long, Set<Long>> entry : deptIdsByTenant.entrySet()) {
             Long tenantId = entry.getKey();
             Set<Long> deptIds = entry.getValue();

             // 获取这些部门的权限
             List<PermDeptVO> deptPerms = permDeptsByTDeptIds.stream()
                     .flatMap(map -> map.values().stream())
                     .flatMap(List::stream)
                     .filter(permDept -> deptIds.contains(permDept.getTDeptId()))
                     .filter(permDept -> permDept.getState() != null && permDept.getState()) // 只取启用的
                     .toList();

             // 获取租户权限ID列表
             List<Long> tenantPermIdsByDept = deptPerms.stream()
                     .map(PermDeptVO::getTPermId)
                     .filter(Objects::nonNull)
                     .distinct()
                     .toList();

             // 通过租户权限ID获取系统权限ID
             // 首先获取租户权限信息
             List<PermTenantVO> tenantPerms = new ArrayList<>();
             if (!tenantPermIdsByDept.isEmpty()) {
                 // 按照已有代码中的方式，通过租户ID获取租户权限信息
                 List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIdsDept = iPermTenantCommonService.getPermTenantsByTenantIds(Arrays.asList(tenantId));
                 tenantPerms = permTenantsByTenantIdsDept.stream()
                         .flatMap(map -> map.values().stream())
                         .flatMap(List::stream)
                         .filter(permTenant -> tenantPermIds.contains(permTenant.getId()))
                         .toList();
             }

             // 建立租户权限ID到系统权限的映射
             Map<Long, Long> tenantPermIdToSysPermId = tenantPerms.stream()
                     .collect(Collectors.toMap(PermTenantVO::getId, PermTenantVO::getSPermId));

             // 获取对应的系统权限
             List<SysPermVO> perms = tenantPerms.stream()
                     .map(permTenant -> sysPermMap.get(permTenant.getSPermId()))
                     .filter(Objects::nonNull)
                     .distinct()
                     .collect(Collectors.toList());

             validDeptPermByTenant.put(tenantId, perms);
         }

         // 11.4 角色级有效权限 - 按租户分组（无级联禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> validRolePermByTenant = new HashMap<>();
         for (Map.Entry<Long, Set<Long>> entry : roleIdsByTenant.entrySet()) {
             Long tenantId = entry.getKey();
             Set<Long> roleIds = entry.getValue();

             // 获取这些角色的权限
             List<PermRoleVO> rolePerms = permRolesByTRoleIds.stream()
                     .flatMap(map -> map.values().stream())
                     .flatMap(List::stream)
                     .filter(permRole -> roleIds.contains(permRole.getTRoleId()))
                     .filter(permRole -> permRole.getState() != null && permRole.getState()) // 只取启用的
                     .toList();

             // 获取租户权限ID列表
             List<Long> tenantPermIdsByRole = rolePerms.stream()
                     .map(PermRoleVO::getTPermId)
                     .filter(Objects::nonNull)
                     .distinct()
                     .toList();

             // 通过租户权限ID获取系统权限ID
             // 首先获取租户权限信息
             List<PermTenantVO> tenantPerms = new ArrayList<>();
             if (!tenantPermIdsByRole.isEmpty()) {
                 // 按照已有代码中的方式，通过租户ID获取租户权限信息
                 List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIdsRole = iPermTenantCommonService.getPermTenantsByTenantIds(Arrays.asList(tenantId));
                 tenantPerms = permTenantsByTenantIdsRole.stream()
                         .flatMap(map -> map.values().stream())
                         .flatMap(List::stream)
                         .filter(permTenant -> tenantPermIds.contains(permTenant.getId()))
                         .toList();
             }

             // 建立租户权限ID到系统权限的映射
             Map<Long, Long> tenantPermIdToSysPermId = tenantPerms.stream()
                     .collect(Collectors.toMap(PermTenantVO::getId, PermTenantVO::getSPermId));

             // 获取对应的系统权限
             List<SysPermVO> perms = tenantPerms.stream()
                     .map(permTenant -> sysPermMap.get(permTenant.getSPermId()))
                     .filter(Objects::nonNull)
                     .distinct()
                     .collect(Collectors.toList());

             validRolePermByTenant.put(tenantId, perms);
         }

         // 11.5 用户级有效权限 - 按租户分组（无级联禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> validUserPermByTenant = new HashMap<>();
         // 遍历所有有效租户
         for (Long tenantId : validTenantIds) {
             // 获取该租户下的用户权限
             List<SysPermVO> perms = new ArrayList<>();
             for (Map<Long, List<PermUserVO>> userPermMap : permUsersByUserId) {
                 for (Map.Entry<Long, List<PermUserVO>> entry : userPermMap.entrySet()) {
                     Long userId = entry.getKey();
                     List<PermUserVO> userPerms = entry.getValue();
                     // 只处理指定用户的权限
                     if (tUserIds.contains(userId)) {
                         for (PermUserVO permUser : userPerms) {
                             // 检查权限是否有效
                             if (validUserPermIds.contains(permUser.getTPermId())
                                     && permUser.getStatus() != null && permUser.getStatus()) {
                                 // 通过租户权限ID获取系统权限
                                 SysPermVO sysPerm = sysPermMap.get(permUser.getTPermId());
                                 if (sysPerm != null) {
                                     perms.add(sysPerm);
                                 }
                             }
                         }
                     }
                 }
             }
             // 去重并添加到结果中
             List<SysPermVO> distinctPerms = perms.stream().distinct().collect(Collectors.toList());
             validUserPermByTenant.put(tenantId, distinctPerms);
         }

         // 12. 按租户分组组装各级禁用权限数据
         // 12.1 系统级禁用权限 - 按租户分组（直接禁用，cascadeDisable = null）
         Map<Long, List<SysPermVO>> disabledSysPermByTenant = validTenantIds.stream()
                 .collect(Collectors.toMap(
                         tenantId -> tenantId,
                         tenantId -> disabledSysPerm.stream()
                                 .collect(Collectors.toList())
                 ));

         // 12.2 租户级禁用权限 - 按租户分组（需要区分直接禁用和级联禁用）
         Map<Long, List<SysPermVO>> disabledTenantPermByTenant = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.entrySet().stream())
                 .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                 .collect(Collectors.groupingBy(
                         Map.Entry::getKey,
                         Collectors.mapping(
                                 entry -> entry.getValue().stream()
                                         .filter(permTenant -> disabledTenantPermTenantIds.contains(permTenant.getId())
                                                 || cascadeDisabledPermBySys.contains(permTenant.getId()))
                                         .map(permTenant -> {
                                             SysPermVO originalPerm = sysPermMap.get(permTenant.getSPermId());

                                             // 判断是否为级联禁用
                                             if (cascadeDisabledPermBySys.contains(permTenant.getId())) {
                                                 // 因系统级权限禁用而级联禁用的租户级权限
                                                 return new SysPermVO(
                                                         originalPerm.getId(),
                                                         originalPerm.getName(),
                                                         originalPerm.getAlias(),
                                                         originalPerm.getDesc(),
                                                         originalPerm.getCreatedAt(),
                                                         originalPerm.getUpdatedAt(),
                                                         originalPerm.getCreatedById(),
                                                         originalPerm.getUpdatedById(),
                                                         originalPerm.getColor(),
                                                         originalPerm.getState(),
                                                         "system", // 设置级联禁用标识为 "system"
                                                         originalPerm.getDeleted()
                                                 );
                                             } else {
                                                 // 直接禁用的租户级权限，cascadeDisable 保持原值（通常为 null）
                                                 return originalPerm;
                                             }
                                         })
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toList()),
                                 Collectors.flatMapping(List::stream, Collectors.toList())
                         )
                 ));

         // 12.3 部门级禁用权限 - 按租户分组（需要区分直接禁用和级联禁用）
         Map<Long, List<SysPermVO>> disabledDeptPermByTenant = new HashMap<>();
         for (Map.Entry<Long, Set<Long>> entry : deptIdsByTenant.entrySet()) {
             Long tenantId = entry.getKey();
             Set<Long> deptIds = entry.getValue();

             // 获取这些部门的权限
             List<PermDeptVO> deptPerms = permDeptsByTDeptIds.stream()
                     .flatMap(map -> map.values().stream())
                     .flatMap(List::stream)
                     .filter(permDept -> deptIds.contains(permDept.getTDeptId()))
                     .filter(permDept -> permDept.getState() == null || !permDept.getState()) // 只取禁用的
                     .toList();

             // 获取租户权限ID列表
             List<Long> tenantPermIdsDept = deptPerms.stream()
                     .map(PermDeptVO::getTPermId)
                     .filter(Objects::nonNull)
                     .distinct()
                     .toList();

             // 通过租户权限ID获取系统权限ID
             // 首先获取租户权限信息
             List<PermTenantVO> tenantPerms = new ArrayList<>();
             if (!tenantPermIdsDept.isEmpty()) {
                 // 按照已有代码中的方式，通过租户ID获取租户权限信息
                 List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIdsByDept = iPermTenantCommonService.getPermTenantsByTenantIds(Arrays.asList(tenantId));
                 tenantPerms = permTenantsByTenantIdsByDept.stream()
                         .flatMap(map -> map.values().stream())
                         .flatMap(List::stream)
                         .filter(permTenant -> tenantPermIds.contains(permTenant.getId()))
                         .toList();
             }

             List<SysPermVO> perms = tenantPerms.stream()
                     .map(permTenant -> {
                         SysPermVO originalPerm = sysPermMap.get(permTenant.getSPermId());
                         if (originalPerm != null) {
                             // 因部门级权限禁用而级联禁用
                             return new SysPermVO(
                                     originalPerm.getId(),
                                     originalPerm.getName(),
                                     originalPerm.getAlias(),
                                     originalPerm.getDesc(),
                                     originalPerm.getCreatedAt(),
                                     originalPerm.getUpdatedAt(),
                                     originalPerm.getCreatedById(),
                                     originalPerm.getUpdatedById(),
                                     originalPerm.getColor(),
                                     originalPerm.getState(),
                                     "dept", // 设置级联禁用标识为 "dept"
                                     originalPerm.getDeleted()
                             );
                         }
                         return null;
                     })
                     .filter(Objects::nonNull)
                     .distinct()
                     .collect(Collectors.toList());

             disabledDeptPermByTenant.put(tenantId, perms);
         }

         // 12.4 角色级禁用权限 - 按租户分组（需要区分直接禁用和级联禁用）
         Map<Long, List<SysPermVO>> disabledRolePermByTenant = new HashMap<>();
         for (Map.Entry<Long, Set<Long>> entry : roleIdsByTenant.entrySet()) {
             Long tenantId = entry.getKey();
             Set<Long> roleIds = entry.getValue();

             // 获取这些角色的权限
             List<PermRoleVO> rolePerms = permRolesByTRoleIds.stream()
                     .flatMap(map -> map.values().stream())
                     .flatMap(List::stream)
                     .filter(permRole -> roleIds.contains(permRole.getTRoleId()))
                     .filter(permRole -> permRole.getState() == null || !permRole.getState()) // 只取禁用的
                     .toList();

             // 获取租户权限ID列表
             List<Long> tenantPermIdsRole = rolePerms.stream()
                     .map(PermRoleVO::getTPermId)
                     .filter(Objects::nonNull)
                     .distinct()
                     .toList();

             // 通过租户权限ID获取系统权限ID
             // 首先获取租户权限信息
             List<PermTenantVO> tenantPerms = new ArrayList<>();
             if (!tenantPermIdsRole.isEmpty()) {
                 // 按照已有代码中的方式，通过租户ID获取租户权限信息
                 List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIdsByRole = iPermTenantCommonService.getPermTenantsByTenantIds(Arrays.asList(tenantId));
                 tenantPerms = permTenantsByTenantIdsByRole.stream()
                         .flatMap(map -> map.values().stream())
                         .flatMap(List::stream)
                         .filter(permTenant -> tenantPermIds.contains(permTenant.getId()))
                         .toList();
             }

             List<SysPermVO> perms = tenantPerms.stream()
                     .map(permTenant -> {
                         SysPermVO originalPerm = sysPermMap.get(permTenant.getSPermId());
                         if (originalPerm != null) {
                             // 因角色级权限禁用而级联禁用
                             return new SysPermVO(
                                     originalPerm.getId(),
                                     originalPerm.getName(),
                                     originalPerm.getAlias(),
                                     originalPerm.getDesc(),
                                     originalPerm.getCreatedAt(),
                                     originalPerm.getUpdatedAt(),
                                     originalPerm.getCreatedById(),
                                     originalPerm.getUpdatedById(),
                                     originalPerm.getColor(),
                                     originalPerm.getState(),
                                     "role", // 设置级联禁用标识为 "role"
                                     originalPerm.getDeleted()
                             );
                         }
                         return null;
                     })
                     .filter(Objects::nonNull)
                     .distinct()
                     .collect(Collectors.toList());

             disabledRolePermByTenant.put(tenantId, perms);
         }

         // 12.5 用户级禁用权限 - 按租户分组（需要区分直接禁用和级联禁用）
         Map<Long, List<SysPermVO>> disabledUserPermByTenant = new HashMap<>();
         // 遍历所有有效租户
         for (Long tenantId : validTenantIds) {
             // 获取该租户下的用户权限
             List<SysPermVO> perms = new ArrayList<>();
             for (Map<Long, List<PermUserVO>> userPermMap : permUsersByUserId) {
                 for (Map.Entry<Long, List<PermUserVO>> entry : userPermMap.entrySet()) {
                     Long userId = entry.getKey();
                     List<PermUserVO> userPerms = entry.getValue();
                     // 只处理指定用户的权限
                     if (tUserIds.contains(userId)) {
                         for (PermUserVO permUser : userPerms) {
                             // 检查权限是否禁用
                             if (disabledUserPermIds.contains(permUser.getTPermId())
                                     || cascadeDisabledPermByTenant.contains(permUser.getTPermId())) {
                                 // 通过租户权限ID获取系统权限
                                 SysPermVO originalPerm = sysPermMap.get(permUser.getTPermId());
                                 if (originalPerm != null) {
                                     // 判断级联禁用的类型
                                     if (cascadeDisabledPermBySys.contains(permUser.getTPermId())) {
                                         // 因系统级权限禁用而级联禁用
                                         perms.add(new SysPermVO(
                                                 originalPerm.getId(),
                                                 originalPerm.getName(),
                                                 originalPerm.getAlias(),
                                                 originalPerm.getDesc(),
                                                 originalPerm.getCreatedAt(),
                                                 originalPerm.getUpdatedAt(),
                                                 originalPerm.getCreatedById(),
                                                 originalPerm.getUpdatedById(),
                                                 originalPerm.getColor(),
                                                 originalPerm.getState(),
                                                 "system", // 因系统级禁用而级联
                                                 originalPerm.getDeleted()
                                         ));
                                     } else if (disabledTenantPermTenantIds.contains(permUser.getTPermId())) {
                                         // 因租户级权限禁用而级联禁用
                                         perms.add(new SysPermVO(
                                                 originalPerm.getId(),
                                                 originalPerm.getName(),
                                                 originalPerm.getAlias(),
                                                 originalPerm.getDesc(),
                                                 originalPerm.getCreatedAt(),
                                                 originalPerm.getUpdatedAt(),
                                                 originalPerm.getCreatedById(),
                                                 originalPerm.getUpdatedById(),
                                                 originalPerm.getColor(),
                                                 originalPerm.getState(),
                                                 "tenant", // 因租户级禁用而级联
                                                 originalPerm.getDeleted()
                                         ));
                                     } else {
                                         // 直接禁用的用户级权限，cascadeDisable 保持原值（通常为 null）
                                         perms.add(originalPerm);
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
             // 去重并添加到结果中
             List<SysPermVO> distinctPerms = perms.stream().distinct().collect(Collectors.toList());
             disabledUserPermByTenant.put(tenantId, distinctPerms);
         }

         // 12.6 获取用户所绑定的禁用租户的权限，并将这些权限信息存储到disabledPermByTenant
         Map<Long, List<SysPermVO>> disabledPermByTenant = permTenantsByTenantIds.stream()
                 .flatMap(map -> map.entrySet().stream())
                 .filter(entry -> disabledTenantIds.contains(entry.getKey())) // 只处理禁用租户
                 .collect(Collectors.groupingBy(
                         Map.Entry::getKey,
                         Collectors.mapping(
                                 entry -> entry.getValue().stream()
                                         .map(permTenant -> {
                                             SysPermVO originalPerm = sysPermMap.get(permTenant.getSPermId());
                                             // 设置级联禁用标识为 "tenant"
                                             if (originalPerm != null) {
                                                 return new SysPermVO(
                                                         originalPerm.getId(),
                                                         originalPerm.getName(),
                                                         originalPerm.getAlias(),
                                                         originalPerm.getDesc(),
                                                         originalPerm.getCreatedAt(),
                                                         originalPerm.getUpdatedAt(),
                                                         originalPerm.getCreatedById(),
                                                         originalPerm.getUpdatedById(),
                                                         originalPerm.getColor(),
                                                         originalPerm.getState(),
                                                         "tenant", // 因租户禁用而级联
                                                         originalPerm.getDeleted()
                                                 );
                                             }
                                             return null;
                                         })
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toList()),
                                 Collectors.flatMapping(List::stream, Collectors.toList())
                         )
                 ));

         // 13. 获取用户在所有有效租户中的有效权限，组装为全局有效权限
         Map<Long, List<SysPermVO>> validGlobalPerm = validTenantIds.stream()
                 .collect(Collectors.toMap(
                         tenantId -> tenantId,
                         tenantId -> {
                             // 合并系统级、租户级、部门级、角色级和用户级的有效权限
                             List<SysPermVO> sysPerms = validSysPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> tenantPerms = validTenantPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> deptPerms = validDeptPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> rolePerms = validRolePermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> userPerms = validUserPermByTenant.getOrDefault(tenantId, Collections.emptyList());

                             // 合并所有有效权限并去重
                             return Stream.of(sysPerms, tenantPerms, deptPerms, rolePerms, userPerms)
                                     .flatMap(List::stream)
                                     .distinct()
                                     .collect(Collectors.toList());
                         }
                 ));

         // 14. 获取用户在所有有效租户中的禁用权限，组装为全局禁用权限
         Map<Long, List<SysPermVO>> disabledGlobalPerm = validTenantIds.stream()
                 .collect(Collectors.toMap(
                         tenantId -> tenantId,
                         tenantId -> {
                             // 合并系统级、租户级、部门级、角色级、用户级和因租户禁用而级联禁用的权限
                             List<SysPermVO> sysPerms = disabledSysPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> tenantPerms = disabledTenantPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> deptPerms = disabledDeptPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> rolePerms = disabledRolePermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> userPerms = disabledUserPermByTenant.getOrDefault(tenantId, Collections.emptyList());
                             List<SysPermVO> tenantCascadePerms = disabledPermByTenant.getOrDefault(tenantId, Collections.emptyList());

                             // 合并所有禁用权限并去重
                             return Stream.of(sysPerms, tenantPerms, deptPerms, rolePerms, userPerms, tenantCascadePerms)
                                     .flatMap(List::stream)
                                     .distinct()
                                     .collect(Collectors.toList());
                         }
                 ));

         // 15. 确保每个租户都有对应的条目
         allTenantIds.forEach(tenantId -> {
             // 只有有效租户才需要在前几个字段中有条目
             if (validTenantIds.contains(tenantId)) {
                 validSysPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 validTenantPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 validDeptPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 validRolePermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 validUserPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 disabledSysPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 disabledTenantPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 disabledDeptPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 disabledRolePermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 disabledUserPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
                 validGlobalPerm.putIfAbsent(tenantId, Collections.emptyList());
                 disabledGlobalPerm.putIfAbsent(tenantId, Collections.emptyList());
             }
             // 所有租户（包括禁用租户）在disabledPermByTenant中都需要有条目
             disabledPermByTenant.putIfAbsent(tenantId, Collections.emptyList());
         });

         // 16. 组装并返回结果
         return new CheckPermVO(
                 validSysPermByTenant,
                 validTenantPermByTenant,
                 validDeptPermByTenant,
                 validRolePermByTenant,
                 validUserPermByTenant,
                 disabledSysPermByTenant,
                 disabledTenantPermByTenant,
                 disabledDeptPermByTenant,
                 disabledRolePermByTenant,
                 disabledUserPermByTenant,
                 disabledPermByTenant,
                 disabledDeptPermByTenant, // disabledPermByDept
                 disabledRolePermByTenant, // disabledPermByRole
                 validGlobalPerm,
                 disabledGlobalPerm
         );
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
     * @param tUserIds 租户用户ID 列表
     * @param sTenantIds 租户ID 列表
     * @return 部门认证结果
     */
    public CheckDeptVO checkDept(List<Long> tUserIds, List<Long> sTenantIds) {
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
        List<Map<Long, List<DeptUserVO>>> deptIdsByUserId = iDeptUserCommonService.getDeptUsersByUserIds(tUserIds);

        // 2.2 排除 其余租户部门信息 只保留 用户 绑定的部门信息（无论有效或禁用的租户）
        // 首先提取用户绑定的所有部门ID
        Set<Long> userDeptIds = deptIdsByUserId.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(DeptUserVO::getTDeptId)
                .collect(Collectors.toSet());

        deptTenantsBySTenantIds = deptTenantsBySTenantIds.stream()
                .map(tenantDeptMap -> tenantDeptMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> userDeptIds.contains(deptTenant.getId()))
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
        List<Map<Long, List<DeptUserVO>>> deptUsersByUserId = iDeptUserCommonService.getDeptUsersByUserIds(tUserIds);

        // 5.1 获取 禁用的 用户租户部门Id [ 禁用部门 用户级 ]
        List<Long> disabledUserDeptIds = deptUsersByUserId.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
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
                                        .filter(deptTenant -> userDeptIds.contains(deptTenant.getId()))
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

        // 9. 获取用户在所有有效租户中的有效部门，组装为全局有效部门
        Map<Long, List<SysDeptVO>> validGlobalDept = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> {
                            // 合并系统级、租户级和用户级的有效部门
                            List<SysDeptVO> sysDepts = validSysDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> tenantDepts = validTenantDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> userDepts = validUserDeptByTenant.getOrDefault(tenantId, Collections.emptyList());

                            // 合并所有有效部门并去重
                            return Stream.of(sysDepts, tenantDepts, userDepts)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .collect(Collectors.toList());
                        }
                ));

        // 10. 获取用户在所有有效租户中的禁用部门，组装为全局禁用部门
        Map<Long, List<SysDeptVO>> disabledGlobalDept = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> {
                            // 合并系统级、租户级、用户级和因租户禁用而级联禁用的部门
                            List<SysDeptVO> sysDepts = disabledSysDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> tenantDepts = disabledTenantDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> userDepts = disabledUserDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> tenantCascadeDepts = disabledDeptByTenant.getOrDefault(tenantId, Collections.emptyList());

                            // 合并所有禁用部门并去重
                            return Stream.of(sysDepts, tenantDepts, userDepts, tenantCascadeDepts)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .collect(Collectors.toList());
                        }
                ));

        // 11. 确保每个租户都有对应的条目（有效租户在前7个字段中，所有租户在disabledDeptByTenant中）
        allTenantIds.forEach(tenantId -> {
            // 只有有效租户才需要在前7个字段中有条目
            if (validTenantIds.contains(tenantId)) {
                validSysDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validTenantDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validUserDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledSysDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledTenantDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledUserDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validGlobalDept.putIfAbsent(tenantId, Collections.emptyList());
                disabledGlobalDept.putIfAbsent(tenantId, Collections.emptyList());
            }
            // 所有租户（包括禁用租户）在disabledDeptByTenant中都需要有条目
            disabledDeptByTenant.putIfAbsent(tenantId, Collections.emptyList());
        });

        // 12. 组装并返回结果
        return new CheckDeptVO(
                validSysDeptByTenant,
                validTenantDeptByTenant,
                validUserDeptByTenant,
                disabledSysDeptByTenant,
                disabledTenantDeptByTenant,
                disabledUserDeptByTenant,
                disabledDeptByTenant,
                validGlobalDept,
                disabledGlobalDept
        );
    }

    /**
     * 角色认证
     * @param tUserIds 租户用户ID 列表
     * @param sTenantIds 有效租户ID 列表
     * @return 角色认证结果
     */
    public CheckRoleVO checkRole(List<Long> tUserIds, List<Long> sTenantIds) {
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

        // 2. 根据 所有租户Id列表（有效+禁用） 查询 租户角色信息
        List<Long> allTenantIds = Stream.concat(validTenantIds.stream(), disabledTenantIds.stream())
                .distinct()
                .toList();
        List<Map<Long, List<RoleTenantVO>>> roleTenantsBySTenantIds = iRoleTenantCommonService.getRoleTenantsByTenantIds(allTenantIds);

        // 2.1 根据租户用户Id 查询 绑定 租户角色Id
        List<Map<Long, List<RoleUserVO>>> roleIdsByUserId = iRoleUserCommonService.getRoleUsersByUserIds(tUserIds);

        // 2.2 排除 其余租户角色信息 只保留 用户 绑定的角色信息（无论有效或禁用的租户）
        // 首先提取用户绑定的所有角色ID
        Set<Long> userRoleIds = roleIdsByUserId.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(RoleUserVO::getTRoleId)
                .collect(Collectors.toSet());

        roleTenantsBySTenantIds = roleTenantsBySTenantIds.stream()
                .map(tenantRoleMap -> tenantRoleMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> userRoleIds.contains(roleTenant.getId()))
                                        .collect(Collectors.toList())
                        )))
                .filter(map -> !map.isEmpty() || map.values().stream().anyMatch(list -> !list.isEmpty()))
                .toList();

        // 3. 获取 所有系统角色Id
        List<Long> sysRoleIds = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(RoleTenantVO::getSRoleId)
                .distinct()
                .toList();

        // 3.1 根据 系统角色Id 获取 系统角色信息 Map<系统角色ID, 角色VO>
        Map<Long, SysRoleVO> sysRolesByIds = iSysRoleCommonService.getSysRolesByIds(sysRoleIds);

        // 3.2 获取 禁用系统角色Id [ 禁用角色 系统级 ]
        List<SysRoleVO> disabledSysRole = sysRolesByIds.values().stream()
                .filter(role -> role.getState() == null || !role.getState())
                .toList();

        // 3.3 获取 有效系统角色 [ 有效角色 系统级 ]
        List<SysRoleVO> validSysRole = sysRolesByIds.values().stream()
                .filter(role -> role.getState() != null && role.getState())
                .toList();

        // 3.4 建立 系统角色Id->租户角色信息 键值对（包含租户ID信息）
        Map<Long, List<RoleTenantVO>> sysRoleIdToTenantRoleVOs = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(RoleTenantVO::getSRoleId));

        // 3.5 获取 因系统角色禁用 级联禁用的对应的租户角色Id
        List<Long> cascadeDisabledRoleBySys = disabledSysRole.stream()
                .map(SysRoleVO::getId)
                .flatMap(sysRoleId -> sysRoleIdToTenantRoleVOs.getOrDefault(sysRoleId, Collections.emptyList()).stream())
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 4. 获取 所有租户角色Id
        List<Long> tenantRoleIds = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 4.1 获取 禁用租户角色Id [ 禁用角色 租户级 ]
        List<Long> disabledTenantRoleTenantIds = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .filter(roleTenant -> roleTenant.getState() == null || !roleTenant.getState())
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 4.2 获取 有效租户角色Id [ 有效角色 租户级 ] = tenantRoleIds - disabledTenantRoleTenantIds - cascadeDisabledRoleBySys
        List<Long> validTenantRoleSystemIds = tenantRoleIds.stream()
                .filter(tenantRoleId -> !disabledTenantRoleTenantIds.contains(tenantRoleId))
                .filter(tenantRoleId -> !cascadeDisabledRoleBySys.contains(tenantRoleId))
                .toList();

        // 4.3 获取 因租户角色禁用 级联禁用的对应的租户角色Id = disabledTenantRoleTenantIds + cascadeDisabledRoleBySys
        List<Long> cascadeDisabledRoleByTenant = Stream.concat(
                disabledTenantRoleTenantIds.stream(),
                cascadeDisabledRoleBySys.stream()
        ).distinct().toList();

        // 5. 根据 租户用户Id 查询 绑定 租户角色信息
        List<Map<Long, List<RoleUserVO>>> roleUsersByUserId = iRoleUserCommonService.getRoleUsersByUserIds(tUserIds);

        // 5.1 获取 禁用的 用户租户角色Id [ 禁用角色 用户级 ]
        List<Long> disabledUserRoleIds = roleUsersByUserId.stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .filter(roleUser -> roleUser.getState() == null || !roleUser.getState())
                .map(RoleUserVO::getTRoleId)
                .toList();

        // 5.2 获取 有效角色Id [ 有效角色 用户级 ] = tenantRoleIds - disabledUserRoleIds - cascadeDisabledRoleByTenant
        List<Long> validUserRoleIds = tenantRoleIds.stream()
                .filter(tenantRoleId -> !disabledUserRoleIds.contains(tenantRoleId))
                .filter(tenantRoleId -> !cascadeDisabledRoleByTenant.contains(tenantRoleId))
                .toList();

        // 6. 建立租户ID到系统角色的映射关系（只包含有效租户）
        Map<Long, List<SysRoleVO>> tenantIdToSysRoleVOs = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
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

        // 7. 按租户分组组装各级有效角色数据时，需要设置 cascadeDisable 字段（只包含有效租户）
        // 7.1 系统级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> validSysRoleByTenant = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> validSysRole.stream()
                                .filter(role -> tenantIdToSysRoleVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysRole -> sysRole.getId().equals(role.getId())))
                                // 系统级角色不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                .collect(Collectors.toList())
                ));

        // 7.2 租户级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysRoleVO>> validTenantRoleByTenant = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validTenantRoleSystemIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            // 租户级有效角色不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                            return originalRole;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7.3 用户级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysRoleVO>> validUserRoleByTenant = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validUserRoleIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            // 用户级有效角色不存在级联禁用，cascadeDisable 保持原值（通常为 null）
                                            return originalRole;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8. 按租户分组组装各级禁用角色数据时，需要设置 cascadeDisable 字段（只包含有效租户）
        // 8.1 系统级禁用角色 - 按租户分组（直接禁用，cascadeDisable = null）
        Map<Long, List<SysRoleVO>> disabledSysRoleByTenant = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> disabledSysRole.stream()
                                .filter(role -> tenantIdToSysRoleVOs.getOrDefault(tenantId, Collections.emptyList())
                                        .stream().anyMatch(sysRole -> sysRole.getId().equals(role.getId())))
                                // 系统级禁用角色是直接禁用，不是级联禁用，cascadeDisable 保持原值（通常为 null）
                                .collect(Collectors.toList())
                ));

        // 8.2 租户级禁用角色 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysRoleVO>> disabledTenantRoleByTenant = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
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

        // 8.3 用户级禁用角色 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysRoleVO>> disabledUserRoleByTenant = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
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

        // 8.4 获取用户所绑定的禁用租户的角色，并将这些角色信息存储到disabledRoleByTenant
        Map<Long, List<SysRoleVO>> disabledRoleByTenant = roleTenantsBySTenantIds.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> disabledTenantIds.contains(entry.getKey())) // 只处理禁用租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        // 只包含用户绑定的角色
                                        .filter(roleTenant -> userRoleIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            // 设置级联禁用标识为 "tenant"
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
                                                    "tenant", // 因租户禁用而级联
                                                    originalRole.getDeleted()
                                            );
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 9. 获取用户在所有有效租户中的有效角色，组装为全局有效角色
        Map<Long, List<SysRoleVO>> validGlobalRole = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> {
                            // 合并系统级、租户级和用户级的有效角色
                            List<SysRoleVO> sysRoles = validSysRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> tenantRoles = validTenantRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> userRoles = validUserRoleByTenant.getOrDefault(tenantId, Collections.emptyList());

                            // 合并所有有效角色并去重
                            return Stream.of(sysRoles, tenantRoles, userRoles)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .collect(Collectors.toList());
                        }
                ));

        // 10. 获取用户在所有有效租户中的禁用角色，组装为全局禁用角色
        Map<Long, List<SysRoleVO>> disabledGlobalRole = validTenantIds.stream()
                .collect(Collectors.toMap(
                        tenantId -> tenantId,
                        tenantId -> {
                            // 合并系统级、租户级、用户级和因租户禁用而级联禁用的角色
                            List<SysRoleVO> sysRoles = disabledSysRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> tenantRoles = disabledTenantRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> userRoles = disabledUserRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> tenantCascadeRoles = disabledRoleByTenant.getOrDefault(tenantId, Collections.emptyList());

                            // 合并所有禁用角色并基于角色ID去重
                            return Stream.of(sysRoles, tenantRoles, userRoles, tenantCascadeRoles)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toMap(
                                            SysRoleVO::getId, // 使用角色ID作为键
                                            role -> role,     // 使用角色对象作为值
                                            (existing, replacement) -> existing // 如果ID重复，保留第一个
                                    ))
                                    .values()
                                    .stream()
                                    .collect(Collectors.toList());
                        }
                ));

        // 11. 确保每个租户都有对应的条目（有效租户在前7个字段中，所有租户在disabledRoleByTenant中）
        allTenantIds.forEach(tenantId -> {
            // 只有有效租户才需要在前7个字段中有条目
            if (validTenantIds.contains(tenantId)) {
                validSysRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validTenantRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validUserRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledSysRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledTenantRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                disabledUserRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
                validGlobalRole.putIfAbsent(tenantId, Collections.emptyList());
                disabledGlobalRole.putIfAbsent(tenantId, Collections.emptyList());
            }
            // 所有租户（包括禁用租户）在disabledRoleByTenant中都需要有条目
            disabledRoleByTenant.putIfAbsent(tenantId, Collections.emptyList());
        });

        // 12. 组装并返回结果
        return new CheckRoleVO(
                validSysRoleByTenant,
                validTenantRoleByTenant,
                validUserRoleByTenant,
                disabledSysRoleByTenant,
                disabledTenantRoleByTenant,
                disabledUserRoleByTenant,
                disabledRoleByTenant,
                validGlobalRole,
                disabledGlobalRole
        );
    }

}
