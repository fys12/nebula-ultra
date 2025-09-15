package com.cloudvalley.nebula.ultra.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.perm.converter.SysPermConverter;
import com.cloudvalley.nebula.ultra.business.perm.mapper.SysPermMapper;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.SysPerm;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.ISysPermCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysPermCommonServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements ISysPermCommonService {

    @Autowired
    private SysPermConverter sysPermConverter;

    /**
     * 根据ID查询单个系统权限信息
     * @param id 系统权限唯一标识ID
     * @return 对应的 SysPermVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public SysPermVO getSysPermById(Long id) {
        LambdaQueryWrapper<SysPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPerm::getId, id)
                .eq(SysPerm::getDeleted, false);

        SysPerm sysPerm = this.getOne(queryWrapper);
        return sysPerm != null ? sysPermConverter.EnToVO(sysPerm) : null;
    }

    /**
     * 根据多个系统权限ID全量查询权限信息（不分页）
     * @param ids 系统权限ID列表
     * @return 所有匹配的 SysPermVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<SysPermVO> getSysPermsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<SysPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysPerm::getId, ids)
                .eq(SysPerm::getDeleted, false)
                .orderByDesc(SysPerm::getCreatedAt);
        List<SysPerm> list = this.list(queryWrapper);
        return sysPermConverter.EnListToVOList(list);
    }

    /**
     * 获取所有系统权限
     * @return SysPermVO
     */
    @Override
    public List<SysPermVO> getSysPerms() {
        LambdaQueryWrapper<SysPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPerm::getDeleted, false)
                .orderByDesc(SysPerm::getCreatedAt);
        List<SysPerm> list = this.list(queryWrapper);
        return sysPermConverter.EnListToVOList(list);
    }

}
