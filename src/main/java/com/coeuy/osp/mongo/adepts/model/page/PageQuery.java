package com.coeuy.osp.mongo.adepts.model.page;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Optional;

/**
 * <p>
 * Mongo Data 分页对象
 * {@link Pageable}
 * </p>
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

    public PageQuery(Integer current, Integer size) {
        this.pageInfo = PageInfo.page(current, size);
    }

    public PageQuery(Integer current, Integer size, Sort sort) {
        this.pageInfo = new PageInfo(current, size, sort);
    }

    public static PageQuery page(PageInfo pageInfo) {
        return new PageQuery(pageInfo);
    }


    @Override
    public boolean isPaged() {
        return Pageable.super.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return Pageable.super.isUnpaged();
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
        return (long) (pageInfo.getCurrent() - 1) * pageInfo.getSize();
    }

    @Override
    public @NonNull Sort getSort() {
        if (getPageInfo().getPageSort().getOrder().equals(PageSort.DESC)) {
            return Sort.by(Sort.Direction.DESC, getPageInfo().getPageSort().getKey());
        }
        return Sort.by(Sort.Direction.ASC, getPageInfo().getPageSort().getKey());

    }

    @Override
    public @NonNull Sort getSortOr(@NonNull Sort sort) {
        return Pageable.super.getSortOr(sort);
    }

    @Override
    public @NonNull Pageable next() {
        return new PageQuery(this.getPageNumber() + 1, this.getPageSize());
    }

    @Override
    public @NonNull Pageable previousOrFirst() {
        return this.getPageNumber() == 0 ? this : new PageQuery(this.getPageNumber() - 1, this.getPageSize());
    }

    @Override
    public @NonNull Pageable first() {
        return new PageQuery(0, this.getPageSize());
    }

    @Override
    public @NonNull Pageable withPage(int i) {
        return new PageQuery(i, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.getPageNumber() > 0;
    }

    @Override
    public @NonNull Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }
}
