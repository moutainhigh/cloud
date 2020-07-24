package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.base.interfaces.vo.BaseAppVO;
import com.smart4y.cloud.core.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseAppConverter extends AbstractConverter<BaseApp, BaseAppVO> {

    @Override
    public BaseAppVO convert(BaseApp source, Map<String, Object> parameters) {
        BaseAppVO target = new BaseAppVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}