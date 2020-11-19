package com.volcano.range.autoconfigretion;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.volcano.range.autoconfigretion.anno.EnableRangeFilter;
import com.volcano.range.filter.AbstructRangeFilter;
import com.volcano.range.filter.MysqlFilter;
import com.volcano.range.filter.RangeFilter;
import com.volcano.range.mapping.ITableMapping;
import com.volcano.range.mvc.RangeMVCInterceptor;
import com.volcano.range.mybatis.RangeInterceptor;

/**
 * @author volcano
 * @version 1.0
 * @date 2020/9/23 18:58
 */
@Configuration
@ComponentScan(basePackages = {"com.volcano.range"})
@ConditionalOnClass({EnableRangeFilter.class})
public class RangeAutoConfig implements WebMvcConfigurer{


    @Bean
    @SuppressWarnings("unchecked")
    public Object getRangeInterceptor(SqlSessionFactory sqlSessionFactory, RangeInterceptor interceptor) {
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        return "rangeInterceptor";
    }


    @Bean
    public MysqlFilter initMysqlFilter(){
        MysqlFilter mysqlFilter=new MysqlFilter();
        return mysqlFilter;
    }


    @Bean
    public RangeInterceptor initRangeInterceptor(MysqlFilter mysqlFilter){
        return new RangeInterceptor(mysqlFilter);
    }


    @Bean
    public RangeMVCInterceptor rangeMVCInterceptor(){
        return new RangeMVCInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rangeMVCInterceptor());

    }
}
