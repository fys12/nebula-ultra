package com.cloudvalley.nebula.monolith.business.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.SysDeptRTO;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.SysDeptVO;

import java.util.List;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 分页查询系统部门列表。
     * @param page 分页参数
     * @return 分页的系统部门VO
     */
    IPage<SysDeptVO> getSysDeptList(Page<SysDept> page);

    /**
     * 根据ID列表分页查询系统部门。
     * @param ids  部门ID列表
     * @param page 分页参数
     * @return 分页的部门VO列表
     */
    IPage<SysDeptVO> getSysDeptsByIds(List<Long> ids, Page<SysDept> page);

    /**
     * 根据ID列表查询所有系统部门（全量）。
     * @param ids 部门ID列表
     * @return 所有匹配的部门VO列表
     */
    List<SysDeptVO> getSysDeptsByIds(List<Long> ids);

    /**
     * 新增系统部门。
     * @param sysDeptRTO 新增数据
     * @return 是否创建成功
     */
    boolean createSysDept(SysDeptRTO sysDeptRTO);

    /**
     * 更新系统部门。
     * @param sysDeptRTO 更新数据（需包含ID）
     * @return 是否更新成功
     */
    boolean updateSysDept(SysDeptRTO sysDeptRTO);

    /**
     * 更新系统部门状态。
     * @param id    部门ID
     * @param state 目标状态（true: 启用，false: 禁用）
     * @return 是否更新成功
     */
    boolean updateSysDeptState(Long id, Boolean state);

    /**
     * 物理删除系统部门（不可恢复）。
     * @param id 部门ID
     * @return 是否删除成功
     */
    boolean deleteSysDept(Long id);

    /**
     * 软删除系统部门（标记删除）。
     * @param id 部门ID
     * @return 是否软删除成功
     */
    boolean softDeleteSysDept(Long id);

    /**
     * 批量物理删除系统部门。
     * @param ids 部门ID列表
     * @return 是否全部删除成功
     */
    boolean batchDeleteSysDepts(List<Long> ids);

    /**
     * 批量软删除系统部门。
     * @param ids 部门ID列表
     * @return 是否批量软删除成功
     */
    boolean batchSoftDeleteSysDepts(List<Long> ids);

}
