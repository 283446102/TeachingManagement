package com.csu.utils;

import android.content.Context;
import android.widget.Toast;

import com.csu.teachingmanagement.app.APP;


/**
 * @Date 2020-04-08 23:48
 * 功能：toast
 */
public class ToastUtil {
    private static synchronized Context getContext(){
        return APP.getInstance().getApplicationContext();
    }

    public static void showMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
}
