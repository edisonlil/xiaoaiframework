package com.xiaoaiframework.spring.mongo.util;

import cn.hutool.core.text.StrBuilder;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiaoaiframework.spring.mongo.parser.UpdateParser;
import org.bson.Document;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;

/**
 * mongo语句构造构造器
 * {"id":{$eq:1}}
 * @author edison
 */
public class StatementBuilder {

    private MongoConverter converter;

    private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
    private final UpdateMapper updateMapper;
    private QueryMapper queryMapper;

    public StatementBuilder(MongoTemplate template){
        this(template.getConverter(),template.getMongoDbFactory());
    }

    public StatementBuilder(MongoConverter converter, MongoDbFactory factory){

        this.converter = converter == null ? getDefaultMongoConverter(factory) : converter;
        this.queryMapper = new QueryMapper(this.converter);
        this.updateMapper = new UpdateMapper(this.converter);
        mappingContext = this.converter.getMappingContext();
    }


    /**
     * 加入缓存提高性能
     */
    private static final Cache<Class,MongoPersistentEntity> MONGO_PERSISTENT_ENTITY_CACHE;
    static {
        //初始化缓存
        MONGO_PERSISTENT_ENTITY_CACHE = Caffeine.newBuilder()
                .weakKeys().weakValues()
                //初始大小
                .initialCapacity(100)
                //最大数量
                .maximumSize(1000)
                .build();

    }

    /**
     * 获取PersistentEntity
     * @param entityClass
     * @return
     */
    private MongoPersistentEntity<?> getPersistentEntity(Class entityClass){

        MongoPersistentEntity<?> persistentEntity  = MONGO_PERSISTENT_ENTITY_CACHE.getIfPresent(entityClass);
        if(persistentEntity == null){
            synchronized (MONGO_PERSISTENT_ENTITY_CACHE){
                if(persistentEntity == null){
                    persistentEntity = mappingContext.getPersistentEntity(entityClass);
                    MONGO_PERSISTENT_ENTITY_CACHE.put(entityClass,persistentEntity);
                }
            }
        }
        return persistentEntity;
    }


    private static final Cache<Class,org.springframework.data.mongodb.core.mapping.Document> DOCUMENT_ANNOTATION_CACHE;
    static {
        //初始化缓存
        DOCUMENT_ANNOTATION_CACHE = Caffeine.newBuilder()
                .weakKeys().weakValues()
                //初始大小
                .initialCapacity(100)
                //最大数量
                .maximumSize(1000)
                .build();

    }

    /**
     * 获取实体类上的Document
     * @param entityClass
     * @return
     */
    private org.springframework.data.mongodb.core.mapping.Document getDocumentAnnotation(Class entityClass){

        org.springframework.data.mongodb.core.mapping.Document documentAnnotation =
                DOCUMENT_ANNOTATION_CACHE.getIfPresent(entityClass);

        if(documentAnnotation == null){
            synchronized (DOCUMENT_ANNOTATION_CACHE){
                if(documentAnnotation == null){

                    Object o = entityClass.getDeclaredAnnotation(org.springframework.data.mongodb.core.mapping.Document.class);
                    if(o != null){
                        documentAnnotation = (org.springframework.data.mongodb.core.mapping.Document) o;
                        DOCUMENT_ANNOTATION_CACHE.put(entityClass,documentAnnotation);
                    }

                }
            }
        }
        return documentAnnotation;
    }


    /**
     * 获取实体类上的Document的value
     * @param entityClass
     * @return
     */
    private String getDocumentAnnotationValue(Class entityClass){

        org.springframework.data.mongodb.core.mapping.Document documentAnnotation = getDocumentAnnotation(entityClass);
        if(documentAnnotation == null){
            return null;
        }
        return documentAnnotation.value();
    }

    /**
     * 获取默认mongo转换器
     * @param factory
     * @return
     */
    private static MongoConverter getDefaultMongoConverter(MongoDbFactory factory) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MongoCustomConversions conversions = new MongoCustomConversions(Collections.emptyList());

        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        mappingContext.afterPropertiesSet();

        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setCustomConversions(conversions);
        converter.setCodecRegistryProvider(factory);
        converter.afterPropertiesSet();

