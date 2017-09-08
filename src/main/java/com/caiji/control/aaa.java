package com.caiji.control;

import java.util.Calendar;

/**
 * Created by Administrator on 2017-8-21.
 */


public class aaa {
    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        int i1 = instance.get(Calendar.YEAR);
        String time=String.valueOf(i1);
        System.out.printf(time);
    }


}
