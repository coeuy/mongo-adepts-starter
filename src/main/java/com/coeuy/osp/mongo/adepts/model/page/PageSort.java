package com.coeuy.osp.mongo.adepts.model.page;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/15 16:38
 */
@Data
public class PageSort implements Serializable {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    private static final long serialVersionUID = -1770771353436985887L;
    private String key;
    private String order;

    public static PageSort orderByAsc(String key) {
        PageSort pageSort = new PageSort();
        pageSort.setKey(key);
        pageSort.setOrder(ASC);
        return pageSort;
    }

    public static PageSort orderByDesc(String key) {
        PageSort pageSort = new PageSort();
        pageSort.setKey(key);
        pageSort.setOrder(DESC);
        return pageSort;
    }
}
