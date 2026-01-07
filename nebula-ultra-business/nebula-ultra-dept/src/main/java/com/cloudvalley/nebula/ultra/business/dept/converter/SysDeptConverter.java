package com.cloudvalley.nebula.ultra.business.dept.converter;

import com.cloudvalley.nebula.ultra.business.dept.model.entity.SysDept;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.SysDeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysDeptConverter {

    /**
     * 将系统部门实体类（SysDept）转换为系统部门视图对象（SysDeptVO）。
     *
     * @param sysDept 系统部门实体对象，通常来自数据库查询结果
     * @return 转换后的 SysDeptVO 对象，用于向前端或其他层传输数据
     * @note 若传入参数为 null，则返回 null；转换过程应避免抛出异常。
     *       转换内容包括但不限于：部门ID、名称、编码、父级ID、排序号、状态、创建/更新时间等字段。
     *       此方法主要用于单个对象的数据脱敏与视图适配，确保VO层不暴露持久化实体细节。
     */
    @Mapping(source = "DDesc", target = "desc")
    SysDeptVO EnToVO(SysDept sysDept);

    /**
     * 将系统部门实体列表（List<SysDept>）批量转换为对应的视图对象列表（List<SysDeptVO>）。
     *
     * @param sysDeptList 系统部门实体对象列表，可能来自数据库批量查询结果
     * @return 转换后的 SysDeptVO 列表，保持原始列表顺序；若输入为 null 或空列表，则返回空列表
     * @note 该方法内部应对 null 元素做安全处理（即跳过或转换为 null 元素，依实现而定），
     *       并保证不会因列表中个别元素为 null 而导致整个转换失败。
     *       适用于分页查询、树形结构扁平化数据等场景下的批量数据封装。
     *       是 EnToVO 方法的集合扩展，提升批量数据响应效率与一致性。
     */
    List<SysDeptVO> EnListToVOList(List<SysDept> sysDeptList);

}
