package com.cloudvalley.nebula.ultra.business.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.log.converter.ComboChangeLogConverter;
import com.cloudvalley.nebula.ultra.business.log.model.entity.ComboChangeLog;
import com.cloudvalley.nebula.ultra.business.log.mapper.ComboChangeLogMapper;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.ComboChangeLogVO;
import com.cloudvalley.nebula.ultra.shared.api.log.service.IComboChangeLogCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComboChangeLogCommonServiceImpl extends ServiceImpl<ComboChangeLogMapper, ComboChangeLog> implements IComboChangeLogCommonService {

    @Autowired
    private ComboChangeLogConverter comboChangeLogConverter;

    /**
     * 根据套餐变更日志ID查询单个套餐变更日志信息
     * @param id 套餐变更日志的唯一标识ID
     * @return 对应的套餐变更日志VO，若未找到或已删除则返回null
     */
    @Override
    public ComboChangeLogVO getComboChangeLogById(Long id) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getId, id)
                .eq(ComboChangeLog::getDeleted, false);

        ComboChangeLog comboChangeLog = this.getOne(queryWrapper);
        return comboChangeLog != null ? comboChangeLogConverter.EnToVO(comboChangeLog) : null;
    }

    /**
     * 根据套餐变更日志ID列表批量查询套餐变更日志（全量数据）
     * @param ids 套餐变更日志ID列表，支持多个ID查询
     * @return 匹配的套餐变更日志VO列表，按更改时间倒序排列，忽略已删除记录
     */
    @Override
    public List<ComboChangeLogVO> getComboChangeLogsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getId, ids)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> list = this.list(queryWrapper);
        return comboChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户ID查询该租户下的所有套餐变更日志（全量数据）
     * @param sTenantId 系统租户ID
     * @return 该租户相关的套餐变更日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<ComboChangeLogVO> getComboChangeLogsBySTenantId(Long sTenantId) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getSTenantId, sTenantId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> list = this.list(queryWrapper);
        return comboChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户ID列表批量查询套餐变更日志，并按租户ID分组返回结果（全量数据）
     * @param sTenantIds 系统租户ID列表
     * @return 包含一个Map的列表，Map的键为sTenantId，值为对应租户的日志VO列表，按更改时间倒序排列
     */
    @Override
    public Map<Long, List<ComboChangeLogVO>> getComboChangeLogsBySTenantIds(List<Long> sTenantIds) {
        if (sTenantIds == null || sTenantIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getSTenantId, sTenantIds)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> entities = this.list(queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<ComboChangeLogVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboChangeLogVO::getSTenantId));
        return grouped;
    }

    /**
     * 根据旧系统套餐ID查询所有关联的套餐变更日志（全量数据）
     * @param oldSComboId 旧系统套餐ID
     * @return 与该旧套餐ID相关的套餐变更日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<ComboChangeLogVO> getComboChangeLogsByOldSComboId(Long oldSComboId) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getOldSComboId, oldSComboId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> list = this.list(queryWrapper);
        return comboChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个旧系统套餐ID批量查询套餐变更日志，并按旧套餐ID分组返回结果（全量数据）
     * @param oldSComboIds 旧系统套餐ID列表
     * @return 包含一个Map的列表，Map的键为oldSComboId，值为对应套餐的日志VO列表，按更改时间倒序排列
     */
    @Override
    public Map<Long, List<ComboChangeLogVO>> getComboChangeLogsByOldSComboIds(List<Long> oldSComboIds) {
        if (oldSComboIds == null || oldSComboIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getOldSComboId, oldSComboIds)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> entities = this.list(queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<ComboChangeLogVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboChangeLogVO::getOldSComboId));
        return grouped;
    }

    /**
     * 根据新系统套餐ID查询所有关联的套餐变更日志（全量数据）
     * @param newSComboId 新系统套餐ID
     * @return 与该新套餐ID相关的套餐变更日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<ComboChangeLogVO> getComboChangeLogsByNewSComboId(Long newSComboId) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getNewSComboId, newSComboId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> list = this.list(queryWrapper);
        return comboChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个新系统套餐ID批量查询套餐变更日志，并按新套餐ID分组返回结果（全量数据）
     * @param newSComboIds 新系统套餐ID列表
     * @return 包含一个Map的列表，Map的键为newSComboId，值为对应套餐的日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByNewSComboIds(List<Long> newSComboIds) {
        if (newSComboIds == null || newSComboIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getNewSComboId, newSComboIds)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> entities = this.list(queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<ComboChangeLogVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboChangeLogVO::getNewSComboId));
        return Collections.singletonList(grouped);
    }

    /**
     * 根据操作人用户ID查询其操作的所有套餐变更日志（全量数据）
     * @param operatorUserId 操作人用户ID
     * @return 该用户操作的套餐变更日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<ComboChangeLogVO> getComboChangeLogsByOperatorUserId(Long operatorUserId) {
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboChangeLog::getOperatorUserId, operatorUserId)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> list = this.list(queryWrapper);
        return comboChangeLogConverter.EnListToVOList(list);
    }

    /**
     * 根据多个操作人用户ID批量查询套餐变更日志，并按用户ID分组返回结果（全量数据）
     * @param operatorUserIds 操作人用户ID列表
     * @return 包含一个Map的列表，Map的键为operatorUserId，值为对应用户的操作日志VO列表，按更改时间倒序排列
     */
    @Override
    public List<Map<Long, List<ComboChangeLogVO>>> getComboChangeLogsByOperatorUserIds(List<Long> operatorUserIds) {
        if (operatorUserIds == null || operatorUserIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboChangeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboChangeLog::getOperatorUserId, operatorUserIds)
                .eq(ComboChangeLog::getDeleted, false)
                .orderByDesc(ComboChangeLog::getChangedAt);
        List<ComboChangeLog> entities = this.list(queryWrapper);
        List<ComboChangeLogVO> voList = comboChangeLogConverter.EnListToVOList(entities);
        Map<Long, List<ComboChangeLogVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboChangeLogVO::getOperatorUserId));
        return Collections.singletonList(grouped);
    }


}
