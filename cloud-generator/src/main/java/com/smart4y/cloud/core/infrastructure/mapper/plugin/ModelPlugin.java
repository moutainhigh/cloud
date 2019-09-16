package com.smart4y.cloud.core.infrastructure.mapper.plugin;

import com.smart4y.cloud.core.infrastructure.mapper.template.ModelTemplateFormatter;
import com.smart4y.cloud.core.infrastructure.mapper.template.TableColumnBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.ObjectFactory;
import tk.mybatis.mapper.generator.file.GenerateByTemplateFile;
import tk.mybatis.mapper.generator.formatter.TemplateFormatter;
import tk.mybatis.mapper.generator.model.TableClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 每一个模板都需要配置一个插件，可以配置多个
 * <p>
 * 单文件模式
 * <pre>
 * &lt;plugin type="xxx.TemplateFilePlugin"&gt;
 *      &lt;property name="targetPackage"     value="com.xxx.controller"/&gt;
 * &lt;/plugin&gt;
 * </pre>
 *
 * @author Youtao
 *         Created by youtao on 2018/7/18.
 */
@Slf4j
public final class ModelPlugin extends PluginAdapter {

    /**
     * 编码
     */
    private static final String encoding = "UTF-8";
    /**
     * 项目路径（目录需要已经存在）
     */
    private static final String targetProject = "src/main/java";
    /**
     * 模板路径
     */
    private static final String templatePath = "mybatis/model.ftl";
    /**
     * 模板
     */
    private static final String templateFormatterClass = ModelTemplateFormatter.class.getName();
    /**
     * 模板生成器
     */
    private Object templateFormatter;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 生成的包（路径不存在则创建）
     */
    private String targetPackage;

    @Override
    public boolean validate(List<String> warnings) {
        boolean right = true;
        try {
            URL resourceUrl = null;
            try {
                resourceUrl = ObjectFactory.getResource(templatePath);
            } catch (Exception e) {
                warnings.add("本地加载\"templatePath\" 模板路径失败，尝试 URL 方式获取!");
            }
            if (resourceUrl == null) {
                resourceUrl = new URL(templatePath);
            }
            InputStream inputStream = resourceUrl.openConnection().getInputStream();
            templateContent = read(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            warnings.add("读取模板文件出错: " + e.getMessage());
            right = false;
        }
        if (StringUtils.isBlank(templateFormatterClass)) {
            warnings.add("没有配置 \"templateFormatterClass\" 模板处理器，使用默认的处理器!");
        }
        try {
            templateFormatter = Class.forName(templateFormatterClass).newInstance();
        } catch (Exception e) {
            warnings.add("初始化 templateFormatter 出错:" + e.getMessage());
            right = false;
        }
        if (!right) {
            return false;
        }
        int errorCount = 0;
        if (StringUtils.isBlank(targetProject)) {
            errorCount++;
            warnings.add("没有配置 \"targetProject\" 路径!");
        }
        if (StringUtils.isBlank(targetPackage)) {
            errorCount++;
            warnings.add("没有配置 \"targetPackage\" 路径!");
        }
        if (errorCount >= 2) {
            warnings.add("由于没有配置任何有效路径，不会生成任何额外代码!");
            return false;
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> list = new ArrayList<>();

        TableClass tableClass = TableColumnBuilder.build(introspectedTable);

        String fileName = tableClass.getShortClassName() + ".java";

        list.add(new GenerateByTemplateFile(tableClass,
                (TemplateFormatter) templateFormatter, properties, targetProject, targetPackage, fileName, templateContent));
        return list;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return new ArrayList<>();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.targetPackage = properties.getProperty("targetPackage");

        properties.setProperty("singleMode", "true");
        properties.setProperty("targetProject", targetProject);
        properties.setProperty("templateFormatter", templateFormatterClass);
        properties.setProperty("encoding", "UTF-8");
    }

    /**
     * 读取文件
     */
    private String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line).append("\n");
            line = reader.readLine();
        }
        return builder.toString();
    }
}