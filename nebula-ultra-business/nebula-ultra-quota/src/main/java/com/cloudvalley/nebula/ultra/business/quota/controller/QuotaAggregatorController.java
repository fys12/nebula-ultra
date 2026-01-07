package com.cloudvalley.nebula.ultra.business.quota.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.quota.model.vo.QuoatDetailsVO;
import com.cloudvalley.nebula.ultra.business.quota.service.IQuotaAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotaAggregator")
public class QuotaAggregatorController {

    @Autowired
    private IQuotaAggregatorService iQuotaAggregatorService;

    /**
     * 获取租户 配额 信息
     * @param current 当前页
     * @param size 每页数量
     * @return 租户配额信息
     */
    @GetMapping("/tenant/quota/{current}/{size}")
    public SaResult getTenantQuotaInfo(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<QuoatDetailsVO> quoatDetailsVO = iQuotaAggregatorService.getTenantQuotaInfo(current, size);
        return SaResult.ok("租户配额详情信息列表获取成功").setData(quoatDetailsVO);
    }

}
