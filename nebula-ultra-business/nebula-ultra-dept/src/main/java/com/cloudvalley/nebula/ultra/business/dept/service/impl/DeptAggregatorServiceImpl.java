package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptDetailsVO;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptListVO;
import com.cloudvalley.nebula.ultra.business.dept.service.IDeptAggregatorService;
import com.cloudvalley.nebula.ultra.business.dept.service.ISysDeptService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.UserTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.service.IUserTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeptAggregatorServiceImpl implements IDeptAggregatorService {

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysDeptCommonService iSysDeptCommonService;

    @Autowired
    private IDeptUserCommonService iDeptUserCommonService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    @Autowired
    private IDeptTenantCommonService iDeptTenantCommonService;

    @Autowired
    private IUserTenantCommonService iUserTenantCommonService;

    /**
     * 获取部门树列表
     * @param current 当前页
     * @param size 每页数量
     * @return 部门树列表
     */
    @Override
    public IPage<DeptListVO> getDeptTree(Integer current, Integer size) {
        // 1. 查询 部门 基本信息
        IPage<SysDeptVO> sysDeptList = iSysDeptService.getSysDeptList(new Page<>(current, size));

        // 2. 获取所有部门信息，构建ID到部门的映射
        List<SysDeptVO> allDepts = iSysDeptCommonService.getAllSysDepts();
        Map<Long, SysDeptVO> deptMap = allDepts.stream()
                .collect(Collectors.toMap(SysDeptVO::getId, dept -> dept));

        // 3. 构建父ID到子部门列表的映射
        Map<Long, List<SysDeptVO>> parentToChildrenMap = allDepts.stream()
                .filter(dept -> dept.getParentId() != null)
                .collect(Collectors.groupingBy(SysDeptVO::getParentId));

        // 4. 获取所有部门的用户数量
        List<Long> sDeptIds = allDepts.stream()
                .map(SysDeptVO::getId)
                .collect(Collectors.toList());

        // 获取系统部门ID到租户部门ID的映射
        Map<Long, List<DeptTenantVO>> sDeptIdToTDeptIds = iDeptTenantCommonService.getDeptTenantsBySDeptIds(sDeptIds);

        // 提取所有租户部门ID
        List<Long> tDeptIds = sDeptIdToTDeptIds.values().stream()
                .flatMap(List::stream)
                .map(DeptTenantVO::getId)
                .collect(Collectors.toList());

        // 根据租户部门ID查询用户数量
        Map<Long, Integer> deptUserCountMap = iDeptUserCommonService.getDeptUserCount(tDeptIds);

        // 构建系统部门ID到用户数量的映射
        Map<Long, Integer> sDeptIdToUserCountMap = sDeptIdToTDeptIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(DeptTenantVO::getId)
                                .map(tDeptId -> deptUserCountMap.getOrDefault(tDeptId, 0))
                                .reduce(0, Integer::sum)
                ));

        // 5. 对分页结果中的每个部门构建完整的树形结构
        List<DeptListVO> deptTreeList = sysDeptList.getRecords().stream()
                .map(dept -> buildDeptTree(dept, deptMap, parentToChildrenMap, sDeptIdToTDeptIds, sDeptIdToUserCountMap, 0))
                .collect(Collectors.toList());

        // 6. 创建新的分页对象并返回
        IPage<DeptListVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(deptTreeList);
        resultPage.setTotal(sysDeptList.getTotal());
        resultPage.setPages(sysDeptList.getPages());

        return resultPage;
    }

    /**
     * 递归构建部门树（使用租户部门信息）
     * @param dept 当前部门
     * @param deptMap 部门ID到部门的映射
     * @param parentToChildrenMap 父ID到子部门列表的映射
     * @param sDeptIdToTDeptIds 系统部门ID到租户部门列表的映射
     * @param sDeptIdToUserCountMap 系统部门ID到用户数量的映射
     * @return 部门树
     */
    private DeptListVO buildDeptTree(SysDeptVO dept, Map<Long, SysDeptVO> deptMap,
                                                   Map<Long, List<SysDeptVO>> parentToChildrenMap,
                                                   Map<Long, List<DeptTenantVO>> sDeptIdToTDeptIds,
                                                   Map<Long, Integer> sDeptIdToUserCountMap, int depth) {
        // 1. 获取当前部门的子部门列表
        List<SysDeptVO> childDepts = parentToChildrenMap.getOrDefault(dept.getId(), new ArrayList<>());

        // 2. 递归构建子部门树
        List<DeptListVO> childDeptVOs = childDepts.stream()
                .map(childDept -> buildDeptTree(childDept, deptMap, parentToChildrenMap, sDeptIdToTDeptIds, sDeptIdToUserCountMap, depth + 1))
                .collect(Collectors.toList());

        // 3. 获取当前部门的用户数量，默认为0
        Integer userCount = sDeptIdToUserCountMap.getOrDefault(dept.getId(), 0);

        // 4. 获取当前部门对应的租户部门ID列表
        List<DeptTenantVO> tDeptList = sDeptIdToTDeptIds.getOrDefault(dept.getId(), new ArrayList<>());

        // 5. 如果存在租户部门，使用第一个租户部门的ID作为部门ID；否则使用系统部门ID
        Long deptId = dept.getId();
        if (!tDeptList.isEmpty()) {
            deptId = tDeptList.get(0).getId(); // 使用第一个租户部门ID
        }

        // 6. 构建当前部门的DeptListVO
        return new DeptListVO(
                deptId, // 使用租户部门ID
                depth > 0 ? deptId + "-" + depth : deptId.toString(), // displayId
                dept.getName(),
                dept.getColor(),
                userCount,
                childDeptVOs
        );
    }

    /**
     * 获取部门详情
     * @param tDeptId 租户部门id
     * @return 部门详情
     */
    @Override
    public DeptDetailsVO getDeptDetails(Long tDeptId) {
        // 1. 根据 租户部门Id 查询 对应 租户部门信息
        DeptTenantVO tenantDept = iDeptTenantCommonService.getDeptTenantById(tDeptId);

        // 2. 根据 系统部门Id 查询 部门基本信息
        SysDeptVO sysDept = iSysDeptCommonService.getSysDeptById(tenantDept.getSDeptId());

        // 3. 获取部门绑定的用户ID列表
        List<Long> userIds = new ArrayList<>(iDeptUserCommonService.getUserIdsByDeptId(tDeptId));

        // 3.1 根据 租户用户Id列表 查询 租户用户信息
        List<UserTenantVO> userTenantsByIds = iUserTenantCommonService.getUserTenantsByIds(userIds);

        // 3.2 获取 系统用户Id
        List<Long> sysUserIds = userTenantsByIds.stream()
                .map(UserTenantVO::getSUserId)
                .toList();

        // 4. 根据 系统用户ID列表 查询 用户详细信息
        List<SysUserVO> bandUsers = new ArrayList<>();
        if (!userIds.isEmpty()) {
            bandUsers = iSysUserCommonService.getUsersByIds(sysUserIds);
        }

        // 5. 获取创建人和更新人用户名
        Map<Long, SysUserVO> userMap = iSysUserCommonService.getUsersByIds(new ArrayList<>(Arrays.asList(sysDept.getCreatedById(), sysDept.getUpdatedById()))).stream()
                .collect(Collectors.toMap(SysUserVO::getId, sysUserVO -> sysUserVO));

        // 6. 构建并返回部门详情VO
        return new DeptDetailsVO(
                tenantDept.getId(),
                sysDept.getName(),
                sysDept.getDesc(),
                tenantDept.getCreatedAt(),
                tenantDept.getUpdatedAt(),
                userMap.get(sysDept.getCreatedById()),
                userMap.get(sysDept.getUpdatedById()),
                bandUsers,
                tenantDept.getColor(),
                tenantDept.getState(),
                tenantDept.getDeleted()
        );
    }

}
