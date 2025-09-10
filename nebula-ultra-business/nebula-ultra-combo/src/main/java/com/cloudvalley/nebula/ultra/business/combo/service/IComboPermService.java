package com.cloudvalley.nebula.ultra.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboPerm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboPermRTO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboPermVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐-权限绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IComboPermService extends IService<ComboPerm> {

    /**
     * 分页查询套餐-权限绑定关系列表
     *
     * @param page 分页参数对象，包含页码、每页条数等信息
     * @return 分页结果对象，包含当前页数据及分页信息
     */
    IPage<ComboPermVO> getComboPermList(Page<ComboPerm> page);

    /**
     * 批量根据绑定ID分页查询套餐-权限绑定关系
     *
     * @param ids  绑定关系ID列表
     * @param page 分页参数对象
     * @return 分页结果对象，包含符合条件的绑定关系数据
     */
    IPage<ComboPermVO> getComboPermsByIds(List<Long> ids, Page<ComboPerm> page);

    /**
     * 根据系统套餐ID分页查询相关的套餐-权限绑定关系
     *
     * @param sComboId 系统套餐唯一标识ID
     * @param page     分页参数对象
     * @return 分页结果对象，包含该套餐下的所有权限绑定关系
     */
    IPage<ComboPermVO> getComboPermsBySComboId(Long sComboId, Page<ComboPerm> page);

    /**
     * 批量根据系统套餐ID分页查询绑定关系，并按套餐ID分组
     *
     * @param sComboIds 系统套餐ID列表
     * @param page      分页参数对象
     * @return 分页结果对象，records中包含一个Map，key为套餐ID，value为该套餐下的绑定关系列表
     */
    IPage<Map<Long, List<ComboPermVO>>> getComboPermsBySComboIds(List<Long> sComboIds, Page<ComboPerm> page);

    /**
     * 根据系统权限ID分页查询相关的套餐-权限绑定关系
     *
     * @param sPermId 系统权限唯一标识ID
     * @param page    分页参数对象
     * @return 分页结果对象，包含拥有该权限的所有套餐绑定关系
     */
    IPage<ComboPermVO> getComboPermsBySPermId(Long sPermId, Page<ComboPerm> page);

    /**
     * 批量根据系统权限ID分页查询绑定关系，并按权限ID分组
     *
     * @param sPermIds 系统权限ID列表
     * @param page     分页参数对象
     * @return 分页结果对象，records中包含一个Map，key为权限ID，value为拥有该权限的套餐绑定关系列表
     */
    IPage<Map<Long, List<ComboPermVO>>> getComboPermsBySPermIds(List<Long> sPermIds, Page<ComboPerm> page);

    /**
     * 新增套餐-权限绑定关系
     *
     * @param comboPermRTO 套餐-权限绑定关系请求传输对象，包含待新增的绑定信息
     * @return 操作结果：true-新增成功，false-新增失败
     */
    boolean createComboPerm(ComboPermRTO comboPermRTO);

    /**
     * 更新套餐-权限绑定关系
     *
     * @param comboPermRTO 套餐-权限绑定关系请求传输对象，包含待更新的绑定信息
     * @return 操作结果：true-更新成功，false-更新失败
     */
    boolean updateComboPerm(ComboPermRTO comboPermRTO);

    /**
     * 物理删除套餐-权限绑定关系（从数据库中彻底删除）
     *
     * @param id 绑定关系唯一标识ID
     * @return 操作结果：true-删除成功，false-删除失败
     */
    boolean deleteComboPerm(Long id);

    /**
     * 逻辑删除套餐-权限绑定关系（仅标记删除状态，不实际删除数据）
     *
     * @param id 绑定关系唯一标识ID
     * @return 操作结果：true-删除成功，false-删除失败
     */
    boolean softDeleteComboPerm(Long id);

    /**
     * 批量物理删除套餐-权限绑定关系（从数据库中彻底删除）
     *
     * @param ids 绑定关系ID列表
     * @return 操作结果：true-全部删除成功，false-部分或全部删除失败
     */
    boolean batchDeleteComboPerms(List<Long> ids);

    /**
     * 批量逻辑删除套餐-权限绑定关系（仅标记删除状态，不实际删除数据）
     *
     * @param ids 绑定关系ID列表
     * @return 操作结果：true-全部删除成功，false-部分或全部删除失败
     */
    boolean batchSoftDeleteComboPerms(List<Long> ids);

}
