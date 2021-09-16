package com.coeuy.osp.mongo.adepts.model.next;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/16
 */
@Data
public class NextInfo implements Serializable {
    private static final long serialVersionUID = -6295787249219330641L;

    private long size;

    private String sortKey;
    /**
     * 使用该对象接受前端数据可能会解析错误，比如时间日期，会导致无法比较
     * 建议使用其他对象接收前端数据明确类型后再丢进这个Object
     */
    private Object startValue;

    private boolean isDesc;
}
