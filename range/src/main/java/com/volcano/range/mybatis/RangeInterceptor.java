package com.volcano.range.mybatis;

import com.volcano.mybatis.BaseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import com.volcano.range.filter.MysqlFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;


@Slf4j
/**
 * 后期可以处理成自动装配
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        //@Signature(type = StatementHandler.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
        //        CacheKey.class, BoundSql.class}),
        //@Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        //@Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class RangeInterceptor extends BaseInterceptor {

    protected MysqlFilter mysqlFilter;
    public RangeInterceptor(MysqlFilter mysqlFilter){
        this.mysqlFilter=mysqlFilter;
    }

    public void setMysqlFilter(MysqlFilter mysqlFilter){
        this.mysqlFilter=mysqlFilter;
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable  {
//        StatementHandler statementHandler = realTarget(invocation.getTarget());
        StatementHandler statementHandler = realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        // 先判断是不是SELECT操作  (2019-04-10 00:37:31 跳过存储过程)
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
                || StatementType.CALLABLE == mappedStatement.getStatementType()) {
            return invocation.proceed();
        }

        // 针对定义了rowBounds，做为mapper接口方法的参数
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();
        String sql = boundSql.getSql();
        sql = sql.toLowerCase().replaceAll("[\\s]+", " ");
        String filterSql = mysqlFilter.filter(sql);
        log.info("new sql：{}",filterSql);
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, filterSql);

        Object result=invocation.proceed();

        field.set(boundSql, sql);
        metaObject.setValue("delegate.boundSql",boundSql);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }
}
