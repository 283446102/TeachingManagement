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
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.CourseAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.manager.StudentActivity;
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
 * @Date 2020-04-10 12:05
 * 功能：
 */
public class ManagerFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;

    private CourseAdapter mCourseAdapter = null;

    public static ManagerFragment newInstance() {
        ManagerFragment fragment = new ManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_course_manager;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        mCourseAdapter = new CourseAdapter(null);
        initRecyclerView(mRecyclerView, mCourseAdapter);
        loadData();

        mCourseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean bean = (CourseBean) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", bean);
                Intent intent = new Intent(getContext(), StudentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
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
