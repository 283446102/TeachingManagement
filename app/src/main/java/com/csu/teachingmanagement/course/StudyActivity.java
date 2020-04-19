package com.csu.teachingmanagement.course;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.MyFragmentPagerAdapter;
import com.csu.utils.AccountHelper;
import com.csu.view.MyViewPager;

import butterknife.BindView;

/**
 * @author
 * @Date 2020-04-09 15:47
 * 功能：
 */
public class StudyActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.rg_tab_bar)
    RadioGroup mRgTabBar;
    @BindView(R.id.rb_sign_in)
    RadioButton mRbSignIn;
    @BindView(R.id.rb_discuss)
    RadioButton mRbDiscuss;
    @BindView(R.id.rb_test)
    RadioButton mRbTest;
    @BindView(R.id.vpager)
    MyViewPager mVpgaer;

    private MyFragmentPagerAdapter mAdapter;
    private String role;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    @Override
    public int getLayout() {
        return R.layout.activity_study;
    }

    @Override
    public void onBindView() {
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mRbSignIn.setChecked(true);
        initView();
    }

    private void initView() {
        mVpgaer.setAdapter(mAdapter);
        mVpgaer.setCurrentItem(0);
        mRgTabBar.setOnCheckedChangeListener(this);
        mVpgaer.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sign_in:
                mVpgaer.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_discuss:
                mVpgaer.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_test:
                mVpgaer.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mVpgaer.getCurrentItem()) {
                case PAGE_ONE:
                    mRbSignIn.setChecked(true);
                    break;
                case PAGE_TWO:
                    mRbDiscuss.setChecked(true);
                    break;
                case PAGE_THREE:
                    mRbTest.setChecked(true);
                    break;
            }
        }
    }

}
