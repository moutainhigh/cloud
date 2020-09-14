package com.smart4y.cloud.mapper.generator.template;

import com.smart4y.cloud.mapper.generator.TableClass;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Entity 生成模板
 *
 * @author Youtao
 * on 2020/05/14 15:49
 */
@Slf4j
public class ModelTemplateFormatter {

    private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

    public ModelTemplateFormatter() {
        configuration.setLocale(Locale.CHINA);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new StringTemplateLoader());
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_0));
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

    /**
     * 获取根据模板生成的数据
     *
     * @param tableClass
     * @param properties
     * @param targetPackage
     * @param templateContent
     * @return
     */
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

    public Map<String, Object> buildTemplateFormatterParams(TableClass tableClass, Properties properties, String targetPackage) {
        Map<String, Object> params = new HashMap<>();
        // params.put("props", properties);
        // params.put("package", targetPackage);
        params.put("tableClass", tableClass);
        properties.setProperty("templatePath", "mybatis/model.ftl");
        return params;
    }
}