package com.csu.teachingmanagement.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.ThemeAdapter;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-09 20:49
 * 功能：主题详情页面
 */
public class ThemeDetailsActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_reply)
    TextView mTvReply;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_discuss)
    TextView mTvDiscuss;
    @BindView(R.id.tv_discuss_time)
    TextView mTvDiscussTime;
    @BindView(R.id.tv_discuss_count)
    TextView mTvDiscussCount;

    private ThemeAdapter mAdapter = null;
    private DiscussBean bean;
    private boolean first = true;

    @Override
    public int getLayout() {
        return R.layout.activity_theme_details;
    }

    @Override
    public void onBindView() {
        bean = (DiscussBean) getIntent().getExtras().getSerializable("discuss");
        initView();
        loadData();

        mTvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("discuss", bean);
                Intent intent = new Intent(getApplication(), ReplyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        if (bean != null) {
            mTvTitle.setText(bean.getTitle());
            mTvDiscuss.setText(bean.getDiscuss());
            mTvDiscussTime.setText(bean.getDay());
        }


        mAdapter = new ThemeAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {

        BmobQuery<ThemeBean> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("discussId", bean.getObjectId());
        query.findObjects(new FindListener<ThemeBean>() {
            @Override
            public void done(List<ThemeBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        mTvDiscussCount.setText("回复 " + list.size());
                    }
                    if (mAdapter != null) {
                        mAdapter.setNewData(list);
                    }
                } else {
                    ToastUtil.showMessage("查询失败");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (first) {
            first = false;
        } else {
            loadData();
        }
    }
}
