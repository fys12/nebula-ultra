package com.cloudvalley.nebula.ultra.business.dept.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptDetailsVO;
import com.cloudvalley.nebula.ultra.business.dept.model.vo.DeptListVO;
import com.cloudvalley.nebula.ultra.business.dept.service.IDeptAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deptAggregator")
public class DeptAggregatorController {

    @Autowired
    private IDeptAggregatorService iDeptAggregatorService;

    /**
     * 获取部门树列表
     * @param current 当前页
     * @param size 每页数量
     * @return 部门树列表
     */
    @GetMapping("/deptTree/{current}/{size}")
    public SaResult getDeptTree(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<DeptListVO> deptTree = iDeptAggregatorService.getDeptTree(current, size);
        return SaResult.ok("部门树列表获取成功").setData(deptTree);
    }

    /**
     * 获取部门详情
     * @param tDeptId 部门Id
     * @return 部门详情
     */
    @GetMapping("/dept/{tDeptId}")
    public SaResult getDeptDetails(@PathVariable Long tDeptId) {
        if (tDeptId == null) {
            return SaResult.error("参数缺失");
        }
        DeptDetailsVO deptDetails = iDeptAggregatorService.getDeptDetails(tDeptId);
        return SaResult.ok("部门详情获取成功").setData(deptDetails);
    }

}
