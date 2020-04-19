package com.csu.teachingmanagement.course;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.MyCourseAdapter;
import com.csu.teachingmanagement.adapter.StudentSignInAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.SignInBean;
import com.csu.teachingmanagement.bean.StudentSignInBean;
import com.csu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-11 12:31
 * 功能：
 */
public class MyStudentActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private SignInBean bean;

    private StudentSignInAdapter mAdapter =null;

    @Override
    public int getLayout() {
        return R.layout.activity_my_student;
    }

    @Override
    public void onBindView() {
        bean = (SignInBean) getIntent().getExtras().getSerializable("sign");
        initView();
        loadData();
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        mAdapter = new StudentSignInAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
    }

    private void loadData() {
        BmobQuery<StudentSignInBean> query = new BmobQuery<>();
        query.addWhereEqualTo("courseId",bean.getCourseId());
        query.findObjects(new FindListener<StudentSignInBean>() {
            @Override
            public void done(List<StudentSignInBean> object, BmobException e) {
                if (e==null){
                    List<StudentSignInBean> list = new ArrayList<>();
                    for (int i=0;i<object.size();i++){
                        if (object.get(i).getDay().equals(bean.getDay())){
                            list.add(object.get(i));
                        }
                    }
                    if (mAdapter!=null){
                        mAdapter.setNewData(list);
                    }
                }else {
                    ToastUtil.showMessage(e.getMessage());
                }
            }
        });

    }
}
