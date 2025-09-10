package com.cloudvalley.nebula.ultra.business.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.role.model.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.role.model.rto.SysRoleRTO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询系统角色列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysRoleVO> getSysRoleList(Page<SysRole> page);

    /**
     * 根据id查询系统角色(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 系统角色ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysRoleVO> getSysRolesByIds(List<Long> ids, Page<SysRole> page);

    /**
     * 新增系统角色
     * @param sysRoleRTO 系统角色信息
     * @return 是否成功
     */
    boolean createSysRole(SysRoleRTO sysRoleRTO);

    /**
     * 更新系统角色
     * @param sysRoleRTO 系统角色信息
     * @return 是否成功
     */
    boolean updateSysRole(SysRoleRTO sysRoleRTO);

    /**
     * 更新系统角色状态
     * @param id 系统角色ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateSysRoleState(Long id, Boolean state);

    /**
     * 删除系统角色（真删）
     * @param id 系统角色ID
     * @return 是否成功
     */
    boolean deleteSysRole(Long id);

    /**
     * 软删除系统角色
     * @param id 系统角色ID
     * @return 是否成功
     */
    boolean softDeleteSysRole(Long id);

    /**
     * 批量删除系统角色（真删）
     * @param ids 系统角色ID列表
     * @return 是否成功
     */
    boolean batchDeleteSysRoles(List<Long> ids);

    /**
     * 批量软删除系统角色
     * @param ids 系统角色ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteSysRoles(List<Long> ids);

}
