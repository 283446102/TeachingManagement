package com.csu.teachingmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.bean.User;
import com.csu.utils.AccountHelper;
import com.csu.utils.L;
import com.csu.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author LiWeiChi
 * @Date 2020-01-08 15:48
 * 功能：登录页面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_phone)
    EditText mEditPhone;
    @BindView(R.id.edt_password)
    EditText mEditPassword;
    @BindView(R.id.cb_remember)
    CheckBox mCbRemember;
    @BindView(R.id.iv_is_visible)
    ImageView mIvIsVisible;

    private boolean isVisible = false;
    private String role = "";
    private String name = "";

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onBindView() {
        mEditPhone.setText(AccountHelper.getLoginInfo(this, "userName"));
        mEditPassword.setText(AccountHelper.getLoginInfo(this, "password"));

        initView();

    }

    private void initView() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @OnClick({R.id.tv_register, R.id.tv_forget, R.id.tv_login, R.id.iv_is_visible})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_register:
                intent = new Intent(this, RegisterOrResetActivity.class);
                Bundle data = new Bundle();
                data.putInt("type", 1);
                intent.putExtra("data", data);
                startActivity(intent);
                break;
            case R.id.tv_forget:
                intent = new Intent(this, RegisterOrResetActivity.class);
                Bundle data1 = new Bundle();
                data1.putInt("type", 0);
                intent.putExtra("data", data1);
                startActivity(intent);
                break;
            case R.id.tv_login:
                String userName = mEditPhone.getText().toString();
                String userPassword = mEditPassword.getText().toString();
                login(userName, userPassword);
                break;
            case R.id.iv_is_visible:
                if (!isVisible) {
                    mIvIsVisible.setImageResource(R.drawable.ic_open_eye);
                    mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isVisible = true;
                } else {
                    mIvIsVisible.setImageResource(R.drawable.ic_close_eye);
                    mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isVisible = false;
                }
                break;
        }
    }

    //登录
    private void login(final String userName, final String password) {
        if (userName.equals("")) {
            ToastUtil.showMessage("请输入用户名");
            return;
        } else if (password.equals("")) {
            ToastUtil.showMessage("请输入密码");
            return;
        }

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //查询成功
                    name = user.getName();
                    role = user.getRole();
                    ToastUtil.showMessage("登录成功");
                    loginSuccess(userName, password);

                } else {
                    L.d("登录失败:" + e.getErrorCode() + "  " + e.getMessage());
                    ToastUtil.showMessage("用户名或密码错误");
                }
            }
        });


    }

    private void loginSuccess(String userName, String password) {
        saveInfo(userName, password);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    //保存用户信息至本地
    private void saveInfo(String userName, String password) {
        if (mCbRemember.isChecked()) {
            AccountHelper.saveLoginInfo(this, userName, password, role, name);
        } else {
            AccountHelper.saveLoginInfo(this, "", "", "", "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("lwc", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
