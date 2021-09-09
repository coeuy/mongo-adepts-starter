package com.coeuy.osp.mongo.adepts.handler;

import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.constants.Option;
import com.coeuy.osp.mongo.adepts.model.query.Wrapper;
import com.coeuy.osp.mongo.adepts.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.coeuy.osp.mongo.adepts.constants.Option.*;


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
     * @param abstractAdepts abstractAdepts
     * @return boolean
     */
    public boolean verifyEqNoBlank(QueryWrapper abstractAdepts) {
        return verifyOptionNoBlank(abstractAdepts, EQ);
    }

    private boolean verifyOptionNoBlank(QueryWrapper abstractAdepts, Option... options) {
        List<Wrapper> wrappers = abstractAdepts.getWrappers();
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
     * @param abstractAdepts abstractAdepts
     * @return boolean
     */
    public boolean verifyConditionNoBlank(QueryWrapper abstractAdepts) {
        return verifyOptionNoBlank(abstractAdepts, EQ, LIKE, LIKE_LEFT, LIKE_RIGHT, NE, GE, GE_AND_LE, LE, IN, NOT_IN);
    }

    public boolean verifyConditionIsBlank(QueryWrapper abstractAdepts) {
        return !verifyConditionNoBlank(abstractAdepts);
    }

    public boolean verifyNeNoBlank(QueryWrapper abstractAdepts) {
        return verifyOptionNoBlank(abstractAdepts, NE);
    }

    public boolean verifyInNoBlank(QueryWrapper abstractAdepts) {
        return verifyOptionNoBlank(abstractAdepts, IN);
    }

    public boolean verifyEqIsBlank(QueryWrapper abstractAdepts) {
        return !verifyEqNoBlank(abstractAdepts);
    }
}
