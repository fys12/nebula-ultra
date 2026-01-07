package com.cloudvalley.nebula.ultra.business.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.user.model.dto.LoginDTO;
import com.cloudvalley.nebula.ultra.business.user.model.vo.UserAggregatorInfoVO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;

public interface IUserAggregatorService {

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录用户信息
     */
    SysUserVO login(LoginDTO loginDTO);

    /**
     * 获取用户聚合信息 [ 用户 所属部门 角色 ] 分页
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户聚合分页信息
     */
    IPage<UserAggregatorInfoVO> getUserAggregatorInfo(Integer current, Integer size);

}
