package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.interfaces.vo.BaseApiVO;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseApiConverter extends AbstractConverter<BaseApi, BaseApiVO> {

    @Override
    public BaseApiVO convert(BaseApi source, Map<String, Object> parameters) {
        BaseApiVO target = new BaseApiVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}