package com.cloudvalley.nebula.ultra.shared.api.dept.service;

import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptUserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDeptUserCommonService {

    /**
     * 根据租户用户租户部门id查询租户用户租户部门分配
     * @param id 租户用户租户部门ID
     * @return 租户用户租户部门分配信息
     */
    DeptUserVO getDeptUserById(Long id);

    /**
     * 根据租户用户-租户部门分配ID列表，全量查询租户用户-租户部门分配关系
     * @param ids 租户用户-租户部门分配关系的主键ID列表（雪花算法ID）
     * @return List<DeptUserVO> 全量的租户用户-租户部门分配VO列表
     */
    List<DeptUserVO> getDeptUsersByIds(List<Long> ids);

    /**
     * 根据租户用户ID，全量查询该用户的租户部门分配关系
     * @param userId 租户用户的主键ID（雪花算法ID）
     * @return List<DeptUserVO> 该用户的全量租户部门分配VO列表
     */
    List<DeptUserVO> getDeptUsersByUserId(Long userId);

    /**
     * 根据租户用户ID列表，批量全量查询用户的租户部门分配关系，返回按用户ID分组的结果
     * @param userIds 租户用户的主键ID列表（雪花算法ID）
     * @return List<Map<Long, List<DeptUserVO>>> 分组结果列表（单个Map，key为租户用户ID，value为该用户的部门分配VO列表）
     */
    Map<Long, List<DeptUserVO>> getDeptUsersByUserIds(List<Long> userIds);

    /**
     * 根据租户部门ID，全量查询该部门的租户用户分配关系
     * @param deptId 租户部门的主键ID（雪花算法ID）
     * @return List<DeptUserVO> 该部门的全量租户用户分配VO列表
     */
    List<DeptUserVO> getDeptUsersByDeptId(Long deptId);

    /**
     * 根据租户部门ID列表，批量全量查询部门的租户用户分配关系，返回按部门ID分组的结果
     * @param deptIds 租户部门的主键ID列表（雪花算法ID）
     * @return List<Map<Long, List<DeptUserVO>>> 分组结果列表（单个Map，key为租户部门ID，value为该部门的用户分配VO列表）
     */
    List<Map<Long, List<DeptUserVO>>> getDeptUsersByDeptIds(List<Long> deptIds);

    /**
     * 根据租户用户ID查询租户部门ID列表
     * @param userId 租户用户ID
     * @return 租户部门ID集合
     */
    Set<Long> getDeptIdsByUserId(Long userId);

    /**
     * 根据租户部门ID查询租户用户ID列表
     * @param deptId 租户部门ID
     * @return 租户用户ID集合
     */
    Set<Long> getUserIdsByDeptId(Long deptId);

    /**
     * 根据租户用户ID列表查询租户部门ID列表
     * @param userIds 租户用户ID列表
     * @return 租户部门ID列表 Map 键-用户 值-部门
     */
    Map<Long, Set<Long>> getDeptIdsByUserIds(List<Long> userIds);

    /**
     * 根据租户部门ID列表查询租户用户ID列表
     * @param deptIds 租户部门ID列表
     * @return 租户用户ID列表 Map 键-部门 值-用户
     */
    Map<Long, Set<Long>> getUserIdsByDeptIds(List<Long> deptIds);


}
