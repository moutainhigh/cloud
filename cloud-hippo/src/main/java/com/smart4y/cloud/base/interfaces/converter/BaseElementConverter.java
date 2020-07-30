package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseElement;
import com.smart4y.cloud.base.interfaces.vo.BaseActionVO;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseElementConverter extends AbstractConverter<BaseElement, BaseActionVO> {

    @Override
    public BaseActionVO convert(BaseElement source, Map<String, Object> parameters) {
        BaseActionVO target = new BaseActionVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}