package com.volcano.test;


import com.volcano.cache.autoconfigretion.anno.EnableCache;
import com.volcano.range.autoconfigretion.anno.EnableRangeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableCache
//@EnableRangeFilter
@ComponentScan(basePackages = {"com.volcano.redission"})
public class TestApplication {

    public static void main(String[] args) {

        SpringApplication.run(TestApplication.class, args);
    }
}
