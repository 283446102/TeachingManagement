package com.csu.base;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder = null;//添加对butterknife的绑定

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//初始化窗口为无标题栏的
        if (getLayout() != 0) {
            setContentView(getLayout());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mUnbinder = ButterKnife.bind(this);
        onBindView();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayout();

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        super.onDestroy();
    }
    /*
    * 初始化控件
    * */
    public abstract void onBindView();

    protected void replaceContainer(@IdRes int containerViewId, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment);
        ft.commit();
    }
}
