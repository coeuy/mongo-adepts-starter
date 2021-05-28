package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.exception.MongoAdeptsException;
import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageQuery;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoCollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 抽象Adepts
 * </p>
 *
 * @author yarnk
 * @date 2021/5/29
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAdepts {

    private final static String MONGO_ID = "_id";

    private final MongoTemplate mongoTemplate;
    
    private final QueryHandler queryHandler;

    private final UpdateHandler updateHandler;

    private final MongoAdeptsProperties properties;

    private final @NonNull
    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context;

    private MongoTemplate template() {
        return this.mongoTemplate;
    }

    public String getCollectionName(Class<?> entityClass) {
        return this.determineCollectionName(entityClass);
    }

    public String determineCollectionName(@Nullable Class<?> entityClass) {
        if (entityClass == null) {
            throw new MongoAdeptsException(
                    "No class parameter provided, entity collection can't be determined!");
        }
        return context.getRequiredPersistentEntity(entityClass).getCollection();
    }

    public <T> T getOne(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findOne(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> T getById(Serializable id, Class<T> entityClass) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public <T> List<T> list(Class<T> entityClass) {
        return mongoTemplate.findAll(entityClass);
    }

    public <T> List<T> list(QueryWrapper<T> queryWrapper) {
        return mongoTemplate.find(queryHandler.parse(queryWrapper), queryWrapper.getEntityClass());
    }

    public <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> entityClass) {
        Iterable<T> allByIds = mongoTemplate.find(Query.query(Criteria.where(MONGO_ID).in(idList)), entityClass);
        return Lists.newArrayList(allByIds);
    }

    public <T> PageResult<T> page(PageInfo pageInfo, Class<T> entityClass) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(new Query().with(page), entityClass, collectionName);
        long total = mongoTemplate.count(new Query(), entityClass, collectionName);
        return new PageResult<>(list, total, page);
    }

    public <T> PageResult<T> page(PageInfo pageInfo, QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Query query = queryHandler.parse(queryWrapper);
        if (queryWrapper.getTextSearch() != null) {
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(queryWrapper.getTextSearch()));
        }
        long total = mongoTemplate.count(query, entityClass, collectionName);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(query.with(page), entityClass, collectionName);
        return new PageResult<>(list, total, page);
    }

    public <T> T insert(T entity) {
        return mongoTemplate.insert(entity, getCollectionName(ClassUtils.getUserClass(entity)));
    }

    public <T> boolean insertBatch(Collection<T> entityList, Class<T> entityClass) {
        Collection<T> ts = mongoTemplate.insertAll(entityList);
        return ts.size() == entityList.size();
    }

    public <T> T save(T entity) {
        return mongoTemplate.save(entity);
    }


    public <T> boolean update(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = updateHandler.parse(queryWrapper);

        if (Objects.isNull(update)) {
            if (properties.isDebug()){
                log.warn("条件构造不满足更新操作");
            }
            return false;
        }
        return mongoTemplate.updateFirst(queryHandler.parse(queryWrapper), update, entityClass, collectionName).wasAcknowledged();
    }

    public <T> T findAndModify(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        Update update = updateHandler.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return null;
        }
        return mongoTemplate.findAndModify(queryHandler.parse(queryWrapper), update, entityClass, collectionName);
    }


    public <T> boolean updateMulti(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = updateHandler.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return true;
        }
        return mongoTemplate.updateMulti(queryHandler.parse(queryWrapper), update, entityClass, collectionName).wasAcknowledged();
    }


    public <T> boolean delete(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        final Query parse = queryHandler.parse(queryWrapper);
        if (properties.isDebug()){
            log.info("删除数据操作监听:\n - {}", parse);
        }
        DeleteResult remove = mongoTemplate.remove(parse, entityClass, collectionName);
        return remove.wasAcknowledged();
    }

    public <T> boolean delete(T t, Class<T> entityClass) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        DeleteResult remove = mongoTemplate.remove(t, collectionName);
        return remove.wasAcknowledged();
    }


    public <T> int count(QueryWrapper<T> queryWrapper) {
        return (int) mongoTemplate.count(queryHandler.parse(queryWrapper), queryWrapper.getEntityClass());
    }

    public <T> boolean deleteAll(Class<T> entityClass) {
        if (properties.isDebug()){
            log.warn("删除集合全部数据操作监听:\n - {}", entityClass);
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        if (mongoTemplate.collectionExists(entityClass)) {
            DeleteResult remove = mongoTemplate.remove(entityClass, collectionName);
            return remove.wasAcknowledged();
        }
        return true;
    }

    public <T> boolean exists(QueryWrapper<T> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.exists(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> boolean exists(Class<T> entityClass) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.collectionExists(collectionName) || mongoTemplate.collectionExists(entityClass);
    }

    public <T> List<T> group(QueryWrapper<T> queryWrapper, String... keys) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Criteria criteria = queryHandler.parseCriteria(queryWrapper);
        TypedAggregation<T> aggregation = TypedAggregation.newAggregation(
                entityClass,
                Aggregation.match(criteria),
                Aggregation.group(keys)

        );
        return mongoTemplate.aggregate(aggregation, collectionName, entityClass).getMappedResults();
    }
    

    public <T> PageResult<T> group(PageInfo pageInfo, QueryWrapper<T> queryWrapper, String... keys) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        PageQuery page = PageQuery.page(pageInfo);
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Criteria criteria = queryHandler.parseCriteria(queryWrapper);
        TypedAggregation<T> aggregation = TypedAggregation.newAggregation(
                entityClass,
                Aggregation.match(criteria),
                Aggregation.group(keys),
                Aggregation.skip((long) (page.getPageNumber() - 1) * page.getPageSize()),
                Aggregation.limit(page.getPageSize())
        );
        AggregationResults<T> aggregate = mongoTemplate.aggregate(aggregation, entityClass);
        long count = mongoTemplate.count(Query.query(criteria), entityClass, collectionName);
        return PageResult.page(aggregate.getMappedResults(), count, page.getPageNumber(), page.getPageSize(), page.getOffset());
    }

}
