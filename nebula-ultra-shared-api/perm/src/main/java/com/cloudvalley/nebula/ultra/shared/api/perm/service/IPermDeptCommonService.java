package com.cloudvalley.nebula.ultra.shared.api.perm.service;

import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermDeptVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPermDeptCommonService {

    /**
     * 根据ID查询单个租户部门-租户权限关联信息
     * @param id 关联关系的唯一标识ID
     * @return 对应的 PermDeptVO 对象，若不存在则返回 null
     */
    PermDeptVO getPermDeptById(Long id);

    /**
     * 根据多个ID全量查询租户部门-租户权限关联（不分页）
     * @param ids 关联ID列表
     * @return 所有匹配的 PermDeptVO 列表，忽略已删除记录
     */
    List<PermDeptVO> getPermDeptsByIds(List<Long> ids);

    /**
     * 根据租户部门ID全量查询其关联的权限列表
     * @param tDeptId 租户部门ID
     * @return 该部门拥有的所有 PermDeptVO 列表
     */
    List<PermDeptVO> getPermDeptsByTDeptId(Long tDeptId);

    /**
     * 根据多个租户部门ID全量查询关联权限，并按部门ID分组返回
     * @param tDeptIds 租户部门ID列表
     * @return 列表中包含一个 Map，键为 tDeptId，值为对应部门的 PermDeptVO 列表
     */
    Map<Long, List<PermDeptVO>> getPermDeptsByTDeptIds(List<Long> tDeptIds);

    /**
     * 根据租户权限ID全量查询使用该权限的部门列表
     * @param tPermId 租户权限ID
     * @return 所有拥有该权限的部门对应的 PermDeptVO 列表
     */
    List<PermDeptVO> getPermDeptsByTPermId(Long tPermId);

    /**
     * 根据多个租户权限ID全量查询使用部门，并按权限ID分组返回
     * @param tPermIds 租户权限ID列表
     * @return 列表中包含一个 Map，键为 tPermId，值为对应权限的部门VO列表
     */
    Map<Long, List<PermDeptVO>> getPermDeptsByTPermIds(List<Long> tPermIds);

    /**
     * 根据租户部门ID查询其拥有的所有租户权限ID列表
     * @param tDeptId 租户部门ID
     * @return 该部门关联的租户权限ID集合
     */
    Set<Long> getTPermIdsByTDeptId(Long tDeptId);

    /**
     * 根据租户权限ID查询所有拥有该权限的租户部门ID列表
     * @param tPermId 租户权限ID
     * @return 拥有该权限的所有部门ID集合
     */
    Set<Long> getTDeptIdsByTPermId(Long tPermId);

    /**
     * 根据多个租户部门ID查询其拥有的所有租户权限ID列表
     * @param tDeptIds 租户部门ID列表
     * @return Map结构，键为租户部门ID，值为该部门关联的租户权限ID集合
     */
    Map<Long, Set<Long>> getTPermIdsByTDeptIds(List<Long> tDeptIds);

    /**
     * 根据多个租户权限ID查询所有拥有这些权限的租户部门ID列表
     * @param tPermIds 租户权限ID列表
     * @return Map结构，键为租户权限ID，值为拥有该权限的所有部门ID集合
     */
    Map<Long, Set<Long>> getTDeptIdsByTPermIds(List<Long> tPermIds);

}
