package com.csu.teachingmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csu.base.BaseFragment;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.CourseAdapter;
import com.csu.teachingmanagement.adapter.MyCourseAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.course.MyCourseActivity;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-09 11:30
 * 功能：
 */
public class CourseFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;

    private CourseAdapter mCourseAdapter = null;
    private MyCourseAdapter mMyCourseAdapter = null;
    private String role;

    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_course;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        role = AccountHelper.getLoginInfo(getContext(), "role");
        if (role.equals("0")) {
            mMyCourseAdapter = new MyCourseAdapter(null);
            initRecyclerView(mRecyclerView, mMyCourseAdapter);
            mMyCourseAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    MyCourseBean bean = (MyCourseBean) adapter.getData().get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", bean);
                    Intent intent = new Intent(getContext(), MyCourseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } else {
            mCourseAdapter = new CourseAdapter(null);
            initRecyclerView(mRecyclerView, mCourseAdapter);
            mCourseAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Bundle bundle = new Bundle();
                    CourseBean bean = (CourseBean) adapter.getData().get(position);
                    bundle.putSerializable("course", bean);
                    Intent intent = new Intent(getContext(), MyCourseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        loadData();


    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {

        if (role.equals("0")) {
            BmobQuery<MyCourseBean> query = new BmobQuery<>();
            query.addWhereEqualTo("student", AccountHelper.getLoginInfo(getContext(), "userName"));
            query.findObjects(new FindListener<MyCourseBean>() {
                @Override
                public void done(List<MyCourseBean> object, BmobException e) {
                    if (object.size() == 0) {
                        mLlData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    if (mMyCourseAdapter != null) {
                        mMyCourseAdapter.setNewData(object);
                    }
                }
            });
        } else {
            BmobQuery<CourseBean> query = new BmobQuery<>();
            query.addWhereEqualTo("teacher", AccountHelper.getLoginInfo(getContext(), "userName"));
            query.findObjects(new FindListener<CourseBean>() {
                @Override
                public void done(List<CourseBean> object, BmobException e) {
                    if (object.size() == 0) {
                        mLlData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    if (mCourseAdapter != null) {
                        mCourseAdapter.setNewData(object);
                    }
                }
            });
        }
    }
}
