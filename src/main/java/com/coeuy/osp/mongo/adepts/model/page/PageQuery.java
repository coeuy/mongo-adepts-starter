package com.coeuy.osp.mongo.adepts.model.page;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/15 15:41
 */
@Data
public class PageQuery implements Pageable, Serializable {

    private static final long serialVersionUID = 8355541120651086250L;
    private PageInfo pageInfo;

    public PageQuery(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    public PageQuery(Integer current,Integer size) {
        this.pageInfo = PageInfo.page(current,size);
    }
    public PageQuery(Integer current,Integer size,Sort sort) {
        this.pageInfo = new  PageInfo(current,size,sort);
    }

    public static PageQuery page(PageInfo pageInfo) {
        return new PageQuery(pageInfo);
    }



    @Override
    public int getPageNumber() {
        return getPageInfo().getCurrent();
    }

    @Override
    public int getPageSize() {
        return getPageInfo().getSize();
    }

    @Override
    public long getOffset() {
        return (pageInfo.getCurrent() - 1) * pageInfo.getSize();
    }

    @Override
    public Sort getSort() {
        if (getPageInfo().getPageSort().getOrder().equals(PageSort.DESC)) {
            return Sort.by(Sort.Direction.DESC, getPageInfo().getPageSort().getKey());
        }
        if (getPageInfo().getPageSort().getOrder().equals(PageSort.ASC)) {
            return Sort.by(Sort.Direction.ASC, getPageInfo().getPageSort().getKey());
        }
        return null;
    }

    @Override
    public Pageable next() {
        return new PageQuery(this.getPageNumber()+1,this.getPageSize());
    }

    @Override
    public Pageable previousOrFirst() {
        return this.getPageNumber() == 0 ? this : new PageQuery(this.getPageNumber() - 1, this.getPageSize());
    }

    @Override
    public Pageable first() {
       return new PageQuery(0,this.getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
