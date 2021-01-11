package com.xiaoaiframework.spring.mongo.kit;

import org.springframework.data.domain.PageRequest;


/**
 * Mongo分页工具类
 * @author edison
 */
public class MongoPageHelper {

    static ThreadLocal<PageRequest> LOCAL_PAGE;

    public static void startPage(int curPage,int pageRecord){
        LOCAL_PAGE.set(PageRequest.of(curPage-1,pageRecord));
    }

    public static PageRequest get(){
        return LOCAL_PAGE.get();
    }

    
    public static void clear(){
        LOCAL_PAGE.remove();
    }
}
