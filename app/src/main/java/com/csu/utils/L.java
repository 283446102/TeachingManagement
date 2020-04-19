package com.csu.utils;

import android.util.Log;

/**
 * @Date 2020-04-08 23:48
 * 功能：打印日志
 */
public class L {
    public static void d(String msg){
        Log.d("lwc",msg);
    }
    public static void i(String msg){
        Log.i("lwc",msg);
    }
    public static void e(String msg){
        Log.e("lwc",msg);
    }
}
