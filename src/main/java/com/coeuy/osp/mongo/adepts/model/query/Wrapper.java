package com.coeuy.osp.mongo.adepts.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wrapper implements Serializable {

    private static final long serialVersionUID = 7155926662327537226L;
    /**
     * 操作
     */
    private Option option;

    /**
     * key value
     */
    private List<Condition> conditions;
    /**
     * 比较值键
     */
    private String key;
    /**
     * 比较值1
     */
    private Object var1;
    /**
     * 比较值2
     */
    private Object var2;

    private QueryWrapper<?> queryWrapper;

    public Wrapper(Option option, QueryWrapper<?> queryWrapper) {
        this.option = option;
        this.queryWrapper = queryWrapper;
    }

    public Wrapper(Option option, List<Condition> conditions) {
        this.option = option;
        this.conditions = conditions;
    }

    public Wrapper(Option option, String key, Object var1, Object var2) {
        this.option = option;
        this.key = key;
        this.var1 = var1;
        this.var2 = var2;
    }
}
