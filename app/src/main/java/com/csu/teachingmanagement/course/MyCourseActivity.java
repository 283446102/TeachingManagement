package com.csu.teachingmanagement.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.utils.AccountHelper;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * @author
 * @Date 2020-04-09 11:50
 * 功能：
 */
public class MyCourseActivity extends BaseActivity {

    @BindView(R.id.tv_into)
    TextView mTvInto;
    @BindView(R.id.tv_course_name)
    TextView mTvCourseName;
    @BindView(R.id.tv_course_number)
    TextView mTvCourseNumber;
    @BindView(R.id.tv_teacher_name)
    TextView mTvTeacherName;
    @BindView(R.id.tv_introduction)
    TextView mTvIntroduction;

    private CourseBean bean = null;
    private MyCourseBean bean1 = null;
    private String role;

    @Override
    public int getLayout() {
        return R.layout.activity_my_course;
    }

    @Override
    public void onBindView() {
        role = AccountHelper.getLoginInfo(this, "role");
        if (role.equals("0")) {
            bean1 = (MyCourseBean) getIntent().getSerializableExtra("course");
            if (bean1 != null) {
                mTvCourseName.setText(bean1.getName());
                BmobQuery<CourseBean> query = new BmobQuery<>();
                query.getObject(bean1.getCourseId(), new QueryListener<CourseBean>() {
                    @Override
                    public void done(CourseBean courseBean, BmobException e) {
                        if (e==null){
                            mTvCourseNumber.setText("班级人数："+courseBean.getStuNumber());
                        }
                    }
                });
                mTvIntroduction.setText(bean1.getIntroduction());
                mTvTeacherName.setText("任课老师：" +bean1.getTeacherName());
            }
        } else {
            bean = (CourseBean) getIntent().getSerializableExtra("course");
            if (bean != null) {
                mTvCourseName.setText(bean.getName());
                mTvCourseNumber.setText("班级人数：" + bean.getStuNumber());
                mTvIntroduction.setText(bean.getIntroduction());
                mTvTeacherName.setText("任课老师：" +bean.getTeacherName());
            }
        }

        mTvInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (role.equals("0")){
                    bundle.putSerializable("course",bean1);
                }else {
                    bundle.putSerializable("course",bean);
                }
                Intent intent = new Intent(getApplication(), StudyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
