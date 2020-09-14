package com.smart4y.cloud.mapper.generator.template;

import com.smart4y.cloud.mapper.generator.TableClass;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;

import java.util.Properties;

/**
 * Entity 模板文件
 *
 * @author Youtao
 * on 2020/05/14 15:49
 */
public class GenerateByTemplateFile extends GeneratedJavaFile {

    public static final String ENCODING = "UTF-8";
    private final String targetPackage;
    private final String fileName;
    private final String templateContent;
    private final Properties properties;
    private final TableClass tableClass;
    private final ModelTemplateFormatter templateFormatter;

    public GenerateByTemplateFile(TableClass tableClass, ModelTemplateFormatter templateFormatter, Properties properties, String targetProject, String targetPackage, String fileName, String templateContent) {
        super(null, targetProject, ENCODING, null);
        this.targetProject = targetProject;
        this.targetPackage = targetPackage;
        this.fileName = fileName;
        this.templateContent = templateContent;
        this.properties = properties;
        this.tableClass = tableClass;
        this.templateFormatter = templateFormatter;
    }

    @Override
    public CompilationUnit getCompilationUnit() {
        return null;
    }

    @Override
    public String getFileName() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, fileName);
    }

    @Override
    public String getFormattedContent() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, templateContent);
    }

    @Override
    public String getTargetPackage() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, targetPackage);
    }

    @Override
    public boolean isMergeable() {
        return false;
    }
}