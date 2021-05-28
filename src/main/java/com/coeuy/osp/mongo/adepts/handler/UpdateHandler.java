package com.coeuy.osp.mongo.adepts.handler;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.exception.MongoAdeptsException;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.model.query.Wrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 更新条件解析
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/18 10:43
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateHandler {

    private final MongoAdeptsProperties properties;
    private final WrapperHandler wrapperHandler;

    public Update parse(QueryWrapper<?> queryWrapper) {
        if (wrapperHandler.verifyEqIsBlank(queryWrapper)) {
            log.warn("更新条件没有指定匹配精确条件");
            if (wrapperHandler.verifyConditionIsBlank(queryWrapper)) {
                log.warn("更新条件没有指定任何匹配条件");
                throw new MongoAdeptsException("更新条件没有指定任何匹配条件");
            }
        }
        log.info("更新数据监听:{}", queryWrapper.toString());
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
                case PULL:
                    wrapper.getConditions().forEach(w -> {
                        updateSize.getAndIncrement();
                        update.pull(w.getKey(), w.getValue());
                    });
                    break;
                case UN_UPDATE:
                    wrapper.getConditions().forEach(w -> update.unset(w.getKey()));
                    break;
                case INC:
                    wrapper.getConditions().forEach(w -> update.inc(w.getKey(), (int) w.getValue()));
                    break;
                default:
                    break;
            }
        }

        if (updateSize.get() == 0) {
            log.warn("没有任何更新的值");
            return null;
        }
        return update;
    }
}
