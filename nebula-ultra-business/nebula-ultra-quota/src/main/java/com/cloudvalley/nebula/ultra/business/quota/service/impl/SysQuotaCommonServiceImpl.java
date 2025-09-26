package com.cloudvalley.nebula.ultra.business.quota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.quota.converter.SysQuotaConverter;
import com.cloudvalley.nebula.ultra.business.quota.mapper.SysQuotaMapper;
import com.cloudvalley.nebula.ultra.business.quota.model.entity.SysQuota;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.service.ISysQuotaCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SysQuotaCommonServiceImpl extends ServiceImpl<SysQuotaMapper, SysQuota> implements ISysQuotaCommonService {

    @Autowired
    private SysQuotaConverter sysQuotaConverter;

    /**
     * 根据ID查询单个系统配额信息
     * @param id 系统配额唯一标识ID
     * @return 对应的 SysQuotaVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public SysQuotaVO getSysQuotaById(Long id) {
        LambdaQueryWrapper<SysQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysQuota::getId, id)
                .eq(SysQuota::getDeleted, false);

        SysQuota sysQuota = this.getOne(queryWrapper);
        return sysQuota != null ? sysQuotaConverter.EnToVO(sysQuota) : null;
    }

    /**
     * 根据多个系统配额ID全量查询配额信息（不分页）
     * @param ids 系统配额ID列表
     * @return 所有匹配的 SysQuotaVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public Map<Long, SysQuotaVO> getSysQuotasByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<SysQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysQuota::getId, ids)
                .eq(SysQuota::getDeleted, false)
                .orderByDesc(SysQuota::getCreatedAt);
        List<SysQuota> list = this.list(queryWrapper);

        // 使用Converter将实体列表转为VO列表后，再转换为Map
        List<SysQuotaVO> voList = sysQuotaConverter.EnListToVOList(list);
        return voList.stream()
                .collect(Collectors.toMap(SysQuotaVO::getId, Function.identity()));
    }

}
