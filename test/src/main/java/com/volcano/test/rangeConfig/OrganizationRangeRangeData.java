package com.volcano.test.rangeConfig;

import com.volcano.range.dto.IRangeData;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于收集、传输过滤sql得数据
 * 比如  where xxx in (1,2,3)//这类得数据
 */
public class OrganizationRangeRangeData extends IRangeData<List<Integer>> {

    public List<String> getOrgIdsString(){
        List<Integer> orgIds = get();
        if(CollectionUtils.isEmpty(orgIds))
            return null;
        return orgIds.stream().map(x -> x.toString()).collect(Collectors.toList());
    }

    @Override
    public void fillRangeData() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        //todo 通过request拿到用户信息  然后根据用户得相关信息 调用set方法


        set(Arrays.asList(1,2,3));//本次模拟数据
    }
}
