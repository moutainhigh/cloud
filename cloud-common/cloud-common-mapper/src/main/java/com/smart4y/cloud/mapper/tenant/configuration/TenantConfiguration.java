package com.smart4y.cloud.mapper.tenant.configuration;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.smart4y.cloud.mapper.tenant.TenantProperties;
import com.smart4y.cloud.mapper.tenant.interceptor.TenantInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 启动租户配置
 *
 * @author Youtao on 2020/9/14 16:19
 */
@Configuration
@AutoConfigureAfter(PageHelperAutoConfiguration.class)
@EnableConfigurationProperties({TenantProperties.class})
public class TenantConfiguration {

    @Autowired
    private TenantProperties tenantProperties;
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @PostConstruct
    public void tenantInterceptor() {
        if (tenantProperties.isEnable()) {
            TenantInterceptor interceptor = new TenantInterceptor(tenantProperties);
            sqlSessionFactories.forEach(x -> x.getConfiguration().addInterceptor(interceptor));
        }
    }
}