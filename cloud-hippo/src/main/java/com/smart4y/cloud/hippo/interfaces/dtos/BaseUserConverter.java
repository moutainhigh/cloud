package com.smart4y.cloud.hippo.interfaces.dtos;

import com.smart4y.cloud.hippo.domain.model.BaseUser;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseUserConverter extends AbstractConverter<BaseUser, BaseUserVO> {

    @Override
    public BaseUserVO convert(BaseUser source, Map<String, Object> parameters) {
        BaseUserVO target = new BaseUserVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}