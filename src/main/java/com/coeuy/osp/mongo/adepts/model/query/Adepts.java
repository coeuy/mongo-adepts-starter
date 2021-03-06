package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;

/**
 * <p>
 * Lambda条件构造器
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/7/23 9:45
 */
@Data
public class Adepts {

    public static  <T> LambdaQueryAdepts <T> lambdaQuery() {
        return new LambdaQueryAdepts<>();
    }
}
