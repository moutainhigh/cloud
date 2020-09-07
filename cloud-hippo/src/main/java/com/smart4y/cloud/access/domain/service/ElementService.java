package com.smart4y.cloud.access.domain.service;

import com.smart4y.cloud.access.domain.entity.RbacElement;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/13 15:28
 */
@DomainService
public class ElementService extends BaseDomainService<RbacElement> {

    /**
     * 获取分页列表
     *
     * @param pageNo      页码
     * @param pageSize    页大小
     * @param elementName 名称
     * @param elementCode 标识
     * @return 分页列表
     */
    public Page<RbacElement> getPageLike(int pageNo, int pageSize, String elementName, String elementCode) {
        Weekend<RbacElement> weekend = Weekend.of(RbacElement.class);
        WeekendCriteria<RbacElement, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(elementName)) {
            criteria.andLike(RbacElement::getElementName, "%" + elementName + "%");
        }
        if (StringUtils.isNotBlank(elementCode)) {
            criteria.andLike(RbacElement::getElementCode, "%" + elementCode + "%");
        }
        weekend
                .orderBy("createdDate").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }

    /**
     * 指定元素是否存在
     *
     * @param elementCode 元素标识
     * @return true存在，false不存在
     */
    public boolean existByCode(String elementCode) {
        Weekend<RbacElement> weekend = Weekend.of(RbacElement.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacElement::getElementCode, elementCode);
        weekend
                .setOrderByClause("element_id DESC LIMIT 1");
        return this.exist(weekend);
    }
}