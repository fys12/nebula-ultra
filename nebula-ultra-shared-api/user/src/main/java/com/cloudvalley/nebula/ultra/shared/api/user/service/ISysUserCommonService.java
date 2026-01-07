package com.cloudvalley.nebula.ultra.shared.api.user.service;


import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;

import java.util.List;
import java.util.Map;

public interface ISysUserCommonService {

    /**
     * 根据ID查询系统用户（单个）
     * @param id 用户ID
     * @return 用户信息
     */
    SysUserVO getUserById(Long id);

    /**
     * 根据ID查询系统用户（多个）
     * @param ids 用户ID
     * @return 用户信息
     */
    List<SysUserVO> getUsersByIds(List<Long> ids);

}
