package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.interfaces.vo.BaseRoleVO;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseRoleConverter extends AbstractConverter<BaseRole, BaseRoleVO> {

    @Override
    public BaseRoleVO convert(BaseRole source, Map<String, Object> parameters) {
        BaseRoleVO target = new BaseRoleVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}