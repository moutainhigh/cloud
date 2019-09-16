package com.smart4y.cloud.core.infrastructure.toolkit.reflection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 反射操作工具类
 *
 * @author Youtao
 *         Created By Youtao on 2017/12/06.
 */
@Slf4j
public enum ReflectionHelper {

    INSTANCE;

    /**
     * 对于被 cglib AOP 过的对象包含此符号
     */
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    /**
     * 获取属性名
     *
     * @param fnp 对象的 属性引用
     * @return 属性名
     */
    public <P> String getFieldName(Fnp<P> fnp) {
        try {
            Method method = fnp.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fnp);
            String getter = serializedLambda.getImplMethodName();
            if (GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if (IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }
            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取属性值
     *
     * @param obj       对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public Object invokeGetter(Object obj, String fieldName) {
        String getterMethodName = "get" + StringUtils.capitalize(fieldName);
        try {
            return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 设置属性值
     *
     * @param obj        对象
     * @param fnp        对象的属性引用
     * @param fieldValue 属性值
     */
    public <P> void invokeSetter(Object obj, Fnp<P> fnp, Object fieldValue) {
        String fieldName = getFieldName(fnp);
        invokeSetter(obj, fieldName, fieldValue, null);
    }

    /**
     * 设置属性值
     *
     * @param obj        对象
     * @param fieldName  属性名
     * @param fieldValue 属性值
     */
    public void invokeSetter(Object obj, String fieldName, Object fieldValue) {
        invokeSetter(obj, fieldName, fieldValue, null);
    }

    /**
     * 设置属性值
     *
     * @param obj        对象
     * @param fieldName  属性名
     * @param fieldValue 属性值
     * @param fieldType  属性类型，例如：{@link String}
     */
    public void invokeSetter(Object obj, String fieldName, Object fieldValue, Class<?> fieldType) {
        Class<?> type = fieldType != null ? fieldType : fieldValue.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(fieldName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{fieldValue});
    }

    /**
     * 获取属性值
     * <p>
     * 直接读取对象属性值，无视 private/protected 修饰符，不经过 getter 函数
     * </p>
     *
     * @param obj 对象
     * @param fnp 对象的属性引用
     * @return 属性值
     */
    public <P> Object getFieldValue(final Object obj, final Fnp<P> fnp) {
        String fieldName = getFieldName(fnp);
        return getFieldValue(obj, fieldName);
    }

    /**
     * 获取属性值
     * <p>
     * 直接读取对象属性值，无视 private/protected 修饰符，不经过 getter 函数
     * </p>
     *
     * @param obj       对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 设置属性值
     * <p>
     * 直接设置对象属性值，无视 private/protected 修饰符，不经过 setter 函数
     * </p>
     *
     * @param obj        对象
     * @param fieldName  属性名
     * @param fieldValue 属性值
     */
    public void setFieldValue(final Object obj, final String fieldName, final Object fieldValue) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try {
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 对于被 cglib AOP 过的对象，取得真实的 Class 类型
     */
    public Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     */
    public Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     */
    public Field getAccessibleField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked,rawtypes")
    public <T> Class<T> getSuperClassGenericType(final Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("rawtypes")
    public Class getSuperClassGenericType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 将对象组装成 MAP
     *
     * @param obj 对象
     * @return 属性名与属性值映射（fieldName ==> fieldValue）
     */
    public Map<String, Object> getFieldMapForClass(Object obj) {
        Map<String, Object> parameterMap;
        try {
            parameterMap = new HashMap<>();
            Field[] fields = obj.getClass().getDeclaredFields();
            // set self property
            for (Field field : fields) {
                populateFieldMap(field, obj, parameterMap);
            }
            // set parent property
            Field[] parentFields = obj.getClass().getSuperclass().getDeclaredFields();
            for (Field parentField : parentFields) {
                populateFieldMap(parentField, obj, parameterMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return parameterMap;
    }

    private void populateFieldMap(Field field, Object object, Map<String, Object> fieldMap) throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        fieldMap.put(field.getName(), field.get(object));
        field.setAccessible(isAccessible);
    }
}