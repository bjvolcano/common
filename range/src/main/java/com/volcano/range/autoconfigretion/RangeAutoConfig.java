package com.volcano.range.autoconfigretion;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.volcano.range.autoconfigretion.anno.EnableRangeFilter;
import com.volcano.range.filter.AbstructRangeFilter;
import com.volcano.range.filter.MysqlFilter;
import com.volcano.range.filter.OrganizationRangeFilter;
import com.volcano.range.filter.RangeFilter;
import com.volcano.range.mapping.ITableMapping;
import com.volcano.range.mapping.OrganizationRangeTableMapping;
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
    public Object getRangeInterceptor(SqlSessionFactory sqlSessionFactory, RangeInterceptor interceptor) {
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        return "rangeInterceptor";
    }

    @Bean
    public ITableMapping initOrgRangeTableMapping(){
        return new OrganizationRangeTableMapping();
    }

    @ConditionalOnMissingBean(RangeFilter.class)
    @Bean
    public RangeFilter initOrgRangeFilter(ITableMapping mapping){
        RangeFilter filter=new OrganizationRangeFilter();
        ((AbstructRangeFilter)filter).setMapping(mapping);
        return filter;
    }


    @Bean
    public MysqlFilter initMysqlFilter(RangeFilter rangeFilter){
        MysqlFilter mysqlFilter=new MysqlFilter();
        mysqlFilter.addFilter(rangeFilter);
        //在此添加数据隔离过滤规则
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
