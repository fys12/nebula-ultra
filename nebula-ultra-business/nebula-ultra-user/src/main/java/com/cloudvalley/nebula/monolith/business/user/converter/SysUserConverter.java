package com.cloudvalley.nebula.monolith.business.user.converter;

import com.cloudvalley.nebula.monolith.business.user.model.entity.SysUser;
import com.cloudvalley.nebula.monolith.shared.api.user.model.vo.SysUserVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysUserConverter {

    /**
     * 将系统用户实体对象（SysUser）转换为视图对象（SysUserVO）。
     *
     * @param sysUser 系统用户实体，来自数据库查询结果；可为 null
     * @return 转换后的 SysUserVO 对象；若输入为 null，则返回 null
     */
    SysUserVO EnToVO(SysUser sysUser);

    /**
     * 将系统用户实体列表批量转换为视图对象列表。
     *
     * @param sysUserList 系统用户实体列表，可能包含多个 SysUser 对象；可为 null 或空
     * @return 转换后的 SysUserVO 列表；若输入为 null 或空，则返回空列表
     */
    List<SysUserVO> EnListToVOList(List<SysUser> sysUserList);

}
