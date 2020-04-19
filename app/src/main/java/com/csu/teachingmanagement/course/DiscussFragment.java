package com.csu.teachingmanagement.course;

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
import com.csu.teachingmanagement.adapter.DiscussAdapter;
import com.csu.teachingmanagement.adapter.StudentRecordAdapter;
import com.csu.teachingmanagement.adapter.StudentSignInAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.bean.StudentRecordBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-09 15:42
 * 功能：主题页面
 */
public class DiscussFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView1;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.ll_data)
    LinearLayout mLlDate;
    @BindView(R.id.ll_record)
    LinearLayout mLlRecord;

    private DiscussAdapter mAdapter = null;
    private StudentRecordAdapter adapter = null;
    private List<StudentRecordBean> beanList;
    private HashMap<String, Integer> hashMap;
    private String role;
    private MyCourseBean bean;
    private CourseBean bean1;
    private boolean first = true;

    public static DiscussFragment newInstance() {
        DiscussFragment fragment = new DiscussFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_discuss;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        role = AccountHelper.getLoginInfo(getContext(), "role");
        if (role.equals("0")) {
            bean = (MyCourseBean) getActivity().getIntent().getExtras().getSerializable("course");
            mTvAdd.setVisibility(View.GONE);
        } else {
            mLlRecord.setVisibility(View.VISIBLE);
            hashMap = new HashMap<>();
            beanList = new ArrayList<>();
            bean1 = (CourseBean) getActivity().getIntent().getSerializableExtra("course");
            mTvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course_info", bean1);
                    Intent intent = new Intent(getContext(), DiscussActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        initView();
        loadData();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscussBean data = (DiscussBean) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("discuss", data);
                Intent intent = new Intent(getContext(), ThemeDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mAdapter = new DiscussAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
        adapter = new StudentRecordAdapter(null);
        if (role.equals("1")) {
            initRecyclerView(mRecyclerView1, adapter);
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        BmobQuery<DiscussBean> query = new BmobQuery<>();
        if (role.equals("0")) {
            query.addWhereEqualTo("courseId", bean.getCourseId());
        } else {
            query.addWhereEqualTo("courseId", bean1.getObjectId());
        }
        query.order("-createdAt");
        query.findObjects(new FindListener<DiscussBean>() {
            @Override
            public void done(List<DiscussBean> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        mLlDate.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    if (mAdapter != null) {
                        mAdapter.setNewData(object);
                    }
                } else {
                    ToastUtil.showMessage(e.getMessage());
                }
            }
        });

        if (role.equals("1")) {
            BmobQuery<ThemeBean> query1 = new BmobQuery<>();
            query1.addWhereEqualTo("isTeacher", "0");
            query1.findObjects(new FindListener<ThemeBean>() {
                @Override
                public void done(List<ThemeBean> object, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < object.size(); i++) {
                            if (object.get(i).getCourseId().equals(bean1.getObjectId())) {
                                Integer count = hashMap.get(object.get(i).getStudent());
                                if (count == null) {
                                    count = 1;
                                } else {
                                    count++;
                                }
                                hashMap.put(object.get(i).getStudent(), count);
                            }
                        }
                        for (String key : hashMap.keySet()) {
                            StudentRecordBean data = new StudentRecordBean();
                            data.setName("学生账号：" + key);
                            data.setCount("" + hashMap.get(key));
                            beanList.add(data);
                        }

                        //排序
                        for (int i = 0; i < beanList.size(); i++) {
                            for (int j = i; j < beanList.size(); j++) {
                                if (Integer.parseInt(beanList.get(j).getCount()) > Integer.parseInt(beanList.get(i).getCount())) {
                                    String tem = beanList.get(i).getCount();
                                    beanList.get(i).setCount(beanList.get(j).getCount());
                                    beanList.get(j).setCount(tem);
                                }
                            }
                        }
                        for (int i = 0; i < beanList.size(); i++) {
                            beanList.get(i).setCount("讨论次数：" + beanList.get(i).getCount());
                        }
                        if (adapter != null) {
                            adapter.setNewData(beanList);
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (first) {
            first = false;
        } else {
            loadData();
        }
    }
}
