package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseDeveloperVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
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
        target.setCreatedDate(source.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        target.setLastModifiedDate(source.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return target;
    }
}