package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;

/**
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/7/23 9:45
 */
@Data
public class Wrappers {
    public static <T> QueryWrapper<T> query() {
        return new QueryWrapper<>();
    }
}
