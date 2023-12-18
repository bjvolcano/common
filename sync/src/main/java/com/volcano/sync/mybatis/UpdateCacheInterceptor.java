package com.volcano.sync.mybatis;

import com.alibaba.fastjson.JSON;
import com.volcano.mybatis.BaseInterceptor;
import com.volcano.sync.service.ISyncService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
@Slf4j
public class UpdateCacheInterceptor extends BaseInterceptor {

    @Resource
    private ISyncService syncService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = realTarget(invocation.getTarget());
        SpringManagedTransaction transaction = (SpringManagedTransaction) executor.getTransaction();
        TransactionStatus status = syncService.getTransactionStatus();
        Connection connection = transaction.getConnection();
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object args = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(args);
        String format = this.getSql(mappedStatement.getConfiguration(), boundSql);
        log.info("execute sql : {}", format);
        syncService.buildSqls(status, connection, format);
        Object returnValue = invocation.proceed();
        return returnValue;
    }

    @Override
    public void remove() {
    }
}
