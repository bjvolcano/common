package com.volcano.range.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RangeData {

    private static ThreadLocal<List<Integer>> orgIds = new ThreadLocal<>();

    public static List<Integer> get(){
        return orgIds.get();
    }

    public static List<String> getOrgIdsString(){
        List<Integer> orgIds = get();
        if(CollectionUtils.isEmpty(orgIds))
            return null;
        return orgIds.stream().map(x -> x.toString()).collect(Collectors.toList());
    }

    public static void set(List<Integer> ids){
        orgIds.set(ids);
    }
    public static void removeOrgIds(){
        orgIds.remove();
    }
}
