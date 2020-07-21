package com.smart4y.cloud.core.infrastructure.mapper.generator;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 表信息
 *
 * @author Youtao
 * on 2020/5/14 14:43
 */
@Getter
@Setter
public class TableClass implements Serializable {

    private IntrospectedTable introspectedTable;

    private String tableName;       // 表名
    private String tableRemarks;    // 表备注
    private String tableAuthor;     // 表作者
    private String variableName;
    private String lowerCaseName;
    private String shortClassName;
    private String fullClassName;
    private String packageName;
    private FullyQualifiedJavaType type;    // 表类型 TABLE/VIEW

    private List<ColumnField> pkFields;
    private List<ColumnField> baseFields;
    private List<ColumnField> blobFields;
    private List<ColumnField> allFields;

    private Set<FullyQualifiedJavaType> javaTypes;    // 所有类型　
    private String createdAt;  // 创建时间
}