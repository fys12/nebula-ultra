package com.cloudvalley.nebula.ultra.shared.api.combo.service;

import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboPermVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IComboPermCommonService {

    /**
     * 根据绑定ID查询单个套餐-权限绑定关系详情
     *
     * @param id 绑定关系唯一标识ID
     * @return 套餐-权限绑定关系视图对象，包含完整绑定信息
     */
    ComboPermVO getComboPermById(Long id);

    /**
     * 批量根据绑定ID查询所有套餐-权限绑定关系（全量，不分页）
     *
     * @param ids 绑定关系ID列表
     * @return 套餐-权限绑定关系视图对象列表
     */
    List<ComboPermVO> getComboPermsByIds(List<Long> ids);

    /**
     * 根据系统套餐ID查询所有相关的套餐-权限绑定关系（全量，不分页）
     *
     * @param sComboId 系统套餐唯一标识ID
     * @return 该套餐下所有权限绑定关系的视图对象列表
     */
    List<ComboPermVO> getComboPermsBySComboId(Long sComboId);

    /**
     * 批量根据系统套餐ID查询所有绑定关系，并按套餐ID分组（全量，不分页）
     *
     * @param sComboIds 系统套餐ID列表
     * @return 分组结果列表，每个Map包含一个套餐ID与对应的绑定关系列表
     */
    Map<Long, List<ComboPermVO>> getComboPermsBySComboIds(List<Long> sComboIds);

    /**
     * 根据系统权限ID查询所有相关的套餐-权限绑定关系（全量，不分页）
     *
     * @param sPermId 系统权限唯一标识ID
     * @return 拥有该权限的所有套餐绑定关系视图对象列表
     */
    List<ComboPermVO> getComboPermsBySPermId(Long sPermId);

    /**
     * 批量根据系统权限ID查询所有绑定关系，并按权限ID分组（全量，不分页）
     *
     * @param sPermIds 系统权限ID列表
     * @return 分组结果列表，每个Map包含一个权限ID与对应的套餐绑定关系列表
     */
    Map<Long, List<ComboPermVO>> getComboPermsBySPermIds(List<Long> sPermIds);

    /**
     * 根据系统套餐ID查询其所关联的所有系统权限ID
     *
     * @param sComboId 系统套餐唯一标识ID
     * @return 系统权限ID集合，不含重复值
     */
    Set<Long> getSPermIdsBySComboId(Long sComboId);

    /**
     * 根据系统权限ID查询所有关联了该权限的系统套餐ID
     *
     * @param sPermId 系统权限唯一标识ID
     * @return 系统套餐ID集合，不含重复值
     */
    Set<Long> getSComboIdsBySPermId(Long sPermId);

}
