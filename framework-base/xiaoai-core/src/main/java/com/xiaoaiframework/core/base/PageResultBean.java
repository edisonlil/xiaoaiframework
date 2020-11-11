package com.xiaoaiframework.core.base;


import com.xiaoaiframework.core.constant.enums.BaseCode;

import java.util.Collection;


/**
 * 分页的结果集封装
 * @author edison
 * @version 1.0.0
 * @since 2020/04/09 05:33
 */
public class PageResultBean<T> extends ResultBean<Collection<T>> {

    /**
     * 分页信息
     */
    private PageBean pagination;



    public PageResultBean() {
        super();
    }

    /**
     * 创建当前对象
     *
     * @return 当前对象
     */
    public static PageResultBean create() {
        return new PageResultBean();
    }


    /**
     * 返回参数校验错误结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean illegalArgument() {
        return new PageResultBean().setCode(BaseCode.illegalArgument).setMessage(BaseCode.illegalArgument.getCodeExplain());
    }


    /**
     * 返回成功结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean success() {
        return new PageResultBean().setMessage(BaseCode.success.getCodeExplain()).setCode(BaseCode.success);
    }

    /**
     * 返回失败结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean fail() {
        return new PageResultBean().setCode(BaseCode.fail).setMessage(BaseCode.fail.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回空结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean blank() {
        return new PageResultBean().setCode(BaseCode.blank).setMessage(BaseCode.blank.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回系统繁忙结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean systemBusy() {
        return new PageResultBean().setCode(BaseCode.systemBusy).setMessage(BaseCode.systemBusy.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回异常结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean exception() {
        return new PageResultBean().setCode(BaseCode.exception).setMessage(BaseCode.exception.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回返回未登录结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean notlogin() {
        return new PageResultBean().setCode(BaseCode.notLogin).setMessage(BaseCode.notLogin.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回返回认证失败结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean authFeiled() {
        return new PageResultBean().setCode(BaseCode.authFeiled).setMessage(BaseCode.authFeiled.getCodeExplain()).setSuccess(false);
    }

    /**
     * 返回返回没有授权结果集对象
     *
     * @return 当前对象
     */
    public static PageResultBean notPermited() {
        return new PageResultBean().setCode(BaseCode.notPermited).setMessage(BaseCode.notPermited.getCodeExplain()).setSuccess(false);
    }


    @Override
    public PageResultBean<T> setCode(BaseCode baseCode) {
        super.setCode(baseCode);
        return this;
    }

    @Override
    public PageResultBean<T> setMessage(String message) {
        super.setMessage(message);
        return this;
    }


    @Override
    public PageResultBean<T> setData(Collection<T> data) {
        super.setData(data);
        return this;
    }

    @Override
    public PageResultBean<T> setSuccess(Boolean success) {
        super.setSuccess(success);
        return this;
    }

    @Override
    public String toString() {
        return "PageResultBean{" +
                "pagination=" + pagination +
                '}';
    }


    @Override
    public Collection<T> getData() {
        return super.getData();
    }


    public PageBean getPagination() {
        return pagination;
    }

    public PageResultBean setPagination(PageBean pagination) {
        this.pagination = pagination;
        return this;
    }
}
