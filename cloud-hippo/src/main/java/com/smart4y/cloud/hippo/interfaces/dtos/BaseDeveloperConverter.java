package com.smart4y.cloud.hippo.interfaces.dtos;

import com.smart4y.cloud.hippo.domain.model.BaseDeveloper;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseDeveloperConverter extends AbstractConverter<BaseDeveloper, BaseDeveloperVO> {

    @Override
    public BaseDeveloperVO convert(BaseDeveloper source, Map<String, Object> parameters) {
        BaseDeveloperVO target = new BaseDeveloperVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}