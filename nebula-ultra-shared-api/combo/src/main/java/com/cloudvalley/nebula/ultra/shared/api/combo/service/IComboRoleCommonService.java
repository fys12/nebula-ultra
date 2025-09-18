package com.cloudvalley.nebula.ultra.shared.api.combo.service;


import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboRoleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IComboRoleCommonService {

    /**
     * 根据绑定 id 查询绑定关系（单个）
     * @param id 绑定ID
     * @return 绑定信息
     */
    ComboRoleVO getComboRoleById(Long id);

    /**
     * 根据绑定 id 批量查询绑定关系 [全量]
     * @param ids 绑定ID列表
     * @return 绑定信息
     */
    List<ComboRoleVO> getComboRolesByIds(List<Long> ids);

    /**
     * 根据系统套餐 id 查询绑定关系 [全量]
     * @param sComboId 系统套餐ID
     * @return 绑定信息
     */
    List<ComboRoleVO> getComboRolesBySComboId(Long sComboId);

    /**
     * 根据系统套餐 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @return 分组结果
     */
    Map<Long, List<ComboRoleVO>> getComboRolesBySComboIds(List<Long> sComboIds);

    /**
     * 根据系统角色 id 查询绑定关系 [全量]
     * @param sRoleId 系统角色ID
     * @return 绑定信息
     */
    List<ComboRoleVO> getComboRolesBySRoleId(Long sRoleId);

    /**
     * 根据系统角色 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sRoleIds 系统角色ID列表
     * @return 分组结果
     */
    Map<Long, List<ComboRoleVO>> getComboRolesBySRoleIds(List<Long> sRoleIds);

    /**
     * 根据系统套餐 ID 查询系统角色 ID 列表
     * @param sComboId 系统套餐ID
     * @return 系统角色ID集合
     */
    Set<Long> getSRoleIdsBySComboId(Long sComboId);

    /**
     * 根据系统角色 ID 查询系统套餐 ID 列表
     * @param sRoleId 系统角色ID
     * @return 系统套餐ID集合
     */
    Set<Long> getSComboIdsBySRoleId(Long sRoleId);

}
