package com.smart4y.cloud.mapper.tenant.interceptor;

import com.smart4y.cloud.core.security.OpenHelper;
import com.smart4y.cloud.core.security.OpenUserDetails;
import com.smart4y.cloud.mapper.tenant.TenantProperties;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.statement.Statement;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 添加租户ID拦截器
 *
 * @author Youtao on 2020/9/14 15:41
 */
@Slf4j
@Component
//@Intercepts(@Signature(type = Executor.class, method = "query",
//        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class TenantInterceptor implements Interceptor {

    private final JSqlParser jSqlParser = new CCJSqlParserManager();
    private final TenantProperties tenantProperties;

    public TenantInterceptor(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!tenantProperties.isEnable()) {
            return invocation.proceed();
        }

        OpenUserDetails user = OpenHelper.getUser();
        if (isSuperAdmin(user)) {
            return invocation.proceed();
        }

        log.info(">>>>>>>>> 添加租户ID拦截器 intercept");
        // 通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性;：MetaObject是Mybatis提供的一个用于方便、
        // 优雅访问对象属性的对象，通过它可以简化代码、不需要try/catch各种reflect异常，同时它支持对JavaBean、Collection、Map三种类型对象的操作。
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

        // 先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // id为执行的mapper方法的全路径名，如com.uv.dao.UserMapper.insertUser
        String id = mappedStatement.getId();
        // sql语句类型 select、delete、insert、update
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();

        // 数据库连接信息
        Configuration configuration = mappedStatement.getConfiguration();
        DataSource dataSource = configuration.getEnvironment().getDataSource();

        BoundSql boundSql = statementHandler.getBoundSql();
        // 获取到原始sql语句
        String sql = boundSql.getSql();
        System.out.println(sql);

        Object[] args = invocation.getArgs();
        Method method = invocation.getMethod();
        Object target = invocation.getTarget();

        Statement parse = jSqlParser.parse(new StringReader(sql));
//        parse.accept();
        metaObject.setValue("delegate.boundSql.sql", sql);

//        // 通过反射修改sql语句
//        String mSql = sql + " ";
//        Field field = boundSql.getClass().getDeclaredField("sql");
//        field.setAccessible(true);
//        field.set(boundSql, mSql);

        return invocation.proceed();
    }

    private boolean isSuperAdmin(OpenUserDetails user) {
        if (null == user) {
            return false;
        }
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return authorities.contains("ROLE_ADMIN");
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}