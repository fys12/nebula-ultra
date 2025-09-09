package com.cloudvalley.nebula.monolith.business.quota.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.QuotaTenant;
import com.cloudvalley.nebula.monolith.business.quota.model.rto.QuotaTenantRTO;
import com.cloudvalley.nebula.monolith.business.quota.service.IQuotaTenantService;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.QuotaTenantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户配额总览 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/quota-tenant")
public class QuotaTenantController {

    @Autowired
    private IQuotaTenantService iQuotaTenantService;

    /**
     * 分页查询租户配额绑定列表
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页的 QuotaTenantVO 列表，按创建时间倒序排列；若无数据返回空分页
     */
    @GetMapping
    public SaResult getQuotaTenantList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<QuotaTenantVO> result = iQuotaTenantService.getQuotaTenantList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户配额数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户配额列表成功").setData(result);
    }

    /**
     * 根据多个租户配额ID分页批量查询绑定信息
     * @param ids 租户配额关联ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 QuotaTenantVO 列表，表示匹配的「租户-配额」绑定关系
     */
    @GetMapping("/batch")
    public SaResult getQuotaTenantsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<QuotaTenantVO> result = iQuotaTenantService.getQuotaTenantsByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无租户配额数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户配额信息成功").setData(result);
    }

    /**
     * 根据系统租户ID分页查询其绑定的配额列表（查看某租户拥有哪些配额）
     * @param tenantId 系统租户ID（sTenantId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 QuotaTenantVO 列表，表示该租户绑定的配额信息
     */
    @GetMapping("/by-tenant/{tenantId}")
    public SaResult getQuotaTenantsByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<QuotaTenantVO> result = iQuotaTenantService.getQuotaTenantsByTenantId(tenantId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该租户暂无配额").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取租户配额列表成功").setData(result);
    }

    /**
     * 根据多个系统租户ID分页批量查询绑定配额，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 sTenantId，值为对应租户的 QuotaTenantVO 列表
     */
    @GetMapping("/by-tenants")
    public SaResult getQuotaTenantsByTenantIds(
            @RequestParam List<Long> tenantIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaTenantVO>>> result = iQuotaTenantService.getQuotaTenantsByTenantIds(tenantIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些租户暂无配额").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取租户配额列表成功").setData(result);
    }

    /**
     * 根据系统配额ID分页查询使用该配额的租户列表（查看某配额被哪些租户使用）
     * @param quotaId 系统配额ID（sQuotaId）
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 QuotaTenantVO 列表，表示使用该配额的租户信息
     */
    @GetMapping("/by-quota/{quotaId}")
    public SaResult getQuotaTenantsByQuotaId(
            @PathVariable Long quotaId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<QuotaTenantVO> result = iQuotaTenantService.getQuotaTenantsByQuotaId(quotaId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该配额暂无租户使用").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取配额租户列表成功").setData(result);
    }

    /**
     * 根据多个系统配额ID分页批量查询使用租户，并按配额ID分组返回结果
     * @param quotaIds 系统配额ID列表
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页的 Map 列表，键为 sQuotaId，值为对应配额的 QuotaTenantVO 列表
     */
    @GetMapping("/by-quotas")
    public SaResult getQuotaTenantsByQuotaIds(
            @RequestParam List<Long> quotaIds,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<QuotaTenant> page = new Page<>(current, size);
        IPage<Map<Long, List<QuotaTenantVO>>> result = iQuotaTenantService.getQuotaTenantsByQuotaIds(quotaIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些配额暂无租户使用").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取配额租户列表成功").setData(result);
    }

    /**
     * 新增「系统租户-系统配额」绑定关系
     * @param quotaTenantRTO 请求传输对象，包含 sTenantId 和 sQuotaId
     * @return 创建成功返回 ok，失败返回 error
     */
    @PostMapping
    public SaResult createQuotaTenant(@RequestBody QuotaTenantRTO quotaTenantRTO) {
        boolean result = iQuotaTenantService.createQuotaTenant(quotaTenantRTO);
        if (result) {
            return SaResult.ok("创建租户配额成功");
        }
        return SaResult.error("创建租户配额失败");
    }

    /**
     * 更新「系统租户-系统配额」绑定信息
     * @param id 绑定关系的唯一标识ID
     * @param quotaTenantRTO 更新数据对象
     * @return 更新成功返回 ok，失败返回 error
     */
    @PutMapping("/{id}")
    public SaResult updateQuotaTenant(@PathVariable Long id, @RequestBody QuotaTenantRTO quotaTenantRTO) {
        quotaTenantRTO.setId(id);
        boolean result = iQuotaTenantService.updateQuotaTenant(quotaTenantRTO);
        if (result) {
            return SaResult.ok("更新租户配额成功");
        }
        return SaResult.error("更新租户配额失败");
    }

    /**
     * 删除租户配额绑定（物理删除）
     * @param id 绑定关系ID
     * @return 删除成功返回 ok，失败返回 error
     */
    @DeleteMapping("/{id}")
    public SaResult deleteQuotaTenant(@PathVariable Long id) {
        boolean result = iQuotaTenantService.deleteQuotaTenant(id);
        if (result) {
            return SaResult.ok("删除租户配额成功");
        }
        return SaResult.error("删除租户配额失败");
    }

    /**
     * 软删除租户配额绑定（标记 deleted = true）
     * @param id 绑定关系ID
     * @return 操作成功返回 ok，失败返回 error
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteQuotaTenant(@PathVariable Long id) {
        boolean result = iQuotaTenantService.softDeleteQuotaTenant(id);
        if (result) {
            return SaResult.ok("软删除租户配额成功");
        }
        return SaResult.error("软删除租户配额失败");
    }

    /**
     * 批量删除多个租户配额绑定（物理删除）
     * @param ids 绑定关系ID列表
     * @return 全部删除成功返回 ok，否则返回 error
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteQuotaTenants(@RequestParam List<Long> ids) {
        boolean result = iQuotaTenantService.batchDeleteQuotaTenants(ids);
        if (result) {
            return SaResult.ok("批量删除租户配额成功");
        }
        return SaResult.error("批量删除租户配额失败");
    }

    /**
     * 批量软删除多个租户配额绑定（标记 deleted = true）
     * @param ids 绑定关系ID列表
     * @return 全部操作成功返回 ok，否则返回 error
     */
    @PatchMapping("/batch/soft-delete")
    public SaResult batchSoftDeleteQuotaTenants(@RequestParam List<Long> ids) {
        boolean result = iQuotaTenantService.batchSoftDeleteQuotaTenants(ids);
        if (result) {
            return SaResult.ok("批量软删除租户配额成功");
        }
        return SaResult.error("批量软删除租户配额失败");
    }

}
