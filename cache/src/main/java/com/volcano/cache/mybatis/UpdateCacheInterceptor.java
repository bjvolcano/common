package com.volcano.cache.mybatis;

import com.volcano.cache.service.ICacheService;
import com.volcano.mybatis.BaseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author volcano
 * @version 1.0
 * @date 2020/10/30 11:00
 */
@Intercepts({
        //@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Slf4j
public class UpdateCacheInterceptor extends BaseInterceptor {
    @Autowired
    private ICacheService cacheService;


    public UpdateCacheInterceptor(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object args = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(args);
        //Collection<Object> parameters = getParameters(mappedStatement.getConfiguration(), boundSql,args);
        String format = getSql(mappedStatement.getConfiguration(), boundSql);
        cacheService.getKey(format, null);
        // 执行结果
        Object returnValue = invocation.proceed();

        //
        cacheService.remove();

        return returnValue;
    }

}
