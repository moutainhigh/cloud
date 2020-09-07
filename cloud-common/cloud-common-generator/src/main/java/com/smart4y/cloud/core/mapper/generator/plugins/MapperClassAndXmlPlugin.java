package com.smart4y.cloud.core.mapper.generator.plugins;

import com.smart4y.cloud.core.mapper.generator.enums.LombokAnnotations;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.MapperException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 通用 Mapper.java, Mapper.xml 生成器插件
 *
 * @author Youtao
 * on 2020/5/14 14:37
 */
public class MapperClassAndXmlPlugin extends PluginAdapter {

    private final Collection<LombokAnnotations> annotations;
    private final Set<String> mappers = new HashSet<>();
    private boolean caseSensitive = false;
    private boolean useMapperCommentGenerator = true;
    /**
     * 开始的分隔符（缺省为 mysql 方式，例如 mysql 为`，sqlserver 为[）
     * 设置为空
     */
    private String beginningDelimiter = "";
    /**
     * 结束的分隔符（缺省为 mysql 方式，例如 mysql 为`，sqlserver 为]）
     * 设置为空
     */
    private String endingDelimiter = "";
    /**
     * 数据库模式
     */
    private String schema;
    /**
     * 注释生成器
     */
    private CommentGeneratorConfiguration commentCfg;
    /**
     * 强制生成注解
     */
    private boolean forceAnnotation;
    /**
     * 是否生成实体
     */
    private boolean generateEntity = true;
    /**
     * 作者
     */
    private String author;

    public MapperClassAndXmlPlugin() {
        annotations = new HashSet<>(LombokAnnotations.values().length);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    private String getDelimiterName(String name) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtility.stringHasValue(schema)) {
            nameBuilder.append(schema);
            nameBuilder.append(".");
        }
        nameBuilder.append(beginningDelimiter);
        nameBuilder.append(name);
        nameBuilder.append(endingDelimiter);
        return nameBuilder.toString();
    }

    /**
     * 生成的 Mapper.java 接口文件
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // 获取实体类
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        // import 接口
        for (String mapper : mappers) {
            interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
            interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
        }
        // import 实体类
        interfaze.addImportedType(entityType);

        // 添加 @Mapper 注解  import Mapper
        interfaze.addAnnotation("@Mapper");
        interfaze.addImportedType(new FullyQualifiedJavaType(Mapper.class.getName()));
        // 添加 @Repository
        interfaze.addAnnotation("@Repository");
        interfaze.addImportedType(new FullyQualifiedJavaType(Repository.class.getName()));
        // 添加 类注释
        String javaDocLine = String.format(
                "/**\n" +
                        " * %s\n" +
                        " *\n" +
                        " * @author %s on %s\n" +
                        " */",
                introspectedTable.getRemarks(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                this.author);
        interfaze.addJavaDocLine(javaDocLine);

        return true;
    }

    /**
     * 处理实体类的包和@Table注解
     */
    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // #1 引入 Lombok 注解
        for (LombokAnnotations annotation : this.annotations) {
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }

        // #2 引入 JPA 注解
        topLevelClass.addImportedType("javax.persistence.*");
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        // 如果包含空格，或者需要分隔符，需要完善
        if (StringUtility.stringContainsSpace(tableName)) {
            tableName = this.context.getBeginningDelimiter()
                    + tableName
                    + this.context.getEndingDelimiter();
        }
        // 是否忽略大小写，对于区分大小写的数据库，会有用
        if (this.caseSensitive && !topLevelClass.getType().getShortName().equals(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (!topLevelClass.getType().getShortName().equalsIgnoreCase(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (StringUtility.stringHasValue(this.schema)
                || StringUtility.stringHasValue(this.beginningDelimiter)
                || StringUtility.stringHasValue(this.endingDelimiter)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (this.forceAnnotation) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        }
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        // 设置默认的注释生成器
        this.useMapperCommentGenerator = !"FALSE".equalsIgnoreCase(context.getProperty("useMapperCommentGenerator"));
        if (this.useMapperCommentGenerator) {
            this.commentCfg = new CommentGeneratorConfiguration();
            //commentCfg.setConfigurationType(MapperCommentGenerator.class.getCanonicalName());
            this.commentCfg.setConfigurationType(DefaultCommentGenerator.class.getCanonicalName());
            context.setCommentGeneratorConfiguration(this.commentCfg);
        }
        // 支持 oracle 获取注释
        context.getJdbcConnectionConfiguration().addProperty("remarksReporting", "true");
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String mappers = this.properties.getProperty("mappers");
        if (StringUtility.stringHasValue(mappers)) {
            Collections.addAll(this.mappers, mappers.split(","));
        } else {
            throw new MapperException("Mapper 插件缺少必要的 mappers 属性!");
        }

        String caseSensitive = this.properties.getProperty("caseSensitive");
        if (StringUtility.stringHasValue(caseSensitive)) {
            this.caseSensitive = caseSensitive.equalsIgnoreCase("TRUE");
        }

        String forceAnnotation = this.properties.getProperty("forceAnnotation");
        if (StringUtility.stringHasValue(forceAnnotation)) {
            if (this.useMapperCommentGenerator) {
                this.commentCfg.addProperty("forceAnnotation", forceAnnotation);
            }
            this.forceAnnotation = forceAnnotation.equalsIgnoreCase("TRUE");
        }

        String beginningDelimiter = this.properties.getProperty("beginningDelimiter");
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            this.beginningDelimiter = beginningDelimiter;
        }
        this.commentCfg.addProperty("beginningDelimiter", this.beginningDelimiter);
        String endingDelimiter = this.properties.getProperty("endingDelimiter");
        if (StringUtility.stringHasValue(endingDelimiter)) {
            this.endingDelimiter = endingDelimiter;
        }
        this.commentCfg.addProperty("endingDelimiter", this.endingDelimiter);

        String schema = this.properties.getProperty("schema");
        if (StringUtility.stringHasValue(schema)) {
            this.schema = schema;
        }

        if (this.useMapperCommentGenerator) {
            this.commentCfg.addProperty("beginningDelimiter", this.beginningDelimiter);
            this.commentCfg.addProperty("endingDelimiter", this.endingDelimiter);
        }
        this.generateEntity = !"FALSE".equalsIgnoreCase(this.properties.getProperty("generateEntity"));

        this.author = this.properties.getProperty("author");

        // 添加 Lombok 注解
        // @Data is default annotation
        this.annotations.add(LombokAnnotations.DATA);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());
            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                LombokAnnotations annotation = LombokAnnotations.getValueOf(paramName);
                if (annotation != null) {
                    this.annotations.add(annotation);
                    this.annotations.addAll(LombokAnnotations.getDependencies(annotation));
                }
            }
        }
    }

    /**
     * 不生成实体类属性的`get`方法
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * 不生成实体类属性的`set`方法
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * 生成基础实体类
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return this.generateEntity;
    }

    /**
     * 生成实体类注解 KEY 对象
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return this.generateEntity;
    }

    /**
     * 生成带 BLOB 字段的对象
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return false;
    }

//**************下面所有 return false 的方法都不生成。这些都是基础的 CRUD 方法，改为使用通用 Mapper 实现

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }
}