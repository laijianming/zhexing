package com.zhexing.social.websocket;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {

//        String message = "123!∮@∮!aaa";
//        String userIdstr = message.substring(0, message.indexOf("!∮@∮!"));
//        System.out.println(userIdstr);

        Map<Integer,String> map = new HashMap<>();
        map.put(1,"a");
        map.put(3,"a");
        map.put(4,"a");
        map.put(6,"a");
        for(Integer i : map.keySet()){
            System.out.println(i);
        }

    }


}
