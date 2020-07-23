package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.interfaces.vo.BaseMenuVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseMenuConverter extends AbstractConverter<BaseMenu, BaseMenuVO> {

    @Override
    public BaseMenuVO convert(BaseMenu source, Map<String, Object> parameters) {
        BaseMenuVO target = new BaseMenuVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}