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
     * 获取租 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param current 当前页
     * @param size 每页数量
     * @return 套餐详情信息
     */
    @GetMapping("/combo/{current}/{size}")
    public SaResult getComboInfo(@PathVariable Integer current, @PathVariable Integer size) {
        if (current == null || size == null) {
            return SaResult.error("参数缺失");
        }
        IPage<ComboDetailsVO> comboDetailsVO = iComboAggregatorService.getComboInfo(current, size);
        return SaResult.ok("套餐详情信息列表获取成功").setData(comboDetailsVO);
    }

}
