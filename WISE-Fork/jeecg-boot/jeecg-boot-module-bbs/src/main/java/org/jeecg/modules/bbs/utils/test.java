package org.jeecg.modules.bbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    /**
     * 没用的类
     */
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d = sdf.parse("2100-01-01 00:00:00");
            System.out.println(d.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
