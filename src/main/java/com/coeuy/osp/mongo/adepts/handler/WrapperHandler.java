package com.coeuy.osp.mongo.adepts.handler;

import com.coeuy.osp.mongo.adepts.model.query.QueryAdepts;
import com.coeuy.osp.mongo.adepts.model.query.Option;
import com.coeuy.osp.mongo.adepts.model.query.Wrapper;
import com.coeuy.osp.mongo.adepts.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.coeuy.osp.mongo.adepts.model.query.Option.*;


/**
 * <p>
 * 条件构造器处理验证
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2021/3/27
 */
@Slf4j
public class WrapperHandler {
    /**
     * 判断条件中是否有 eq 条件
     *
     * @param queryAdepts queryAdepts
     * @return boolean
     */
    public boolean verifyEqNoBlank(QueryAdepts queryAdepts) {
        return verifyOptionNoBlank(queryAdepts, EQ);
    }

    private boolean verifyOptionNoBlank(QueryAdepts queryAdepts, Option... options) {
        List<Wrapper> wrappers = queryAdepts.getWrappers();
        if (CollectionUtils.isEmpty(wrappers)) {
            return false;
        }
        if (options.length == 0) {
            return false;
        }
        for (Wrapper w : wrappers) {
            for (Option option : options) {
                log.info("条件检测: {}\n {}=>{}", wrappers, option, w.getOption());
                if (option.equals(w.getOption())) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 判断匹配条件是否为空
     *
     * @param queryAdepts queryAdepts
     * @return boolean
     */
    public boolean verifyConditionNoBlank(QueryAdepts queryAdepts) {
        return verifyOptionNoBlank(queryAdepts, EQ, LIKE, LIKE_LEFT, LIKE_RIGHT, NE, GE, GE_AND_LE, LE, IN, NOT_IN);
    }

    public boolean verifyConditionIsBlank(QueryAdepts queryAdepts) {
        return !verifyConditionNoBlank(queryAdepts);
    }

    public boolean verifyNeNoBlank(QueryAdepts queryAdepts) {
        return verifyOptionNoBlank(queryAdepts, NE);
    }

    public boolean verifyInNoBlank(QueryAdepts queryAdepts) {
        return verifyOptionNoBlank(queryAdepts, IN);
    }

    public boolean verifyEqIsBlank(QueryAdepts queryAdepts) {
        return !verifyEqNoBlank(queryAdepts);
    }
}
