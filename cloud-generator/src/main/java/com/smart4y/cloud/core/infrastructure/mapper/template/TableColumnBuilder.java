package com.smart4y.cloud.core.infrastructure.mapper.template;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import tk.mybatis.mapper.generator.model.ColumnField;
import tk.mybatis.mapper.generator.model.TableClass;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Youtao
 *         Created by youtao on 2018/7/18.
 */
public final class TableColumnBuilder {

    /**
     * 创建 TableClass
     */
    public static TableClass build(IntrospectedTable introspectedTable) {
        TableClass tableClass = new TableClass();
        tableClass.setIntrospectedTable(introspectedTable);

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        tableClass.setTableName(fullyQualifiedTable.getIntrospectedTableName());

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        tableClass.setType(type);
        tableClass.setVariableName(Introspector.decapitalize(type.getShortName()));
        tableClass.setLowerCaseName(type.getShortName().toLowerCase());
        tableClass.setShortClassName(type.getShortName());
        tableClass.setFullClassName(type.getFullyQualifiedName());
        tableClass.setPackageName(type.getPackageName());

        List<ColumnField> pkFields = new ArrayList<>();
        List<ColumnField> baseFields = new ArrayList<>();
        List<ColumnField> blobFields = new ArrayList<>();
        List<ColumnField> allFields = new ArrayList<>();

        // 排除基类中的属性
        Field[] fields = BaseEntity.class.getDeclaredFields();

        introspectedTable.getPrimaryKeyColumns().stream()
                .filter(x -> notExitsBaseEntityField(fields, x.getJavaProperty()))
                .forEach(column -> {
                    column.setIdentity(Boolean.TRUE);
                    ColumnField field = build(column);
                    field.setTableClass(tableClass);
                    pkFields.add(field);
                    allFields.add(field);
                });

        introspectedTable.getBaseColumns().stream()
                .filter(x -> notExitsBaseEntityField(fields, x.getJavaProperty()))
                .forEach(column -> {
                    ColumnField field = build(column);
                    field.setTableClass(tableClass);
                    baseFields.add(field);
                    allFields.add(field);
                });

        introspectedTable.getBLOBColumns().stream()
                .filter(x -> notExitsBaseEntityField(fields, x.getJavaProperty()))
                .forEach(column -> {
                    ColumnField field = build(column);
                    field.setTableClass(tableClass);
                    blobFields.add(field);
                    allFields.add(field);
                });

        tableClass.setPkFields(pkFields);
        tableClass.setBaseFields(baseFields);
        tableClass.setBlobFields(blobFields);
        tableClass.setAllFields(allFields);

        return tableClass;
    }

    /**
     * 创建 ColumnField
     */
    private static ColumnField build(IntrospectedColumn column) {
        ColumnField field = new ColumnField();
        field.setColumnName(column.getActualColumnName());
        field.setJdbcType(column.getJdbcTypeName());
        field.setFieldName(column.getJavaProperty());
        field.setRemarks(column.getRemarks());

        // FullyQualifiedJavaType type = column.getFullyQualifiedJavaType();
        FullyQualifiedJavaType type = jdbcType2JavaType(column.getJdbcTypeName(), column.getFullyQualifiedJavaType());

        field.setType(type);
        field.setTypePackage(type.getPackageName());
        field.setShortTypeName(type.getShortName());
        field.setFullTypeName(type.getFullyQualifiedName());
        field.setIdentity(column.isIdentity());
        field.setNullable(column.isNullable());
        field.setSequenceColumn(column.isSequenceColumn());
        field.setBlobColumn(column.isBLOBColumn());
        field.setStringColumn(column.isStringColumn());
        field.setJdbcCharacterColumn(column.isJdbcCharacterColumn());
        field.setJdbcDateColumn(column.isJDBCDateColumn());
        field.setJdbcTimeColumn(column.isJDBCTimeColumn());
        field.setLength(column.getLength());
        field.setScale(column.getScale());
        return field;
    }

    /**
     * 判断 javaProperty 字段名是否在 fields属性中
     *
     * @param fields       Field[]
     * @param javaProperty javaProperty
     * @return true 不存在
     */
    private static boolean notExitsBaseEntityField(Field[] fields, String javaProperty) {
        return Arrays.stream(fields)
                .noneMatch(x -> x.getName().equalsIgnoreCase(javaProperty));
    }

    /**
     * 添加 import
     */
    static Set<FullyQualifiedJavaType> fullyQualifiedJavaTypes(TableClass tableClass) {
        return tableClass.getAllFields().stream()
                .map(x -> jdbcType2JavaType(x.getJdbcType(), null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static FullyQualifiedJavaType jdbcType2JavaType(String jdbcTypeName, FullyQualifiedJavaType fullyQualifiedJavaType) {
        FullyQualifiedJavaType type = fullyQualifiedJavaType;
        switch (jdbcTypeName.toUpperCase()) {
            case "DATE":
                type = new FullyQualifiedJavaType(LocalDate.class.getName());
                break;
            case "DATETIME":
            case "TIMESTAMP":
                //type = new FullyQualifiedJavaType(Date.class.getName());
                type = new FullyQualifiedJavaType(LocalDateTime.class.getName());
                break;
            case "TIME":
                type = new FullyQualifiedJavaType(LocalTime.class.getName());
                break;
            case "NUMERIC":
            case "DECIMAL":
                type = new FullyQualifiedJavaType(BigDecimal.class.getName());
                break;
            default:
                break;
        }
        return type;
    }
}