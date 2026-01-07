package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.group.converter.SysGroupConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.SysGroup;
import com.cloudvalley.nebula.ultra.business.group.mapper.SysGroupMapper;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import com.cloudvalley.nebula.ultra.shared.api.group.service.ISysGroupCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SysGroupCommonServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements ISysGroupCommonService {

    @Autowired
    private SysGroupConverter sysGroupConverter;

    /**
     * 根据系统组ID查询系统组（单个）
     * @param id 系统组ID
     * @return 系统组信息
     */
    @Override
    public SysGroupVO getSysGroupById(Long id) {
        LambdaQueryWrapper<SysGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysGroup::getId, id)
                .eq(SysGroup::getDeleted, false);

        SysGroup sysGroup = this.getOne(queryWrapper);
        return sysGroup != null ? sysGroupConverter.EnToVO(sysGroup) : null;
    }

    /**
     * 根据系统组ID批量查询系统组 [全量]
     * @param ids 系统组ID列表
     * @return 系统组信息
     */
    @Override
    public List<SysGroupVO> getSysGroupsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysGroup::getId, ids)
                .eq(SysGroup::getDeleted, false)
                .orderByDesc(SysGroup::getCreatedAt);
        List<SysGroup> list = this.list(queryWrapper);
        return sysGroupConverter.EnListToVOList(list);
    }

}
