package com.csu.teachingmanagement.setting;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.BrowseAdapter;
import com.csu.teachingmanagement.adapter.DiscussAdapter;
import com.csu.teachingmanagement.bean.BrowseBean;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.utils.AccountHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-10 13:02
 * 功能：
 */
public class BrowseRecordActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlDate;

    private BrowseAdapter mAdapter = null;

    @Override
    public int getLayout() {
        return R.layout.activity_browse_record;
    }

    @Override
    public void onBindView() {
        initView();
        loadData();
    }

    private void initView() {
        mAdapter = new BrowseAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        BmobQuery<BrowseBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username", AccountHelper.getLoginInfo(getApplication(),"userName"));
        query.order("-createdAt");
        query.findObjects(new FindListener<BrowseBean>() {
            @Override
            public void done(List<BrowseBean> object, BmobException e) {
                if (object.size()==0){
                    mLlDate.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                if (mAdapter!=null){
                    mAdapter.setNewData(object);
                }
            }
        });

    }
}
