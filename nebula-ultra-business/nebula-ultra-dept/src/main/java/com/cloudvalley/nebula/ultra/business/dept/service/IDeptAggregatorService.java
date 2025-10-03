package com.cloudvalley.nebula.ultra.business.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptDetailsVO;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptListVO;

public interface IDeptAggregatorService {

    /**
     * 获取部门树列表
     * @param current 当前页
     * @param size 每页数量
     * @return 部门树列表
     */
    IPage<DeptListVO> getDeptTree(Integer current, Integer size);

    /**
     * 获取部门详情
     * @param tDeptId 部门Id
     * @return 部门详情
     */
    DeptDetailsVO getDeptDetails(Long tDeptId);
}
