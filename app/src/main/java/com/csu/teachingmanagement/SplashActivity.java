package com.csu.teachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.csu.base.BaseActivity;
import com.csu.utils.AccountHelper;


/**
 * @author
 * @Date 2020-04-08 23:44
 * 功能：判断是否成功登录过账号，登录过的就跳过登录页面
 */
public class SplashActivity extends BaseActivity {

    private String name;

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBindView() {
        name = AccountHelper.getLoginInfo(getApplication(), "userName");
        toMainActivity();
    }

    private void toMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (name.equals("")) {
                    //用户为空时，跳到登录页面
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //否则直接跳转主页
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

}
