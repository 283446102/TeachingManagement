package com.csu.teachingmanagement.manager;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.DiscussAdapter;
import com.csu.teachingmanagement.adapter.GradeAdapter;
import com.csu.teachingmanagement.adapter.StudentSignInAdapter;
import com.csu.teachingmanagement.adapter.ThemeAdapter;
import com.csu.teachingmanagement.bean.Grade;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.bean.StudentSignInBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-10 12:30
 * 功能：
 */
public class CourseStudentInfoActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;

    private MyCourseBean bean;

    private GradeAdapter adapter1 = null;
    private StudentSignInAdapter adapter2 = null;
    private ThemeAdapter adapter3 = null;
    private String courseId = "";


    @Override
    public int getLayout() {
        return R.layout.activity_course_student_info;
    }

    @Override
    public void onBindView() {
        bean = (MyCourseBean) getIntent().getExtras().getSerializable("student");
        courseId = getIntent().getExtras().getString("courseId");
        initView();
    }

    @OnClick({R.id.tv_sign, R.id.tv_discuss, R.id.tv_test})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign:
                initRecyclerView(mRecyclerView, adapter2);
                BmobQuery<StudentSignInBean> query1 = new BmobQuery<>();
                query1.addWhereEqualTo("student", bean.getStudent());
                query1.order("-createdAt");
                query1.findObjects(new FindListener<StudentSignInBean>() {
                    @Override
                    public void done(List<StudentSignInBean> object, BmobException e) {
                        if (e == null) {
                            List<StudentSignInBean> list = new ArrayList<>();
                            for (int i=0;i<object.size();i++){
                                if (object.get(i).getCourseId().equals(courseId)){
                                    list.add(object.get(i));
                                }
                            }
                            if (list.size() == 0) {
                                mLlData.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mLlData.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            if (adapter2 != null) {
                                adapter2.setNewData(list);
                            } else {
                                ToastUtil.showMessage("签到记录加载失败");
                            }
                        }
                    }
                });
                break;
            case R.id.tv_discuss:
                initRecyclerView(mRecyclerView, adapter3);
                BmobQuery<ThemeBean> query2 = new BmobQuery<>();
                query2.addWhereEqualTo("student", bean.getStudent());
                query2.order("-createdAt");
                query2.findObjects(new FindListener<ThemeBean>() {
                    @Override
                    public void done(List<ThemeBean> object, BmobException e) {
                        if (e == null) {
                            if (object.size() == 0) {
                                mLlData.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mLlData.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            if (adapter3 != null) {
                                adapter3.setNewData(object);
                            } else {
                                ToastUtil.showMessage("评论记录加载失败");
                            }
                        }
                    }
                });

                break;
            case R.id.tv_test:
                initRecyclerView(mRecyclerView, adapter1);
                BmobQuery<Grade> query3 = new BmobQuery<>();
                query3.addWhereEqualTo("userId", bean.getStudent());
                query3.order("-createdAt");
                query3.findObjects(new FindListener<Grade>() {
                    @Override
                    public void done(List<Grade> object, BmobException e) {
                        if (e == null) {
                            if (object.size() == 0) {
                                mLlData.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mLlData.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            if (adapter1 != null) {
                                adapter1.setNewData(object);
                            }
                        } else {
                            ToastUtil.showMessage("评测记录加载失败");
                        }
                    }
                });
                break;
        }
    }

    private void initView() {
        adapter1 = new GradeAdapter(null);
        adapter2 = new StudentSignInAdapter(null);
        adapter3 = new ThemeAdapter(null);

        BmobQuery<StudentSignInBean> query = new BmobQuery<>();
        query.addWhereEqualTo("student", bean.getStudent());
        query.order("-createdAt");
        query.findObjects(new FindListener<StudentSignInBean>() {
            @Override
            public void done(List<StudentSignInBean> object, BmobException e) {
                if (e == null) {
                    List<StudentSignInBean> list = new ArrayList<>();
                    for (int i=0;i<object.size();i++){
                        if (object.get(i).getCourseId().equals(courseId)){
                            list.add(object.get(i));
                        }
                    }
                    if (list.size() == 0) {
                        mLlData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }else {
                        mLlData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    if (adapter2 != null) {
                        adapter2.setNewData(list);
                    } else {
                        ToastUtil.showMessage("签到记录加载失败");
                    }
                }
            }
        });

        initRecyclerView(mRecyclerView, adapter2);

    }


    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
