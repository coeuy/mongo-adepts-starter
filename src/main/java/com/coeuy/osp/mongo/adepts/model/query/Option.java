package com.coeuy.osp.mongo.adepts.model.query;

import java.io.Serializable;

/**
 * <p> 条件枚举类 </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 */
public enum Option implements Serializable {
    /**
     * 条件枚举
     */
    EQ,
    IN,
    NE,
    GE,
    GT,
    LE,
    LT,
    OR,
    INC,
    NOT_IN,
    ORDER_BY_ASC,
    ORDER_BY_DESC,
    HAVING,
    LIKE_LEFT,
    LIKE_RIGHT,
    UPDATE,
    UN_UPDATE,
    PUSH,
    GROUP_BY,
    GE_AND_LE,
    LIKE;

    Option() {
    }
}
