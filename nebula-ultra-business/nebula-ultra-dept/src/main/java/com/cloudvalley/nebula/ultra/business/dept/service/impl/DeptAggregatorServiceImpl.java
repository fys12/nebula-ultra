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
        List<Long> deptIds = allDepts.stream().map(SysDeptVO::getId).collect(Collectors.toList());
        Map<Long, Integer> deptUserCountMap = iDeptUserCommonService.getDeptUserCount(deptIds);

        // 5. 对分页结果中的每个部门构建完整的树形结构
        List<DeptListVO> deptTreeList = sysDeptList.getRecords().stream()
                .map(dept -> buildDeptTree(dept, deptMap, parentToChildrenMap, deptUserCountMap))
                .collect(Collectors.toList());

        // 6. 创建新的分页对象并返回
        IPage<DeptListVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(deptTreeList);
        resultPage.setTotal(sysDeptList.getTotal());
        resultPage.setPages(sysDeptList.getPages());

        return resultPage;
    }

    /**
     * 递归构建部门树
     * @param dept 当前部门
     * @param deptMap 部门ID到部门的映射
     * @param parentToChildrenMap 父ID到子部门列表的映射
     * @param deptUserCountMap 部门ID到用户数量的映射
     * @return 部门树
     */
    private DeptListVO buildDeptTree(SysDeptVO dept, Map<Long, SysDeptVO> deptMap,
                                     Map<Long, List<SysDeptVO>> parentToChildrenMap,
                                     Map<Long, Integer> deptUserCountMap) {
        // 1. 获取当前部门的子部门列表
        List<SysDeptVO> childDepts = parentToChildrenMap.getOrDefault(dept.getId(), new ArrayList<>());

        // 2. 递归构建子部门树
        List<DeptListVO> childDeptVOs = childDepts.stream()
                .map(childDept -> buildDeptTree(childDept, deptMap, parentToChildrenMap, deptUserCountMap))
                .collect(Collectors.toList());

        // 3. 获取当前部门的用户数量，默认为0
        Integer userCount = deptUserCountMap.getOrDefault(dept.getId(), 0);

        // 4. 构建当前部门的DeptListVO
        return new DeptListVO(
                dept.getId(),
                dept.getName(),
                dept.getColor(),
                userCount,
                childDeptVOs
        );
    }

    /**
     * 获取部门详情
     * @param deptId 部门id
     * @param tenantId 租户Id
     * @return 部门详情
     */
    @Override
    public DeptDetailsVO getDeptDetails(Long deptId, Long tenantId) {
        // 1. 获取部门基本信息
        SysDeptVO dept = iSysDeptCommonService.getSysDeptById(deptId);
        if (dept == null) {
            return null;
        }

        // 2. 根据 系统部门Id和租户Id 查询 对应 租户部门信息
        DeptTenantVO deptTenantBySTenantIdAndSDeptId = iDeptTenantCommonService.getDeptTenantBySTenantIdAndSDeptId(tenantId, deptId);

        // 2.1 获取 租户部门Id
        Long tDeptId = deptTenantBySTenantIdAndSDeptId.getId();

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
        Map<Long, SysUserVO> userMap = iSysUserCommonService.getUsersByIds(new ArrayList<>(Arrays.asList(dept.getCreatedById(), dept.getUpdatedById()))).stream()
                .collect(Collectors.toMap(SysUserVO::getId, sysUserVO -> sysUserVO));

        // 6. 构建并返回部门详情VO
        return new DeptDetailsVO(
                dept.getId(),
                dept.getName(),
                dept.getDesc(),
                dept.getCreatedAt(),
                dept.getUpdatedAt(),
                userMap.get(dept.getCreatedById()),
                userMap.get(dept.getUpdatedById()),
                bandUsers,
                dept.getColor(),
                dept.getState(),
                dept.getDeleted()
        );
    }

}
