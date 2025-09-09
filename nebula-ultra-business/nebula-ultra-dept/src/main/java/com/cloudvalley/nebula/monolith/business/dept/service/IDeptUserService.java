package com.cloudvalley.nebula.monolith.business.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.DeptUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.DeptUserRTO;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.DeptUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户用户-租户部门分配 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IDeptUserService extends IService<DeptUser> {

    /**
     * 查询租户用户租户部门分配列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptUserVO> getDeptUserList(Page<DeptUser> page);

    /**
     * 根据租户用户租户部门id查询租户用户租户部门分配(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 租户用户租户部门ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptUserVO> getDeptUsersByIds(List<Long> ids, Page<DeptUser> page);

    /**
     * 根据租户用户id查询租户用户租户部门分配(可查询单个或多个，传入的id可以单个或多个) [查某个（或多个）用户在哪些部门][分页]
     * @param userId 租户用户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptUserVO> getDeptUsersByUserId(Long userId, Page<DeptUser> page);

    /**
     * 根据租户用户id查询租户用户租户部门分配(批量查询多个用户) [查某个（或多个）用户在哪些部门][分页]
     * @param userIds 租户用户ID列表
     * @param page 分页参数
     * @return 分页结果，按用户ID分组
     */
    IPage<Map<Long, List<DeptUserVO>>> getDeptUsersByUserIds(List<Long> userIds, Page<DeptUser> page);

    /**
     * 根据租户部门id查询租户用户租户部门分配(可查询单个或多个，传入的id可以单个或多个) [查某个（或多个）部门有哪些用户][分页]
     * @param deptId 租户部门ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptUserVO> getDeptUsersByDeptId(Long deptId, Page<DeptUser> page);

    /**
     * 根据租户部门id查询租户用户租户部门分配(批量查询多个部门) [查某个（或多个）部门有哪些用户][分页]
     * @param deptIds 租户部门ID列表
     * @param page 分页参数
     * @return 分页结果，按部门ID分组
     */
    IPage<Map<Long, List<DeptUserVO>>> getDeptUsersByDeptIds(List<Long> deptIds, Page<DeptUser> page);

    /**
     * 新增用户部门
     * @param userDeptRTO 用户部门信息
     * @return 是否成功
     */
    boolean createDeptUser(DeptUserRTO userDeptRTO);

    /**
     * 更新用户部门
     * @param userDeptRTO 用户部门信息
     * @return 是否成功
     */
    boolean updateDeptUser(DeptUserRTO userDeptRTO);

    /**
     * 更新用户部门状态 [该用户在该部门中的状态]
     * @param id 用户部门ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateDeptUserState(Long id, Boolean state);

    /**
     * 删除用户部门(真)
     * @param id 用户部门ID
     * @return 是否成功
     */
    boolean deleteDeptUser(Long id);

    /**
     * 软删除用户部门
     * @param id 用户部门ID
     * @return 是否成功
     */
    boolean softDeleteDeptUser(Long id);

    /**
     * 批量删除用户部门(真)
     * @param ids 用户部门ID列表
     * @return 是否成功
     */
    boolean batchDeleteDeptUsers(List<Long> ids);

    /**
     * 批量软删除用户部门
     * @param ids 用户部门ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteDeptUsers(List<Long> ids);


}
