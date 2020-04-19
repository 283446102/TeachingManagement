package com.csu.teachingmanagement.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.MainActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.TeacherRecordBean;
import com.csu.teachingmanagement.fragment.HomeFragment;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author
 * @Date 2020-04-09 14:23
 * 功能：
 */
public class HandleCourseActivity extends BaseActivity {

    @BindView(R.id.sp_course_type)
    Spinner mSpCourseType;
    @BindView(R.id.tv_update_create)
    TextView mTvUpdateCreate;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.edt_course_name)
    EditText mEdtCourseName;
    @BindView(R.id.edt_course)
    EditText mEdtCourse;

    private String type = "0";
    private CourseBean bean = null;

    @Override
    public int getLayout() {
        return R.layout.activity_course_handle;
    }

    @Override
    public void onBindView() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        bean = (CourseBean) bundle.getSerializable("course");
        if (type.equals("1")) {
            //类型1为创建课程
            mTvUpdateCreate.setText("创建课程");
            mTvDelete.setVisibility(View.GONE);
            initOnClick();
        } else {
            if (bean != null) {
                mEdtCourseName.setText(bean.getName());
                mEdtCourse.setText(bean.getIntroduction());
            }
            initUpdate(bean);
        }

        initSp();
    }

    //更新和删除课程
    private void initUpdate(CourseBean bean) {
        mSpCourseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bean.setType(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //更新按钮
        mTvUpdateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setName(mEdtCourseName.getText().toString());
                bean.setIntroduction(mEdtCourse.getText().toString());
                bean.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtil.showMessage("更新成功！");
                            finish();
                        } else {
                            ToastUtil.showMessage("更新失败");
                        }
                    }
                });
            }
        });
        //删除按钮
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            BmobQuery<TeacherRecordBean> query = new BmobQuery<>();
                            query.addWhereEqualTo("userId",AccountHelper.getLoginInfo(getApplication(),"userName"));
                            query.findObjects(new FindListener<TeacherRecordBean>() {
                                @Override
                                public void done(List<TeacherRecordBean> object, BmobException e) {
                                    if (e==null){
                                        if (object.size()!=0){
                                            //记录删除课程数
                                            TeacherRecordBean data = object.get(0);
                                            data.setDeleteCourse(data.getDeleteCourse()+1);
                                            data.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e==null){
                                                    }else {
                                                        ToastUtil.showMessage("删除课程数据更新错误"+e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                            ToastUtil.showMessage("删除成功！");
                            finish();
                        } else {
                            ToastUtil.showMessage("删除失败");
                        }
                    }
                });
            }
        });
    }

    private void initOnClick() {
        CourseBean bean = new CourseBean();
        mSpCourseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bean.setType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bean.setType(0);
            }
        });

        mTvUpdateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtCourseName.getText().toString().equals("")){
                    ToastUtil.showMessage("课程名称不能为空！");
                    return;
                }
                if (mEdtCourse.getText().toString().equals("")){
                    ToastUtil.showMessage("课程简介不能为空！");
                    return;
                }
                bean.setName(mEdtCourseName.getText().toString());
                bean.setIntroduction(mEdtCourse.getText().toString());
                bean.setTeacherName(AccountHelper.getLoginInfo(getApplication(), "name"));
                bean.setTeacher(AccountHelper.getLoginInfo(getApplication(), "userName"));
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            BmobQuery<TeacherRecordBean> query = new BmobQuery<>();
                            query.addWhereEqualTo("userId",AccountHelper.getLoginInfo(getApplication(),"userName"));
                            query.findObjects(new FindListener<TeacherRecordBean>() {
                                @Override
                                public void done(List<TeacherRecordBean> object, BmobException e) {
                                    if (e==null){
                                        if (object.size()!=0){
                                            //记录创建课程数
                                            TeacherRecordBean data = object.get(0);
                                            int count = data.getCreateCourse()+1;
                                            data.setCreateCourse(count);
                                            data.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e==null){
                                                    }else {
                                                        ToastUtil.showMessage("创建数据更新错误"+e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            finish();
                            ToastUtil.showMessage("创建课程成功！");
                        } else {
                            ToastUtil.showMessage(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void initSp() {
        List<String> list = new ArrayList<>();
        String[] courses = {"计算机", "外语", "理学", "工学", "经济管理", "心理学", "文史学", "医药卫生"};
        for (String course : courses) {
            list.add(course);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, list);
        mSpCourseType.setAdapter(adapter);
    }

}
