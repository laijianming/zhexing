package com.zhexing.social.enums;

/**
 *  社交模块枚举类
 */
public enum SocialEnum {


    DYNAMIC_("动态缓存"),

    TAGS_("话题缓存"),

    TAG_DYNAMIC_("话题下动态排行"),

    HOT_TAG_COUNT_("话题排行计数"),

    HOT_TAG_("热搜话题排行"),

    LIKE_DYNAMIC_("动态点赞"),

    LIKE_COMMENT("评论点赞"),

    DURABLE_DYNAMIC_("需持久化的动态"),

    DYNAMIC_HOT_COMMENT_("动态热评"),

    DYNAMIC_COMMENT_("动态评论"),

    DYNAMIC_COMMENTID_("动态评论id"),

    COMMENT_COMMENT_("评论评论"),

    NEWS_("新鲜事");

    private final String name;

    SocialEnum(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }



}
