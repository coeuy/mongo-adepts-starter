package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 查询对象包装器
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 10:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryAdepts extends QueryWrapper implements Query<String> ,Serializable {

    private static final long serialVersionUID = -8672871214331349354L;



}
