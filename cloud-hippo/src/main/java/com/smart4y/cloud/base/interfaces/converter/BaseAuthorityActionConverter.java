package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseAuthorityAction;
import com.smart4y.cloud.base.interfaces.vo.BaseAuthorityActionVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseAuthorityActionConverter extends AbstractConverter<BaseAuthorityAction, BaseAuthorityActionVO> {

    @Override
    public BaseAuthorityActionVO convert(BaseAuthorityAction source, Map<String, Object> parameters) {
        BaseAuthorityActionVO target = new BaseAuthorityActionVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}