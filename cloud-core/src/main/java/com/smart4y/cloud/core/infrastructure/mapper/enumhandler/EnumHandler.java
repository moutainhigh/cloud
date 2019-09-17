package com.smart4y.cloud.core.infrastructure.mapper.enumhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Objects;

/**
 * 枚举通用转换器
 *
 * @author Youtao
 *         Created By Youtao on 2017/02/20
 */
@Slf4j
public class EnumHandler<E extends Enum<E> & IEnum<K>, K> extends BaseTypeHandler<E> {

    private Class<E> type;

    public EnumHandler() {
    }

    public EnumHandler(Class<E> type) {
        if (type == null) {
            log.error("Type argument cannot be null");
            throw new RuntimeException("Type argument cannot be null");
        }
        this.type = type;

        log.info("EnumHandler --> {}", type.getName());
    }

    @Override
    @SuppressWarnings("all")
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            K id = parameter.getCode();
            if (null == id) {
                log.error("Unsupported [id] type of enum");
                throw new RuntimeException("Unsupported [id] type of enum");
            }
            if (id instanceof Integer || id instanceof Short || id instanceof Character || id instanceof Byte) {
                ps.setInt(i, (Integer) id);
            } else if (id instanceof String) {
                ps.setString(i, (String) id);
            } else if (id instanceof Boolean) {
                ps.setBoolean(i, (Boolean) id);
            } else if (id instanceof Long) {
                ps.setLong(i, (Long) id);
            } else if (id instanceof Double) {
                ps.setDouble(i, (Double) id);
            } else if (id instanceof Float) {
                ps.setFloat(i, (Float) id);
            } else {
                log.error("Unsupported [id] type of enum");
                throw new RuntimeException("Unsupported [id] type of enum");
            }
        } else {
            ps.setObject(i, parameter.getCode(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        return toEnum(object);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object object = rs.getObject(columnIndex);
        return toEnum(object);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object object = cs.getObject(columnIndex);
        return toEnum(object);
    }

    private E toEnum(Object id) {
        return EnumSet.allOf(type).stream()
                .filter(e -> Objects.equals(id, e.getCode()))
                .findFirst()
                .orElse(null);
    }
}