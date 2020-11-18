package com.volcano.cache.autoconfigretion;

import com.volcano.cache.mybatis.QueryCacheInterceptor;
import com.volcano.cache.mybatis.UpdateCacheInterceptor;
import com.volcano.cache.service.CacheService;
import com.volcano.cache.service.ICacheService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装载配置
 * @author volcano
 * @version 1.0
 * @date 2020/10/28 10:29
 */
@Configuration
@ComponentScan(basePackages = {"com.volcano.cache"})
public class CacheAutoConfig {
    @Bean
    ICacheService cacheService(RedissonClient redisCommonClient){
        return new CacheService(redisCommonClient);
    }
    @Bean
    public QueryCacheInterceptor queryCacheInterceptor(ICacheService cacheService){
        return new QueryCacheInterceptor(cacheService);
    }
    @Bean
    public UpdateCacheInterceptor updateCacheInterceptor(ICacheService cacheService){
        return new UpdateCacheInterceptor(cacheService);
    }
    @Bean
    public Object queryCacheInterceptorConfig(SqlSessionFactory sqlSessionFactory,ICacheService cacheService) {
        //PaginationInterceptor paginationInterceptor=new PaginationInterceptor();
        //sqlSessionFactory.getConfiguration().addInterceptor(paginationInterceptor);
        sqlSessionFactory.getConfiguration().addInterceptor(queryCacheInterceptor(cacheService));
        sqlSessionFactory.getConfiguration().addInterceptor(updateCacheInterceptor(cacheService));
        return "cacheInterceptorConfig";
    }
}
