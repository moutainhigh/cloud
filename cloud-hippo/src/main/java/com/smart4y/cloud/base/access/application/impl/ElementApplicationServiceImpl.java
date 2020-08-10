package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.ElementApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacElement;
import com.smart4y.cloud.base.access.interfaces.dtos.element.RbacElementPageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/10 15:22
 */
@Slf4j
@ApplicationService
public class ElementApplicationServiceImpl extends BaseDomainService<RbacElement> implements ElementApplicationService {

    @Override
    public Page<RbacElement> getElementsPage(RbacElementPageQuery query) {
        Weekend<RbacElement> weekend = Weekend.of(RbacElement.class);
        WeekendCriteria<RbacElement, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(query.getElementName())) {
            criteria.andLike(RbacElement::getElementName, "%" + query.getElementName() + "%");
        }
        if (StringUtils.isNotBlank(query.getElementCode())) {
            criteria.andLike(RbacElement::getElementCode, "%" + query.getElementCode() + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return this.findPage(weekend, query.getPage(), query.getLimit());
    }
}