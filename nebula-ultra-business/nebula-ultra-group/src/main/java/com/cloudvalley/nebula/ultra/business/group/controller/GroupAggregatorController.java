package com.cloudvalley.nebula.ultra.business.group.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupAggregator")
public class GroupAggregatorController {

    @Autowired
    private IGroupAggregatorService iGroupAggregatorService;

    /**
     * 获取 权限组 详细 详细
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 详细 详细
     */
    @GetMapping("/perm/{current}/{size}")
    public SaResult getPermGroupDetails(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<GroupPermDetailsVO> groupPermDetailsVO = iGroupAggregatorService.getPermGroupInfo(current, size);
        return SaResult.ok("权限组详情信息列表获取成功").setData(groupPermDetailsVO);
    }

}
