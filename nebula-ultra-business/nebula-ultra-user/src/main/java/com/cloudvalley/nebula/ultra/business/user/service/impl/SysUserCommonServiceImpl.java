package com.cloudvalley.nebula.ultra.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.user.converter.SysUserConverter;
import com.cloudvalley.nebula.ultra.business.user.model.entity.SysUser;
import com.cloudvalley.nebula.ultra.business.user.mapper.SysUserMapper;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserCommonServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserCommonService {

    @Autowired
    private SysUserConverter sysUserConverter;

    /**
     * 根据ID查询系统用户（单个）
     * @param id 用户ID
     * @return 用户信息VO，若不存在或已删除则返回 null
     */
    @Override
    public SysUserVO getUserById(Long id) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId, id)
                .eq(SysUser::getDeleted, false);

        // 使用父类方法查询单条记录
        SysUser user = this.getOne(queryWrapper);
        return user != null ? sysUserConverter.EnToVO(user) : null;
    }

    /**
     * 根据ID查询系统用户（多个）
     * @param ids 用户ID
     * @return 用户信息
     */
    @Override
    public List<SysUserVO> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId, ids)
                .eq(SysUser::getDeleted, false);

        List<SysUser> users = this.list(queryWrapper);
        return sysUserConverter.EnListToVOList(users);
    }

}
