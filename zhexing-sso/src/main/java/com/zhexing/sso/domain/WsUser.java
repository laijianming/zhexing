package com.zhexing.sso.domain;


public class WsUser {


    private Long UserId;

    private String uname;

    private String unickname;

    private String uchathead;

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getUchathead() {
        return uchathead;
    }

    public void setUchathead(String uchathead) {
        this.uchathead = uchathead;
    }


    public WsUser(Long userId, String uname, String unickname, String uchathead) {
        UserId = userId;
        this.uname = uname;
        this.unickname = unickname;
        this.uchathead = uchathead;
    }
}
