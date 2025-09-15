package com.cloudvalley.nebula.ultra.shared.api.perm.service;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISysPermCommonService {

    /**
     * 根据id查询系统权限
     * @param id 系统权限ID
     * @return 系统权限信息
     */
    SysPermVO getSysPermById(Long id);

    /**
     * 根据多个系统权限ID全量查询权限信息（不分页）
     * @param ids 系统权限ID列表
     * @return 所有匹配的 SysPermVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<SysPermVO> getSysPermsByIds(List<Long> ids);

    /**
     * 获取所有系统权限
     * @return SysPermVO
     */
    List<SysPermVO> getSysPerms();


}
