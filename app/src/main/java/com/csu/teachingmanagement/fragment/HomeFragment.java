package com.csu.teachingmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csu.base.BaseFragment;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.CourseAdapter;
import com.csu.teachingmanagement.adapter.HomeCourseAdapter;
import com.csu.teachingmanagement.adapter.IconAdapter;
import com.csu.teachingmanagement.bean.BrowseBean;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.IconBean;
import com.csu.teachingmanagement.home.CourseDetailsActivity;
import com.csu.teachingmanagement.home.HandleCourseActivity;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author
 * @Date 2020-04-09 10:07
 * 功能：
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.recycler_view_type)
    RecyclerView mRecyclerViewType;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.v_line)
    View mLine;

    private IconAdapter mIconAdapter = null;
    private HomeCourseAdapter mCourseAdapter = null;
    private String role = "0";
    private boolean first = true;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        role = AccountHelper.getLoginInfo(getContext(), "role");
        if (role.equals("1")) {
            mTvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), HandleCourseActivity.class);
                    Bundle bundle = new Bundle();
                    //创建课程
                    bundle.putString("type", "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            mRecyclerViewType.setVisibility(View.GONE);
        } else {
            mLine.setVisibility(View.GONE);
            mTvAdd.setVisibility(View.GONE);
            mIconAdapter = new IconAdapter(null);
            initRecyclerView(mRecyclerViewType, mIconAdapter);
        }

        mCourseAdapter = new HomeCourseAdapter(null);
        initRecyclerView1(mRecyclerView, mCourseAdapter);
        loadData();

        //点击课程列表跳到详情页面
        mCourseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //传递对象到新的页面
                CourseBean bean = (CourseBean) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", bean);
                Intent intent;
                if (role.equals("0")) {

                    BrowseBean data = new BrowseBean();
                    data.setCourse(bean.getName());
                    data.setUsername(AccountHelper.getLoginInfo(getContext(), "userName"));
                    data.setDay(df.format(new Date()));
                    data.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                        }
                    });

                    intent = new Intent(getContext(), CourseDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    //更新或删除
                    bundle.putString("type", "0");
                    if (bean.getTeacher().equals(AccountHelper.getLoginInfo(getContext(), "userName"))) {
                        intent = new Intent(getContext(), HandleCourseActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        ToastUtil.showMessage("这不是你创建的课程，无操作权限");
                    }
                }
            }
        });

        if (role.equals("0")) {
            //根据类型查询课程显示在课程列表中
            mIconAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BmobQuery<CourseBean> query = new BmobQuery<>();
                    query.addWhereEqualTo("type", position);
                    query.findObjects(new FindListener<CourseBean>() {
                        @Override
                        public void done(List<CourseBean> list, BmobException e) {
                            if (mCourseAdapter != null) {
                                mCourseAdapter.setNewData(list);
                            }
                        }
                    });
                }
            });
        }

        //模糊查询，本地处理，在线模糊搜索需要开通会员
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = mEdtSearch.getText().toString();
                BmobQuery<CourseBean> query = new BmobQuery<>();
                query.findObjects(new FindListener<CourseBean>() {
                    @Override
                    public void done(List<CourseBean> list, BmobException e) {
                        if (e == null) {
                            List<CourseBean> list1 = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getName().contains(search)) {
                                    list1.add(list.get(i));
                                }
                            }
                            if (mCourseAdapter != null) {
                                mCourseAdapter.setNewData(list1);
                            }

                        } else {
                            ToastUtil.showMessage(e.getMessage());
                        }
                    }
                });
            }
        });

    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        //设置网格布局，每行5个
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4) {
            //去除recyclerView的滑动
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView1(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void loadData() {
        if (role.equals("0")) {
            String[] courses = {"计算机", "外语", "理学", "工学", "经济管理", "心理学", "文史学", "医药卫生"};
            int[] icon = {R.drawable.ic_home_daily_welfare, R.drawable.ic_home_hotel, R.drawable.ic_home_delicious_food, R.drawable.ic_home_entertainment,
                    R.drawable.ic_home_haircut, R.drawable.ic_home_market, R.drawable.ic_home_must_eat, R.drawable.ic_home_movice};
            List<IconBean> list = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                IconBean bean = new IconBean(icon[i], courses[i]);
                list.add(bean);
            }
            mIconAdapter.setNewData(list);
        }

        BmobQuery<CourseBean> query = new BmobQuery<>();
        query.findObjects(new FindListener<CourseBean>() {
            @Override
            public void done(List<CourseBean> list, BmobException e) {
                if (mCourseAdapter != null) {
                    mCourseAdapter.setNewData(list);
                }
            }
        });
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
