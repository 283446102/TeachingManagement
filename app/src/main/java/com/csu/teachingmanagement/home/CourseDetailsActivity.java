package com.csu.teachingmanagement.home;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.MainActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.bean.StudentCourseBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.L;
import com.csu.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author
 * @Date 2020-04-09 11:19
 * 功能：
 */
public class CourseDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_course_name)
    TextView mTvCourseName;
    @BindView(R.id.tv_course)
    TextView mTvCourse;
    @BindView(R.id.tv_add)
    TextView mTvAdd;

    private CourseBean bean;

    @Override
    public int getLayout() {
        return R.layout.activity_course_details;
    }

    @Override
    public void onBindView() {
        bean = (CourseBean) getIntent().getSerializableExtra("course");
        if (bean != null) {
            mTvCourseName.setText(bean.getName());
            mTvCourse.setText(bean.getIntroduction());
        }

        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否已经添加
                BmobQuery<MyCourseBean> query = new BmobQuery<>();
                query.addWhereEqualTo("student", AccountHelper.getLoginInfo(getApplication(), "userName"));
                query.findObjects(new FindListener<MyCourseBean>() {
                    @Override
                    public void done(List<MyCourseBean> object, BmobException e) {
                        if (e == null) {
                            boolean flag =true;
                            for (int i = 0; i < object.size(); i++) {
                                if (object.get(i).getCourseId().equals(bean.getObjectId())){
                                    ToastUtil.showMessage("课程已添加");
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag){
                                MyCourseBean data = new MyCourseBean();
                                data.setName(bean.getName());
                                data.setIntroduction(bean.getIntroduction());
                                data.setStudent(AccountHelper.getLoginInfo(getApplication(), "userName"));
                                data.setStuNumber(bean.getStuNumber());
                                data.setTeacher(bean.getTeacher());
                                data.setCourseId(bean.getObjectId());
                                data.setTeacherName(bean.getTeacherName());
                                data.setStudentName(AccountHelper.getLoginInfo(getApplication(), "name"));
                                data.setType(bean.getType());

                                data.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            bean.setStuNumber(bean.getStuNumber() + 1);
                                            bean.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {

                                                }
                                            });
                                            BmobQuery<StudentCourseBean> query = new BmobQuery<>();
                                            query.addWhereEqualTo("userId", AccountHelper.getLoginInfo(getApplication(), "userName"));
                                            query.findObjects(new FindListener<StudentCourseBean>() {
                                                @Override
                                                public void done(List<StudentCourseBean> object, BmobException e) {
                                                    if (e == null) {
                                                        StudentCourseBean item = object.get(0);
                                                        item.setCourse(item.getCourse() + 1);
                                                        item.update(new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            ToastUtil.showMessage("课程添加成功！");
                                            returnMain();
                                            finish();
                                        } else {
                                            ToastUtil.showMessage("课程添加失败" + e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    private void returnMain() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivityForResult(intent, 1);
    }
}
