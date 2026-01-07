package com.cloudvalley.nebula.ultra.business.perm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.perm.model.entity.SysPerm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.perm.model.rto.SysPermRTO;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysPermService extends IService<SysPerm> {

    /**
     * 查询系统权限列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysPermVO> getSysPermList(Page<SysPerm> page);

    /**
     * 根据id查询系统权限(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 系统权限ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysPermVO> getSysPermsByIds(List<Long> ids, Page<SysPerm> page);

    /**
     * 新增系统权限
     * @param sysPermRTO 系统权限信息
     * @return 是否成功
     */
    boolean createSysPerm(SysPermRTO sysPermRTO);

    /**
     * 更新系统权限
     * @param sysPermRTO 系统权限信息
     * @return 是否成功
     */
    boolean updateSysPerm(SysPermRTO sysPermRTO);

    /**
     * 更新系统权限状态（启用/禁用）
     * @param id 系统权限ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateSysPermState(Long id, Boolean state);

    /**
     * 删除系统权限（真删）
     * @param id 系统权限ID
     * @return 是否成功
     */
    boolean deleteSysPerm(Long id);

    /**
     * 软删除系统权限
     * @param id 系统权限ID
     * @return 是否成功
     */
    boolean softDeleteSysPerm(Long id);

    /**
     * 批量删除系统权限（真删）
     * @param ids 系统权限ID列表
     * @return 是否成功
     */
    boolean batchDeleteSysPerms(List<Long> ids);

    /**
     * 批量软删除系统权限
     * @param ids 系统权限ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteSysPerms(List<Long> ids);

}
