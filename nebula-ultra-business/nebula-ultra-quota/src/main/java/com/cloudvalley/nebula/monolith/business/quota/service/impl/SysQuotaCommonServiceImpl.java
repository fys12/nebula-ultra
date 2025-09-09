package com.cloudvalley.nebula.monolith.business.quota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.quota.converter.SysQuotaConverter;
import com.cloudvalley.nebula.monolith.business.quota.mapper.SysQuotaMapper;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.SysQuota;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.SysQuotaVO;
import com.cloudvalley.nebula.monolith.shared.api.quoat.service.ISysQuotaCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<SysQuotaVO> getSysQuotasByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<SysQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysQuota::getId, ids)
                .eq(SysQuota::getDeleted, false)
                .orderByDesc(SysQuota::getCreatedAt);
        List<SysQuota> list = this.list(queryWrapper);
        return sysQuotaConverter.EnListToVOList(list);
    }

}
