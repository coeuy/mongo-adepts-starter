package com.coeuy.osp.mongo.adepts.constants;

import java.io.Serializable;

/**
 * <p>
 * 条件枚举类
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 */
public enum Option implements Serializable {
    /**
     * 条件枚举
     */
    EQ("等于","="),
    IN("包含","in"),
    NE("不等于","!="),
    GE("大于等于",">="),
    GT("大于",">"),
    LE("小于等于","<="),
    LT("小于","<="),
    OR("或者","or"),
    INC("加减","inc"),
    NOT_IN("不包含","not in"),
    ORDER_BY_ASC("正序排序","order by asc"),
    ORDER_BY_DESC("正序排序","order by desc"),
    HAVING("含有","having"),
    LIKE_LEFT("模糊查询(左)","like left"),
    LIKE_RIGHT("模糊查询(右)","kike right"),
    UPDATE("更新","set"),
    UN_UPDATE("不更新","unset"),
    PUSH("追加子文档","push"),
    PULL("删除子文档","pull"),
    GROUP_BY("分组","group"),
    SCOPE("范围查询","scope"),
    LIKE("模糊查询","like"),
    PHASE("分段查询","phase");

    private final String cnName;

    private final String enName;

    Option(String cnName, String enName) {
        this.cnName = cnName;
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
    }
}
