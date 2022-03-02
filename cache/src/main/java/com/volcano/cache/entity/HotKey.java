package com.volcano.cache.entity;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class HotKey {
    private Integer count;
    private Long lastTime;
}
