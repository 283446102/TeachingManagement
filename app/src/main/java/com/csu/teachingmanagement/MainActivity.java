package com.csu.teachingmanagement;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.fragment.CourseFragment;
import com.csu.teachingmanagement.fragment.HomeFragment;
import com.csu.teachingmanagement.fragment.ManagerFragment;
import com.csu.teachingmanagement.fragment.SettingFragment;
import com.csu.utils.AccountHelper;


import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ll_manager)
    LinearLayout mLlManager;
    @BindView(R.id.iv_1)
    ImageView imageView1;
    @BindView(R.id.iv_2)
    ImageView imageView2;
    @BindView(R.id.iv_3)
    ImageView imageView3;
    @BindView(R.id.iv_4)
    ImageView imageView4;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onBindView() {
        if (AccountHelper.getLoginInfo(this, "role").equals("0")) {
            mLlManager.setVisibility(View.GONE);
        }
        replaceContainer(R.id.fl_contain, HomeFragment.newInstance());
    }

    @OnClick({R.id.ll_home, R.id.ll_course, R.id.ll_manager, R.id.ll_setting})
    public void OnClick(View view) {
        switch (view.getId()) {
            //首面
            case R.id.ll_home:
                setImage(0);
                imageView1.setImageResource(R.drawable.ic_home_press);
                replaceContainer(R.id.fl_contain, HomeFragment.newInstance());
                break;
            //我的课程页面
            case R.id.ll_course:
                setImage(1);
                replaceContainer(R.id.fl_contain, CourseFragment.newInstance());
                break;
            //管理页面
            case R.id.ll_manager:
                setImage(2);
                replaceContainer(R.id.fl_contain, ManagerFragment.newInstance());
                break;
            //个人中心
            case R.id.ll_setting:
                setImage(3);
                replaceContainer(R.id.fl_contain, SettingFragment.newInstance());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 1) {
            replaceContainer(R.id.fl_contain, HomeFragment.newInstance());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImage(int index) {
        ImageView [] imageViews = {imageView1,imageView2,imageView3,imageView4};
        int[] image = {R.drawable.ic_home, R.drawable.ic_ranking, R.drawable.ic_manager, R.drawable.ic_mine};
        int[] imagePress = {R.drawable.ic_home_press, R.drawable.ic_ranking_press, R.drawable.ic_manager_press, R.drawable.ic_mine_press};
        for (int i = 0; i < 4; i++) {
            if (i==index){
                imageViews[i].setImageResource(imagePress[i]);
            }else {
                imageViews[i].setImageResource(image[i]);
            }
        }
    }
}
