package com.coeuy.osp.mongo.adepts.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 条件构造器键值对象
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 16:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition implements Serializable {

    private static final long serialVersionUID = -3367174887698234087L;

    private String key;

    private Object value;

    public Condition(Object value) {
        this.value = value;
    }
}
