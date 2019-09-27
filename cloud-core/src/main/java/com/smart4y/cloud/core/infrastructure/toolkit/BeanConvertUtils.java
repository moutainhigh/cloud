package com.smart4y.cloud.core.infrastructure.toolkit;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class BeanConvertUtils {

    /**
     * 方法说明：将bean转化为另一种bean实体
     */
    public static <T> T convertBean(Object object, Class<T> entityClass) {
        if (null == object) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(object), entityClass);
    }


    /**
     * 方法说明：对象转换
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     */
    public static <T> T copy(Object source, Class<T> target, String... ignoreProperties) {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, targetInstance);
        } else {
            BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
        }
        return targetInstance;

    }

    /**
     * 方法说明：对象转换(List)
     *  
     *
     * @param list             原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     */
    public static <T, E> List<T> copyList(List<E> list, Class<T> target, String... ignoreProperties) {
        List<T> targetList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return targetList;
        }
        for (E e : list) {
            targetList.add(copy(e, target, ignoreProperties));
        }
        return targetList;
    }

    /**
     * 方法说明：map转化为对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> t) {
        try {
            T instance = t.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(instance, map);
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 方法说明：对象转化为Map
     */
    public static Map<?, ?> objectToMap(Object object) {
        return convertBean(object, Map.class);
    }
}