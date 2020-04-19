package com.csu.teachingmanagement.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csu.base.BaseFragment;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.adapter.SignInAdapter;
import com.csu.teachingmanagement.adapter.StudentRecordAdapter;
import com.csu.teachingmanagement.adapter.StudentSignInAdapter;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.MyCourseBean;
import com.csu.teachingmanagement.bean.SignInBean;
import com.csu.teachingmanagement.bean.StudentRecordBean;
import com.csu.teachingmanagement.bean.StudentSignInBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.L;
import com.csu.utils.ToastUtil;
import com.csu.view.MyViewPager;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author
 * @Date 2020-04-09 15:42
 * 功能：签到
 */
public class SignInFragment extends BaseFragment {


    @BindView(R.id.pattern_locker_view)
    PatternLockerView mPatternLockerView;
    @BindView(R.id.ll_sign_in)
    LinearLayout mLlSignIn;
    @BindView(R.id.ll_sign_record)
    LinearLayout mLlSignRecord;
    @BindView(R.id.tv_sign_time)
    TextView mTvSignTime;
    @BindView(R.id.ll_time)
    LinearLayout mLlTime;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_course_name)
    TextView mTvCourseName;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_data)
    LinearLayout mLlData;
    @BindView(R.id.ll_record)
    LinearLayout mLlRecord;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView1;

    private MyViewPager viewPager;
    private SimpleDateFormat df = new SimpleDateFormat("HHmm");//设置日期格式时分
    private SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式年月日
    private String today = df1.format(new Date());
    private String role;
    private CourseBean bean;
    private MyCourseBean bean1;
    private TimePickerView pvTime;
    private SignInBean sign;
    private boolean flag = true;
    private String startTime = "";
    private String endTime = "";
    private String courseId = "";
    private String key = "";

    private SignInAdapter mAdapter = null;
    private StudentSignInAdapter adapter = null;
    private StudentRecordAdapter adapter1 = null;
    private List<StudentRecordBean> beanList;
    private HashMap<String, Integer> hashMap;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_sign_in;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        role = AccountHelper.getLoginInfo(getContext(), "role");
        initView();
        if (role.equals("0")) {
            //学生
            mLlTime.setVisibility(View.GONE);
            if (getActivity() != null) {
                bean1 = (MyCourseBean) getActivity().getIntent().getSerializableExtra("course");
            }
            if (bean1 != null) {
                mTvCourseName.setText(bean1.getName());
                courseId = bean1.getCourseId();
            }

            //学生签到情况
            showStudent();

        } else {
            hashMap = new HashMap<>();
            beanList = new ArrayList<>();
            mTvSignTime.setVisibility(View.GONE);
            if (getActivity() != null) {
                bean = (CourseBean) getActivity().getIntent().getSerializableExtra("course");
            }
            if (bean != null) {
                mTvCourseName.setText(bean.getName());
                courseId = bean.getObjectId();
            }
            mTvStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = true;
                    pvTime.show();
                }
            });
            //老师设置的签到记录
            showTeacher();

            mTvEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (startTime.equals("")) {
                        ToastUtil.showMessage("请先设置开始时间");
                    } else {
                        flag = false;
                        pvTime.show();
                    }
                }
            });
        }


        mLlSignIn.setVisibility(View.VISIBLE);
        viewPager = getActivity().findViewById(R.id.vpager);
        mPatternLockerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewPager.setTouchIntercept(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        viewPager.setTouchIntercept(true);
                        break;
                }
                return false;
            }
        });

        mPatternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView patternLockerView) {

            }

            @Override
            public void onChange(PatternLockerView patternLockerView, List<Integer> list) {

            }

            @Override
            public void onComplete(PatternLockerView patternLockerView, List<Integer> list) {
                String s = "";
                for (int i = 0; i < list.size(); i++) {
                    s += list.get(i);
                }
                if (role.equals("0")) {
                    String currentTime = df.format(new Date());
                    if (Integer.parseInt(currentTime) < Integer.parseInt(sign.getStartTime())) {
                        ToastUtil.showMessage("未到签到时间");
                    } else if (Integer.parseInt(currentTime) > Integer.parseInt(sign.getEndTime())) {
                        ToastUtil.showMessage("签到时间已过");
                        getStuRecord();

                    } else {
                        if (s.equals(sign.getKey())) {
                            StudentSignInBean bean = new StudentSignInBean();
                            bean.setCourseId(sign.getCourseId());
                            bean.setDay(today);
                            bean.setName(bean1.getStudentName());
                            bean.setSignTime(currentTime);
                            bean.setStudent(bean1.getStudent());
                            bean.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        getStuRecord();
                                        ToastUtil.showMessage("签到成功");
                                    } else {
                                        ToastUtil.showMessage(e.getMessage());
                                    }
                                }
                            });
                        } else {
                            ToastUtil.showMessage("签到手势不正确，请联系老师获取签到手势密码");
                        }
                    }
                } else {//老师设置签到时间
                    if (startTime.equals("") || endTime.equals("")) {
                        ToastUtil.showMessage("请先设置签到时间段");
                        return;
                    }
                    if (list.size() < 5) {
                        ToastUtil.showMessage("至少连接6个点");
                        return;
                    }

                    sign = new SignInBean();
                    sign.setCourseId(bean.getObjectId());
                    sign.setName(bean.getTeacherName());
                    sign.setDay(today);
                    sign.setStartTime(startTime);
                    sign.setEndTime(endTime);
                    sign.setKey(s);
                    sign.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                showTeacher();
                                loadData();
                                mLlRecord.setVisibility(View.VISIBLE);
                                mLlSignIn.setVisibility(View.GONE);
                                mLlSignRecord.setVisibility(View.VISIBLE);
                                ToastUtil.showMessage("手势签到密码设置成功!");
                            } else {
                                ToastUtil.showMessage("手势签到密码设置失败,错误码：" + e.getErrorCode());
                            }
                        }
                    });
                }
            }

            @Override
            public void onClear(PatternLockerView patternLockerView) {
            }
        });
    }

    private void loadData() {
        if (role.equals("1")) {
            BmobQuery<StudentSignInBean> query1 = new BmobQuery<>();
            query1.addWhereEqualTo("courseId",bean.getObjectId());
            query1.findObjects(new FindListener<StudentSignInBean>() {
                @Override
                public void done(List<StudentSignInBean> object, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < object.size(); i++) {
                            Integer count = hashMap.get(object.get(i).getStudent());
                            if (count == null) {
                                count = 1;
                            } else {
                                count++;
                            }
                            hashMap.put(object.get(i).getStudent(), count);
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
                            beanList.get(i).setCount("签到次数：" + beanList.get(i).getCount());
                        }
                        if (adapter1 != null) {
                            adapter1.setNewData(beanList);
                        }
                    }
                }
            });
        }
    }


    private void initView() {

        adapter1 = new StudentRecordAdapter(null);
        if (role.equals("1")) {
            initRecyclerView(mRecyclerView1, adapter1);
        }

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                if (flag) {
                    startTime = df.format(date);
                    mTvStartTime.setText("开始时间：" + getTimes(date));
                } else {
                    endTime = df.format(date);
                    if (Integer.parseInt(startTime) > Integer.parseInt(endTime)) {
                        ToastUtil.showMessage("开始时间不能大于结束时间，请重新设置");
                    } else {
                        mTvEndTime.setText("结束时间：" + getTimes(date));
                    }
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "时", "分", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    private String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    private void initStuAdapter() {
        adapter = new StudentSignInAdapter(null);
        initRecyclerView(mRecyclerView, adapter);
    }

    private void initAdapter() {
        mAdapter = new SignInAdapter(null);
        initRecyclerView(mRecyclerView, mAdapter);
    }

    private void initRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    //显示学生签到记录
    private void showStudent() {

        BmobQuery<StudentSignInBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("courseId", courseId);
        query1.findObjects(new FindListener<StudentSignInBean>() {
            @Override
            public void done(List<StudentSignInBean> object, BmobException e) {
                boolean flag2 = false;
                //先查询今天学生有没有签到记录
                for (int i = 0; i < object.size(); i++) {
                    if (object.get(i).getDay().equals(today)) {
                        flag2 = true;
                        break;
                    }
                }
                if (flag2) {//如果已经签到，则去看签到记录表
                    getStuRecord();

                } else {//如果没有查询到签到记录，则查询今天老师是否设置了签到
                    BmobQuery<SignInBean> query = new BmobQuery<>();
                    query.addWhereEqualTo("courseId", courseId);
                    query.findObjects(new FindListener<SignInBean>() {
                        @Override
                        public void done(List<SignInBean> list, BmobException e) {
                            boolean flag = false;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getDay().equals(today)) {
                                    mTvSignTime.setText("签到时间段： " + getTime(list.get(i).getStartTime()) + "-" + getTime(list.get(i).getEndTime()));
                                    sign = list.get(i);
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                getStuRecord();
                                //如果没有查询到老师今天设置的签到，则去看签到记录
                            }
                        }
                    });
                }
            }
        });


    }

    //显示老师设置签到记录
    private void showTeacher() {
        BmobQuery<SignInBean> query = new BmobQuery<>();
        query.addWhereEqualTo("courseId", courseId);
        query.findObjects(new FindListener<SignInBean>() {
            @Override
            public void done(List<SignInBean> list, BmobException e) {
                if (e == null) {
                    boolean isRecord = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getDay().equals(today)) {
                            isRecord = true;
                            break;
                        }
                    }
                    if (isRecord) {
                        loadData();
                        mLlRecord.setVisibility(View.VISIBLE);
                        mLlSignIn.setVisibility(View.GONE);
                        mLlSignRecord.setVisibility(View.VISIBLE);
                        //显示签到记录
                        initAdapter();
                        if (mAdapter != null) {
                            mAdapter.setNewData(list);
                            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    SignInBean bean = (SignInBean) adapter.getData().get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("sign", bean);
                                    Intent intent = new Intent(getContext(), MyStudentActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                } else {
                    ToastUtil.showMessage(e.getMessage());
                }
            }
        });

    }

    private String getTime(String time) {
        String day = "";
        for (int i = 0; i < time.length(); i++) {
            if (i == 2) {
                day += ":" + time.charAt(i);
            } else {
                day += time.charAt(i);
            }
        }
        return day;
    }

    private void getStuRecord() {
        initStuAdapter();
        BmobQuery<StudentSignInBean> query = new BmobQuery<>();
        query.addWhereEqualTo("student", AccountHelper.getLoginInfo(getContext(), "userName"));
        query.findObjects(new FindListener<StudentSignInBean>() {
            @Override
            public void done(List<StudentSignInBean> object, BmobException e) {
                List<StudentSignInBean> data = new ArrayList<>();
                for (int i = 0; i < object.size(); i++) {
                    if (object.get(i).getCourseId().equals(courseId)) {
                        data.add(object.get(i));
                    }
                }
                if (adapter != null) {
                    adapter.setNewData(data);
                }
                if (data.size() == 0) {
                    mLlData.setVisibility(View.VISIBLE);
                    mLlSignIn.setVisibility(View.GONE);
                    mLlSignRecord.setVisibility(View.GONE);
                } else {
                    mLlSignIn.setVisibility(View.GONE);
                    mLlSignRecord.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
