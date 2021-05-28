package com.coeuy.osp.mongo.adepts.utils;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsConfiguration;
import com.coeuy.osp.mongo.adepts.exception.MongoAdeptsException;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.model.query.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/18 10:43
 */
@Slf4j
public class UpdateParseUtils {

    private static MongoAdeptsConfiguration mongoPlusConfiguration;

    @Resource
    public static void setMongoPlusConfiguration(MongoAdeptsConfiguration mongoPlusConfiguration) {
        UpdateParseUtils.mongoPlusConfiguration = mongoPlusConfiguration;
    }

    public static Update parse(QueryWrapper<?> queryWrapper) {
        if (WrapperVerifyUtils.verifyEqIsBlank(queryWrapper)) {
            log.warn("更新条件没有指定匹配精确条件");
            if (WrapperVerifyUtils.verifyConditionIsBlank(queryWrapper)){
                log.warn("更新条件没有指定任何匹配条件");
                throw new MongoAdeptsException("更新条件没有指定任何匹配条件");
            }
        }
        log.info("更新数据监听:{}",queryWrapper.toString());
        List<Wrapper> wrappers = queryWrapper.getWrappers();
        Update update = new Update();
        AtomicInteger updateSize = new AtomicInteger();
        for (Wrapper wrapper : wrappers) {
            switch (wrapper.getOption()) {
                case UPDATE:
                    wrapper.getConditions().forEach(w -> {
                        updateSize.getAndIncrement();
                        update.set(w.getKey(), w.getValue());
                    });
                    break;
                case PUSH:
                    wrapper.getConditions().forEach(w -> {
                        updateSize.getAndIncrement();
                        update.push(w.getKey(), w.getValue());
                    });
                    break;
                case UN_UPDATE:
                    wrapper.getConditions().forEach(w -> update.unset(w.getKey()));
                    break;
                case INC:
                    wrapper.getConditions().forEach(w -> update.inc(w.getKey(),(int)w.getValue()));
                    break;
                default:
                    break;
            }
        }

        if (updateSize.get()==0){
            log.warn("没有任何更新的值");
            return null;
        }
        return update;
    }
}
