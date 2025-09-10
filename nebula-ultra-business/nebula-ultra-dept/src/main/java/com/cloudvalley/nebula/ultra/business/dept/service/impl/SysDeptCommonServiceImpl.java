package com.cloudvalley.nebula.ultra.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.dept.converter.SysDeptConverter;
import com.cloudvalley.nebula.ultra.business.dept.model.entity.SysDept;
import com.cloudvalley.nebula.ultra.business.dept.mapper.SysDeptMapper;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import com.cloudvalley.nebula.ultra.shared.api.dept.service.ISysDeptCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysDeptCommonServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptCommonService {

    @Autowired
    private SysDeptConverter sysDeptConverter;

    /**
     * 根据主键 ID 查询单个未删除的系统部门。
     *
     * @param id 部门ID
     * @return 对应的 SysDeptVO，若不存在或已删除则返回 null
     */
    @Override
    public SysDeptVO getSysDeptById(Long id) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getId, id)
                .eq(SysDept::getDeleted, false);
        SysDept entity = this.getOne(queryWrapper);
        return entity != null ? sysDeptConverter.EnToVO(entity) : null;
    }

    /**
     * 根据ID列表查询多个系统部门。
     * @param ids 部门ID列表
     * @return 对应的部门VO列表 Map<部门ID, 部门VO>
     */
    @Override
    public Map<Long, SysDeptVO> getSysDeptsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDept::getId, ids)
                .eq(SysDept::getDeleted, false);

        List<SysDept> entities = this.list(queryWrapper);
        List<SysDeptVO> voList = sysDeptConverter.EnListToVOList(entities);

        return voList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        SysDeptVO::getId,
                        vo -> vo
                ));
    }

}
