package com.cloudvalley.nebula.ultra.business.group.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupPermListVO {

    /**
     * 主键ID（雪花算法ID）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    /**
     * 系统组
     */
    SysGroupVO sysGroup;

}
