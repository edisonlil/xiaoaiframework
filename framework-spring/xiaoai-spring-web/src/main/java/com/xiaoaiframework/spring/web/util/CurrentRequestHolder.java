package com.xiaoaiframework.spring.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author edison
 */
public class CurrentRequestHolder {

    /**
     * 获取当前请求
     * @return
     */
    public static HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
}
