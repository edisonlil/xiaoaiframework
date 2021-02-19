package com.xiaoaiframework.spring.mongo.processor;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.*;

public interface AggregateSelectProcessor<I,O> {

    void frontProcessor(List<AggregationOperation> operations, Class<I> input,Class<O> output);

    /**
     *
     * @param result  If the result is a collection
     *                then the primitive type is the element type in the collection,
     *                otherwise it should be the same as the result type
     * @param input
     * @param output
     * @return result
     */
    Object postProcessor(Object result, Class<I> input,Class<O> output);
}
