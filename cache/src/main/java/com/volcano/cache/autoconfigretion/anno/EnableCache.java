package com.volcano.cache.autoconfigretion.anno;

import com.volcano.cache.autoconfigretion.CacheAutoImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启缓存 volcano
 * @author volcano
 * @version 1.0
 * @date 2020/10/28 10:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CacheAutoImportSelector.class)
public @interface EnableCache {
}
