package com.coeuy.osp.mongo.adepts.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 分页结果
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/15 14:23
 */
@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -862700531381496278L;
    private List<T> records;

    @Min(1)
    private long size;
    @Min(1)
    private long current;

    private long total;

    private long pages;

    public PageResult() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
    }

    public PageResult(long total, long page, long size, long current) {
        this.pages = page;
        this.total = total;
        this.size = size;
        this.current = current;
    }

    public PageResult(List<T> records, PageResult<?> result) {
        this.records = records;
        this.size = result.getSize();
        this.current = result.getCurrent();
        this.total = result.getTotal();
        this.pages = result.getPages();
    }

    public PageResult(List<T> records, long total, Pageable pageable) {
        this.records = records;
        this.total = total;
        this.current = pageable.getPageNumber();
        if (pageable.getPageSize() > total) {
            this.size = total;
        } else {
            this.size = pageable.getPageSize();
        }
        if (total % pageable.getPageSize() == 0) {
            this.pages = (int) (total / (long) pageable.getPageSize());
        } else {
            this.pages = (int) (total / (long) pageable.getPageSize()) + 1;
        }
    }

    public PageResult(long current, long size, long total) {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = Math.max(current, 1L);
        this.size = size;
        this.total = total;
    }

    public static <T> PageResult<T> page(List<T> records, PageResult<?> result) {
        return new PageResult<>(records, result);
    }

    public static <T> PageResult<T> page(List<T> records, long total, long page, long size, long current) {
        return new PageResult<>(records, total, page, size, current);
    }
}
