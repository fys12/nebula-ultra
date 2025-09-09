package com.cloudvalley.nebula.monolith.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.SysCombo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.combo.model.rto.SysComboRTO;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.SysComboVO;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysComboService extends IService<SysCombo> {

    /**
     * 查询系统套餐列表 [分页]
     *
     * @param page 分页参数
     * @return 套餐列表
     */
    IPage<SysComboVO> getSysComboList(Page<SysCombo> page);

    /**
     * 根据ID批量查询系统套餐 [分页]
     *
     * @param ids 套餐ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysComboVO> getSysCombosByIds(List<Long> ids, Page<SysCombo> page);

    /**
     * 新增系统套餐
     */
    boolean createSysCombo(SysComboRTO sysComboRTO);

    /**
     * 更新系统套餐
     */
    boolean updateSysCombo(SysComboRTO sysComboRTO);

    /**
     * 删除系统套餐（真删）
     */
    boolean deleteSysCombo(Long id);

    /**
     * 软删除系统套餐
     */
    boolean softDeleteSysCombo(Long id);

    /**
     * 批量删除系统套餐（真删）
     */
    boolean batchDeleteSysCombos(List<Long> ids);

    /**
     * 批量软删除系统套餐
     */
    boolean batchSoftDeleteSysCombos(List<Long> ids);

}
