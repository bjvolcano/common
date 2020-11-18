package com.volcano.range.mapping;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public abstract class ITableMapping implements InitializingBean {
    protected List<Mapping> mappings;


    public List<Mapping> getMappings(){
        return mappings;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initMapping();
    }

    /**
     * 初始化方法，每个模块初始化自己的表名、字段名映射
     */
    public abstract void initMapping();

}