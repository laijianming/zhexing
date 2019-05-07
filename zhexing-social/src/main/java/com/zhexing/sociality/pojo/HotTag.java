package com.zhexing.sociality.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotTag {


    private String tname;


    // 该话题下有多少条动态
    private Long dynamics;
}
