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
     * @param tUserIds 租户用户Id 列表
     * @param sTenantIds 有效租户Id 列表
     * @return 权限认证结果
     */
     public CheckPermVO checkPerm(List<Long> tUserIds, List<Long> sTenantIds) {
         // 1. 调用租户认证
         CheckTenantVO checkTenantVO = checkTenant(sTenantIds);

         // 1.1 获取 禁用租户Id
         List<Long> disabledTenantIds = checkTenantVO.getDisabledSysTenant().stream()
                 .map(SysTenantVO::getId)
                 .toList();

         // 1.2 获取 有效租户Id
         List<Long> validTenantIds = checkTenantVO.getValidSysTenant().stream()
                 .map(SysTenantVO::getId)
                 .toList();

         // 2. 调用部门认证
         CheckDeptVO checkDeptVO = checkDept(tUserIds, sTenantIds);

         // 2.1 获取 禁用部门Id
         List<Long> disabledDeptIds = checkDeptVO.getDisabledGlobalDept().values().stream()
                 .flatMap(List::stream)
                 .map(SysDeptVO::getId)
                 .toList();

         // 2.2 获取 有效部门Id
         List<Long> validDeptIds = checkDeptVO.getValidGlobalDept().values().stream()
                 .flatMap(List::stream)
                 .map(SysDeptVO::getId)
                 .toList();

         // 3. 调用角色认证
         CheckRoleVO checkRoleVO = checkRole(tUserIds, sTenantIds);

         // 3.1 获取 禁用角色Id
         List<Long> disabledRoleIds = checkRoleVO.getDisabledGlobalRole().values().stream()
                 .flatMap(List::stream)
                 .map(SysRoleVO::getId)
                 .toList();

         // 3.2 获取 有效角色Id
         List<Long> validRoleIds = checkRoleVO.getValidGlobalRole().values().stream()
                 .flatMap(List::stream)
                 .map(SysRoleVO::getId)
                 .toList();

         // 4. 调用实体认证
         CheckPermVO checkPermVOByEntity = checkPermByEntity(disabledTenantIds, disabledDeptIds, disabledRoleIds);

         // 4.1 获取 因禁用租户 禁用权限Id
         List<Long> disabledPermIdsByTenant = checkPermVOByEntity.getDisabledPermByTenant().values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .toList();

         // 4.2 获取 因禁用部门 禁用权限Id
         List<Long> disabledPermIdsByDept = checkPermVOByEntity.getDisabledPermByDept().values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .toList();

         // 4.3 获取 因禁用角色 禁用权限Id
         List<Long> disabledPermIdsByRole = checkPermVOByEntity.getDisabledPermByRole().values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .toList();

         // 5. 调用层级认证
         CheckPermVO checkPermVOByLevel = checkPermByLevel(tUserIds, validTenantIds, validDeptIds, validRoleIds);

         // 5.1 获取 有效权限信息 [ 系统级 ]
         Map<Long, List<SysPermVO>> validSysPerms = checkPermVOByLevel.getValidSysPerm();

         // 5.2 获取 禁用权限信息 [ 系统级 ]
         Map<Long, List<SysPermVO>> disabledSysPerms = checkPermVOByLevel.getDisabledSysPerm();

         // 5.3 获取 有效权限信息 [ 租户级 ]
         Map<Long, List<SysPermVO>> validTenantPerms = checkPermVOByLevel.getValidTenantPerm();

         // 5.4 获取 禁用权限信息 [ 租户级 ]
         Map<Long, List<SysPermVO>> disabledTenantPerms = checkPermVOByLevel.getDisabledTenantPerm();

         // 5.5 获取 有效权限信息 [ 部门级 ]
         Map<Long, List<SysPermVO>> validDeptPerms = checkPermVOByLevel.getValidDeptPerm();

         // 5.6 获取 禁用权限信息 [ 部门级 ]
         Map<Long, List<SysPermVO>> disabledDeptPerms = checkPermVOByLevel.getDisabledDeptPerm();

         // 5.7 获取 有效权限信息 [ 角色级 ]
         Map<Long, List<SysPermVO>> validRolePerms = checkPermVOByLevel.getValidRolePerm();

         // 5.8 获取 禁用权限信息 [ 角色级 ]
         Map<Long, List<SysPermVO>> disabledRolePerms = checkPermVOByLevel.getDisabledRolePerm();

         // 5.9 获取 有效权限信息 [ 用户级 ]
         Map<Long, List<SysPermVO>> validUserPerms = checkPermVOByLevel.getValidUserPerm();

         // 5.10 获取 禁用权限信息 [ 用户级 ]
         Map<Long, List<SysPermVO>> disabledUserPerms = checkPermVOByLevel.getDisabledUserPerm();

         // 6. 计算全局禁用权限
         // 合并所有层级的禁用权限（已经经过级联禁用处理）
         Map<Long, List<SysPermVO>> globalDisabledPerms = new HashMap<>();

         // 合并系统级禁用权限
         disabledSysPerms.forEach((tenantId, perms) ->
                 globalDisabledPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并租户级禁用权限
         disabledTenantPerms.forEach((tenantId, perms) ->
                 globalDisabledPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并部门级禁用权限
         disabledDeptPerms.forEach((tenantId, perms) ->
                 globalDisabledPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并角色级禁用权限
         disabledRolePerms.forEach((tenantId, perms) ->
                 globalDisabledPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并用户级禁用权限 - 这里需要转换用户ID为租户ID
         // 首先获取用户到租户的映射关系
         Map<Long, Long> userToTenantMap = new HashMap<>();
         // 由于我们没有直接的用户到租户映射，我们需要通过用户部门关系来推断
         // 这里简化处理，假设所有用户都属于有效租户
         for (Long userId : tUserIds) {
             for (Long tenantId : validTenantIds) {
                 userToTenantMap.put(userId, tenantId);
                 break; // 简化处理，假设一个用户只属于一个租户
             }
         }

         disabledUserPerms.forEach((userId, perms) -> {
             Long tenantId = userToTenantMap.get(userId);
             if (tenantId != null) {
                 globalDisabledPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 });
             }
         });

         // 去重处理：确保每个租户下的权限ID唯一
         globalDisabledPerms.replaceAll((tenantId, perms) -> {
             Map<Long, SysPermVO> uniquePerms = new HashMap<>();
             perms.forEach(perm -> uniquePerms.put(perm.getId(), perm));
             return new ArrayList<>(uniquePerms.values());
         });

         // 7. 获取全局有效权限
         // 合并所有层级的有效权限（已经经过级联禁用处理）
         Map<Long, List<SysPermVO>> globalValidPerms = new HashMap<>();

         // 合并系统级有效权限
         validSysPerms.forEach((tenantId, perms) ->
                 globalValidPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并租户级有效权限
         validTenantPerms.forEach((tenantId, perms) ->
                 globalValidPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并部门级有效权限
         validDeptPerms.forEach((tenantId, perms) ->
                 globalValidPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并角色级有效权限
         validRolePerms.forEach((tenantId, perms) ->
                 globalValidPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 })
         );

         // 合并用户级有效权限 - 这里需要转换用户ID为租户ID
         validUserPerms.forEach((userId, perms) -> {
             Long tenantId = userToTenantMap.get(userId);
             if (tenantId != null) {
                 globalValidPerms.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     return merged;
                 });
             }
         });

         // 去重处理：确保每个租户下的权限ID唯一
         globalValidPerms.replaceAll((tenantId, perms) -> {
             Map<Long, SysPermVO> uniquePerms = new HashMap<>();
             perms.forEach(perm -> uniquePerms.put(perm.getId(), perm));
             return new ArrayList<>(uniquePerms.values());
         });

         return new CheckPermVO(
                 validSysPerms,
                 validTenantPerms,
                 validDeptPerms,
                 validRolePerms,
                 validUserPerms,
                 disabledSysPerms,
                 disabledTenantPerms,
                 disabledDeptPerms,
                 disabledRolePerms,
                 disabledUserPerms,
                 checkPermVOByEntity.getDisabledPermByTenant(),
                 checkPermVOByEntity.getDisabledPermByDept(),
                 checkPermVOByEntity.getDisabledPermByRole(),
                 globalValidPerms,
                 globalDisabledPerms,
                 checkTenantVO,
                 checkDeptVO,
                 checkRoleVO
         );
     }

     /**
     * 权限实体认证
     * @param disabledSysTenantIds 禁用租户ID 列表
     * @param disabledDeptIds 禁用部门ID 列表
     * @param disabledRoleIds 禁用角色ID 列表
     * @return 实体认证结果
     */
     private CheckPermVO checkPermByEntity(List<Long> disabledSysTenantIds, List<Long> disabledDeptIds, List<Long> disabledRoleIds) {
         // 1. 获取因租户禁用而级联禁用的权限
         Map<Long, List<SysPermVO>> disabledPermByTenant = new HashMap<>();

         if (disabledSysTenantIds != null && !disabledSysTenantIds.isEmpty()) {
             // 1.1 获取禁用租户关联的权限ID
             Map<Long, Set<Long>> permIdsByTenantIds = iPermTenantCommonService.getPermIdsByTenantIds(disabledSysTenantIds);

             // 1.2 获取所有涉及的权限ID
             Set<Long> allPermIds = permIdsByTenantIds.values().stream()
                     .flatMap(Set::stream)
                     .collect(Collectors.toSet());

             if (!allPermIds.isEmpty()) {
                 // 1.3 查询权限详细信息
                 List<SysPermVO> permVOs = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allPermIds));

                 // 1.4 按租户ID分组并设置级联禁用标识
                 for (Map.Entry<Long, Set<Long>> entry : permIdsByTenantIds.entrySet()) {
                     Long tenantId = entry.getKey();
                     Set<Long> permIds = entry.getValue();

                     List<SysPermVO> tenantDisabledPerms = permVOs.stream()
                             .filter(perm -> permIds.contains(perm.getId()))
                             .map(perm -> new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     null,
                                     perm.getDeleted()
                             ))
                             .collect(Collectors.toList());

                     disabledPermByTenant.put(tenantId, tenantDisabledPerms);
                 }
             }
         }

         // 2. 获取因部门禁用而级联禁用的权限
         Map<Long, List<SysPermVO>> disabledPermByDept = new HashMap<>();

         if (disabledDeptIds != null && !disabledDeptIds.isEmpty()) {
             // 2.1 获取禁用部门关联的权限ID
             Map<Long, Set<Long>> permIdsByDeptIds = iPermDeptCommonService.getTPermIdsByTDeptIds(disabledDeptIds);

             // 2.2 获取所有涉及的权限ID
             Set<Long> allPermIds = permIdsByDeptIds.values().stream()
                     .flatMap(Set::stream)
                     .collect(Collectors.toSet());

             if (!allPermIds.isEmpty()) {
                 // 2.3 查询权限详细信息
                 List<SysPermVO> permVOs = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allPermIds));

                 // 2.4 按部门ID分组并设置级联禁用标识
                 for (Map.Entry<Long, Set<Long>> entry : permIdsByDeptIds.entrySet()) {
                     Long deptId = entry.getKey();
                     Set<Long> permIds = entry.getValue();

                     List<SysPermVO> deptDisabledPerms = permVOs.stream()
                             .filter(perm -> permIds.contains(perm.getId()))
                             .map(perm -> new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     null, // 设置级联禁用标识为"dept"
                                     perm.getDeleted()
                             ))
                             .collect(Collectors.toList());

                     disabledPermByDept.put(deptId, deptDisabledPerms);
                 }
             }
         }

         // 3. 获取因角色禁用而级联禁用的权限
         Map<Long, List<SysPermVO>> disabledPermByRole = new HashMap<>();

         if (disabledRoleIds != null && !disabledRoleIds.isEmpty()) {
             // 3.1 获取禁用角色关联的权限ID
             Map<Long, Set<Long>> permIdsByRoleIds = iPermRoleCommonService.getPermIdsByRoleIds(disabledRoleIds);

             // 3.2 获取所有涉及的权限ID
             Set<Long> allPermIds = permIdsByRoleIds.values().stream()
                     .flatMap(Set::stream)
                     .collect(Collectors.toSet());

             if (!allPermIds.isEmpty()) {
                 // 3.3 查询权限详细信息
                 List<SysPermVO> permVOs = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allPermIds));

                 // 3.4 按角色ID分组并设置级联禁用标识
                 for (Map.Entry<Long, Set<Long>> entry : permIdsByRoleIds.entrySet()) {
                     Long roleId = entry.getKey();
                     Set<Long> permIds = entry.getValue();

                     List<SysPermVO> roleDisabledPerms = permVOs.stream()
                             .filter(perm -> permIds.contains(perm.getId()))
                             .map(perm -> new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     null, // 设置级联禁用标识为"role"
                                     perm.getDeleted()
                             ))
                             .collect(Collectors.toList());

                     disabledPermByRole.put(roleId, roleDisabledPerms);
                 }
             }
         }

         // 4. 构建并返回结果
         return new CheckPermVO(
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 disabledPermByTenant,
                 disabledPermByDept,
                 disabledPermByRole,
                 null,
                 null,
                 null,
                 null,
                 null
         );
     }

     /**
      * 权限层级认证
      * @param tUserIds 租户用户Id列表
      * @param validSysTenantIds 有效租户Id
      * @param validGlobalDeptIds 有效部门Id
      * @param validGlobalRoleIds 有效角色Id
      * @return 权限认证结果
      */
     private CheckPermVO checkPermByLevel(List<Long> tUserIds, List<Long> validSysTenantIds, List<Long> validGlobalDeptIds, List<Long> validGlobalRoleIds) {
         // 1. 获取系统级权限信息
         List<SysPermVO> allSysPerms = iSysPermCommonService.getSysPerms();

         // 区分系统级有效和禁用权限
         List<SysPermVO> sysValidPerms = allSysPerms.stream()
                 .filter(perm -> perm.getState() != null && perm.getState() && !perm.getDeleted())
                 .collect(Collectors.toList());

         List<SysPermVO> sysDisabledPerms = allSysPerms.stream()
                 .filter(perm -> perm.getState() == null || !perm.getState() || perm.getDeleted())
                 .collect(Collectors.toList());

         // 构建系统级权限映射 (按租户映射)
         Map<Long, List<SysPermVO>> validSysPerm = new HashMap<>();
         Map<Long, List<SysPermVO>> disabledSysPerm = new HashMap<>();

         for (Long tenantId : validSysTenantIds) {
             validSysPerm.put(tenantId, sysValidPerms);
             disabledSysPerm.put(tenantId, sysDisabledPerms);
         }

         // 2. 获取用户直接权限 [ 用户级 ]
         Map<Long, List<PermUserVO>> permUsersByUserId = new HashMap<>();
         List<Map<Long, List<PermUserVO>>> permUsersByTUserIds = iPermUserCommonService.getPermUsersByTUserIds(tUserIds);
         for (Map<Long, List<PermUserVO>> map : permUsersByTUserIds) {
             permUsersByUserId.putAll(map);
         }

         // 提取用户级权限ID
         Map<Long, Set<Long>> userPermIdsMap = new HashMap<>();
         for (Map.Entry<Long, List<PermUserVO>> entry : permUsersByUserId.entrySet()) {
             Long userId = entry.getKey();
             List<PermUserVO> permUsers = entry.getValue();
             Set<Long> permIds = permUsers.stream()
                     .filter(permUser -> permUser.getState())
                     .map(PermUserVO::getTPermId)
                     .collect(Collectors.toSet());
             userPermIdsMap.put(userId, permIds);
         }

         // 查询用户级权限详细信息
         Set<Long> allUserPermIds = userPermIdsMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
         List<SysPermVO> allUserPerms = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allUserPermIds));
         Map<Long, SysPermVO> permIdToPermMap = allUserPerms.stream().collect(Collectors.toMap(SysPermVO::getId, perm -> perm));

         // 构建用户级有效权限映射 - 按租户ID分组，而不是用户ID
         Map<Long, List<SysPermVO>> validUserPerm = new HashMap<>();

         // 获取用户到租户的映射关系
         Map<Long, Long> userToTenantMap = new HashMap<>();
         // 简化处理：假设所有用户都属于所有有效租户（实际情况可能需要通过其他服务获取准确映射）
         for (Long userId : tUserIds) {
             for (Long tenantId : validSysTenantIds) {
                 userToTenantMap.put(userId, tenantId);
                 break; // 简化处理，假设一个用户只属于一个租户
             }
         }

         for (Map.Entry<Long, Set<Long>> entry : userPermIdsMap.entrySet()) {
             Long userId = entry.getKey();
             Long tenantId = userToTenantMap.get(userId);
             if (tenantId != null) {
                 Set<Long> permIds = entry.getValue();
                 List<SysPermVO> perms = permIds.stream()
                         .map(permIdToPermMap::get)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将用户权限合并到对应的租户下
                 validUserPerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 构建用户级禁用权限映射 - 按租户ID分组，而不是用户ID
         Map<Long, List<SysPermVO>> disabledUserPerm = new HashMap<>();
         for (Map.Entry<Long, List<PermUserVO>> entry : permUsersByUserId.entrySet()) {
             Long userId = entry.getKey();
             Long tenantId = userToTenantMap.get(userId);
             if (tenantId != null) {
                 List<PermUserVO> permUsers = entry.getValue();
                 List<SysPermVO> perms = permUsers.stream()
                         .filter(permUser -> !permUser.getState())
                         .map(permUser -> permIdToPermMap.get(permUser.getTPermId()))
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将用户权限合并到对应的租户下
                 disabledUserPerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 3. 获取角色权限 [ 角色级 ]
         Map<Long, List<PermRoleVO>> permRolesByRoleId = new HashMap<>();
         List<Map<Long, List<PermRoleVO>>> permRolesByRoleIds = iPermRoleCommonService.getPermRolesByRoleIds(validGlobalRoleIds);
         for (Map<Long, List<PermRoleVO>> map : permRolesByRoleIds) {
             permRolesByRoleId.putAll(map);
         }

         // 提取角色级权限ID
         Map<Long, Set<Long>> rolePermIdsMap = new HashMap<>();
         for (Map.Entry<Long, List<PermRoleVO>> entry : permRolesByRoleId.entrySet()) {
             Long roleId = entry.getKey();
             List<PermRoleVO> permRoles = entry.getValue();
             Set<Long> permIds = permRoles.stream()
                     .filter(permRole -> permRole.getState() != null && permRole.getState() && !permRole.getDeleted())
                     .map(PermRoleVO::getTPermId)
                     .collect(Collectors.toSet());
             rolePermIdsMap.put(roleId, permIds);
         }

         // 查询角色级权限详细信息
         Set<Long> allRolePermIds = rolePermIdsMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
         List<SysPermVO> allRolePerms = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allRolePermIds));
         Map<Long, SysPermVO> roleIdToPermMap = allRolePerms.stream().collect(Collectors.toMap(SysPermVO::getId, perm -> perm));

         // 构建角色级有效权限映射 - 按租户ID分组
         Map<Long, List<SysPermVO>> validRolePerm = new HashMap<>();

         // 获取角色到租户的映射关系
         Map<Long, Long> roleToTenantMap = new HashMap<>();
         // 简化处理：假设所有角色都属于所有有效租户
         for (Long roleId : validGlobalRoleIds) {
             for (Long tenantId : validSysTenantIds) {
                 roleToTenantMap.put(roleId, tenantId);
                 break; // 简化处理，假设一个角色只属于一个租户
             }
         }

         for (Map.Entry<Long, Set<Long>> entry : rolePermIdsMap.entrySet()) {
             Long roleId = entry.getKey();
             Long tenantId = roleToTenantMap.get(roleId);
             if (tenantId != null) {
                 Set<Long> permIds = entry.getValue();
                 List<SysPermVO> perms = permIds.stream()
                         .map(roleIdToPermMap::get)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将角色权限合并到对应的租户下
                 validRolePerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 构建角色级禁用权限映射 - 按租户ID分组
         Map<Long, List<SysPermVO>> disabledRolePerm = new HashMap<>();
         for (Map.Entry<Long, List<PermRoleVO>> entry : permRolesByRoleId.entrySet()) {
             Long roleId = entry.getKey();
             Long tenantId = roleToTenantMap.get(roleId);
             if (tenantId != null) {
                 List<PermRoleVO> permRoles = entry.getValue();
                 List<SysPermVO> perms = permRoles.stream()
                         .filter(permRole -> permRole.getState() == null || !permRole.getState() || permRole.getDeleted())
                         .map(permRole -> roleIdToPermMap.get(permRole.getTPermId()))
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将角色权限合并到对应的租户下
                 disabledRolePerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 4. 获取部门权限 [ 部门级 ]
         Map<Long, List<PermDeptVO>> permDeptsByDeptId = new HashMap<>();
         List<Map<Long, List<PermDeptVO>>> permDeptsByTDeptIds = iPermDeptCommonService.getPermDeptsByTDeptIds(validGlobalDeptIds);
         for (Map<Long, List<PermDeptVO>> map : permDeptsByTDeptIds) {
             permDeptsByDeptId.putAll(map);
         }

         // 提取部门级权限ID
         Map<Long, Set<Long>> deptPermIdsMap = new HashMap<>();
         for (Map.Entry<Long, List<PermDeptVO>> entry : permDeptsByDeptId.entrySet()) {
             Long deptId = entry.getKey();
             List<PermDeptVO> permDepts = entry.getValue();
             Set<Long> permIds = permDepts.stream()
                     .filter(permDept -> permDept.getState() != null && permDept.getState() && !permDept.getDeleted())
                     .map(PermDeptVO::getTPermId)
                     .collect(Collectors.toSet());
             deptPermIdsMap.put(deptId, permIds);
         }

         // 查询部门级权限详细信息
         Set<Long> allDeptPermIds = deptPermIdsMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
         List<SysPermVO> allDeptPerms = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allDeptPermIds));
         Map<Long, SysPermVO> deptIdToPermMap = allDeptPerms.stream().collect(Collectors.toMap(SysPermVO::getId, perm -> perm));

         // 构建部门级有效权限映射 - 按租户ID分组
         Map<Long, List<SysPermVO>> validDeptPerm = new HashMap<>();

         // 获取部门到租户的映射关系
         Map<Long, Long> deptToTenantMap = new HashMap<>();
         // 简化处理：假设所有部门都属于所有有效租户
         for (Long deptId : validGlobalDeptIds) {
             for (Long tenantId : validSysTenantIds) {
                 deptToTenantMap.put(deptId, tenantId);
                 break; // 简化处理，假设一个部门只属于一个租户
             }
         }

         for (Map.Entry<Long, Set<Long>> entry : deptPermIdsMap.entrySet()) {
             Long deptId = entry.getKey();
             Long tenantId = deptToTenantMap.get(deptId);
             if (tenantId != null) {
                 Set<Long> permIds = entry.getValue();
                 List<SysPermVO> perms = permIds.stream()
                         .map(deptIdToPermMap::get)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将部门权限合并到对应的租户下
                 validDeptPerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 构建部门级禁用权限映射 - 按租户ID分组
         Map<Long, List<SysPermVO>> disabledDeptPerm = new HashMap<>();
         for (Map.Entry<Long, List<PermDeptVO>> entry : permDeptsByDeptId.entrySet()) {
             Long deptId = entry.getKey();
             Long tenantId = deptToTenantMap.get(deptId);
             if (tenantId != null) {
                 List<PermDeptVO> permDepts = entry.getValue();
                 List<SysPermVO> perms = permDepts.stream()
                         .filter(permDept -> permDept.getState() == null || !permDept.getState() || permDept.getDeleted())
                         .map(permDept -> deptIdToPermMap.get(permDept.getTPermId()))
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());

                 // 将部门权限合并到对应的租户下
                 disabledDeptPerm.merge(tenantId, perms, (existing, newPerms) -> {
                     List<SysPermVO> merged = new ArrayList<>(existing);
                     merged.addAll(newPerms);
                     // 去重
                     Map<Long, SysPermVO> uniquePerms = new HashMap<>();
                     merged.forEach(perm -> uniquePerms.put(perm.getId(), perm));
                     return new ArrayList<>(uniquePerms.values());
                 });
             }
         }

         // 5. 获取租户权限 [ 租户级 ]
         Map<Long, List<PermTenantVO>> permTenantsByTenantId = new HashMap<>();
         List<Map<Long, List<PermTenantVO>>> permTenantsByTenantIds = iPermTenantCommonService.getPermTenantsByTenantIds(validSysTenantIds);
         for (Map<Long, List<PermTenantVO>> map : permTenantsByTenantIds) {
             permTenantsByTenantId.putAll(map);
         }

         // 提取租户级权限ID
         Map<Long, Set<Long>> tenantPermIdsMap = new HashMap<>();
         for (Map.Entry<Long, List<PermTenantVO>> entry : permTenantsByTenantId.entrySet()) {
             Long tenantId = entry.getKey();
             List<PermTenantVO> permTenants = entry.getValue();
             Set<Long> permIds = permTenants.stream()
                     .filter(permTenant -> permTenant.getState() != null && permTenant.getState() && !permTenant.getDeleted())
                     .map(PermTenantVO::getSPermId)
                     .collect(Collectors.toSet());
             tenantPermIdsMap.put(tenantId, permIds);
         }

         // 查询租户级权限详细信息
         Set<Long> allTenantPermIds = tenantPermIdsMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
         List<SysPermVO> allTenantPerms = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(allTenantPermIds));
         Map<Long, SysPermVO> tenantIdToPermMap = allTenantPerms.stream().collect(Collectors.toMap(SysPermVO::getId, perm -> perm));

         // 构建租户级有效权限映射
         Map<Long, List<SysPermVO>> validTenantPerm = new HashMap<>();
         for (Map.Entry<Long, Set<Long>> entry : tenantPermIdsMap.entrySet()) {
             Long tenantId = entry.getKey();
             Set<Long> permIds = entry.getValue();
             List<SysPermVO> perms = permIds.stream()
                     .map(tenantIdToPermMap::get)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
             validTenantPerm.put(tenantId, perms);
         }

         // 构建租户级禁用权限映射
         Map<Long, List<SysPermVO>> disabledTenantPerm = new HashMap<>();
         for (Map.Entry<Long, List<PermTenantVO>> entry : permTenantsByTenantId.entrySet()) {
             Long tenantId = entry.getKey();
             List<PermTenantVO> permTenants = entry.getValue();
             List<SysPermVO> perms = permTenants.stream()
                     .filter(permTenant -> permTenant.getState() == null || !permTenant.getState() || permTenant.getDeleted())
                     .map(permTenant -> tenantIdToPermMap.get(permTenant.getSPermId()))
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
             disabledTenantPerm.put(tenantId, perms);
         }

         // 6. 处理级联禁用逻辑
         // 6.1 系统级权限禁用影响所有下级 - 创建新的SysPermVO对象并设置cascadeDisable为"system"
         Map<Long, List<SysPermVO>> cascadedSysDisabledUserPerm = new HashMap<>();
         Map<Long, List<SysPermVO>> cascadedSysDisabledRolePerm = new HashMap<>();
         Map<Long, List<SysPermVO>> cascadedSysDisabledDeptPerm = new HashMap<>();
         Map<Long, List<SysPermVO>> cascadedSysDisabledTenantPerm = new HashMap<>();

         // 系统级禁用权限ID集合
         Set<Long> sysDisabledPermIds = sysDisabledPerms.stream().map(SysPermVO::getId).collect(Collectors.toSet());

         // 更新用户级权限中的系统级禁用权限
         for (Map.Entry<Long, List<SysPermVO>> entry : validUserPerm.entrySet()) {
             Long tenantId = entry.getKey();
             List<SysPermVO> perms = entry.getValue().stream()
                     .map(perm -> {
                         if (sysDisabledPermIds.contains(perm.getId())) {
                             // 创建新的SysPermVO对象，设置cascadeDisable为"system"
                             return new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     "system",
                                     perm.getDeleted()
                             );
                         }
                         return perm;
                     })
                     .collect(Collectors.toList());
             cascadedSysDisabledUserPerm.put(tenantId, perms);
         }

         // 更新角色级权限中的系统级禁用权限
         for (Map.Entry<Long, List<SysPermVO>> entry : validRolePerm.entrySet()) {
             Long tenantId = entry.getKey();
             List<SysPermVO> perms = entry.getValue().stream()
                     .map(perm -> {
                         if (sysDisabledPermIds.contains(perm.getId())) {
                             // 创建新的SysPermVO对象，设置cascadeDisable为"system"
                             return new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     "system",
                                     perm.getDeleted()
                             );
                         }
                         return perm;
                     })
                     .collect(Collectors.toList());
             cascadedSysDisabledRolePerm.put(tenantId, perms);
         }

         // 更新部门级权限中的系统级禁用权限
         for (Map.Entry<Long, List<SysPermVO>> entry : validDeptPerm.entrySet()) {
             Long tenantId = entry.getKey();
             List<SysPermVO> perms = entry.getValue().stream()
                     .map(perm -> {
                         if (sysDisabledPermIds.contains(perm.getId())) {
                             // 创建新的SysPermVO对象，设置cascadeDisable为"system"
                             return new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     "system",
                                     perm.getDeleted()
                             );
                         }
                         return perm;
                     })
                     .collect(Collectors.toList());
             cascadedSysDisabledDeptPerm.put(tenantId, perms);
         }

         // 更新租户级权限中的系统级禁用权限
         for (Map.Entry<Long, List<SysPermVO>> entry : validTenantPerm.entrySet()) {
             Long tenantId = entry.getKey();
             List<SysPermVO> perms = entry.getValue().stream()
                     .map(perm -> {
                         if (sysDisabledPermIds.contains(perm.getId())) {
                             // 创建新的SysPermVO对象，设置cascadeDisable为"system"
                             return new SysPermVO(
                                     perm.getId(),
                                     perm.getName(),
                                     perm.getAlias(),
                                     perm.getDesc(),
                                     perm.getCreatedAt(),
                                     perm.getUpdatedAt(),
                                     perm.getCreatedById(),
                                     perm.getUpdatedById(),
                                     perm.getColor(),
                                     perm.getState(),
                                     "system",
                                     perm.getDeleted()
                             );
                         }
                         return perm;
                     })
                     .collect(Collectors.toList());
             cascadedSysDisabledTenantPerm.put(tenantId, perms);
         }

         // 7. 返回结果 (只返回前10个属性)
         return new CheckPermVO(
                 validSysPerm,
                 cascadedSysDisabledTenantPerm,
                 cascadedSysDisabledDeptPerm,
                 cascadedSysDisabledRolePerm,
                 cascadedSysDisabledUserPerm,
                 disabledSysPerm,
                 disabledTenantPerm,
                 disabledDeptPerm,
                 disabledRolePerm,
                 disabledUserPerm,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null
         );
     }

    /**
     * 租户认证
     * @param sTenantIds 租户Id 列表
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
     * 只验证 有效租户且用户绑定的部门
     * @param tUserIds 租户用户Id 列表
     * @param sTenantIds 租户Id 列表
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

                            // 合并所有有效部门
                            List<SysDeptVO> allValidDepts = Stream.of(sysDepts, tenantDepts, userDepts)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .collect(Collectors.toList());

                            // 获取需要排除的禁用部门（三个禁用集合的合并去重）
                            List<SysDeptVO> disabledSysDepts = disabledSysDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> disabledTenantDepts = disabledTenantDeptByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysDeptVO> disabledUserDepts = disabledUserDeptByTenant.getOrDefault(tenantId, Collections.emptyList());

                            Set<Long> disabledDeptIds = Stream.of(disabledSysDepts, disabledTenantDepts, disabledUserDepts)
                                    .flatMap(List::stream)
                                    .map(SysDeptVO::getId)
                                    .collect(Collectors.toSet());

                            // 从所有有效部门中排除禁用部门
                            return allValidDepts.stream()
                                    .filter(dept -> !disabledDeptIds.contains(dept.getId()))
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
                                    .collect(Collectors.toMap(
                                            SysDeptVO::getId, // 使用ID作为键
                                            dept -> dept,     // 使用对象作为值
                                            (existing, replacement) -> existing // 如果ID重复，保留第一个
                                    ))
                                    .values()
                                    .stream()
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
     * 只验证 有效租户且用户绑定的角色
     * @param tUserIds 租户用户Id 列表
     * @param sTenantIds 有效租户Id 列表
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

                            // 合并所有有效角色
                            List<SysRoleVO> allValidRoles = Stream.of(sysRoles, tenantRoles, userRoles)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .collect(Collectors.toList());

                            // 获取需要排除的禁用角色（三个禁用集合的合并去重）
                            List<SysRoleVO> disabledSysRoles = disabledSysRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> disabledTenantRoles = disabledTenantRoleByTenant.getOrDefault(tenantId, Collections.emptyList());
                            List<SysRoleVO> disabledUserRoles = disabledUserRoleByTenant.getOrDefault(tenantId, Collections.emptyList());

                            Set<Long> disabledRoleIds = Stream.of(disabledSysRoles, disabledTenantRoles, disabledUserRoles)
                                    .flatMap(List::stream)
                                    .map(SysRoleVO::getId)
                                    .collect(Collectors.toSet());

                            // 从所有有效角色中排除禁用角色
                            return allValidRoles.stream()
                                    .filter(role -> !disabledRoleIds.contains(role.getId()))
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