        return converter;
    }


    /**
     * 构造条件字符串
     * @param criteria
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String criteriaStr(Class<T> entityClass, Criteria... criteria){
        MongoPersistentEntity<?> persistentEntity  = getPersistentEntity(entityClass);
        Document mappedQuery = queryMapper.getMappedObject(getQueryObject(criteria), persistentEntity);
        return serializeToJsonSafely(mappedQuery);
    }

    /**
     * 构造条件字符串
     * @param criteria
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String criteriaStr(Class<T> entityClass, List<Criteria> criteria){
        MongoPersistentEntity<?> persistentEntity  = getPersistentEntity(entityClass);
        Document mappedQuery = queryMapper.getMappedObject(getQueryObject(criteria), persistentEntity);
        return serializeToJsonSafely(mappedQuery);
    }

    /**
     * 构造条件字符串
     * @param query
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String criteriaStr(Query query, Class<T> entityClass){
        return criteriaStr(query.getQueryObject(), entityClass);
    }

    /**
     * 构造条件字符串
     * @param document
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String criteriaStr(Document document,Class<T> entityClass){
        MongoPersistentEntity<?> persistentEntity  = getPersistentEntity(entityClass);
        Document mappedQuery = queryMapper.getMappedObject(document, persistentEntity);
        return serializeToJsonSafely(mappedQuery);
    }


    /**
     * 生成查询字符串
     * @param query
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String generateQueryStr(Query query,Class<T> entityClass){
        return generateQueryStr(query.getQueryObject(), entityClass);
    }

    /**
     * 生成查询字符串
     * @param document
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T>String generateQueryStr(Document document,Class<T> entityClass){

        String collectionName = getDocumentAnnotationValue(entityClass);
        if(collectionName == null){
            collectionName = entityClass.getSimpleName();
        }

        StrBuilder builder = StrBuilder.create("db.").append(collectionName)
                .append(".find(");
        builder.append(criteriaStr(document,entityClass)).append(");");
        return builder.toString();
    }

    /**
     * 生成查询字符串
     * @param entityClass
     * @param criteria
     * @param <T>
     * @return
     */
    public <T>String generateQueryStr(Class<T> entityClass,Criteria... criteria){
        return generateQueryStr(getQueryObject(criteria), entityClass);
    }


    /**
     * 生成查询字符串
     * @param entityClass
     * @param criterias
     * @param <T>
     * @return
     */
    public <T>String generateQueryStr(Class<T> entityClass,List<Criteria> criterias){
        return generateQueryStr(getQueryObject(criterias), entityClass);
    }


    /**
     * 构建更新字符串
     * @param update
     * @param entityClass
     * @param <T>
     * @return
     */
    private <T>String updateSnippetStr(Update update, Class<T> entityClass){
        MongoPersistentEntity<?> persistentEntity  = getPersistentEntity(entityClass);
        Document updateObj = updateMapper.getMappedObject(update.getUpdateObject(), persistentEntity);
        if(updateObj == null){return null;}
        return serializeToJsonSafely(updateObj);
    }

    /**
     * 更新语句片段  <update>
     * @param entity
     * @param entityClass
     * @param <T>
     * @return
     */
    private <T>String updateSnippetStr(Object entity,Class<T> entityClass){
        return updateSnippetStr(UpdateParser.convertUpdate(entity),entityClass);
    }

    /**
     * 更新语句片段 <update>
     * @param entity
     * @param <T>
     * @return
     */
    private <T>String updateSnippetStr(Object entity){
        return updateSnippetStr(entity,entity.getClass());
    }

    /**
     * 生成更新语句
     * @param entity
     * @param criteria
     * @param <T>
     * @return
     */
    public <T>String generateUpdateStr(Object entity,Criteria... criteria){

        assert entity != null;
        assert criteria != null;
        Class entityClass = entity.getClass();
        return generateUpdateStr(entity, criteriaStr(entityClass,criteria));
    }


    /**
     * 生成更新语句
     * @param entity
     * @param criteriaStr
     * @param <T>
     * @return
     */
    public <T>String generateUpdateStr(Object entity,String criteriaStr){

        assert entity != null;
        assert criteriaStr != null;

        Class entityClass = entity.getClass();
        String collectionName = getDocumentAnnotationValue(entityClass);
        if(collectionName == null){
            collectionName = entityClass.getSimpleName();
        }

        StrBuilder builder = StrBuilder.create("db.").append(collectionName)
                .append(".update(");
        builder.append(criteriaStr).append(",");
        builder.append(updateSnippetStr(entity,entityClass)).append(");");
        return builder.toString();
    }


    private Document getQueryObject(List<Criteria> criteria) {

        Document document = new Document();
        for (CriteriaDefinition definition : criteria) {
            document.putAll(definition.getCriteriaObject());
        }
        return document;
    }

    /**
     * @return the query {@link Document}.
     */
    private Document getQueryObject(Criteria... criteria) {

        Document document = new Document();
        for (CriteriaDefinition definition : criteria) {
            document.putAll(definition.getCriteriaObject());
        }
        return document;
    }


}
