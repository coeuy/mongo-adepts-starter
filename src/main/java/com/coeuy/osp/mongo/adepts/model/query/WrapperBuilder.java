package com.coeuy.osp.mongo.adepts.model.query;

import com.coeuy.osp.mongo.adepts.constants.Option;
import com.google.common.collect.Lists;

import java.io.Serializable;

/**
 * <p>
 * Wrappers生成
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public abstract class WrapperBuilder implements Serializable {
    private static final long serialVersionUID = 9030342935857096384L;

    protected Wrapper buildUnUpdate(String field, Object value) {
        return new Wrapper(Option.UN_UPDATE, Lists.newArrayList(new Condition(field, value)));
    }

    protected Wrapper buildUpdate(String field, Object value) {
        return new Wrapper(Option.UPDATE, Lists.newArrayList(new Condition(field, value)));
    }

    protected Wrapper buildPush(String field, Object value) {
        return new Wrapper(Option.PUSH, Lists.newArrayList(new Condition(field, value)));
    }

    protected Wrapper buildPull(String field, Object value) {
        return new Wrapper(Option.PUSH, Lists.newArrayList(new Condition(field, value)));
    }

    protected Wrapper buildInc(String field, Object value) {
        return new Wrapper(Option.INC, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildEq(String field, Object value) {
        return new Wrapper(Option.EQ, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildLe(String field, Object value) {
        return new Wrapper(Option.LE, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildNe(String field, Object value) {
        return new Wrapper(Option.NE, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildIn(String field, Object value) {
        return new Wrapper(Option.IN, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildGe(String field, Object value) {
        return new Wrapper(Option.GE, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildGt(String field, Object value) {
        return new Wrapper(Option.GT, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildLt(String field, Object value) {
        return new Wrapper(Option.LT, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildScope(String field, Object ge, Object le) {
        return new Wrapper(Option.SCOPE, field,ge,le);
    }

    protected Wrapper buildPhase(String field, Object ge, Object lt) {
        return new Wrapper(Option.PHASE, field,ge,lt);
    }
    protected Wrapper buildLike(String field, Object value) {
        return new Wrapper(Option.LIKE, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildLikeLeft(String field, Object value) {
        return new Wrapper(Option.LIKE_LEFT, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildLikeRight(String field, Object value) {
        return new Wrapper(Option.LIKE_RIGHT, Lists.newArrayList(new Condition(field, value)));
    }
    protected Wrapper buildOrderAsc(String field) {
        return new Wrapper(Option.ORDER_BY_ASC, Lists.newArrayList(new Condition(field,null)));
    }

    protected Wrapper buildOrderDesc(String field) {
        return new Wrapper(Option.ORDER_BY_DESC, Lists.newArrayList(new Condition(field,null)));
    }
}
