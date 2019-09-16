package com.smart4y.cloud.core.infrastructure.mapper.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import tk.mybatis.mapper.generator.formatter.ListTemplateFormatter;
import tk.mybatis.mapper.generator.formatter.TemplateFormatter;
import tk.mybatis.mapper.generator.model.TableClass;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author Youtao
 *         Created by youtao on 2018/12/10.
 */
@Slf4j
public abstract class AbstractTemplateFormatter implements TemplateFormatter, ListTemplateFormatter {

    private final Configuration configuration = new Configuration();

    AbstractTemplateFormatter() {
        configuration.setLocale(Locale.CHINA);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new StringTemplateLoader());
        configuration.setObjectWrapper(new DefaultObjectWrapper());
    }

    /**
     * 根据模板处理
     */
    private String process(String templateName, String templateSource, Map<String, Object> params, Configuration configuration) {
        try {
            StringReader stringReader = new StringReader(templateSource);
            Template template = new Template(templateName, stringReader, configuration);

            Writer writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormattedContent(TableClass tableClass, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        Map<String, Object> customParams = buildTemplateFormatterParams(tableClass, properties, targetPackage);
        if (MapUtils.isNotEmpty(customParams)) {
            params.putAll(customParams);
        }
        return process(properties.getProperty("templatePath"), templateContent, params, configuration);
    }

    abstract Map<String, Object> buildTemplateFormatterParams(TableClass tableClass, Properties properties, String targetPackage);

    @Override
    public String getFormattedContent(Set<TableClass> tableClassSet, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        Map<String, Object> customParams = buildListTemplateFormatterParams(tableClassSet, properties, targetPackage);
        if (MapUtils.isNotEmpty(customParams)) {
            params.putAll(customParams);
        }
        return process(properties.getProperty("templatePath"), templateContent, params, configuration);
    }

    abstract Map<String, Object> buildListTemplateFormatterParams(Set<TableClass> tableClassSet, Properties properties, String targetPackage);
}