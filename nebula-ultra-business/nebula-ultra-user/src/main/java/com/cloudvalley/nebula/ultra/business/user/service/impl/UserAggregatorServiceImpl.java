package com.cloudvalley.nebula.ultra.business.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.model.vo.UserAggregatorInfoVO;
import com.cloudvalley.nebula.ultra.business.user.service.ISysUserService;
import com.cloudvalley.nebula.ultra.business.user.service.IUserAggregatorService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.service.IRoleUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.service.ISysTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.service.IUserTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserAggregatorServiceImpl implements IUserAggregatorService {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    @Autowired
    private IUserTenantCommonService iUserTenantCommonService;

    @Autowired
    private ISysTenantCommonService iSysTenantCommonService;

    @Autowired
    private IDeptUserCommonService iDeptUserCommonService;

    @Autowired
    private ISysDeptCommonService iSysDeptCommonService;

    @Autowired
    private IDeptTenantCommonService iDeptTenantCommonService;

    @Autowired
    private IRoleUserCommonService iRoleUserCommonService;

    @Autowired
    private ISysRoleCommonService iSysRoleCommonService;

    @Autowired
    private IRoleTenantCommonService iRoleTenantCommonService;

    /**
     * 用户登录
     * @param username 用户名
     * @param passwordHash 密码
     * @return 登录用户信息
     */
    @Override
    public SysUserVO login(String username, String passwordHash) {
        // 1. 登录 根据用户名和密码(hash加密后)查询用户信息
        SysUserVO userInfo = iSysUserService.getUserByUsernameAndPasswordHash(username, passwordHash);

        // 2. 登录失败
        if (userInfo == null) {
            return null;
        }

        // 3. 登录成功(用户信息不为空)  权限校验

        // 4. 部门校验

        // 5. 角色校验

        return null;
    }

    /**
     * 获取用户聚合信息 [ 用户 所属部门 角色 ] 分页
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户聚合分页信息
     */
    @Override
    public IPage<UserAggregatorInfoVO> getUserAggregatorInfo(Integer current, Integer size) {
        // 1. 查询系统用户列表
        IPage<SysUserVO> userList = iSysUserService.getUserList(new Page<>(current, size));

        // 2. 获取系统用户ID列表
        List<Long> systemUserIds = userList.getRecords().stream()
                .map(SysUserVO::getId)
                .toList();

        // 3. 根据系统用户ID查询用户-租户绑定关系，得到租户用户关联信息
        List<Map<Long, List<UserTenantVO>>> userTenantMaps = iUserTenantCommonService.getUserTenantsByUserIds(systemUserIds);
        Map<Long, List<UserTenantVO>> userTenantMap = userTenantMaps.isEmpty() ? Map.of() : userTenantMaps.get(0);

        // 4. 获取所有租户用户ID（UserTenant表的主键，作为tUserId使用）
        List<Long> tenantUserIds = userTenantMap.values().stream()
                .flatMap(List::stream)
                .map(UserTenantVO::getId)  // 这个ID是租户用户表的主键，就是tUserId
                .distinct()
                .toList();

        // 5. 获取所有租户ID
        Set<Long> tenantIds = userTenantMap.values().stream()
                .flatMap(List::stream)
                .map(UserTenantVO::getSTenantId)
                .collect(Collectors.toSet());

        // 6. 根据租户ID查询租户信息
        Map<Long, SysTenantVO> sysTenantsByIds = iSysTenantCommonService.getSysTenantsByIds(new ArrayList<>(tenantIds));

        // ======== 部门信息查询 ========
        // 7. 根据租户用户ID查询用户-部门绑定关系，得到租户部门ID
        Map<Long, Set<Long>> tenantDeptIdsByTenantUserIds = iDeptUserCommonService.getDeptIdsByUserIds(tenantUserIds);

        // 8. 获取所有租户部门ID
        List<Long> tenantDeptIds = tenantDeptIdsByTenantUserIds.values().stream()
                .flatMap(Set::stream)
                .distinct()
                .toList();

        // 9. 根据租户部门ID查询部门-租户绑定关系，得到系统部门ID
        Map<Long, List<DeptTenantVO>> deptTenantMap = iDeptTenantCommonService.getDeptTenantsBySTenantIds(new ArrayList<>(tenantIds));

        // 10. 建立租户部门ID到系统部门ID的映射关系
        Map<Long, Long> tenantDeptToSystemDeptMap = deptTenantMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(DeptTenantVO::getId, DeptTenantVO::getSDeptId, (v1, v2) -> v1));

        // 11. 获取所有系统部门ID
        List<Long> systemDeptIds = deptTenantMap.values().stream()
                .flatMap(List::stream)
                .map(DeptTenantVO::getSDeptId)
                .distinct()
                .toList();

        // 12. 根据系统部门ID查询系统部门信息
        Map<Long, SysDeptVO> sysDeptsByIds = iSysDeptCommonService.getSysDeptsByIds(systemDeptIds);

        // ======== 角色信息查询 ========
        // 13. 根据租户用户ID查询用户-角色绑定关系，得到租户角色ID
        Map<Long, Set<Long>> tenantRoleIdsByTenantUserIds = iRoleUserCommonService.getRoleIdsByUserIds(tenantUserIds);

        // 14. 获取所有租户角色ID
        List<Long> tenantRoleIds = tenantRoleIdsByTenantUserIds.values().stream()
                .flatMap(Set::stream)
                .distinct()
                .toList();

        // 15. 根据租户角色ID查询角色-租户绑定关系，得到系统角色ID
        Map<Long, List<RoleTenantVO>> roleTenantMap = iRoleTenantCommonService.getRoleTenantsByTenantIds(new ArrayList<>(tenantIds));

        // 16. 建立租户角色ID到系统角色ID的映射关系
        Map<Long, Long> tenantRoleToSystemRoleMap = roleTenantMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(RoleTenantVO::getId, RoleTenantVO::getSRoleId, (v1, v2) -> v1));

        // 17. 获取所有系统角色ID
        List<Long> systemRoleIds = roleTenantMap.values().stream()
                .flatMap(List::stream)
                .map(RoleTenantVO::getSRoleId)
                .distinct()
                .toList();

        // 18. 根据系统角色ID查询系统角色信息
        Map<Long, SysRoleVO> sysRolesByIds = iSysRoleCommonService.getSysRolesByIds(systemRoleIds);

        // 19. 获取创建人ID和更新人ID
        List<Long> creatorAndUpdaterIds = userList.getRecords().stream()
                .flatMap(user -> Stream.of(user.getCreatedById(), user.getUpdatedById()))
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 20. 根据创建人ID和更新人ID获取用户信息
        Map<Long, SysUserVO> usersByIds = iSysUserCommonService.getUsersByIds(creatorAndUpdaterIds);

        // 21. 组装用户聚合信息
        List<UserAggregatorInfoVO> userAggregatorInfoList = userList.getRecords().stream()
                .map(user -> {
                    Long systemUserId = user.getId();

                    // 获取该系统用户的租户绑定信息
                    List<UserTenantVO> userTenants = userTenantMap.getOrDefault(systemUserId, List.of());

                    // 获取租户信息
                    List<SysTenantVO> tenantInfo = userTenants.stream()
                            .map(ut -> sysTenantsByIds.get(ut.getSTenantId()))
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    // 获取该系统用户在各个租户下的租户用户ID
                    List<Long> userTenantIds = userTenants.stream()
                            .map(UserTenantVO::getId)
                            .toList();

                    // 获取该用户的部门信息
                    List<SysDeptVO> deptInfo = userTenantIds.stream()
                            .flatMap(tUserId -> tenantDeptIdsByTenantUserIds.getOrDefault(tUserId, Set.of()).stream())
                            .map(tenantDeptToSystemDeptMap::get)  // 租户部门ID转系统部门ID
                            .filter(Objects::nonNull)
                            .map(sysDeptsByIds::get)  // 系统部门ID转系统部门信息
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    // 获取该用户的角色信息
                    List<SysRoleVO> roleInfo = userTenantIds.stream()
                            .flatMap(tUserId -> tenantRoleIdsByTenantUserIds.getOrDefault(tUserId, Set.of()).stream())
                            .map(tenantRoleToSystemRoleMap::get)  // 租户角色ID转系统角色ID
                            .filter(Objects::nonNull)
                            .map(sysRolesByIds::get)  // 系统角色ID转系统角色信息
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    // 获取创建人和更新人的用户名
                    String createdByUser = null;
                    String updatedByUser = null;

                    if (user.getCreatedById() != null) {
                        SysUserVO createdByUserVO = usersByIds.get(user.getCreatedById());
                        createdByUser = createdByUserVO != null ? createdByUserVO.getUsername() : null;
                    }

                    if (user.getUpdatedById() != null) {
                        SysUserVO updatedByUserVO = usersByIds.get(user.getUpdatedById());
                        updatedByUser = updatedByUserVO != null ? updatedByUserVO.getUsername() : null;
                    }

                    // 创建UserAggregatorInfoVO实例
                    return new UserAggregatorInfoVO(
                            user.getId(),
                            user.getUsername(),
                            user.getPasswordHash(),
                            user.getMfaSecret(),
                            tenantInfo,
                            deptInfo,
                            roleInfo,
                            user.getLastLoginAt(),
                            user.getCreatedAt(),
                            user.getUpdatedAt(),
                            createdByUser,
                            updatedByUser,
                            user.getColor(),
                            user.getState(),
                            user.getDeleted()
                    );
                })
                .toList();

        // 创建新的分页对象并返回
        IPage<UserAggregatorInfoVO> result = new Page<>(userList.getCurrent(), userList.getSize(), userList.getTotal());
        result.setRecords(userAggregatorInfoList);

        return result;
    }
}
