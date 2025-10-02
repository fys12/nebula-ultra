package com.cloudvalley.nebula.ultra.business.tenant.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantDetailsVO;
import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantSubscribeDetailsVO;
import com.cloudvalley.nebula.ultra.business.tenant.service.ITenantAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tenantAggregator")
public class TenantAggregatorController {

    @Autowired
    private ITenantAggregatorService iTenantAggregatorService;

    /**
     * 获取租户详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户详情信息
     */
    @GetMapping("/tenant/{current}/{size}")
    public SaResult getTenantInfo(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<TenantDetailsVO> tenantDetailsVO = iTenantAggregatorService.getTenantInfo(current, size);
        return SaResult.ok("租户详情信息列表获取成功").setData(tenantDetailsVO);
    }

    /**
     * 获取租户订阅（租户套餐）详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户订阅（租户套餐）
     */
    @GetMapping("/subscription/{current}/{size}")
    public SaResult getTenantSubscribeInfo(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<TenantSubscribeDetailsVO> subscriptionInfo = iTenantAggregatorService.getTenantSubscribeInfo(current, size);
        return SaResult.ok("租户订阅详情信息列表获取成功").setData(subscriptionInfo);
    }

}
