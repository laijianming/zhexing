package com.zhexing.sociality.enums;

public enum  TagEnum {

    TAG_NAME_("话题"),

    HOT_TAG_NAME_("热点话题");

    private final String name;

    private TagEnum(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
