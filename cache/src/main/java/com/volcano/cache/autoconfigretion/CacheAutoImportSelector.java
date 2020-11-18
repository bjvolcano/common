package com.volcano.cache.autoconfigretion;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自动装载引导
 * @author volcano
 * @version 1.0
 * @date 2020/10/28 10:29
 */
public class CacheAutoImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{CacheAutoConfig.class.getName()};
    }
}
