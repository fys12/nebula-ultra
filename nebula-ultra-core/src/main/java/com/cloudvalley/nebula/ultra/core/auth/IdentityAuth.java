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
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.IUserTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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

    @Autowired
    private IUserTenantCommonService iUserTenantCommonService;

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

         // 根据 禁用的系统部门Id 查询 对应 租户部门信息
         Map<Long, List<DeptTenantVO>> deptTenantsBySDeptIds = iDeptTenantCommonService.getDeptTenantsBySDeptIds(disabledDeptIds);

         // 获取 租户部门Id
         List<Long> tDeptIds = deptTenantsBySDeptIds.values().stream()
                 .flatMap(List::stream)
                 .map(DeptTenantVO::getId)
                 .toList();

         // 建立 租户部门Id → 租户Id 的映射关系
         Map<Long, Long> tDeptIdToTenantIdMap = deptTenantsBySDeptIds.values().stream()
                 .flatMap(List::stream)
                 .collect(Collectors.toMap(
                         DeptTenantVO::getId,        // key: 租户部门Id (tDeptId)
                         DeptTenantVO::getSTenantId  // value: 租户Id (sTenantId)
                 ));

         if (tDeptIds != null && !tDeptIds.isEmpty()) {
             // 2.1 获取禁用部门关联的权限ID（返回的是 tPermId）
             Map<Long, Set<Long>> permIdsByDeptIds = iPermDeptCommonService.getTPermIdsByTDeptIds(tDeptIds);

             // 2.2 获取所有涉及的权限ID（tPermId）
             List<Long> allPermIds = permIdsByDeptIds.values().stream()
                     .flatMap(Set::stream)
                     .distinct()
                     .toList();

             if (!allPermIds.isEmpty()) {
                 // 2.3 查询租户权限详细信息（包含 sPermId）
                 List<PermTenantVO> permTenantsByIds = iPermTenantCommonService.getPermTenantsByIds(allPermIds);

                 // 构建 tPermId → sPermId 映射
                 Map<Long, Long> tPermIdToSPermIdMap = permTenantsByIds.stream()
                         .collect(Collectors.toMap(
                                 PermTenantVO::getId,     // tPermId
                                 PermTenantVO::getSPermId // sPermId
                         ));

                 // 获取对应的系统权限ID
                 List<Long> sysPermIds = permTenantsByIds.stream()
                         .map(PermTenantVO::getSPermId)
                         .distinct()
                         .toList();

                 List<SysPermVO> permVOs = iSysPermCommonService.getSysPermsByIds(sysPermIds);

                 // 2.4 按部门ID分组并设置级联禁用标识
                 for (Map.Entry<Long, Set<Long>> entry : permIdsByDeptIds.entrySet()) {
                     Long tDeptId = entry.getKey(); // 租户部门ID
                     Set<Long> tPermIds = entry.getValue(); // 租户权限ID集合

                     Long tenantId = tDeptIdToTenantIdMap.get(tDeptId);
                     if (tenantId == null) {
                         continue;
                     }

                     // 将 tPermId 转为 sPermId
                     Set<Long> sPermIds = tPermIds.stream()
                             .map(tPermIdToSPermIdMap::get)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toSet());

                     // 用 sPermId 过滤系统权限
                     List<SysPermVO> deptDisabledPerms = permVOs.stream()
                             .filter(perm -> sPermIds.contains(perm.getId()))
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

                     // 合并到租户维度
                     disabledPermByDept.merge(tenantId, deptDisabledPerms, (existingList, newList) -> {
                         existingList.addAll(newList);
                         return existingList;
                     });
                 }
             }
         }

         // 3. 获取因角色禁用而级联禁用的权限
         Map<Long, List<SysPermVO>> disabledPermByRole = new HashMap<>();

         // 根据 禁用的系统角色Id 查询 对应 租户角色信息
         Map<Long, List<RoleTenantVO>> roleTenantsBySRoleIds = iRoleTenantCommonService.getRoleTenantsByRoleIds(disabledRoleIds);

         // 获取 租户角色Id
         List<Long> tRoleIds = roleTenantsBySRoleIds.values().stream()
                 .flatMap(List::stream)
                 .map(RoleTenantVO::getId)
                 .toList();

         // 建立 租户角色Id → 租户Id 的映射关系
         Map<Long, Long> tRoleIdToTenantIdMap = roleTenantsBySRoleIds.values().stream()
                 .flatMap(List::stream)
                 .collect(Collectors.toMap(
                         RoleTenantVO::getId,        // key: 租户角色Id (tRoleId)
                         RoleTenantVO::getSTenantId  // value: 租户Id (sTenantId)
                 ));

         if (tRoleIds != null && !tRoleIds.isEmpty()) {
             // 3.1 获取禁用角色关联的权限ID（返回的是 tPermId）
             Map<Long, Set<Long>> permIdsByRoleIds = iPermRoleCommonService.getPermIdsByRoleIds(tRoleIds);

             // 3.2 获取所有涉及的权限ID（tPermId）
             List<Long> allPermIds = permIdsByRoleIds.values().stream()
                     .flatMap(Set::stream)
                     .distinct()
                     .toList();

             if (!allPermIds.isEmpty()) {
                 // 3.3 查询租户权限详细信息（包含 sPermId）
                 List<PermTenantVO> permTenantsByIds = iPermTenantCommonService.getPermTenantsByIds(allPermIds);

                 // 构建 tPermId → sPermId 映射
                 Map<Long, Long> tPermIdToSPermIdMap = permTenantsByIds.stream()
                         .collect(Collectors.toMap(
                                 PermTenantVO::getId,     // tPermId
                                 PermTenantVO::getSPermId // sPermId
                         ));

                 // 获取对应的系统权限ID
                 List<Long> sysPermIds = permTenantsByIds.stream()
                         .map(PermTenantVO::getSPermId)
                         .distinct()
                         .toList();

                 List<SysPermVO> permVOs = iSysPermCommonService.getSysPermsByIds(sysPermIds);

                 // 3.4 按角色ID分组并设置级联禁用标识
                 for (Map.Entry<Long, Set<Long>> entry : permIdsByRoleIds.entrySet()) {
                     Long tRoleId = entry.getKey(); // 租户角色ID
                     Set<Long> tPermIds = entry.getValue(); // 租户权限ID集合

                     Long tenantId = tRoleIdToTenantIdMap.get(tRoleId);
                     if (tenantId == null) {
                         continue;
                     }

                     // 将 tPermId 转为 sPermId
                     Set<Long> sPermIds = tPermIds.stream()
                             .map(tPermIdToSPermIdMap::get)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toSet());

                     // 用 sPermId 过滤系统权限
                     List<SysPermVO> roleDisabledPerms = permVOs.stream()
                             .filter(perm -> sPermIds.contains(perm.getId()))
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
                                     "role", // 设置级联禁用标识为"role"
                                     perm.getDeleted()
                             ))
                             .collect(Collectors.toList());

                     // 合并到租户维度
                     disabledPermByRole.merge(tenantId, roleDisabledPerms, (existingList, newList) -> {
                         existingList.addAll(newList);
                         return existingList;
                     });
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
         // 1. 根据 有效租户Id列表 查询 租户权限信息 Map<租户Id 租户权限信息>
         Map<Long, List<PermTenantVO>> permTenantsByTenantIds = iPermTenantCommonService.getPermTenantsByTenantIds(validSysTenantIds);

         // 1.1 获取所有 系统权限Id
         List<Long> sysPermIds = permTenantsByTenantIds.values().stream()
                 .flatMap(List::stream)
                 .map(PermTenantVO::getSPermId)
                 .toList();

         // 2. 根据 系统权限Id列表 获取 系统权限信息
         List<SysPermVO> sysPermVOs = iSysPermCommonService.getSysPermsByIds(sysPermIds);

         // 2.1 获取 有效权限 [ 系统级 ]
         Map<Long, List<SysPermVO>> validSysPerms = permTenantsByTenantIds.entrySet().stream()
                 .collect(Collectors.toMap(
                         Map.Entry::getKey,
                         entry -> entry.getValue().stream()
                                 .filter(permTenant -> sysPermVOs.stream()
                                         .anyMatch(sysPerm -> sysPerm.getId().equals(permTenant.getSPermId()) && sysPerm.getState()))
                                 .map(permTenant -> sysPermVOs.stream()
                                         .filter(sysPerm -> sysPerm.getId().equals(permTenant.getSPermId()))
                                         .findFirst()
                                         .orElse(null))
                                 .filter(Objects::nonNull)
                                 .collect(Collectors.toList()),
                         (existing, replacement) -> {
                             List<SysPermVO> merged = new ArrayList<>(existing);
                             merged.addAll(replacement);
                             return merged;
                         }
                 ));

         // 2.2 获取 禁用权限 [ 系统级 ]
         Map<Long, List<SysPermVO>> disabledSysPerms = permTenantsByTenantIds.entrySet().stream()
                 .collect(Collectors.toMap(
                         Map.Entry::getKey,
                         entry -> entry.getValue().stream()
                                 .filter(permTenant -> sysPermVOs.stream()
                                         .anyMatch(sysPerm -> sysPerm.getId().equals(permTenant.getSPermId()) && !sysPerm.getState()))
                                 .map(permTenant -> sysPermVOs.stream()
                                         .filter(sysPerm -> sysPerm.getId().equals(permTenant.getSPermId()))
                                         .findFirst()
                                         .orElse(null))
                                 .filter(Objects::nonNull)
                                 .collect(Collectors.toList()),
                         (existing, replacement) -> {
                             List<SysPermVO> merged = new ArrayList<>(existing);
                             merged.addAll(replacement);
                             return merged;
                         }
                 ));

         // 2.3 获取 禁用权限Id [ 系统级 ] 用于 级联禁用
         List<Long> disabledSysPermIds = disabledSysPerms.values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .distinct()
                 .collect(Collectors.toList());

         // 2.4 建立 系统权限Id -> 租户权限VO
         Map<Long, PermTenantVO> sysPermsToPermTenants = permTenantsByTenantIds.values().stream()
                 .flatMap(List::stream)
                 .collect(Collectors.toMap(
                         PermTenantVO::getSPermId,
                         permTenantVO -> permTenantVO,
                         (existing, replacement) -> existing
                 ));

         // 2.5 构建 租户权限ID → 系统权限ID 映射
         Map<Long, Long> tPermIdToSysPermId = sysPermsToPermTenants.values().stream()
                 .collect(Collectors.toMap(
                         PermTenantVO::getId,
                         PermTenantVO::getSPermId,
                         (existing, replacement) -> existing
                 ));

         // 3. 获取 租户级 禁用/有效权限
         // 3.1 获取 禁用权限 [ 租户级 ]
         Map<Long, List<SysPermVO>> disabledTenantPerm = permTenantsByTenantIds.entrySet().stream()
                 .collect(Collectors.toMap(
                         Map.Entry::getKey,
                         entry -> {
                             Long tenantId = entry.getKey();
                             List<PermTenantVO> permTenantList = entry.getValue();

                             List<SysPermVO> result = new ArrayList<>();

                             for (PermTenantVO permTenant : permTenantList) {
                                 // 查找对应的系统权限
                                 SysPermVO sysPerm = sysPermVOs.stream()
                                         .filter(p -> p.getId().equals(permTenant.getSPermId()))
                                         .findFirst()
                                         .orElse(null);

                                 if (sysPerm != null) {
                                     // 判断是否需要加入禁用列表
                                     // 租户级禁用
                                     if (permTenant.getState() == null || !permTenant.getState()) {
                                         SysPermVO tenantPerm = new SysPermVO(
                                                 sysPerm.getId(),
                                                 sysPerm.getName(),
                                                 sysPerm.getAlias(),
                                                 sysPerm.getDesc(),
                                                 sysPerm.getCreatedAt(),
                                                 sysPerm.getUpdatedAt(),
                                                 sysPerm.getCreatedById(),
                                                 sysPerm.getUpdatedById(),
                                                 permTenant.getColor(),
                                                 permTenant.getState(),
                                                 null,
                                                 permTenant.getDeleted()
                                         );
                                         result.add(tenantPerm);
                                     }
                                     // 系统级权限级联禁用
                                     else if (!sysPerm.getState()) {
                                         // 系统级禁用但租户级启用，需要标记级联禁用
                                         SysPermVO cascadedPerm = new SysPermVO(
                                                 sysPerm.getId(),
                                                 sysPerm.getName(),
                                                 sysPerm.getAlias(),
                                                 sysPerm.getDesc(),
                                                 sysPerm.getCreatedAt(),
                                                 sysPerm.getUpdatedAt(),
                                                 sysPerm.getCreatedById(),
                                                 sysPerm.getUpdatedById(),
                                                 sysPerm.getColor(),
                                                 sysPerm.getState(),
                                                 "system",
                                                 sysPerm.getDeleted()
                                         );
                                         result.add(cascadedPerm);
                                     }
                                 }
                             }
                             return result;
                         },
                         (existing, replacement) -> {
                             List<SysPermVO> merged = new ArrayList<>(existing);
                             merged.addAll(replacement);
                             return merged;
                         }
                 ));

         // 3.2 获取 禁用权限Id 包含系统级联禁用的 [ 租户级 ]
         List<Long> disabledTenantPermIds = disabledTenantPerm.values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .distinct()
                 .collect(Collectors.toList());

         // 3.3 获取 有效权限 [ 租户级 ]
         Map<Long, List<SysPermVO>> validTenantPerm = permTenantsByTenantIds.entrySet().stream()
                 .collect(Collectors.toMap(
                         Map.Entry::getKey,
                         entry -> {
                             Long tenantId = entry.getKey();
                             List<PermTenantVO> permTenantList = entry.getValue();

                             List<SysPermVO> result = new ArrayList<>();

                             for (PermTenantVO permTenant : permTenantList) {
                                 // 查找对应的系统权限
                                 SysPermVO sysPerm = sysPermVOs.stream()
                                         .filter(p -> p.getId().equals(permTenant.getSPermId()))
                                         .findFirst()
                                         .orElse(null);

                                 if (sysPerm != null) {
                                     // 判断是否为有效权限
                                     // 条件1: 租户级权限启用
                                     // 条件2: 系统级权限启用
                                     // 条件3: 不在租户级禁用权限列表中
                                     if (permTenant.getState() &&
                                             sysPerm.getState() &&
                                             !disabledTenantPermIds.contains(sysPerm.getId())) {
                                         SysPermVO validPerm = new SysPermVO(
                                                 sysPerm.getId(),
                                                 sysPerm.getName(),
                                                 sysPerm.getAlias(),
                                                 sysPerm.getDesc(),
                                                 sysPerm.getCreatedAt(),
                                                 sysPerm.getUpdatedAt(),
                                                 sysPerm.getCreatedById(),
                                                 sysPerm.getUpdatedById(),
                                                 permTenant.getColor(),
                                                 sysPerm.getState(),
                                                 null,
                                                 sysPerm.getDeleted()
                                         );
                                         result.add(validPerm);
                                     }
                                 }
                             }

                             return result;
                         },
                         (existing, replacement) -> {
                             List<SysPermVO> merged = new ArrayList<>(existing);
                             merged.addAll(replacement);
                             return merged;
                         }
                 ));

         // 4. 根据 有效租户Id列表 和 有效部门Id列表 查询 租户部门信息
         Map<Long, List<DeptTenantVO>> deptTenantsBySTenantIdsAndSDeptIds = iDeptTenantCommonService.getDeptTenantsBySTenantIdsAndSDeptIds(validSysTenantIds, validGlobalDeptIds);

         // 4.1 获取 有效租户部门Id列表
         List<Long> validTenantDeptIds = deptTenantsBySTenantIdsAndSDeptIds.values().stream()
                  .flatMap(List::stream)
                  .map(DeptTenantVO::getId)
                  .distinct()
                  .toList();

         // 4.2 根据 有效租户部门Id列表 查询 租户部门权限信息
         Map<Long, List<PermDeptVO>> permDeptsByTDeptIds = iPermDeptCommonService.getPermDeptsByTDeptIds(validTenantDeptIds);

         // 4.3 获取 禁用权限 [ 部门级 ]
         Map<Long, List<SysPermVO>> disabledDeptPerm = permDeptsByTDeptIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             Long tDeptId = entry.getKey();
                             // 从 deptTenantsBySTenantIdsAndSDeptIds 中查找该部门对应的租户ID
                             return deptTenantsBySTenantIdsAndSDeptIds.values().stream()
                                     .flatMap(List::stream)
                                     .filter(deptTenant -> deptTenant.getId().equals(tDeptId))
                                     .findFirst()
                                     .map(DeptTenantVO::getSTenantId)
                                     .orElse(-1L); // 处理异常情况，可根据业务调整
                         },
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tDeptId = entry.getKey();
                                     List<PermDeptVO> permDeptList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();
                                     for (PermDeptVO permDept : permDeptList) {
                                         Long tPermId = permDept.getTPermId();
                                         Long sPermId = tPermIdToSysPermId.get(tPermId);
                                         // 查找系统权限
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);
                                         if (sysPerm == null) continue;

                                         // 系统级禁用
                                         if (!sysPerm.getState()) {
                                             SysPermVO cascadedPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     sysPerm.getColor(),
                                                     sysPerm.getState(),
                                                     "system",
                                                     sysPerm.getDeleted()
                                             );
                                             result.add(cascadedPerm);
                                             continue;
                                         }
                                         // 租户级禁用
                                         PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);
                                         if (permTenant == null || !permTenant.getState()) {
                                             // 租户级禁用或不存在 → 级联禁用
                                             SysPermVO cascadedPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permTenant != null ? permTenant.getColor() : sysPerm.getColor(),
                                                     permTenant != null ? permTenant.getState() : false,
                                                     "tenant",
                                                     permTenant != null ? permTenant.getDeleted() : sysPerm.getDeleted()
                                             );
                                             result.add(cascadedPerm);
                                             continue;
                                         }
                                         // 部门级禁用
                                         if (permDept.getState() == null || !permDept.getState()) {
                                             SysPermVO deptDisabledPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permDept.getColor(),
                                                     permDept.getState(),
                                                     null,
                                                     permDept.getDeleted()
                                             );
                                             result.add(deptDisabledPerm);
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

         // 4.4 获取 禁用权限Id 包含系统、租户级联禁用的 [ 部门级 ]
         List<Long> disabledDeptPermIds = disabledDeptPerm.values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .distinct()
                 .toList();

         // 4.5 获取 有效权限 [ 部门级 ]
         Map<Long, List<SysPermVO>> validDeptPerm = permDeptsByTDeptIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             // 1. 通过 tDeptId 找到对应的 DeptTenantVO
                             Long tDeptId = entry.getKey();
                             List<DeptTenantVO> deptTenantList = deptTenantsBySTenantIdsAndSDeptIds.values().stream()
                                     .flatMap(List::stream)
                                     .filter(deptTenant -> deptTenant.getId().equals(tDeptId))
                                     .collect(Collectors.toList());

                             // 2. 从 DeptTenantVO 中提取租户ID (sTenantId)
                             if (!deptTenantList.isEmpty()) {
                                 return deptTenantList.get(0).getSTenantId(); // 假设一个部门只属于一个租户
                             } else {
                                 // 处理异常情况，例如找不到对应的 DeptTenantVO
                                 // 可以选择返回一个默认租户ID或抛出异常，根据业务逻辑决定
                                 return -1L; // 或者 throw new IllegalStateException("找不到部门ID: " + tDeptId + " 对应的租户信息");
                             }
                         },
                         // 3. 合并同一个租户下的所有权限
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tDeptId = entry.getKey();
                                     List<PermDeptVO> permDeptList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();
                                     for (PermDeptVO permDept : permDeptList) {
                                         // ... (此处保留原有的权限计算逻辑，与原始代码 4.5 中的 for 循环内容一致)
                                         Long sPermId = tPermIdToSysPermId.get(permDept.getTPermId());
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);
                                         PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);
                                         if (permDept.getState() != null && permDept.getState() &&
                                                 sysPerm.getState() &&
                                                 !disabledDeptPermIds.contains(sysPerm.getId())) {
                                             SysPermVO validPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permDept.getColor(),
                                                     permDept.getState(),
                                                     null,
                                                     permDept.getDeleted()
                                             );
                                             result.add(validPerm);
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

         // 5. 根据 有效租户Id列表 和 有效角色Id列表 查询 租户角色信息
         Map<Long, List<RoleTenantVO>> roleTenantsBySTenantIdsAndSRoleIds = iRoleTenantCommonService.getRoleTenantsBySTenantIdsAndSRoleIds(validSysTenantIds, validGlobalRoleIds);

         // 5.1 获取 有效租户角色Id列表
         List<Long> validTenantRoleIds = roleTenantsBySTenantIdsAndSRoleIds.values().stream()
                 .flatMap(List::stream)
                 .map(RoleTenantVO::getId)
                 .distinct()
                 .toList();

         // 5.2 根据 有效租户角色Id列表 查询 租户角色权限信息
         Map<Long, List<PermRoleVO>> permRolesByTRoleIds = iPermRoleCommonService.getPermRolesByRoleIds(validTenantRoleIds);

         // 5.3 获取 禁用权限 [ 角色级 ]
         Map<Long, List<SysPermVO>> disabledRolePerm = permRolesByTRoleIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             Long tRoleId = entry.getKey();
                             // 从 roleTenantsBySTenantIdsAndSRoleIds 中查找该角色对应的租户ID
                             return roleTenantsBySTenantIdsAndSRoleIds.values().stream()
                                     .flatMap(List::stream)
                                     .filter(roleTenant -> roleTenant.getId().equals(tRoleId))
                                     .findFirst()
                                     .map(RoleTenantVO::getSTenantId)
                                     .orElse(-1L); // 处理异常情况，可根据业务调整
                         },
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tRoleId = entry.getKey();
                                     List<PermRoleVO> permRoleList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();
                                     for (PermRoleVO permRole : permRoleList) {
                                         Long tPermId = permRole.getTPermId();
                                         Long sPermId = tPermIdToSysPermId.get(tPermId);
                                         // 查找系统权限
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);
                                         if (sysPerm == null) continue;

                                         // 系统级禁用
                                         if (!sysPerm.getState()) {
                                             SysPermVO cascadedPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     sysPerm.getColor(),
                                                     sysPerm.getState(),
                                                     "system",
                                                     sysPerm.getDeleted()
                                             );
                                             result.add(cascadedPerm);
                                             continue;
                                         }
                                         // 租户级禁用
                                         PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);
                                         if (permTenant == null || !permTenant.getState()) {
                                             SysPermVO cascadedPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permTenant != null ? permTenant.getColor() : sysPerm.getColor(),
                                                     permTenant != null ? permTenant.getState() : false,
                                                     "tenant",
                                                     permTenant != null ? permTenant.getDeleted() : sysPerm.getDeleted()
                                             );
                                             result.add(cascadedPerm);
                                             continue;
                                         }
                                         // 角色级主动禁用
                                         if (permRole.getState() == null || !permRole.getState()) {
                                             SysPermVO roleDisabledPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permRole.getColor(),
                                                     permRole.getState(),
                                                     null,
                                                     permRole.getDeleted()
                                             );
                                             result.add(roleDisabledPerm);
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

         // 5.4 获取 禁用权限Id 包含系统、租户级联禁用的 [ 角色级 ]
         List<Long> disabledRolePermIds = disabledRolePerm.values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .distinct()
                 .toList();

         // 5.5 获取 有效权限 [ 角色级 ]
         Map<Long, List<SysPermVO>> validRolePerm = permRolesByTRoleIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             Long tRoleId = entry.getKey();
                             // 从 roleTenantsBySTenantIdsAndSRoleIds 中查找该角色对应的租户ID
                             return roleTenantsBySTenantIdsAndSRoleIds.values().stream()
                                     .flatMap(List::stream)
                                     .filter(roleTenant -> roleTenant.getId().equals(tRoleId))
                                     .findFirst()
                                     .map(RoleTenantVO::getSTenantId)
                                     .orElse(-1L); // 处理异常情况，可根据业务调整
                         },
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tRoleId = entry.getKey();
                                     List<PermRoleVO> permRoleList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();
                                     for (PermRoleVO permRole : permRoleList) {
                                         Long tPermId = permRole.getTPermId();
                                         Long sPermId = tPermIdToSysPermId.get(tPermId);
                                         // 查找系统权限
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);
                                         if (sysPerm == null) continue;

                                         // 查找租户权限
                                         PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);
                                         if (permTenant == null) continue;

                                         // 条件1: 角色级权限启用
                                         // 条件2: 系统级权限启用
                                         // 条件3: 租户级权限启用
                                         // 条件4: 不在角色级禁用权限列表中
                                         if (permRole.getState() != null && permRole.getState() &&
                                                 sysPerm.getState() &&
                                                 permTenant.getState() &&
                                                 !disabledRolePermIds.contains(sysPerm.getId())) {
                                             SysPermVO validPerm = new SysPermVO(
                                                     sysPerm.getId(),
                                                     sysPerm.getName(),
                                                     sysPerm.getAlias(),
                                                     sysPerm.getDesc(),
                                                     sysPerm.getCreatedAt(),
                                                     sysPerm.getUpdatedAt(),
                                                     sysPerm.getCreatedById(),
                                                     sysPerm.getUpdatedById(),
                                                     permRole.getColor(),
                                                     permRole.getState(),
                                                     null,
                                                     permRole.getDeleted()
                                             );
                                             result.add(validPerm);
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

         // 6. 根据 用户Id列表 查询 租户用户权限信息
         Map<Long, List<PermUserVO>> permUsersByTUserIds = iPermUserCommonService.getPermUsersByTUserIds(tUserIds);

         List<UserTenantVO> userTenantsByIds = iUserTenantCommonService.getUserTenantsByIds(tUserIds);

         // 构建 tUserId -> sTenantId 的映射
         Map<Long, Long> userToTenantMap = userTenantsByIds.stream()
                 .collect(Collectors.toMap(
                         UserTenantVO::getId, // Key: tUserId
                         UserTenantVO::getSTenantId, // Value: sTenantId
                         (existing, replacement) -> existing // 如果有重复，保留第一个
                 ));

         // 6.1 获取 禁用权限 [ 用户级 ]
         Map<Long, List<SysPermVO>> disabledUserPerm = permUsersByTUserIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             Long tUserId = entry.getKey();
                             // 从映射中获取租户ID，如果找不到则使用 -1L 作为默认值（或根据业务逻辑处理）
                             return userToTenantMap.getOrDefault(tUserId, -1L);
                         },
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tUserId = entry.getKey();
                                     List<PermUserVO> permUserList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();

                                     for (PermUserVO permUser : permUserList) {
                                         Long tPermId = permUser.getTPermId();
                                         Long sPermId = tPermIdToSysPermId.get(tPermId);

                                         // 查找系统权限
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);

                                         if (sysPerm != null) {
                                             // 系统级禁用
                                             if (!sysPerm.getState()) {
                                                 SysPermVO cascadedPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         sysPerm.getColor(),
                                                         sysPerm.getState(),
                                                         "system",
                                                         sysPerm.getDeleted()
                                                 );
                                                 result.add(cascadedPerm);
                                                 continue; // 系统级禁用，无需检查其他级别
                                             }

                                             // 租户级禁用
                                             PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);
                                             if (permTenant == null || !permTenant.getState()) {
                                                 SysPermVO cascadedPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         permTenant != null ? permTenant.getColor() : sysPerm.getColor(),
                                                         permTenant != null ? permTenant.getState() : false,
                                                         "tenant",
                                                         permTenant != null ? permTenant.getDeleted() : sysPerm.getDeleted()
                                                 );
                                                 result.add(cascadedPerm);
                                                 continue; // 租户级禁用，无需检查其他级别
                                             }

                                             // 部门级禁用（级联影响）
                                             boolean isDeptDisabled = disabledDeptPermIds.contains(sysPerm.getId());
                                             if (isDeptDisabled) {
                                                 SysPermVO cascadedPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         sysPerm.getColor(),
                                                         false,
                                                         "dept",
                                                         sysPerm.getDeleted()
                                                 );
                                                 result.add(cascadedPerm);
                                                 continue; // 部门级禁用，无需检查其他级别
                                             }

                                             // 角色级禁用（级联影响）
                                             boolean isRoleDisabled = disabledRolePermIds.contains(sysPerm.getId());
                                             if (isRoleDisabled) {
                                                 SysPermVO cascadedPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         sysPerm.getColor(),
                                                         false,
                                                         "role",
                                                         sysPerm.getDeleted()
                                                 );
                                                 result.add(cascadedPerm);
                                                 continue; // 角色级禁用，无需检查其他级别
                                             }

                                             // 用户级主动禁用
                                             if (permUser.getState() == null || !permUser.getState()) {
                                                 SysPermVO userDisabledPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         sysPerm.getColor(),
                                                         permUser.getState(),
                                                         null,
                                                         permUser.getDeleted()
                                                 );
                                                 result.add(userDisabledPerm);
                                             }
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

