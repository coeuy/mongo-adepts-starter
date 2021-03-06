package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.exception.MongoAdeptsException;
import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.model.next.NextResult;
import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageQuery;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.utils.StringUtils;
import com.google.common.collect.Lists;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoCollectionUtils;
import org.springframework.data.mongodb.core.BulkOperations;
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
 * ??????Adepts
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
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "GET ONE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findOne(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> T getById(Serializable id, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n??????ID" + id + "????????????[" + entityClass.getSimpleName() + "]");
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public <T> List<T> list(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "?????????????????????[" + entityClass.getSimpleName() + "]????????????");
        }
        return mongoTemplate.findAll(entityClass);
    }

    public <T> List<T> list(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "LIST"));
        }
        return mongoTemplate.find(queryHandler.parse(queryWrapper), entityClass);
    }

    public <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "??????ID????????????[" + entityClass.getSimpleName() + "]: \n?????? (ID = " + idList + ")");
        }
        Iterable<T> allByIds = mongoTemplate.find(Query.query(Criteria.where(MONGO_ID).in(idList)), entityClass);
        return Lists.newArrayList(allByIds);
    }

    /**
     * ???????????????????????????
     * ??????????????????????????????????????????????????????????????????????????? Next ??????
     *
     * @param pageInfo    ????????????
     * @param entityClass ????????????
     * @param <T>         ??????
     * @return page
     */
    public <T> PageResult<T> page(PageInfo pageInfo, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "?????????????????????[" + entityClass.getSimpleName() + "]???????????????" + pageInfo.getCurrent() + "??????????????????" + pageInfo.getSize() + "???");
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(new Query().with(page), entityClass, collectionName);
        long total = mongoTemplate.count(new Query(), entityClass, collectionName);
        return new PageResult<>(list, total, page);
    }

    public <T> NextResult<T> next(boolean isDesc, String startId, long size, Class<T> entityClass) {
        Sort sort = Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "_id");
        Query query = new Query();
        if (StringUtils.isNotBlank(startId) && !StringUtils.EMPTY.equals(startId)) {
            ObjectId objectId = new ObjectId(startId);
            query.addCriteria(isDesc ? Criteria.where("_id").lt(objectId) : Criteria.where("_id").gt(objectId));
        }
        if (properties.isDebug()) {
            log.info("??????ID???????????? {} DESC:{}, StartId:{} ,Size:{}", entityClass.getSimpleName(), isDesc, startId, size);
        }
        List<T> list = mongoTemplate.find(query.with(sort).limit((int) size), entityClass);
        return NextResult.result(list, startId);
    }


    public <T> NextResult<T> next(QueryWrapper queryWrapper, boolean isDesc, String startId, long size, Class<T> entityClass) {
        Sort sort = Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "_id");
        Query query = queryHandler.parse(queryWrapper);
        if (StringUtils.isNotBlank(startId) && !StringUtils.EMPTY.equals(startId)) {
            ObjectId objectId = new ObjectId(startId);
            query.addCriteria(isDesc ? Criteria.where("_id").lt(objectId) : Criteria.where("_id").gt(objectId));
        }
        if (properties.isDebug()) {
            log.info("??????ID??????????????????:\n{} DESC:{}, StartId:{} ,Size:{}", queryWrapper.getSrt(entityClass, "NEXT"), isDesc, startId, size);
        }
        List<T> list = mongoTemplate.find(query.with(sort).limit((int) size), entityClass);
        return NextResult.result(list, startId);
    }

    public <T> NextResult<T> next(Query query, boolean isDesc, String startId, long size, Class<T> entityClass) {
        Sort sort = Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "_id");
        if (StringUtils.isNotBlank(startId) && !StringUtils.EMPTY.equals(startId)) {
            ObjectId objectId = new ObjectId(startId);
            query.addCriteria(isDesc ? Criteria.where("_id").lt(objectId) : Criteria.where("_id").gt(objectId));
        }
        if (properties.isDebug()) {
            log.info("??????ID??????query???????????? {} DESC:{}, StartId:{} ,Size:{}", entityClass.getSimpleName(), isDesc, startId, size);
        }
        List<T> list = mongoTemplate.find(query.with(sort).limit((int) size), entityClass);
        return NextResult.result(list, startId);
    }


    /**
     * ????????????
     * ????????????????????????????????????????????????????????????????????? Next ??????
     *
     * @param pageInfo     ????????????
     * @param queryWrapper ????????????
     * @param entityClass  ????????????
     * @param <T>          ??????
     * @return page
     */
    public <T> PageResult<T> page(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "????????????[" + entityClass.getSimpleName() + "]???????????????" + pageInfo.getCurrent() + "??????????????????" + pageInfo.getSize() + "???");
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

    /**
     * ???????????? count
     * ?????????????????????????????? count ?????????????????????????????????????????? count?????????????????? 500W+????????????15?????????
     * ?????????????????? skip(?) limit(?)  ?????????????????????????????????????????????  ???skip?????????1W ?????????????????????
     * ??????????????????3-5???????????????skip?????? 10W ??????????????? ?????????500W???????????????????????????????????????20????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????? next ??????
     *
     * @param pageInfo    ????????????
     * @param entityClass ????????????
     * @param <T>         ??????
     * @return ??? count ???????????????
     */
    public <T> PageResult<T> pageNoCount(PageInfo pageInfo, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "???Count??????????????????[" + entityClass.getSimpleName() + "]???????????????" + pageInfo.getCurrent() + "??????????????????" + pageInfo.getSize() + "???");
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(new Query().with(page), entityClass, collectionName);
        return new PageResult<>(list, page);
    }

    public <T> PageResult<T> pageNoCount(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass) {

        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "???Count????????????????????????[" + entityClass.getSimpleName() + "]???????????????" + pageInfo.getCurrent() + "??????????????????" + pageInfo.getSize() + "???");
        }
        Query query = queryHandler.parse(queryWrapper);
        if (queryWrapper.getTextSearch() != null) {
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(queryWrapper.getTextSearch()));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        PageQuery page = PageQuery.page(pageInfo);
        List<T> list = mongoTemplate.find(query.with(page), entityClass, collectionName);
        return new PageResult<>(list, page);
    }

    public <T> T insert(T entity) {
        if (properties.isDebug()){
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n??????????????????:\n" + entity.toString());
        }
        return mongoTemplate.insert(entity, getCollectionName(ClassUtils.getUserClass(entity)));
    }


    /**
     * ?????????????????????????????????????????????????????????????????????
     * @param entityList
     * @param <T>
     * @return
     */
    public <T> BulkWriteResult insertBatchOrdered(Collection<T> entityList){
        String collection = getCollectionName(ClassUtils.getUserClass(entityList));
        BulkOperations operations = mongoTemplate.
        bulkOps(BulkOperations.BulkMode.ORDERED, collection);
        operations.insert(entityList);
        return operations.execute();
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     * @param entityList
     * @param <T>
     * @return
     */
    public <T> BulkWriteResult insertBatchUnordered(Collection<T> entityList){
        String collection = getCollectionName(ClassUtils.getUserClass(entityList));
        BulkOperations operations = mongoTemplate.
        bulkOps(BulkOperations.BulkMode.UNORDERED, collection);
        operations.insert(entityList);
        return operations.execute();
    }

    public <T> boolean insertBatch(Collection<T> entityList) {
        if (properties.isDebug()){
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n??????????????????:\n" + entityList.toString());
        }
        Collection<T> ts = mongoTemplate.insertAll(entityList);
        return ts.size() == entityList.size();
    }

    public <T> T save(T entity) {
        if (properties.isDebug()){
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n???????????????:\n" + entity.toString());

        }
        return mongoTemplate.save(entity);
    }


    public <T> boolean update(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "UPDATE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        Update update = updateHandler.parse(queryWrapper);

        if (Objects.isNull(update)) {
            if (properties.isDebug()) {
                log.warn("?????????????????????????????????");
            }
            return false;
        }
        return mongoTemplate.updateFirst(queryHandler.parse(queryWrapper), update, entityClass, collectionName).wasAcknowledged();
    }

    public <T> T findAndModify(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "FIND AND MODIFY"));
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
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "UPDATE MULTI"));
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
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "DELETE"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);

        final Query parse = queryHandler.parse(queryWrapper);
        if (properties.isDebug()) {
            log.info("????????????????????????:\n - {}", parse);
        }
        DeleteResult remove = mongoTemplate.remove(parse, entityClass, collectionName);
        return remove.wasAcknowledged();
    }

    public <T> boolean delete(T t, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n???????????????????????????\n"+t.toString());
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        DeleteResult remove = mongoTemplate.remove(t, collectionName);
        return remove.wasAcknowledged();
    }


    public <T> int count(QueryWrapper queryWrapper, Class<T> entityClass) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "COUNT"));
        }
        return (int) mongoTemplate.count(queryHandler.parse(queryWrapper), entityClass);
    }

    public <T> int count( Class<T> entityClass) {
        if (properties.isDebug()){
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", "\n????????????["+entityClass.getSimpleName()+"]?????????");
        }
        return (int) mongoTemplate.estimatedCount(entityClass);
    }


    public <T> boolean deleteAll(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.warn("\n????????????????????????????????????:\n - ??????{}", entityClass.getSimpleName());
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
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "EXISTS"));
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.exists(queryHandler.parse(queryWrapper), entityClass, collectionName);
    }

    public <T> boolean exists(Class<T> entityClass) {
        if (properties.isDebug()) {
            log.warn("\n????????????????????????:\n - ??????{}", entityClass.getSimpleName());
        }
        String collectionName = MongoCollectionUtils.getPreferredCollectionName(entityClass);
        return mongoTemplate.collectionExists(collectionName) || mongoTemplate.collectionExists(entityClass);
    }

    public <T> List<T> group(QueryWrapper queryWrapper, Class<T> entityClass, String... keys) {
        if (properties.isDebug()) {
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "GROUP"));
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
            log.info("\n\nADEPTS DEBUG MONITOR???{}\n", queryWrapper.getSrt(entityClass, "GROUP PAGE"));
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


    @SuppressWarnings("unchecked")
    public abstract <T> boolean updateById(T t);

    public abstract <T> boolean updateByIdSkipNull(T t);

    @SuppressWarnings("unchecked")
    public abstract <T> boolean lambdaUpdateById(T t, FieldGetter<T, ?>... fieldGetters);

    public abstract <T> boolean lambdaUpdateByIdSkipNull(T t, FieldGetter<T, ?>... fieldGetters);
}
