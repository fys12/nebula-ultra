package com.cloudvalley.nebula.ultra.shared.api.dept.service;


import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;

import java.util.List;
import java.util.Map;

public interface ISysDeptCommonService {

    /**
     * 获取所有部门信息
     * @return 所有部门信息
     */
    List<SysDeptVO> getAllSysDepts();

    /**
     * 根据ID查询单个系统部门。
     * @param id 部门ID
     * @return 对应的部门VO，不存在则返回null
     */
    SysDeptVO getSysDeptById(Long id);

    /**
     * 根据ID列表查询多个系统部门。
     * @param ids 部门ID列表
     * @return 对应的部门VO列表
     */
     List<SysDeptVO> getSysDeptsByIds(List<Long> ids);

}
