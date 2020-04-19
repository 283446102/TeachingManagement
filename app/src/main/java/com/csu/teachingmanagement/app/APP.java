package com.csu.teachingmanagement.app;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;

import cn.bmob.v3.Bmob;


/**
 * @Date 2020-04-08 22:56
 * 功能：全局初始化
 */
public class APP extends Application {

    private static APP mInstance;

    public static APP getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化bmob数据库
        String bmobAppkey = "13aed8eff3689383c82035630de2654a";
        Bmob.initialize(getApplicationContext(), bmobAppkey);
    }

}
