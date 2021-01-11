package com.xiaoaiframework.spring.mongo.page;


import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;

public class Page<E> extends ArrayList<E> {


    /**
     * 页码，从1开始
     */
    private int pageNum;

    /**
     * 页面大小
     */
    private int pageSize;

    /**
     * 起始行
     */
    private int startRow;

    /**
     * 末行
     */
    private int endRow;

    /**
     * 总数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 分页合理化
     */
    private Boolean reasonable;


    /**
     * 查询条件
     */
    private Query query;

    /**
     * 实体类型
     */
    private Class entityType;

    /**
     * 是否进行count查询
     */
    private Boolean count = true;

    /**
     * 是否不分页
     */
    private Boolean pageSizeZero = true;

    public Page() {
        super();
    }

    public Page(int pageNum, int pageSize) {
        this(pageNum, pageSize, true);
    }

    public Page(int pageNum, int pageSize, boolean count) {

        super(0);
        //如果拿取的是第一页,并且pageSize是最大值则不分页
        if (pageNum == 1 && pageSize == Integer.MAX_VALUE) {
            pageSizeZero = true;
            pageSize = 0;
        }

        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
        calculateStartAndEndRow();
        setReasonable(reasonable);
    }

    public int getPageNum() {
        return pageNum;
    }

    public Page<E> setPageNum(int pageNum) {
        //分页合理化，针对不合理的页码自动处理
        this.pageNum = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Page<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getStartRow() {
        return startRow;
    }

    public Page<E> setStartRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public int getEndRow() {
        return endRow;
    }

    public Page<E> setEndRow(int endRow) {
        this.endRow = endRow;
        return this;
    }

    public Page<E> setTotal(long total) {
        this.total = total;
        if (total == -1) {
            pages = 1;
            return this;
        }
        if (pageSize > 0) {
            pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        } else {
            pages = 0;
        }
        //分页合理化，针对不合理的页码自动处理
        if ((reasonable != null && reasonable) && pageNum > pages) {
            if(pages!=0){
                pageNum = pages;
            }
            calculateStartAndEndRow();
        }

        return this;
    }

    public long getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }


    public Boolean getReasonable() {
        return reasonable;
    }

    public Page<E> setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
        return this;
    }

    public Boolean getCount() {
        return count;
    }

    public Boolean getPageSizeZero() {
        return pageSizeZero;
    }

    /**
     * 计算起止行号
     */
    private void calculateStartAndEndRow() {
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
    }


    public Query getQuery() {
        return query;
    }

    public Page<E> setQuery(Query query) {
        this.query = query;
        return this;
    }

    public Page<E> setEntityType(Class entityType) {
        this.entityType = entityType;
        return this;
    }

    public Class getEntityType() {
        return entityType;
    }
}
