package com.coeuy.osp.mongo.adepts.exception;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2021/3/27
 */
public class MongoAdeptsException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -7085585935760425271L;

    public MongoAdeptsException(String message) {
        super(message);
    }

}
