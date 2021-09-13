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
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
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

    public MongoTemplate template() {
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

    public <T> T getOne(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "GET ONE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findOne(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> T getById(Serializable id, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n根据ID" + id + "查询集合[" + entityClass.getSimpleName() + "]");
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public <T> List<T> list(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "无条件查询集合[" + entityClass.getSimpleName() + "]所有数据");
        }
        return mongoTemplate.findAll(entityClass);
    }

    public <T> List<T> list(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "LIST"));
        }
        return mongoTemplate.find(queryHandler.parse(queryWrapper), entityClass);
    }

    public <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "根据ID集合查询[" + entityClass.getSimpleName() + "]: \n包含 (ID = " + idList + ")");
        }
        Iterable<T> allByIds = mongoTemplate.find(Query.query(Criteria.where(MONGO_ID).in(idList)), entityClass);
        return Lists.newArrayList(allByIds);
    }

    public <T> PageResult<T> page(PageInfo pageInfo, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "无条件查询集合[" + entityClass.getSimpleName() + "]分页，第：" + pageInfo.getCurrent() + "页，页个数：" + pageInfo.getSize() + "个");
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(new Query().with(page), entityClass, collectionName);
        long total = mongoTemplate.count(new Query(), entityClass, collectionName);
        return new PageResult<>(list, total, page);
    }
    public <T> PageResult<T> page(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "查询集合[" + entityClass.getSimpleName() + "]分页，第：" + pageInfo.getCurrent() + "页，页个数：" + pageInfo.getSize() + "个");
            log.info(queryWrapper.getSrt(entityClass, "PAGE"));
        }
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
        log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n新增单个文档:\n" + entity.toString());
        return mongoTemplate.insert(entity, getCollectionName(ClassUtils.getUserClass(entity)));
    }

    public <T> boolean insertBatch(Collection<T> entityList) {
        log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n批量新增文档:\n" + entityList.toString());
        Collection<T> ts = mongoTemplate.insertAll(entityList);
        return ts.size() == entityList.size();
    }

    public <T> T save(T entity) {
        log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n保存或更新:\n" + entity.toString());
        return mongoTemplate.save(entity);
    }


    public <T> boolean update(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "UPDATE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = updateHandler.parse(queryWrapper);

        if (Objects.isNull(update)) {
            if (properties.isDebug()) {
                log.warn("条件构造不满足更新操作");
            }
            return false;
        }
        return mongoTemplate.updateFirst(queryHandler.parse(queryWrapper), update, entityClass, collectionName).wasAcknowledged();
    }

    public <T> T findAndModify(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "FIND AND MODIFY"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        Update update = updateHandler.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return null;
        }
        return mongoTemplate.findAndModify(queryHandler.parse(queryWrapper), update, entityClass, collectionName);
    }


    public <T> boolean updateMulti(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "UPDATE MULTI"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = updateHandler.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return true;
        }
        return mongoTemplate.updateMulti(queryHandler.parse(queryWrapper), update, entityClass, collectionName).wasAcknowledged();
    }


    public <T> boolean delete(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "DELETE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        final Query parse = queryHandler.parse(queryWrapper);
        if (properties.isDebug()) {
            log.info("删除数据操作监听:\n - {}", parse);
        }
        DeleteResult remove = mongoTemplate.remove(parse, entityClass, collectionName);
        return remove.wasAcknowledged();
    }

    public <T> boolean delete(T t, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n根据对象内容删除：\n"+t.toString());
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        DeleteResult remove = mongoTemplate.remove(t, collectionName);
        return remove.wasAcknowledged();
    }


    public <T> int count(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "COUNT"));
        }
        return (int) mongoTemplate.count(queryHandler.parse(queryWrapper), entityClass);
    }

    public <T> int count( Class<T> entityClass) {
        log.info("\n\nADEPTS DEBUG MONITOR：{}\n", "\n查询文档["+entityClass.getSimpleName()+"]总条数");
        return (int) mongoTemplate.estimatedCount(entityClass);
    }


    public <T> boolean deleteAll(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.warn("\n删除集合全部数据操作监听:\n - 集合{}", entityClass.getSimpleName());
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        if (mongoTemplate.collectionExists(entityClass)) {
            DeleteResult remove = mongoTemplate.remove(entityClass, collectionName);
            return remove.wasAcknowledged();
        }
        return true;
    }

    public <T> boolean exists(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "EXISTS"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.exists(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> boolean exists(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.warn("\n查询集合是否存在:\n - 集合{}", entityClass.getSimpleName());
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.collectionExists(collectionName) || mongoTemplate.collectionExists(entityClass);
    }

    public <T> List<T> group(QueryWrapper queryWrapper, Class<T> entityClass, String... keys) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "GROUP"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Criteria criteria = queryHandler.parseCriteria(queryWrapper);
        TypedAggregation<T> aggregation = TypedAggregation.newAggregation(
                entityClass,
                Aggregation.match(criteria),
                Aggregation.group(keys)

        );
        return mongoTemplate.aggregate(aggregation, collectionName, entityClass).getMappedResults();
    }


    public <T> PageResult<T> group(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass, String... keys) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR：{}\n", queryWrapper.getSrt(entityClass, "GROUP PAGE"));
        }
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
