package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.SysGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.group.model.rto.SysGroupRTO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;

import java.util.List;

/**
 * <p>
 * 组 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysGroupService extends IService<SysGroup> {

    /**
     * 查询系统组列表 [分页]
     * @param page 分页参数
     * @return 系统组列表
     */
    IPage<SysGroupVO> getSysGroupList(Page<SysGroup> page);

    /**
     * 根据系统组ID批量查询系统组 [分页]
     * @param ids 系统组ID列表
     * @param page 分页参数
     * @return 系统组信息分页列表
     */
    IPage<SysGroupVO> getSysGroupsByIds(List<Long> ids, Page<SysGroup> page);

    /**
     * 新增系统组
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    boolean createSysGroup(SysGroupRTO sysGroupRTO);

    /**
     * 更新系统组
     * @param sysGroupRTO 系统组信息
     * @return 操作结果
     */
    boolean updateSysGroup(SysGroupRTO sysGroupRTO);

    /**
     * 删除系统组（真删除）
     * @param id 系统组ID
     * @return 操作结果
     */
    boolean deleteSysGroup(Long id);

    /**
     * 软删除系统组
     * @param id 系统组ID
     * @return 操作结果
     */
    boolean softDeleteSysGroup(Long id);

    /**
     * 批量删除系统组（真删除）
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    boolean batchDeleteSysGroups(List<Long> ids);

    /**
     * 批量软删除系统组
     * @param ids 系统组ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteSysGroups(List<Long> ids);

}
