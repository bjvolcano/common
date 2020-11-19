package com.volcano.cache.mybatis;

import com.volcano.cache.entity.TransactionInfo;
import com.volcano.cache.service.ICacheService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.volcano.mybatis.BaseInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Intercepts({
        //@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        //@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
        //@Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Slf4j
public class QueryCacheInterceptor extends BaseInterceptor {
    private static final Random random = new Random(120);

    @Autowired
    private ICacheService cacheService;

    public QueryCacheInterceptor(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (invocation.getTarget() instanceof Executor)
            return dealCacheProceed(invocation);

        return invocation.proceed();
    }


    private Object dealCacheProceed(Invocation invocation) throws InvocationTargetException, IllegalAccessException, SQLException {
        Executor executor=realTarget(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object args = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(args);
        //获取sql
        String format = getSql(mappedStatement.getConfiguration(), boundSql);
        //查询缓存
        Object result = cacheService.getBySqlKey(format, null,executor);

        if (result == null) {
            result = invocation.proceed();
            if (result != null) {
                cacheService.put(result, Math.abs(cacheService.EXPIRE - random.nextInt(120)), TimeUnit.SECONDS);
            }
        } else {
            log.info("走缓存直接返回\nsql:{}\nkey:{},value:{}", format, cacheService.getKey(), JSON.toJSONString(result));
        }
        return result;
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