// 6.2 获取 禁用权限Id 包含系统、租户、部门、角色级联禁用的 [ 用户级 ]
         List<Long> disabledUserPermIds = disabledUserPerm.values().stream()
                 .flatMap(List::stream)
                 .map(SysPermVO::getId)
                 .distinct()
                 .toList();

// 6.3 获取 有效权限 [ 用户级 ]
         Map<Long, List<SysPermVO>> validUserPerm = permUsersByTUserIds.entrySet().stream()
                 .collect(Collectors.groupingBy(
                         entry -> {
                             Long tUserId = entry.getKey();
                             // 从映射中获取租户ID，如果找不到则使用 -1L 作为默认值（或根据业务逻辑处理）
                             return userToTenantMap.getOrDefault(tUserId, -1L);
                         },
                         Collectors.flatMapping(
                                 entry -> {
                                     Long tUserId = entry.getKey();
                                     List<PermUserVO> permUserList = entry.getValue();
                                     List<SysPermVO> result = new ArrayList<>();

                                     for (PermUserVO permUser : permUserList) {
                                         Long tPermId = permUser.getTPermId();
                                         Long sPermId = tPermIdToSysPermId.get(tPermId);

                                         // 查找系统权限
                                         SysPermVO sysPerm = sysPermVOs.stream()
                                                 .filter(p -> p.getId().equals(sPermId))
                                                 .findFirst()
                                                 .orElse(null);

                                         // 查找租户权限
                                         PermTenantVO permTenant = sysPermsToPermTenants.get(sPermId);

                                         if (sysPerm != null && permTenant != null) {
                                             // 检查是否在任何级别被禁用
                                             boolean isDisabledAnywhere = disabledSysPermIds.contains(sysPerm.getId()) ||
                                                     disabledTenantPermIds.contains(sysPerm.getId()) ||
                                                     disabledDeptPermIds.contains(sysPerm.getId()) ||
                                                     disabledRolePermIds.contains(sysPerm.getId()) ||
                                                     disabledUserPermIds.contains(sysPerm.getId());

                                             // 条件1: 用户级权限启用
                                             // 条件2: 系统级权限启用
                                             // 条件3: 租户级权限启用
                                             // 条件4: 部门级权限启用（通过禁用列表检查）
                                             // 条件5: 角色级权限启用（通过禁用列表检查）
                                             // 条件6: 不在任何级别被禁用
                                             if (permUser.getState() != null && permUser.getState() &&
                                                     sysPerm.getState() &&
                                                     permTenant.getState() &&
                                                     !disabledDeptPermIds.contains(sysPerm.getId()) &&
                                                     !disabledRolePermIds.contains(sysPerm.getId()) &&
                                                     !isDisabledAnywhere) {

                                                 SysPermVO validPerm = new SysPermVO(
                                                         sysPerm.getId(),
                                                         sysPerm.getName(),
                                                         sysPerm.getAlias(),
                                                         sysPerm.getDesc(),
                                                         sysPerm.getCreatedAt(),
                                                         sysPerm.getUpdatedAt(),
                                                         sysPerm.getCreatedById(),
                                                         sysPerm.getUpdatedById(),
                                                         sysPerm.getColor(),
                                                         permUser.getState(),
                                                         null,
                                                         permUser.getDeleted()
                                                 );
                                                 result.add(validPerm);
                                             }
                                         }
                                     }
                                     return result.stream();
                                 },
                                 Collectors.toList()
                         )
                 ));

         return new CheckPermVO(
                 validSysPerms,
                 validTenantPerm,
                 validDeptPerm,
                 validRolePerm,
                 validUserPerm,
                 disabledSysPerms,
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
        Map<Long, SysTenantVO> sysTenantsByIds = iSysTenantCommonService.getSysTenantsByIds(sTenantIds).stream()
                .collect(Collectors.toMap(SysTenantVO::getId, sysTenantVO -> sysTenantVO));

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
        Map<Long, List<DeptTenantVO>> deptTenantsBySTenantIds = iDeptTenantCommonService.getDeptTenantsBySTenantIds(allTenantIds);

        // 2.1 根据租户用户Id 查询 绑定 租户部门Id
        Map<Long, List<DeptUserVO>> deptIdsByUserId = iDeptUserCommonService.getDeptUsersByUserIds(tUserIds);

        // 2.2 排除 其余租户部门信息 只保留 用户 绑定的部门信息（无论有效或禁用的租户）
        // 首先提取用户绑定的所有部门ID
        Set<Long> userDeptIds = deptIdsByUserId.values().stream()
                .flatMap(List::stream)
                .map(DeptUserVO::getTDeptId)
                .collect(Collectors.toSet());

        deptTenantsBySTenantIds = deptTenantsBySTenantIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(deptTenant -> userDeptIds.contains(deptTenant.getId()))
                                .collect(Collectors.toList())
                ));

        // 3. 获取 所有系统部门Id
        List<Long> sysDeptIds = deptTenantsBySTenantIds.values().stream()
                .flatMap(List::stream)
                .map(DeptTenantVO::getSDeptId)
                .distinct()
                .toList();

        // 3.1 根据 系统部门Id 获取 系统部门信息 Map<系统部门ID, 部门VO>
        Map<Long, SysDeptVO> sysDeptsByIds = iSysDeptCommonService.getSysDeptsByIds(sysDeptIds).stream()
                .collect(Collectors.toMap(SysDeptVO::getId, Function.identity()));

        // 3.2 获取 禁用系统部门Id [ 禁用部门 系统级 ]
        List<SysDeptVO> disabledSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> dept.getState() == null || !dept.getState())
                .toList();

        // 3.3 获取 有效系统部门 [ 有效部门 系统级 ]
        List<SysDeptVO> validSysDept = sysDeptsByIds.values().stream()
                .filter(dept -> dept.getState() != null && dept.getState())
                .toList();

        // 3.4 建立 系统部门Id->租户部门信息 键值对（包含租户ID信息）
        Map<Long, List<DeptTenantVO>> sysDeptIdToTenantDeptVOs = deptTenantsBySTenantIds.values().stream()
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
        List<Long> tenantDeptIds = deptTenantsBySTenantIds.values().stream()
                .flatMap(List::stream)
                .map(DeptTenantVO::getId)
                .distinct()
                .toList();

        // 4.1 获取 禁用租户部门Id [ 禁用部门 租户级 ]
        List<Long> disabledTenantDeptTenantIds = deptTenantsBySTenantIds.values().stream()
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
        Map<Long, List<DeptUserVO>> deptUsersByUserId = iDeptUserCommonService.getDeptUsersByUserIds(tUserIds);

        // 5.1 获取 禁用的 用户租户部门Id [ 禁用部门 用户级 ]
        List<Long> disabledUserDeptIds = deptUsersByUserId.values().stream()
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
        Map<Long, List<SysDeptVO>> tenantIdToSysDeptVOs = deptTenantsBySTenantIds.entrySet().stream()
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
        Map<Long, List<SysDeptVO>> validTenantDeptByTenant = deptTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> validTenantDeptSystemIds.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            return originalDept;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7.3 用户级有效部门 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysDeptVO>> validUserDeptByTenant = deptTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> validUserDeptIds.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
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
        Map<Long, List<SysDeptVO>> disabledTenantDeptByTenant = deptTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> disabledTenantDeptTenantIds.contains(deptTenant.getId())
                                                || cascadeDisabledDeptBySys.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            if (cascadeDisabledDeptBySys.contains(deptTenant.getId())) {
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getParentId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "system",
                                                        originalDept.getDeleted()
                                                );
                                            } else {
                                                return originalDept;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.3 用户级禁用部门 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysDeptVO>> disabledUserDeptByTenant = deptTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> disabledUserDeptIds.contains(deptTenant.getId())
                                                || cascadeDisabledDeptByTenant.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            if (cascadeDisabledDeptBySys.contains(deptTenant.getId())) {
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getParentId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "system",
                                                        originalDept.getDeleted()
                                                );
                                            } else if (disabledTenantDeptTenantIds.contains(deptTenant.getId())) {
                                                return new SysDeptVO(
                                                        originalDept.getId(),
                                                        originalDept.getParentId(),
                                                        originalDept.getName(),
                                                        originalDept.getDesc(),
                                                        originalDept.getCreatedAt(),
                                                        originalDept.getUpdatedAt(),
                                                        originalDept.getCreatedById(),
                                                        originalDept.getUpdatedById(),
                                                        originalDept.getColor(),
                                                        originalDept.getState(),
                                                        "tenant",
                                                        originalDept.getDeleted()
                                                );
                                            } else {
                                                return originalDept;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.4 获取用户所绑定的禁用租户的部门，并将这些部门信息存储到disabledDeptByTenant
        Map<Long, List<SysDeptVO>> disabledDeptByTenant = deptTenantsBySTenantIds.entrySet().stream()  // ✅ .entrySet()
                .filter(entry -> disabledTenantIds.contains(entry.getKey())) // 只处理禁用租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(deptTenant -> userDeptIds.contains(deptTenant.getId()))
                                        .map(deptTenant -> {
                                            SysDeptVO originalDept = sysDeptsByIds.get(deptTenant.getSDeptId());
                                            return new SysDeptVO(
                                                    originalDept.getId(),
                                                    originalDept.getParentId(),
                                                    originalDept.getName(),
                                                    originalDept.getDesc(),
                                                    originalDept.getCreatedAt(),
                                                    originalDept.getUpdatedAt(),
                                                    originalDept.getCreatedById(),
                                                    originalDept.getUpdatedById(),
                                                    originalDept.getColor(),
                                                    originalDept.getState(),
                                                    "tenant",
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
        Map<Long, List<RoleTenantVO>> roleTenantsBySTenantIds = iRoleTenantCommonService.getRoleTenantsByTenantIds(allTenantIds);

        // 2.1 根据租户用户Id 查询 绑定 租户角色Id
        Map<Long, List<RoleUserVO>> roleIdsByUserId = iRoleUserCommonService.getRoleUsersByUserIds(tUserIds);

        // 2.2 排除 其余租户角色信息 只保留 用户 绑定的角色信息（无论有效或禁用的租户）
        // 首先提取用户绑定的所有角色ID
        Set<Long> userRoleIds = roleIdsByUserId.values().stream()
                .flatMap(List::stream)
                .map(RoleUserVO::getTRoleId)
                .collect(Collectors.toSet());

        roleTenantsBySTenantIds = roleTenantsBySTenantIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(roleTenant -> userRoleIds.contains(roleTenant.getId()))
                                .collect(Collectors.toList())
                ));

        // 3. 获取 所有系统角色Id
        List<Long> sysRoleIds = roleTenantsBySTenantIds.values().stream()
                .flatMap(List::stream)
                .map(RoleTenantVO::getSRoleId)
                .distinct()
                .toList();

        // 3.1 根据 系统角色Id 获取 系统角色信息 Map<系统角色ID, 角色VO>
        Map<Long, SysRoleVO> sysRolesByIds = iSysRoleCommonService.getSysRolesByIds(sysRoleIds).stream()
                .collect(Collectors.toMap(SysRoleVO::getId, sysRoleVO -> sysRoleVO));

        // 3.2 获取 禁用系统角色Id [ 禁用角色 系统级 ]
        List<SysRoleVO> disabledSysRole = sysRolesByIds.values().stream()
                .filter(role -> role.getState() == null || !role.getState())
                .toList();

        // 3.3 获取 有效系统角色 [ 有效角色 系统级 ]
        List<SysRoleVO> validSysRole = sysRolesByIds.values().stream()
                .filter(role -> role.getState() != null && role.getState())
                .toList();

        // 3.4 建立 系统角色Id->租户角色信息 键值对（包含租户ID信息）
        Map<Long, List<RoleTenantVO>> sysRoleIdToTenantRoleVOs = roleTenantsBySTenantIds.values().stream()
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
        List<Long> tenantRoleIds = roleTenantsBySTenantIds.values().stream()
                .flatMap(List::stream)
                .map(RoleTenantVO::getId)
                .distinct()
                .toList();

        // 4.1 获取 禁用租户角色Id [ 禁用角色 租户级 ]
        List<Long> disabledTenantRoleTenantIds = roleTenantsBySTenantIds.values().stream()
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
        Map<Long, List<RoleUserVO>> roleUsersByUserId = iRoleUserCommonService.getRoleUsersByUserIds(tUserIds);

        // 5.1 获取 禁用的 用户租户角色Id [ 禁用角色 用户级 ]
        List<Long> disabledUserRoleIds = roleUsersByUserId.values().stream()
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
        Map<Long, List<SysRoleVO>> tenantIdToSysRoleVOs = roleTenantsBySTenantIds.entrySet().stream()
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
        Map<Long, List<SysRoleVO>> validTenantRoleByTenant = roleTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validTenantRoleSystemIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            return originalRole;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 7.3 用户级有效角色 - 按租户分组（无级联禁用，cascadeDisable = null）（只包含有效租户）
        Map<Long, List<SysRoleVO>> validUserRoleByTenant = roleTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> validUserRoleIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
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
        Map<Long, List<SysRoleVO>> disabledTenantRoleByTenant = roleTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> disabledTenantRoleTenantIds.contains(roleTenant.getId())
                                                || cascadeDisabledRoleBySys.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            if (cascadeDisabledRoleBySys.contains(roleTenant.getId())) {
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
                                                        "system",
                                                        originalRole.getDeleted()
                                                );
                                            } else {
                                                return originalRole;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.3 用户级禁用角色 - 按租户分组（需要区分直接禁用和级联禁用）（只包含有效租户）
        Map<Long, List<SysRoleVO>> disabledUserRoleByTenant = roleTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> validTenantIds.contains(entry.getKey())) // 只处理有效租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> disabledUserRoleIds.contains(roleTenant.getId())
                                                || cascadeDisabledRoleByTenant.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
                                            if (cascadeDisabledRoleBySys.contains(roleTenant.getId())) {
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
                                                        "system",
                                                        originalRole.getDeleted()
                                                );
                                            } else if (disabledTenantRoleTenantIds.contains(roleTenant.getId())) {
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
                                                        "tenant",
                                                        originalRole.getDeleted()
                                                );
                                            } else {
                                                return originalRole;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()),
                                Collectors.flatMapping(List::stream, Collectors.toList())
                        )
                ));

        // 8.4 获取用户所绑定的禁用租户的角色，并将这些角色信息存储到disabledRoleByTenant
        Map<Long, List<SysRoleVO>> disabledRoleByTenant = roleTenantsBySTenantIds.entrySet().stream()
                .filter(entry -> disabledTenantIds.contains(entry.getKey())) // 只处理禁用租户
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                entry -> entry.getValue().stream()
                                        .filter(roleTenant -> userRoleIds.contains(roleTenant.getId()))
                                        .map(roleTenant -> {
                                            SysRoleVO originalRole = sysRolesByIds.get(roleTenant.getSRoleId());
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
                                                    "tenant",
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