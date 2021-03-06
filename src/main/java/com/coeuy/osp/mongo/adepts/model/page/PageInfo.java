package com.coeuy.osp.mongo.adepts.model.page;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * <p>
 * 分页信息
 * 作为分页查询的传输对象
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/15 14:48
 */
@Data
public class PageInfo implements Serializable {
    private static final long serialVersionUID = -3289929523969018074L;
    private Integer current;
    private Integer size;

    private PageSort pageSort;

    private Sort sort;

    public PageInfo(Integer current, Integer size, PageSort pageSort) {
        this.current = current;
        this.size = size;
        this.pageSort = pageSort;
    }

    public PageInfo(Integer current, Integer size, Sort sort) {
        this.current = current;
        this.size = size;
        this.sort = sort;
    }

    /**
     * 分页默认根据ObjectId排序
     *
     * @param current 当前页
     * @param size    页个数
     * @return PageInfo
     */
    public static PageInfo page(Integer current, Integer size) {
        return new PageInfo(current, size, PageSort.orderByDesc("_id"));
    }

    /**
     * 根据指定字段倒序
     * @param current 当前页
     * @param size 页个数
     * @param key 排序字段
     * @return PageInfo
     */
    public static PageInfo pageDescForKey(Integer current, Integer size, String key) {
        return new PageInfo(current, size, PageSort.orderByDesc(key));
    }

    /**
     * 根据指定字段正序
     * @param current 当前页
     * @param size 页个数
     * @param key 排序字段
     * @return PageInfo
     */
    public static PageInfo pageAscForKey(Integer current, Integer size, String key) {
        return new PageInfo(current, size, PageSort.orderByAsc(key));
    }


}
