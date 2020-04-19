package com.csu.teachingmanagement.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.utils.PackageUtils;

import butterknife.BindView;

/**
 * @author
 * @Date 2020-04-10 13:17
 * 功能：
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_app_name)
    TextView mTvAppName;
    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;
    @BindView(R.id.tv_update)
    TextView mTvUpdate;

    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void onBindView() {
        mIvIcon.setImageResource(R.drawable.ic_160_logo);
        mTvAppName.setText(PackageUtils.getAppName(this));
        mTvAppVersion.setText("Version  "+PackageUtils.getVersionName(this));

        mTvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AboutActivity.this).setTitle("提示").setMessage("已经是最新版本了")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }
}
