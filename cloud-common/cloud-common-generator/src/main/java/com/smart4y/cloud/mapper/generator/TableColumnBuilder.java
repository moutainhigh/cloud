package com.smart4y.cloud.mapper.generator;

import com.smart4y.cloud.mapper.SnowflakeId;
import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.ColumnOverride;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.Introspector;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 表信息构造器
 *
 * @author Youtao
 * on 2020/5/14 14:43
 */
public final class TableColumnBuilder {

    /**
     * 创建 TableClass
     */
    public static TableClass build(IntrospectedTable introspectedTable, String author) {
        TableClass tableClass = new TableClass();
        tableClass.setIntrospectedTable(introspectedTable);

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        tableClass.setTableName(fullyQualifiedTable.getIntrospectedTableName());
        tableClass.setTableRemarks(introspectedTable.getRemarks());
        tableClass.setTableAuthor(author);
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
        introspectedTable.getPrimaryKeyColumns()
                .forEach(column -> {
                    column.setIdentity(Boolean.TRUE);
                    ColumnField field = build(column);
                    field.setTableClass(tableClass);
                    pkFields.add(field);
                    allFields.add(field);
                });

        introspectedTable.getBaseColumns()
                .forEach(column -> {
                    ColumnField field = build(column);
                    field.setTableClass(tableClass);
                    baseFields.add(field);
                    allFields.add(field);
                });

        introspectedTable.getBLOBColumns()
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

        // 所有引用import
        tableClass.setJavaTypes(fullyQualifiedJavaTypes(tableClass));
        // 创建时间
        tableClass.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

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
     * 添加 import
     */
    private static Set<FullyQualifiedJavaType> fullyQualifiedJavaTypes(TableClass tableClass) {
        // 添加字段对应类型依赖
        Set<FullyQualifiedJavaType> qualifiedJavaTypeSet = tableClass.getAllFields().stream()
                .map(x -> jdbcType2JavaType(x.getJdbcType(), null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 添加基础实体类依赖
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(BaseEntity.class.getName());
        qualifiedJavaTypeSet.add(fullyQualifiedJavaType);

        // @Id import
        List<ColumnField> pkFields = tableClass.getPkFields();
        if (CollectionUtils.isNotEmpty(pkFields)) {
            qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(Id.class.getName()));
            
            // 若表中有主键，则设置主键生成策略
            boolean isLong = pkFields.stream()
                    .map(ColumnField::getType)
                    .map(FullyQualifiedJavaType::getShortName)
                    .anyMatch("Long"::equals);
            if (isLong) {
                qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(KeySql.class.getName()));
                qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(SnowflakeId.class.getName()));
            }
        }
        qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(Table.class.getName()));
        qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(Column.class.getName()));
        qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(Data.class.getName()));
        qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(Accessors.class.getName()));
        qualifiedJavaTypeSet.add(new FullyQualifiedJavaType(EqualsAndHashCode.class.getName()));


        // 添加枚举 import generatorConfig -> context -> table -> columnOverride.javaType
        IntrospectedTable introspectedTable = tableClass.getIntrospectedTable();
        if (null != introspectedTable) {
            List<ColumnOverride> columnOverrides = introspectedTable.getTableConfiguration().getColumnOverrides();
            if (CollectionUtils.isNotEmpty(columnOverrides)) {
                List<FullyQualifiedJavaType> sub = columnOverrides.stream()
                        .map(x -> new FullyQualifiedJavaType(x.getJavaType()))
                        .collect(Collectors.toList());
                qualifiedJavaTypeSet.addAll(sub);
            }
        }


        return qualifiedJavaTypeSet;
    }

    private static FullyQualifiedJavaType jdbcType2JavaType(String jdbcTypeName, FullyQualifiedJavaType fullyQualifiedJavaType) {
        FullyQualifiedJavaType type = fullyQualifiedJavaType;
        switch (jdbcTypeName.toUpperCase()) {
            case "DATE":
                type = new FullyQualifiedJavaType(LocalDate.class.getName());
                break;
            case "DATETIME":
            case "TIMESTAMP":
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