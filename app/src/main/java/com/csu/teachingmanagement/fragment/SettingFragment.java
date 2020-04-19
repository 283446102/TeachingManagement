package com.csu.teachingmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csu.base.BaseFragment;
import com.csu.teachingmanagement.LoginActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.RegisterOrResetActivity;
import com.csu.teachingmanagement.adapter.SettingsAdapter;
import com.csu.teachingmanagement.bean.SettingBean;
import com.csu.teachingmanagement.bean.StudentCourseBean;
import com.csu.teachingmanagement.bean.TeacherRecordBean;
import com.csu.teachingmanagement.setting.AboutActivity;
import com.csu.teachingmanagement.setting.BrowseRecordActivity;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;
import com.csu.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-09 11:54
 * 功能：
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.ll_teacher_info)
    LinearLayout mLlTeacherInfo;
    @BindView(R.id.tv_create_course)
    TextView mTvCreateCourse;
    @BindView(R.id.tv_delete_course)
    TextView mTvDeleteCourse;
    @BindView(R.id.tv_create_file)
    TextView mTvCreateFile;
    @BindView(R.id.tv_course)
    TextView mTvCourse;

    private SettingsAdapter adapter = null;
    private String role;
    private String userId;
    private String userName;


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView){
        userId = AccountHelper.getLoginInfo(getContext(), "userName");
        userName = AccountHelper.getLoginInfo(getContext(), "name");
        role = AccountHelper.getLoginInfo(getContext(), "role");
        if (role.equals("0")) {
            mLlTeacherInfo.setVisibility(View.GONE);
            BmobQuery<StudentCourseBean> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", AccountHelper.getLoginInfo(getContext(), "userName"));
            query.findObjects(new FindListener<StudentCourseBean>() {
                @Override
                public void done(List<StudentCourseBean> object, BmobException e) {
                    if (e == null) {
                        mTvCourse.setText("我的课程数\n" + object.get(0).getCourse());
                    }
                }
            });

        } else {
            mTvCourse.setVisibility(View.GONE);
            BmobQuery<TeacherRecordBean> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", userId);
            query.findObjects(new FindListener<TeacherRecordBean>() {
                @Override
                public void done(List<TeacherRecordBean> object, BmobException e) {
                    if (e == null) {
                        TeacherRecordBean bean = object.get(0);
                        if (bean != null) {
                            mTvCreateCourse.setText("创建课程数\n" + bean.getCreateCourse());
                            mTvDeleteCourse.setText("删除课程数\n" + bean.getDeleteCourse());
                            mTvCreateFile.setText("上传文件数\n" + bean.getCreateFile());
                        }
                    } else {
                        ToastUtil.showMessage("课程数据查询失败");
                    }
                }
            });
        }
        mTvName.setText("账号：" + userId + "\n" + "姓名：" + userName);
        initView();
    }

    private void initView() {
        String[] names = new String[]{"重置密码", "关于软件", "退出登录", "最近浏览"};
        List<SettingBean> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (role.equals("1")) {
                if (i == 3) {
                    break;
                }
            }
            SettingBean bean = new SettingBean();
            bean.setName(names[i]);
            list.add(bean);
        }

        if (adapter == null) {
            adapter = new SettingsAdapter(list);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecycleView.setAdapter(adapter);
        }

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), RegisterOrResetActivity.class);
                        Bundle data = new Bundle();
                        data.putInt("type", 0);
                        intent.putExtra("data", data);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        AccountHelper.saveLoginInfo(getContext(), "", "", "", "");
                        intent = new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getContext(), BrowseRecordActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
