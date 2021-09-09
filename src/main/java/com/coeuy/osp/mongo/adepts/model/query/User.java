package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
@Data
public class User implements Serializable {

    private String password;
}
