package com.volcano.range.mvc;

import com.volcano.range.dto.IRangeData;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据范围mvc拦截器
 */
public class RangeMVCInterceptor implements HandlerInterceptor {


    private List<IRangeData> rangeDatas=new ArrayList<>();


    public void addRangeData(IRangeData rangeData){
        rangeDatas.add(rangeData);
    }
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //在此收集用户的范围数据，通过
        //RangeData.set(...);
        rangeDatas.forEach(x->x.fillRangeData());
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        rangeDatas.forEach(x->x.remove());
    }

}