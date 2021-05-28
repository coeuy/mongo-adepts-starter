package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageQuery;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.utils.CriteriaParseUtils;
import com.coeuy.osp.mongo.adepts.utils.ReflectionKit;
import com.coeuy.osp.mongo.adepts.utils.UpdateParseUtils;
import com.google.common.collect.Lists;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoCollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Mongo Adepts
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 10:09
 */
@Slf4j
public class MongoService<T> {
    private final static String MONGO_ID = "_id";
    protected Class<T> entityClass = currentModelClass();
    @Resource
    protected  MongoTemplate mongoTemplate;

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    public List<T> list() {
        return mongoTemplate.findAll(entityClass);
    }

    public List<T> list(QueryWrapper<T> queryWrapper) {
        return mongoTemplate.find(CriteriaParseUtils.parse(queryWrapper), entityClass);
    }

    public List<T> listByIds(Collection<? extends Serializable> idList) {
        Iterable<T> allByIds = mongoTemplate.find(Query.query(Criteria.where(MONGO_ID).in(idList)), entityClass);
        return Lists.newArrayList(allByIds);
    }

    public PageResult<T> page(PageInfo pageInfo) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(new Query().with(page), entityClass,collectionName);
        long total = mongoTemplate.count(new Query(), entityClass,collectionName);
        return new PageResult<>(list, total, page);
    }

    public PageResult<T> page(PageInfo pageInfo, QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Query query = CriteriaParseUtils.parse(queryWrapper);
        if (queryWrapper.getTextSearch()!=null){
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(queryWrapper.getTextSearch()));
        }
        long total = mongoTemplate.count(query, entityClass,collectionName);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(query.with(page), entityClass,collectionName);
        return new PageResult<>(list, total, page);
    }

    public T getOne(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findOne(CriteriaParseUtils.parse(queryWrapper), entityClass,collectionName);
    }

    public T getById(Serializable id) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findById(id, entityClass,collectionName);
    }

    public T insert(T t) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.insert(t, collectionName);
    }

    public boolean insertBatch(Collection<T> entityList) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Collection<T> ts = mongoTemplate.insertAll(entityList);
        return ts.size() == entityList.size();
    }

    public T save(T t) {
        return mongoTemplate.save(t);
    }


    public boolean update(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = UpdateParseUtils.parse(queryWrapper);
        if (Objects.isNull(update)) {
            log.warn("更新操作不执行");
            return true;
        }
        return mongoTemplate.updateFirst(CriteriaParseUtils.parse(queryWrapper), update, entityClass,collectionName).wasAcknowledged();
    }

    public T findAndModify(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        Update update = UpdateParseUtils.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return null;
        }
        return mongoTemplate.findAndModify(CriteriaParseUtils.parse(queryWrapper), update, entityClass,collectionName);
    }


    public boolean updateMulti(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = UpdateParseUtils.parse(queryWrapper);
        if (Objects.isNull(update)) {
            return true;
        }
        return mongoTemplate.updateMulti(CriteriaParseUtils.parse(queryWrapper), update, entityClass,collectionName).wasAcknowledged();
    }


    public boolean delete(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        final Query parse = CriteriaParseUtils.parse(queryWrapper);
        log.info("删除数据操作监听:\n - {}", parse);
        DeleteResult remove = mongoTemplate.remove(parse, entityClass,collectionName);
        return remove.wasAcknowledged();
    }

    public boolean delete(T t) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        DeleteResult remove = mongoTemplate.remove(t,collectionName);
        return remove.wasAcknowledged();
    }


    public int count(QueryWrapper<T> queryWrapper) {
        return (int) mongoTemplate.count(CriteriaParseUtils.parse(queryWrapper), entityClass);
    }

    public boolean removeAll() {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        if (mongoTemplate.collectionExists(entityClass)) {
            DeleteResult remove = mongoTemplate.remove(entityClass, collectionName);
            return remove.wasAcknowledged();
        }
        return true;
    }

    public boolean exists(QueryWrapper<T> queryWrapper) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        return mongoTemplate.exists(CriteriaParseUtils.parse(queryWrapper), entityClass,collectionName);
    }

    public boolean collectionExists() {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.collectionExists(collectionName)||mongoTemplate.collectionExists(entityClass);
    }

    public List<T> group(QueryWrapper<T> queryWrapper, String... keys) {
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        Criteria criteria = CriteriaParseUtils.parseCriteria(queryWrapper);
        TypedAggregation<T> aggregation = TypedAggregation.newAggregation(
                entityClass,
                Aggregation.match(criteria),
                Aggregation.group(keys)

        );
        return mongoTemplate.aggregate(aggregation, collectionName,entityClass).getMappedResults();
    }

    public PageResult<T> group(PageInfo pageInfo, QueryWrapper<T> queryWrapper, String... keys) {
        PageQuery page = PageQuery.page(pageInfo);
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Criteria criteria = CriteriaParseUtils.parseCriteria(queryWrapper);
        TypedAggregation<T> aggregation = TypedAggregation.newAggregation(
                entityClass,
                Aggregation.match(criteria),
                Aggregation.group(keys),
                Aggregation.skip((long) (page.getPageNumber() - 1) * page.getPageSize()),
                Aggregation.limit(page.getPageSize())
        );
        AggregationResults<T> aggregate = mongoTemplate.aggregate(aggregation, entityClass);
        long count = mongoTemplate.count(Query.query(criteria), entityClass,collectionName);
        return PageResult.page(aggregate.getMappedResults(), count, page.getPageNumber(), page.getPageSize(), page.getOffset());
    }


}
