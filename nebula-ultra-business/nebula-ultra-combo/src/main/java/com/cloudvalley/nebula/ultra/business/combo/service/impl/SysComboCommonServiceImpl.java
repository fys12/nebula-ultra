package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.combo.converter.SysComboConverter;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.SysCombo;
import com.cloudvalley.nebula.ultra.business.combo.mapper.SysComboMapper;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.ISysComboCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.SysComboVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SysComboCommonServiceImpl extends ServiceImpl<SysComboMapper, SysCombo> implements ISysComboCommonService {

    @Autowired
    private SysComboConverter sysComboConverter;

    /**
     * 根据ID查询系统套餐（单个）
     *
     * @param id 系统套餐的ID
     * @return 符合条件的系统套餐VO对象，若不存在则返回null
     */
    @Override
    public SysComboVO getSysComboById(Long id) {
        LambdaQueryWrapper<SysCombo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCombo::getId, id)
                .eq(SysCombo::getDeleted, false);
        SysCombo entity = this.getOne(queryWrapper);
        return entity != null ? sysComboConverter.EnToVO(entity) : null;
    }

    /**
     * 根据ID批量查询系统套餐 [全量]
     *
     * @param ids 系统套餐的ID列表
     * @return 符合条件的系统套餐VO列表，若ID列表为空则返回空列表
     */
    @Override
    public List<SysComboVO> getSysCombosByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysCombo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysCombo::getId, ids)
                .eq(SysCombo::getDeleted, false)
                .orderByDesc(SysCombo::getCreatedAt);
        List<SysCombo> list = this.list(queryWrapper);
        return sysComboConverter.EnListToVOList(list);
    }

}
