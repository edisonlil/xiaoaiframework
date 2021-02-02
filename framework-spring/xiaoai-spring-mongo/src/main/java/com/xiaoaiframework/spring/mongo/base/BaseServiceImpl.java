package com.xiaoaiframework.spring.mongo.base;

import com.xiaoaiframework.core.base.ResultBean;
import com.xiaoaiframework.spring.log4j2.annotation.LogAspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author edison
 * @param <M>
 */
public abstract class BaseServiceImpl<M extends MongoMapping<E,ID>,ID,E> implements BaseService<ID,E>{

    @Autowired
    protected M mapping;

    @LogAspect
    @Override
    public List<E> all(){return mapping.findAll();}

    @LogAspect
    @Override
    public E getById(ID id){
        return mapping.findById(id);
    }

    @LogAspect
    @Override
    public ResultBean add(E t){
        return mapping.save(t)?ResultBean.success():ResultBean.fail();
    }

    @LogAspect
    @Override
    public ResultBean update(ID id, E t){
        return mapping.update(id,t)?ResultBean.success():ResultBean.fail();
    }

    @LogAspect
    @Override
    public ResultBean delete(ID id){
        return mapping.delete(id)?ResultBean.success():ResultBean.fail();
    }


}
