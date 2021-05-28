package com.coeuy.osp.mongo.adepts.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2021/5/17
 */
@Data
@AllArgsConstructor
public class TextSearch implements Serializable {
    private static final long serialVersionUID = -7249208613958263521L;
    private String text;
}
