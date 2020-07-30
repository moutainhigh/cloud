package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseOperation;
import com.smart4y.cloud.base.interfaces.vo.BaseResourceVO;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseOperationConverter extends AbstractConverter<BaseOperation, BaseResourceVO> {

    @Override
    public BaseResourceVO convert(BaseOperation source, Map<String, Object> parameters) {
        BaseResourceVO target = new BaseResourceVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}