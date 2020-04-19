package com.csu.teachingmanagement.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.StudentAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-10 12:43
 * 功能：
 */
public class StudentActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;

    private StudentAdapter mAdapter = null;
    private CourseBean bean;

    @Override
    public int getLayout() {
        return R.layout.activity_student;
    }

    @Override
    public void onBindView() {
        bean = (CourseBean) getIntent().getExtras().getSerializable("course");
        initView();
        loadData();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyCourseBean data = (MyCourseBean) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student",data);
                bundle.putString("courseId",bean.getObjectId());
                Intent intent = new Intent(getApplication(), CourseStudentInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mAdapter = new StudentAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        BmobQuery<MyCourseBean> query= new BmobQuery<>();
        query.addWhereEqualTo("courseId",bean.getObjectId());
        query.findObjects(new FindListener<MyCourseBean>() {
            @Override
            public void done(List<MyCourseBean> list, BmobException e) {
                if (e==null){
                    if (list.size()==0){
                        mLlData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    if (mAdapter!=null){
                        mAdapter.setNewData(list);
                    }
                }else {
                    ToastUtil.showMessage("数据加载出错");
                }
            }
        });

    }
}
