package com.csu.teachingmanagement.course;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csu.base.BaseFragment;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.ExamAdapter;
import com.csu.teachingmanagement.adapter.StudentRecordAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.teachingmanagement.bean.Exam;
import com.csu.teachingmanagement.bean.Grade;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.bean.StudentRecordBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-09 15:43
 * 功能：
 */
public class TestFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.ll_record)
    LinearLayout mLlRecord;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView1;

    private ExamAdapter mAdapter = null;
    private StudentRecordAdapter adapter = null;
    private List<StudentRecordBean> beanList;
    private CourseBean bean;
    private MyCourseBean bean1;
    private HashMap<String, Integer> hashMap;

    private String role;
    private boolean first = true;
    private String courseId = "";


    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_test;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        role = AccountHelper.getLoginInfo(getContext(), "role");
        if (role.equals("0")) {
            mTvAdd.setVisibility(View.GONE);
            bean1 = (MyCourseBean) getActivity().getIntent().getSerializableExtra("course");
            courseId = bean1.getCourseId();
        } else {
            mLlRecord.setVisibility(View.VISIBLE);
            hashMap = new HashMap<>();
            beanList = new ArrayList<>();
            bean = (CourseBean) getActivity().getIntent().getSerializableExtra("course");
            courseId = bean.getObjectId();
        }

        initView();
        loadData();

        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("courseId",courseId);
                Intent intent = new Intent(getContext(),AddTestActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mAdapter = new ExamAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
        adapter = new StudentRecordAdapter(null);
        if (role.equals("1")) {
            initRecyclerView(mRecyclerView1, adapter);
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        BmobQuery<Exam> query = new BmobQuery<>();
        if (role.equals("0")) {
            query.addWhereEqualTo("courseId", bean1.getCourseId());
        } else {
            query.addWhereEqualTo("courseId", bean.getObjectId());
        }

        query.findObjects(new FindListener<Exam>() {
            @Override
            public void done(List<Exam> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                        mLlData.setVisibility(View.VISIBLE);
                    }
                    if (mAdapter != null) {
                        mAdapter.setNewData(list);
                    }
                } else {
                    ToastUtil.showMessage("查询数据出错");
                }
            }
        });

        if (role.equals("1")) {
            BmobQuery<Grade> query1 = new BmobQuery<>();
            query1.addWhereEqualTo("courseId",bean.getObjectId());
            query1.findObjects(new FindListener<Grade>() {
                @Override
                public void done(List<Grade> object, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < object.size(); i++) {
                            Integer count = hashMap.get(object.get(i).getUserId());
                            if (count == null) {
                                count = 1;
                            } else {
                                count++;
                            }
                            hashMap.put(object.get(i).getUserId(), count);
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
                            beanList.get(i).setCount("测评次数：" + beanList.get(i).getCount());
                        }
                        if (adapter != null) {
                            adapter.setNewData(beanList);
                        }
                    }
                }
            });
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (first){
            first =false;
        }else {
            loadData();
        }
    }
}
