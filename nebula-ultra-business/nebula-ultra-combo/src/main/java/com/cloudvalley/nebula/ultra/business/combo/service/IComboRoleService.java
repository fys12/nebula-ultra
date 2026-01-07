package com.cloudvalley.nebula.ultra.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboRoleRTO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboRoleVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐-角色绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IComboRoleService extends IService<ComboRole> {

    /**
     * 查询套餐 - 角色绑定列表 [分页]
     * @param page 分页参数
     * @return 绑定列表
     */
    IPage<ComboRoleVO> getComboRoleList(Page<ComboRole> page);

    /**
     * 根据绑定 id 批量查询绑定关系 [分页]
     * @param ids 绑定ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboRoleVO> getComboRolesByIds(List<Long> ids, Page<ComboRole> page);

    /**
     * 根据系统套餐 id 查询绑定关系 [分页]
     * @param sComboId 系统套餐ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboRoleVO> getComboRolesBySComboId(Long sComboId, Page<ComboRole> page);

    /**
     * 根据系统套餐 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @param page 分页参数
     * @return 分组分页结果
     */
    IPage<Map<Long, List<ComboRoleVO>>> getComboRolesBySComboIds(List<Long> sComboIds, Page<ComboRole> page);

    /**
     * 根据系统角色 id 查询绑定关系 [分页]
     * @param sRoleId 系统角色ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboRoleVO> getComboRolesBySRoleId(Long sRoleId, Page<ComboRole> page);

    /**
     * 根据系统角色 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sRoleIds 系统角色ID列表
     * @param page 分页参数
     * @return 分组分页结果
     */
    IPage<Map<Long, List<ComboRoleVO>>> getComboRolesBySRoleIds(List<Long> sRoleIds, Page<ComboRole> page);

    /**
     * 新增套餐 - 角色绑定
     */
    boolean createComboRole(ComboRoleRTO comboRoleRTO);

    /**
     * 更新套餐 - 角色绑定
     */
    boolean updateComboRole(ComboRoleRTO comboRoleRTO);

    /**
     * 删除套餐 - 角色绑定（真删）
     */
    boolean deleteComboRole(Long id);

    /**
     * 软删除套餐 - 角色绑定
     */
    boolean softDeleteComboRole(Long id);

    /**
     * 批量删除套餐 - 角色绑定（真删）
     */
    boolean batchDeleteComboRoles(List<Long> ids);

    /**
     * 批量软删除套餐 - 角色绑定
     */
    boolean batchSoftDeleteComboRoles(List<Long> ids);

}
