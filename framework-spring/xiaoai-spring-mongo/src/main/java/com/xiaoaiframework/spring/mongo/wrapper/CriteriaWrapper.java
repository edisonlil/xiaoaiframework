package com.xiaoaiframework.spring.mongo.wrapper;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CriteriaWrapper implements CriteriaDefinition {

    /**
     * Custom "not-null" object as we have to be able to work with {@literal null} values as well.
     */
    private static final Object NOT_SET = new Object();


    Document document = new Document();


    public Document getDocument() {
        return document;
    }


    public Document getAnd(){
        return document;
    }


    public Document getOr(){

        if(!document.containsKey("$or")){
            document.put("$or",new ArrayList<Document>());
        }
        List<Document> list = (List<Document>) document.get("$or");
        if(list.isEmpty()){
            list.add(new Document());
        }
        Document orDocument = list.get(0);
        return orDocument;
    }


    /**
     * [{ "_id" : "1", "sort" : { "$gt" : 1, "$lt" : 2}}]
     * @return
     */
    public CriteriaWrapper or(String key,String operation, Object val) {
        Document document = getOr();
        setVal(document,key,operation,val);
        return this;

    }


    /**
     * [{ "_id" : "1", "sort" : { "$gt" : 1, "$lt" : 2}}]
     * @return
     */
    public CriteriaWrapper and(String key,String operation, Object val) {
        setVal(document,key,operation,val);
        return this;
    }


    private void setVal(Document document,String key,String operation,Object val){

        if(operation.equals("$eq")){
            document.put(key,val);
        }else if(operation.equals("$regex")){
            val = Pattern.compile("^.*" + val + ".*$", Pattern.CASE_INSENSITIVE);
            document.put(key,val);
        }else{

            if(!document.containsKey(key)){
                Document operationDocument = new Document();
                document.put(key,operationDocument);
            }
            Document operationDocument = (Document) document.get(key);
            operationDocument.put(operation,val);
            return;
        }
    }

    @Override
    public Document getCriteriaObject() {
        return document;
    }

    @Override
    public String getKey() {
        return null;
    }
}
