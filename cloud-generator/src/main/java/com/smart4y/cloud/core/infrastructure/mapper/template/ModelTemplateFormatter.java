package com.smart4y.cloud.core.infrastructure.mapper.template;

import com.smart4y.cloud.core.infrastructure.mapper.utils.MyBatisGeneratorHelper;
import com.smart4y.cloud.core.infrastructure.mapper.base.DbMetaHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import tk.mybatis.mapper.generator.model.TableClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 领域模型 生成模板
 *
 * @author Youtao
 *         Created by youtao on 2018/7/18.
 */
@Slf4j
public final class ModelTemplateFormatter extends AbstractTemplateFormatter {

    @Override
    Map<String, Object> buildTemplateFormatterParams(TableClass tableClass, Properties properties, String targetPackage) {
        Map<String, Object> params = new HashMap<>();
        params.put("createdAt", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        params.put("props", properties);
        params.put("package", targetPackage);
        params.put("tableClass", tableClass);
        params.put("tableRemarks", getTableRemarks(tableClass.getTableName()));
        params.put("fullyQualifiedJavaTypes", TableColumnBuilder.fullyQualifiedJavaTypes(tableClass));
        properties.setProperty("templatePath", "mybatis/model.ftl");
        return params;
    }

    @Override
    Map<String, Object> buildListTemplateFormatterParams(Set<TableClass> tableClassSet, Properties properties, String targetPackage) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableClassSet", tableClassSet);
        params.put("package", targetPackage);
        params.put("props", properties);
        params.put("createdAt", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        properties.setProperty("templatePath", "mybatis/model.ftl");
        return params;
    }

    /**
     * 获取 表备注
     *
     * @param tableName 表名
     * @return 表备注
     */
    private String getTableRemarks(String tableName) {
        try {
            JDBCConnectionConfiguration jdbcConnectionConfiguration = MyBatisGeneratorHelper
                    .getMyBatisGeneratorConfigContexts()
                    .getJdbcConnectionConfiguration();
            String url = jdbcConnectionConfiguration.getConnectionURL();
            String user = jdbcConnectionConfiguration.getUserId();
            String password = jdbcConnectionConfiguration.getPassword();

            String schema = null;
            String[] array = url.split("\\?");
            if (ArrayUtils.isNotEmpty(array)) {
                String split = array[0];
                int index = split.lastIndexOf("/");
                schema = split.substring(index + 1);
            }

            DbMetaHelper helper = DbMetaHelper.create(url, user, password);
            Map<String, DbMetaHelper.TableInfo> tables = helper.getTables(schema, tableName);
            if (tables.containsKey(tableName)) {
                return tables.get(tableName).getRemarks();
            }
        } catch (Exception e) {
            log.warn(String.format("获取[%s]表备注错误：%s", tableName, e.getLocalizedMessage()), e);
        }
        return "实体";
    }
}