package com.coeuy.osp.mongo.adepts.handler;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.constants.Option;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.model.query.Wrapper;
import com.coeuy.osp.mongo.adepts.utils.CollectionUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 条件构造器解析工具
 * <p/>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 17:13
 */

@Slf4j
@RequiredArgsConstructor
public class QueryHandler {

    public final String X = "*";

    private final MongoAdeptsProperties properties;

    public Query parse(QueryWrapper queryWrapper) {
        Query query = new Query();
        Criteria criteria = parseCriteria(queryWrapper);
        if (Objects.nonNull(criteria)) {
            query.addCriteria(criteria);
        }
        queryWrapper.getWrappers().forEach(q -> {
            if (q.getOption() == Option.ORDER_BY_ASC) {
                q.getConditions().forEach(w -> query.with(Sort.by(Sort.Direction.ASC, w.getKey())));
            }
        });
        return query;
    }

    public Criteria parseCriteria(QueryWrapper abstractAdepts) {
        if (properties.isDebug()) {
            log.info("\nAdepts wrapper monitor：{}",abstractAdepts);
        }
        List<Wrapper> wrappers = abstractAdepts.getWrappers();
        List<Criteria> criteria = forEachValue(wrappers);
        return addCriteria(Objects.isNull(criteria) ? null : criteria);
    }

    public List<Criteria> forEachValue(List<Wrapper> wrappers) {
        List<Criteria> criteriaSet = Lists.newArrayList();
        wrappers.forEach(wrapper -> criteriaSet.add(caseKeyValue(wrapper)));
        return CollectionUtils.isNotEmpty(criteriaSet) ? criteriaSet : null;
    }

    private Criteria caseKeyValue(Wrapper wrapper) {
        Criteria criteria = new Criteria();
        switch (wrapper.getOption()) {
            case OR:
                List<Criteria> criteriaSet = new ArrayList<>();
                List<Wrapper> orWrappers = wrapper.getAbstractAdepts().getWrappers();
                if (CollectionUtils.isNotEmpty(orWrappers)) {
                    orWrappers.forEach(or -> criteriaSet.add(caseKeyValue(or)));
                    Criteria[] cs = criteriaSet.toArray(new Criteria[0]);
                    criteria.orOperator(cs);
                }
                break;
            case EQ:
                wrapper.getConditions().forEach(w -> criteria.and(w.getKey()).is(w.getValue()));
                break;
            case IN:
                wrapper.getConditions().forEach(w -> {
                    Object value = w.getValue();
                    log.debug(value.getClass().toString());
                    if (value instanceof Collection) {
                        if (properties.isDebug()){
                            log.debug("集合类型 {}", value);
                        }
                        ArrayList<Object> objects = Lists.newArrayList();
                        objects.addAll((Collection<?>) value);
                        criteria.and(w.getKey()).in(objects);
                    } else {
                        if (properties.isDebug()){
                            log.debug("普通类型 {}", value);
                        }
                        criteria.and(w.getKey()).in(value);
                    }
                });
                break;
            case GE_AND_LE:
                if (properties.isDebug()){
                    log.debug("范围取值 {}", wrapper);
                }
                criteria.andOperator(Criteria.where(wrapper.getKey()).gte(wrapper.getVar1()),
                        Criteria.where(wrapper.getKey()).lte(wrapper.getVar2()));
                break;
            case NE:
                wrapper.getConditions().forEach(w -> criteria.and(w.getKey()).ne(w.getValue()));
                break;
            case LE:
                wrapper.getConditions().forEach(w -> criteria.and(w.getKey()).lte(w.getValue()));
                break;
            case GE:
                wrapper.getConditions().forEach(w -> criteria.and(w.getKey()).gte(w.getValue()));
                break;
            case LT:
                wrapper.getConditions().forEach(w -> criteria.and(w.getKey()).lt(w.getValue()));
                break;
            case LIKE:
                wrapper.getConditions().forEach(
                        w -> {
                            Object value = w.getValue();
                            if (value instanceof String && ((String) value).contains(X)) {
                                log.warn("模糊匹配中含有特殊符号「{}」", value);
                                value = ((String) value).replace(X, "");
                            }
                            criteria.and(w.getKey()).regex("^.*" + value + ".*$");
                        });
                break;
            case LIKE_LEFT:
                wrapper.getConditions().forEach(w -> {
                    Object value = w.getValue();
                    if (value instanceof String && ((String) value).contains(X)) {
                        log.warn("模糊匹配中含有特殊符号「{}」", value);
                        value = ((String) value).replace(X, "");
                    }
                    criteria.and(w.getKey()).regex("^" + value + ".*$");
                });
                break;
            case LIKE_RIGHT:
                wrapper.getConditions().forEach(w -> {
                    Object value = w.getValue();
                    if (value instanceof String && ((String) value).contains(X)) {
                        log.warn("模糊匹配中含有特殊符号「{}」", value);
                        value = ((String) value).replace(X, "");
                    }
                    criteria.and(w.getKey()).regex("^.*" + value + "$");
                });
                break;
            default:
                break;
        }
        return criteria;
    }

    public Criteria addCriteria(List<Criteria> criteriaList) {
        if (Objects.isNull(criteriaList)) {
            return null;
        }
        Criteria criteria = new Criteria();
        Criteria[] cs = criteriaList.toArray(new Criteria[0]);
        criteria.andOperator(cs);
        return criteria;
    }
}
