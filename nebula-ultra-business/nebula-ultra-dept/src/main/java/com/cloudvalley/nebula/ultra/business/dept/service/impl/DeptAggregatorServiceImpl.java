package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptListVO;
import com.cloudvalley.nebula.ultra.business.dept.service.IDeptAggregatorService;
import com.cloudvalley.nebula.ultra.business.dept.service.ISysDeptService;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.IDeptUserCommonService;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
