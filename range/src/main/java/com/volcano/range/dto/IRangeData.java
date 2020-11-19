package com.volcano.range.dto;


public abstract class IRangeData<T> {
    protected final ThreadLocal data = new ThreadLocal();
    public <T> T get(){
        return (T)data.get();
    }
    public void set(Object object){
        data.set(object);
    }
    public void remove(){
        data.remove();
    }

    /**
     * 本方法最为核心，每个数据收集得都不一样，通过子类来实现，在mvc拦截器中会自动调用
     */
    public abstract void fillRangeData();
}
