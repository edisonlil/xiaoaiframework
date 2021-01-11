package com.xiaoaiframework.spring.mongo.page;


import java.util.ArrayList;

public class Page<E> extends ArrayList<E> {

    long totalCount;


    public long getTotalCount() {
        return totalCount;
    }


    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
