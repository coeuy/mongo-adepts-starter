package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
@Data
public class QueryWrapper implements Serializable {

    private static final long serialVersionUID = -2523209411758953004L;
    /**
     * 防止多次比较
     */
    protected boolean compared;
    /**
     * 条件集合
     */

    protected List<Wrapper> wrappers = new ArrayList<>();


    protected String textSearch;
}
