package com.cloudvalley.nebula.ultra.business.combo.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.combo.model.vo.ComboDetailsVO;
import com.cloudvalley.nebula.ultra.business.combo.service.IComboAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comboAggregator")
public class ComboAggregatorController {

    @Autowired
    private IComboAggregatorService iComboAggregatorService;

    /**
     * 获取 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param comboId 套餐ID
     * @return 套餐详情信息
     */
    @GetMapping("/combo/detail/{comboId}")
    public SaResult getComboDetails(@PathVariable Long comboId) {
        if (comboId == null) {
            return SaResult.error("参数缺失");
        }
        ComboDetailsVO comboDetailsVO = iComboAggregatorService.getComboDetails(comboId);
        return SaResult.ok("套餐详情信息列表获取成功").setData(comboDetailsVO);
    }

}
