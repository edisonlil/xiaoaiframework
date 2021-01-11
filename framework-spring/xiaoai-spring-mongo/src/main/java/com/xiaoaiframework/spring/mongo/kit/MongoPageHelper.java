package com.xiaoaiframework.spring.mongo.kit;

import com.xiaoaiframework.core.base.PageBean;

import com.xiaoaiframework.spring.mongo.page.Page;
import org.springframework.data.domain.PageRequest;


/**
 * Mongo分页工具类
 * @author edison
 */
public class MongoPageHelper {

    static ThreadLocal<Page> LOCAL_PAGE;


    public static void startPage(int curPage,int pageRecord,boolean count){
        Page page = new Page(curPage,pageRecord,count);
        LOCAL_PAGE.set(page);
    }

    public static void startPage(int curPage,int pageRecord){
        Page page = new Page(curPage,pageRecord);
        LOCAL_PAGE.set(page);
    }

    public static void startPage(PageBean pageBean){
        Page page = new Page(pageBean.getCurPage(),pageBean.getPageRecord());
        LOCAL_PAGE.set(page);
    }

    public static Page get(){
        return LOCAL_PAGE.get();
    }
    
    public static void clear(){
        LOCAL_PAGE.remove();
    }
}
