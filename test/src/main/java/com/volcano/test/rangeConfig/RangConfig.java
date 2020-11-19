package com.volcano.test.rangeConfig;

import com.volcano.range.filter.AbstructRangeFilter;
import com.volcano.range.filter.MysqlFilter;
import com.volcano.range.filter.RangeFilter;
import com.volcano.range.mapping.ITableMapping;
import com.volcano.range.mvc.RangeMVCInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RangConfig {

    @Bean
    public ITableMapping initOrgRangeTableMapping() {
        return new OrganizationRangeTableMapping();
    }

    @Bean
    public ITableMapping initOrgRangeExcludeTableMapping() {
        return new OrganizationRangeTableMapping();
    }

    @Bean
    public OrganizationRangeRangeData organizationRangeRangeData(){
        return new OrganizationRangeRangeData();
    }

    /**
     * 本方法配置过滤规则 与 排除规则
     * @return
     */
    @Bean
    public RangeFilter initOrgRangeFilter() {
        RangeFilter filter = new OrganizationRangeFilter(organizationRangeRangeData());
        ((AbstructRangeFilter) filter).setMapping(initOrgRangeTableMapping());

        //当遇到该表时，本次不做过滤 和 映射配置 配合使用，此处不时必须，根据业务需要
        ((AbstructRangeFilter) filter).setExclude(initOrgRangeExcludeTableMapping());
        return filter;
    }

    /**
     * 向mvc拦截器中增加范围数据收集得对象 mvc会自动收集
     * @param rangeMVCInterceptor
     * @return
     */
    @Bean
    @SuppressWarnings("unchecked")
    public Object fillMvcRangDatas(RangeMVCInterceptor rangeMVCInterceptor){
        rangeMVCInterceptor.addRangeData(organizationRangeRangeData());
        return "fillMvcRangDatas";
    }


    @Bean
    @SuppressWarnings("unchecked")
    public Object fillRangFilters(MysqlFilter mysqlFilter) {
        //在此添加数据隔离过滤规则
        mysqlFilter.addFilter(initOrgRangeFilter());
        //多个规则得话，添加多次
        return "mysqlFilterFill";
    }
}
