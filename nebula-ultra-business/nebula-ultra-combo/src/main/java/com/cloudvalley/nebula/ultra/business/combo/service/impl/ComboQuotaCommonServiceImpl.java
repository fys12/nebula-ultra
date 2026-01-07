package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.combo.converter.ComboQuotaConverter;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboQuota;
import com.cloudvalley.nebula.ultra.business.combo.mapper.ComboQuotaMapper;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.IComboQuotaCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboQuotaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComboQuotaCommonServiceImpl extends ServiceImpl<ComboQuotaMapper, ComboQuota> implements IComboQuotaCommonService {

    @Autowired
    private ComboQuotaConverter comboQuotaConverter;

    /**
     * 根据id查询套餐配额配置（单个）
     * 查询指定ID的未删除套餐配额配置
     *
     * @param id 套餐配额ID
     * @return 套餐配额VO对象，若不存在则返回null
     */
    @Override
    public ComboQuotaVO getComboQuotaById(Long id) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getId, id)
                .eq(ComboQuota::getDeleted, false);
        ComboQuota entity = this.getOne(queryWrapper);
        return entity != null ? comboQuotaConverter.EnToVO(entity) : null;
    }

    /**
     * 根据id批量查询套餐配额配置 [全量]
     * 查询指定ID列表中未删除的套餐配额配置，按创建时间降序排列
     *
     * @param ids 套餐配额ID列表
     * @return 套餐配额VO列表，若ID列表为空则返回空列表
     */
    @Override
    public List<ComboQuotaVO> getComboQuotasByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboQuota::getId, ids)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        List<ComboQuota> list = this.list(queryWrapper);
        return comboQuotaConverter.EnListToVOList(list);
    }

    /**
     * 根据系统套餐id查询配额配置 [全量]
     * 查询指定系统套餐下未删除的所有配额配置，按创建时间降序排列
     *
     * @param sComboId 系统套餐ID
     * @return 套餐配额VO列表
     */
    @Override
    public List<ComboQuotaVO> getComboQuotasBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSComboId, sComboId)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        List<ComboQuota> list = this.list(queryWrapper);
        return comboQuotaConverter.EnListToVOList(list);
    }

    /**
     * 根据系统套餐id批量查询配额配置 [全量] - 批量返回分组结果
     * 查询多个系统套餐下未删除的所有配额配置，并按系统套餐ID分组
     *
     * @param sComboIds 系统套餐ID列表
     * @return 分组结果列表，键为系统套餐ID，值为对应的配额VO列表，若ID列表为空则返回空列表
     */
    @Override
    public Map<Long, List<ComboQuotaVO>> getComboQuotasBySComboIds(List<Long> sComboIds) {
        if (sComboIds == null || sComboIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboQuota::getSComboId, sComboIds)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        List<ComboQuota> entities = this.list(queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(entities);
        Map<Long, List<ComboQuotaVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboQuotaVO::getSComboId));
        return grouped;
    }

    /**
     * 根据系统配额id查询配额配置 [全量]
     * 查询指定系统配额关联的未删除套餐配额配置，按创建时间降序排列
     *
     * @param sQuotaId 系统配额ID
     * @return 套餐配额VO列表
     */
    @Override
    public List<ComboQuotaVO> getComboQuotasBySQuotaId(Long sQuotaId) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSQuotaId, sQuotaId)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        List<ComboQuota> list = this.list(queryWrapper);
        return comboQuotaConverter.EnListToVOList(list);
    }

    /**
     * 根据系统配额id批量查询配额配置 [全量] - 批量返回分组结果
     * 查询多个系统配额关联的未删除套餐配额配置，并按系统配额ID分组
     *
     * @param sQuotaIds 系统配额ID列表
     * @return 分组结果列表，键为系统配额ID，值为对应的配额VO列表，若ID列表为空则返回空列表
     */
    @Override
    public Map<Long, List<ComboQuotaVO>> getComboQuotasBySQuotaIds(List<Long> sQuotaIds) {
        if (sQuotaIds == null || sQuotaIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboQuota::getSQuotaId, sQuotaIds)
                .eq(ComboQuota::getDeleted, false)
                .orderByDesc(ComboQuota::getCreatedAt);
        List<ComboQuota> entities = this.list(queryWrapper);
        List<ComboQuotaVO> voList = comboQuotaConverter.EnListToVOList(entities);
        Map<Long, List<ComboQuotaVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboQuotaVO::getSQuotaId));
        return grouped;
    }

    /**
     * 根据系统套餐ID查询系统配额ID列表
     * 查询指定系统套餐下关联的所有未删除系统配额ID集合
     *
     * @param sComboId 系统套餐ID
     * @return 系统配额ID集合
     */
    @Override
    public Set<Long> getSQuotaIdsBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSComboId, sComboId)
                .eq(ComboQuota::getDeleted, false)
                .select(ComboQuota::getSQuotaId);
        List<ComboQuota> list = this.list(queryWrapper);
        return list.stream().map(ComboQuota::getSQuotaId).collect(Collectors.toSet());
    }

    /**
     * 根据系统配额ID查询系统套餐ID列表
     * 查询指定系统配额关联的所有未删除系统套餐ID集合
     *
     * @param sQuotaId 系统配额ID
     * @return 系统套餐ID集合
     */
    @Override
    public Set<Long> getSComboIdsBySQuotaId(Long sQuotaId) {
        LambdaQueryWrapper<ComboQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboQuota::getSQuotaId, sQuotaId)
                .eq(ComboQuota::getDeleted, false)
                .select(ComboQuota::getSComboId);
        List<ComboQuota> list = this.list(queryWrapper);
        return list.stream().map(ComboQuota::getSComboId).collect(Collectors.toSet());
    }

}
