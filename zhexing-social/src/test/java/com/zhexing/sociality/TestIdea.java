package com.zhexing.sociality;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestIdea {


    public static void main(String[] args) {

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(df.format(date));

    }

}
