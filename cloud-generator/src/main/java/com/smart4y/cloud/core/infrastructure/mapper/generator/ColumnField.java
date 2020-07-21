package com.smart4y.cloud.core.infrastructure.mapper.generator;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;

/**
 * 表字段信息
 *
 * @author Youtao
 * on 2020/5/14 14:43
 */
@Getter
@Setter
public class ColumnField implements Serializable {

    private TableClass tableClass;
    private String columnName;
    private String jdbcType;
    private String fieldName;
    private String remarks;
    private FullyQualifiedJavaType type;
    private String typePackage;
    private String shortTypeName;
    private String fullTypeName;
    private boolean identity;
    private boolean nullable;
    private boolean blobColumn;
    private boolean stringColumn;
    private boolean jdbcCharacterColumn;
    private boolean jdbcDateColumn;
    private boolean jdbcTimeColumn;
    private boolean sequenceColumn;
    private int length;
    private int scale;
}