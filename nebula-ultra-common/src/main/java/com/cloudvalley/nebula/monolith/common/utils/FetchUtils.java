package com.cloudvalley.nebula.monolith.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 获取工具类
 **/
public class FetchUtils {

    /**
     * 获取当前登录用户ID
     * TODO: 实际项目中应该从Spring Security或其他认证框架的上下文中获取
     */
    public static Long getCurrentUserId() {
        // 这里应该从认证上下文中获取当前登录用户的ID
        return 1L;
    }

    /**
     * 通用的分页分组查询模板
     * @param ids               要查询的ID列表（如用户ID、部门ID等）
     * @param page              分页参数
     * @param queryWrapperCustomizer 自定义查询条件（如 in(id, ids) 等）
     * @param pageQueryExecutor 执行分页查询的函数：接收 wrapper 返回 IPage<E>
     * @param converter         实体列表转VO列表的转换器
     * @param groupKeyExtractor VO中用于分组的键提取器（如 UserDeptVO::getUserId）
     * @param <E>               实体类型
     * @param <VO>              VO类型
     * @return 按指定ID分组的分页结果 IPage<Map<Long, List<VO>>>
     */
    public static <E, VO> IPage<Map<Long, List<VO>>> pageGroupQuery(
            List<Long> ids,
            Page<E> page,
            Consumer<LambdaQueryWrapper<E>> queryWrapperCustomizer,
            BiFunction<Page<E>, LambdaQueryWrapper<E>, IPage<E>> pageQueryExecutor,
            Function<List<E>, List<VO>> converter,
            Function<VO, Long> groupKeyExtractor
    ) {
        Map<Long, List<VO>> allGroupData = new HashMap<>();
        long totalRecords = 0;

        if (ids == null || ids.isEmpty()) {
            // 返回空分页
            return new Page<Map<Long, List<VO>>>()
                    .setRecords(new ArrayList<>())
                    .setTotal(0)
                    .setCurrent(page.getCurrent())
                    .setSize(page.getSize());
        }

        // 构建查询条件
        LambdaQueryWrapper<E> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapperCustomizer.accept(queryWrapper);

        // 执行分页查询（由调用方提供执行逻辑）
        IPage<E> dataPage = pageQueryExecutor.apply(page, queryWrapper);
        totalRecords = dataPage.getTotal();

        // 转换为 VO
        List<VO> voList = converter.apply(dataPage.getRecords());

        // 内存中按 key 分组
        Map<Long, List<VO>> groupedData = voList.stream()
                .filter(vo -> groupKeyExtractor.apply(vo) != null)
                .collect(Collectors.groupingBy(groupKeyExtractor));

        // 补全所有传入的 ID（无数据则为空列表）
        for (Long id : ids) {
            allGroupData.put(id, groupedData.getOrDefault(id, new ArrayList<>()));
        }

        // 构造返回结果：records 是一个包含单个 Map 的列表
        List<Map<Long, List<VO>>> records = Arrays.asList(allGroupData);

        return new Page<Map<Long, List<VO>>>()
                .setRecords(records)
                .setTotal(totalRecords)
                .setCurrent(page.getCurrent())
                .setSize(page.getSize());
    }
}
