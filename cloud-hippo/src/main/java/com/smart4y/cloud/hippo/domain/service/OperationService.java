package com.smart4y.cloud.hippo.domain.service;

import com.smart4y.cloud.hippo.domain.entity.RbacOperation;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao on 2020/8/7 14:55
 */
@DomainService
public class OperationService extends BaseDomainService<RbacOperation> {

    /**
     * 获取指定操作列表
     *
     * @param operationIds 操作ID列表
     * @return 操作列表
     */
    public List<RbacOperation> getByIds(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationId, operationIds);
        weekend
                .orderBy("operationPath").asc();
        return this.list(weekend);
    }

    /**
     * 获取指定操作列表
     *
     * @param operationCodes 操作标识列表
     * @return 操作列表
     */
    public List<RbacOperation> getByCodes(Collection<String> operationCodes) {
        if (CollectionUtils.isEmpty(operationCodes)) {
            return Collections.emptyList();
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationCode, operationCodes);
        weekend
                .orderBy("operationPath").asc();
        return this.list(weekend);
    }

    /**
     * 获取指定操作列表
     *
     * @param serviceId 操作所属服务ID
     * @return 操作列表
     */
    public List<RbacOperation> getByServiceId(String serviceId) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacOperation::getOperationServiceId, serviceId);
        weekend
                .orderBy("operationPath").asc();
        return this.list(weekend);
    }

    /**
     * 获取指定操作之外的操作列表（差集）
     *
     * @param serviceId           操作所属服务ID
     * @param validOperationCodes 操作标识列表
     * @return 失效操作列表
     */
    public List<RbacOperation> getOutSideByCodes(String serviceId, Collection<String> validOperationCodes) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        WeekendCriteria<RbacOperation, Object> criteria = weekend.weekendCriteria();
        criteria
                .andEqualTo(RbacOperation::getOperationServiceId, serviceId);
        if (CollectionUtils.isNotEmpty(validOperationCodes)) {
            criteria
                    .andNotIn(RbacOperation::getOperationCode, validOperationCodes);
        }
        return this.list(weekend);
    }

    /**
     * 移除操作
     *
     * @param operationIds 操作ID列表
     */
    public void removeByIds(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return;
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationId, operationIds);
        this.remove(weekend);
    }

    /**
     * 获取分页列表
     *
     * @param pageNo             页码
     * @param pageSize           页大小
     * @param operationName      名称
     * @param operationPath      路径
     * @param operationServiceId 所属服务ID
     * @param operationDesc      描述
     * @return 分页列表
     */
    public Page<RbacOperation> getPageLike(int pageNo, int pageSize, String operationName, String operationPath, String operationServiceId, String operationDesc) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        WeekendCriteria<RbacOperation, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(operationName)) {
            criteria.andLike(RbacOperation::getOperationName, "%" + operationName + "%");
        }
        if (StringUtils.isNotBlank(operationDesc)) {
            criteria.andLike(RbacOperation::getOperationDesc, "%" + operationDesc + "%");
        }
        if (StringUtils.isNotBlank(operationPath)) {
            criteria.andLike(RbacOperation::getOperationPath, "%" + operationPath + "%");
        }
        if (StringUtils.isNotBlank(operationServiceId)) {
            criteria.andLike(RbacOperation::getOperationServiceId, "%" + operationServiceId + "%");
        }
        weekend
                .orderBy("operationPath").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }
}