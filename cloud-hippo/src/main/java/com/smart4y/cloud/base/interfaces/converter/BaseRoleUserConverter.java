package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.BaseRoleUser;
import com.smart4y.cloud.base.interfaces.vo.BaseRoleUserVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class BaseRoleUserConverter extends AbstractConverter<BaseRoleUser, BaseRoleUserVO> {

    @Override
    public BaseRoleUserVO convert(BaseRoleUser source, Map<String, Object> parameters) {
        BaseRoleUserVO target = new BaseRoleUserVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}