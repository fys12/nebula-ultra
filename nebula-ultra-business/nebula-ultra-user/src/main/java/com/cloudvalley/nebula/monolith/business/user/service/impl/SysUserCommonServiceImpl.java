package com.cloudvalley.nebula.monolith.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.user.converter.SysUserConverter;
import com.cloudvalley.nebula.monolith.business.user.model.entity.SysUser;
import com.cloudvalley.nebula.monolith.business.user.mapper.SysUserMapper;
import com.cloudvalley.nebula.monolith.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.monolith.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
     * @return 用户信息 Map<用户Id 用户VO>
     */
    @Override
    public Map<Long, SysUserVO> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId, ids)
                .eq(SysUser::getDeleted, false);

        List<SysUser> users = this.list(queryWrapper);
        List<SysUserVO> voList = sysUserConverter.EnListToVOList(users);

        return voList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        SysUserVO::getId,
                        vo -> vo
                ));
    }

}
