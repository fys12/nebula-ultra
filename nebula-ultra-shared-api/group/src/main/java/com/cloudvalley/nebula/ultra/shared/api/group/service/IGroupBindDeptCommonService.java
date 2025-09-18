package com.cloudvalley.nebula.ultra.shared.api.group.service;


import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGroupBindDeptCommonService {

    /**
     * 根据ID查询绑定关系（单个）
     * @param id 主键ID
     * @return 绑定信息
     */
    GroupBindDeptVO getGroupBindDeptById(Long id);

    /**
     * 根据ID批量查询绑定关系 [全量]
     * @param ids 主键ID列表
     * @return 绑定信息
     */
    List<GroupBindDeptVO> getGroupBindDeptsByIds(List<Long> ids);

    /**
     * 根据系统组ID查询绑定关系 [全量]
     * @param sGroupId 系统组ID
     * @return 绑定列表
     */
    List<GroupBindDeptVO> getGroupBindDeptsBySGroupId(Long sGroupId);

    /**
     * 根据系统组ID批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sGroupIds 系统组ID列表
     * @return 分组结果
     */
    Map<Long, List<GroupBindDeptVO>> getGroupBindDeptsBySGroupIds(List<Long> sGroupIds);

    /**
     * 根据租户部门ID查询绑定关系 [全量]
     * @param tDeptId 租户部门ID
     * @return 绑定列表
     */
    List<GroupBindDeptVO> getGroupBindDeptsByTDeptId(Long tDeptId);

    /**
     * 根据租户部门ID批量查询绑定关系 [全量] - 批量返回分组结果
     * @param tDeptIds 租户部门ID列表
     * @return 分组结果
     */
    Map<Long, List<GroupBindDeptVO>> getGroupBindDeptsByTDeptIds(List<Long> tDeptIds);

    /**
     * 根据系统组ID查询租户部门ID列表
     * @param sGroupId 系统组ID
     * @return 租户部门ID列表
     */
    Set<Long> getTDeptIdsBySGroupId(Long sGroupId);

    /**
     * 根据租户部门ID查询系统组ID列表
     * @param tDeptId 租户部门ID
     * @return 系统组ID列表
     */
    Set<Long> getSGroupIdsByTDeptId(Long tDeptId);

}
