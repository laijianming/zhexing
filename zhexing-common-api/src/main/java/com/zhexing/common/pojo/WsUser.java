package com.zhexing.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsUser {


    private Long UserId;

    private String uname;

    private String unickname;

    private String uchathead;


}
