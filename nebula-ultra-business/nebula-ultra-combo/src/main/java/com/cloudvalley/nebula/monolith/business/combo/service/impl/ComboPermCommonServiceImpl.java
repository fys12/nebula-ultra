package com.cloudvalley.nebula.monolith.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboPerm;
import com.cloudvalley.nebula.monolith.business.combo.mapper.ComboPermMapper;
import com.cloudvalley.nebula.monolith.business.combo.converter.ComboPermConverter;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboPermVO;
import com.cloudvalley.nebula.monolith.shared.api.combo.service.IComboPermCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComboPermCommonServiceImpl extends ServiceImpl<ComboPermMapper, ComboPerm> implements IComboPermCommonService {

    @Autowired
    private ComboPermConverter comboPermConverter;

    /**
     * 根据绑定ID查询单个套餐-权限绑定关系
     *
     * @param id 绑定关系的唯一标识ID
     * @return 套餐-权限绑定关系的VO对象，若不存在则返回null
     */
    @Override
    public ComboPermVO getComboPermById(Long id) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getId, id)
                .eq(ComboPerm::getDeleted, false);
        ComboPerm entity = this.getOne(queryWrapper);
        return entity != null ? comboPermConverter.EnToVO(entity) : null;
    }

    /**
     * 根据绑定ID列表查询所有套餐-权限绑定关系（全量，不分页）
     *
     * @param ids 绑定关系的ID列表
     * @return 套餐-权限绑定关系的VO列表，若参数为空则返回空列表
     */
    @Override
    public List<ComboPermVO> getComboPermsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboPerm::getId, ids)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        List<ComboPerm> comboPermList = this.list(queryWrapper);
        return comboPermConverter.EnListToVOList(comboPermList);
    }

    /**
     * 根据系统套餐ID查询所有相关的套餐-权限绑定关系（全量，不分页）
     *
     * @param sComboId 系统套餐的唯一标识ID
     * @return 该系统套餐相关的所有绑定关系VO列表
     */
    @Override
    public List<ComboPermVO> getComboPermsBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSComboId, sComboId)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        List<ComboPerm> comboPermList = this.list(queryWrapper);
        return comboPermConverter.EnListToVOList(comboPermList);
    }

    /**
     * 根据系统套餐ID列表查询所有绑定关系，并按系统套餐ID分组返回（全量，不分页）
     *
     * @param sComboIds 系统套餐的ID列表
     * @return 包含一个Map的列表，Map中key为系统套餐ID，value为对应的绑定关系VO列表；若参数为空则返回空列表
     */
    @Override
    public List<Map<Long, List<ComboPermVO>>> getComboPermsBySComboIds(List<Long> sComboIds) {
        if (sComboIds == null || sComboIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 构建查询条件
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboPerm::getSComboId, sComboIds)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);

        // 查询所有匹配的记录
        List<ComboPerm> entities = this.list(queryWrapper);

        // 转换为VO对象
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(entities);

        // 按SComboId分组
        Map<Long, List<ComboPermVO>> groupedMap = voList.stream()
                .collect(Collectors.groupingBy(ComboPermVO::getSComboId));

        // 将Map包装在List中返回，保持与分页方法返回类型的一致性
        return Collections.singletonList(groupedMap);
    }

    /**
     * 根据系统权限ID查询所有相关的套餐-权限绑定关系（全量，不分页）
     *
     * @param sPermId 系统权限的唯一标识ID
     * @return 该系统权限相关的所有绑定关系VO列表
     */
    @Override
    public List<ComboPermVO> getComboPermsBySPermId(Long sPermId) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSPermId, sPermId)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);
        List<ComboPerm> comboPermList = this.list(queryWrapper);
        return comboPermConverter.EnListToVOList(comboPermList);
    }

    /**
     * 根据系统权限ID列表查询所有绑定关系，并按系统权限ID分组返回（全量，不分页）
     *
     * @param sPermIds 系统权限的ID列表
     * @return 包含一个Map的列表，Map中key为系统权限ID，value为对应的绑定关系VO列表；若参数为空则返回空列表
     */
    @Override
    public List<Map<Long, List<ComboPermVO>>> getComboPermsBySPermIds(List<Long> sPermIds) {
        if (sPermIds == null || sPermIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 构建查询条件
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboPerm::getSPermId, sPermIds)
                .eq(ComboPerm::getDeleted, false)
                .orderByDesc(ComboPerm::getCreatedAt);

        // 查询所有匹配的记录
        List<ComboPerm> entities = this.list(queryWrapper);

        // 转换为VO对象
        List<ComboPermVO> voList = comboPermConverter.EnListToVOList(entities);

        // 按SPermId分组
        Map<Long, List<ComboPermVO>> groupedMap = voList.stream()
                .collect(Collectors.groupingBy(ComboPermVO::getSPermId));

        // 将Map包装在List中返回，保持与分页方法返回类型的一致性
        return Collections.singletonList(groupedMap);
    }

    /**
     * 根据系统套餐ID查询其所关联的所有系统权限ID
     *
     * @param sComboId 系统套餐的唯一标识ID
     * @return 系统权限ID的集合，若没有关联数据则返回空集合
     */
    @Override
    public Set<Long> getSPermIdsBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSComboId, sComboId)
                .eq(ComboPerm::getDeleted, false)
                .select(ComboPerm::getSPermId);
        List<ComboPerm> list = this.list(queryWrapper);
        return list.stream().map(ComboPerm::getSPermId).collect(Collectors.toSet());
    }

    /**
     * 根据系统权限ID查询所有关联了该权限的系统套餐ID
     *
     * @param sPermId 系统权限的唯一标识ID
     * @return 系统套餐ID的集合，若没有关联数据则返回空集合
     */
    @Override
    public Set<Long> getSComboIdsBySPermId(Long sPermId) {
        LambdaQueryWrapper<ComboPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboPerm::getSPermId, sPermId)
                .eq(ComboPerm::getDeleted, false)
                .select(ComboPerm::getSComboId);
        List<ComboPerm> list = this.list(queryWrapper);
        return list.stream().map(ComboPerm::getSComboId).collect(Collectors.toSet());
    }

}
