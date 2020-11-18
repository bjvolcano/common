package com.volcano.cache.service;

import com.alibaba.druid.util.JdbcConstants;

import java.util.concurrent.TimeUnit;

/**
 * @author volcano
 * @version 1.0
 * @date 2020/10/28 10:38
 */

public interface ICacheService {
    String DB_TYPE = JdbcConstants.MYSQL;
    Long EXPIRE = 300L;//300秒 5分钟

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 获取值
     *
     * @return
     */
    Object get();

    /**
     * 根据sqlkey获取缓存
     *
     * @param sql
     * @param argValues
     * @return
     */
    Object getBySqlKey(String sql, Object argValues);

    /**
     * 放入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean put(String key, Object value);

    /**
     * 根据sql、参数值放入缓存
     *
     * @param value
     * @return
     * @expire 超时时间 默认超时时间 ms
     */
    boolean put(String key, Object value, Long expire, TimeUnit unit);

    /**
     * 根据sql、参数值放入缓存
     *
     * @param value
     * @return
     * @expire 超时时间 默认超时时间 ms
     */
    boolean put(Object value, Long expire, TimeUnit unit);

    /**
     * 获取redis中的key
     *
     * @param sql
     * @param argValues
     * @return
     */
    String getKey(String sql, Object argValues);

    /**
     * 获取本次查询的key,需要在调用getKey 或 getBySqlKey方法后调用
     *
     * @return
     */
    String getKey();


    /**
     * 删除和sql中相关表的缓存
     */
    void remove();

    /**
     * 标识连接的事务状态
     * @param isTranStatus
     */
    void setIsTran(Boolean isTranStatus);

    /**
     * 释放资源
     */
    void removeKeys();
}
