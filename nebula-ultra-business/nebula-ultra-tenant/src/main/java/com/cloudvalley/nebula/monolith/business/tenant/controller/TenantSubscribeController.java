package com.cloudvalley.nebula.monolith.business.tenant.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.tenant.model.entity.TenantSubscribe;
import com.cloudvalley.nebula.monolith.business.tenant.model.rto.TenantSubscribeRTO;
import com.cloudvalley.nebula.monolith.business.tenant.service.ITenantSubscribeService;
import com.cloudvalley.nebula.monolith.shared.api.tenant.model.vo.TenantSubscribeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户订阅的套餐 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/tenant-subscribe")
public class TenantSubscribeController {

    @Autowired
    private ITenantSubscribeService tenantSubscribeService;

    /**
     * 分页查询租户订阅列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 TenantSubscribeVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getTenantSubscribeList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<TenantSubscribeVO> result = tenantSubscribeService.getTenantSubscribeList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户订阅数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户订阅列表成功").setData(result);
    }

    /**
     * 根据多个订阅ID分页批量查询租户订阅信息
     * @param ids 订阅记录ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 TenantSubscribeVO 列表，表示匹配的订阅记录
     */
    @GetMapping("/batch")
    public SaResult getTenantSubscribesByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<TenantSubscribeVO> result = tenantSubscribeService.getTenantSubscribesByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户订阅数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户订阅信息成功").setData(result);
    }

    /**
     * 根据租户ID分页查询其订阅的套餐列表（查看某租户的订阅情况）
     * @param tenantId 租户ID（tTenantId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 TenantSubscribeVO 列表，表示该租户的订阅信息
     */
    @GetMapping("/by-tenant/{tenantId}")
    public SaResult getTenantSubscribesByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<TenantSubscribeVO> result = tenantSubscribeService.getTenantSubscribesByTenantId(tenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂无订阅").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户订阅列表成功").setData(result);
    }

    /**
     * 根据多个租户ID分页批量查询订阅信息，并按租户ID分组返回结果
     * @param tenantIds 租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tTenantId，值为对应租户的 TenantSubscribeVO 列表
     */
    @GetMapping("/by-tenants")
    public SaResult getTenantSubscribesByTenantIds(
            @RequestParam List<Long> tenantIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<Map<Long, List<TenantSubscribeVO>>> result = tenantSubscribeService.getTenantSubscribesByTenantIds(tenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂无订阅").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户订阅列表成功").setData(result);
    }

    /**
     * 根据套餐ID分页查询使用该套餐的租户列表（查看某套餐被哪些租户订阅）
     * @param comboId 套餐ID（tComboId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 TenantSubscribeVO 列表，表示订阅了该套餐的租户信息
     */
    @GetMapping("/by-combo/{comboId}")
    public SaResult getTenantSubscribesByComboId(
            @PathVariable Long comboId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<TenantSubscribeVO> result = tenantSubscribeService.getTenantSubscribesByComboId(comboId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该套餐暂无订阅").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取套餐订阅列表成功").setData(result);
    }

    /**
     * 根据多个套餐ID分页批量查询订阅租户，并按套餐ID分组返回结果
     * @param comboIds 套餐ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 tComboId，值为对应套餐的 TenantSubscribeVO 列表
     */
    @GetMapping("/by-combos")
    public SaResult getTenantSubscribesByComboIds(
            @RequestParam List<Long> comboIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<TenantSubscribe> page = new Page<>(current, size);
        IPage<Map<Long, List<TenantSubscribeVO>>> result = tenantSubscribeService.getTenantSubscribesByComboIds(comboIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些套餐暂无订阅").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取套餐订阅列表成功").setData(result);
    }

    /**
     * 新增租户订阅关系（绑定租户与套餐）
     * @param tenantSubscribeRTO 请求传输对象，包含 tTenantId 和 tComboId
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createTenantSubscribe(@RequestBody TenantSubscribeRTO tenantSubscribeRTO) {
        boolean result = tenantSubscribeService.createTenantSubscribe(tenantSubscribeRTO);
        if (result) {
            return SaResult.ok("创建租户订阅成功");
        }
        return SaResult.error("创建租户订阅失败");
    }

    /**
     * 更新租户订阅信息
     * @param id 订阅记录的唯一标识ID
     * @param tenantSubscribeRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateTenantSubscribe(@PathVariable Long id, @RequestBody TenantSubscribeRTO tenantSubscribeRTO) {
        tenantSubscribeRTO.setId(id);
        boolean result = tenantSubscribeService.updateTenantSubscribe(tenantSubscribeRTO);
        if (result) {
            return SaResult.ok("更新租户订阅成功");
        }
        return SaResult.error("更新租户订阅失败");
    }

    /**
     * 更新租户订阅状态（如：active、expired、suspended）
     * @param id 订阅记录ID
     * @param status 目标状态字符串（需符合业务枚举）
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/status")
    public SaResult updateTenantSubscribeStatus(@PathVariable Long id, @RequestParam String status) {
        boolean result = tenantSubscribeService.updateTenantSubscribeStatus(id, status);
        if (result) {
            return SaResult.ok("更新租户订阅状态成功");
        }
        return SaResult.error("更新租户订阅状态失败");
    }

    /**
     * 删除租户订阅（物理删除）
     * @param id 订阅记录ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteTenantSubscribe(@PathVariable Long id) {
        boolean result = tenantSubscribeService.deleteTenantSubscribe(id);
        if (result) {
            return SaResult.ok("删除租户订阅成功");
        }
        return SaResult.error("删除租户订阅失败");
    }

    /**
     * 软删除租户订阅（标记 deleted = true）
     * @param id 订阅记录ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteTenantSubscribe(@PathVariable Long id) {
        boolean result = tenantSubscribeService.softDeleteTenantSubscribe(id);
        if (result) {
            return SaResult.ok("软删除租户订阅成功");
        }
        return SaResult.error("软删除租户订阅失败");
    }

    /**
     * 批量删除多个租户订阅（物理删除）
     * @param ids 订阅记录ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteTenantSubscribes(@RequestParam List<Long> ids) {
        boolean result = tenantSubscribeService.batchDeleteTenantSubscribes(ids);
        if (result) {
            return SaResult.ok("批量删除租户订阅成功");
        }
        return SaResult.error("批量删除租户订阅失败");
    }

    /**
     * 批量软删除多个租户订阅（标记 deleted = true）
     * @param ids 订阅记录ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteTenantSubscribes(@RequestParam List<Long> ids) {
        boolean result = tenantSubscribeService.batchSoftDeleteTenantSubscribes(ids);
        if (result) {
            return SaResult.ok("批量软删除租户订阅成功");
        }
        return SaResult.error("批量软删除租户订阅失败");
    }

}
