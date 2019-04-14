package com.zhexing.sociality.enums;

/**
 * 动态枚举类
 */
public enum DynamicEnum {

    DYNAMIC_DYNAMICID_("动态"),

    HOT_DYNAMIC_DYNAMICID_("热点动态"),

    DYNAMIC_COMMENT_("动态评论"),

    COMMENT_COMMENT_("评论评论"),

    HOT_COMMENT_("热点评论"),

    LIKE_DYNAMIC_("动态点赞"),

    LIKE_COMMENT("评论点赞");

    private final String name;

    private DynamicEnum(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
