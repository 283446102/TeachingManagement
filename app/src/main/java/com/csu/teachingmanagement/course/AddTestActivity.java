package com.csu.teachingmanagement.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.Exam;
import com.csu.teachingmanagement.bean.Question;
import com.csu.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author
 * @Date 2020-04-13 16:32
 * 功能：
 */
public class AddTestActivity extends BaseActivity {

    @BindView(R.id.ll_add_test)
    LinearLayout mLlAddTest;
    @BindView(R.id.ll_add_question)
    LinearLayout mLlAddQuestion;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    @BindView(R.id.edt_test)
    EditText mEdtTest;
    @BindView(R.id.edt_question)
    EditText mEdtQuestion;
    @BindView(R.id.tv_next_item)
    TextView mTvNextItem;
    @BindView(R.id.tv_completed)
    TextView mTvCompleted;
    @BindView(R.id.edt_q1)
    EditText mEdtQ1;
    @BindView(R.id.edt_q2)
    EditText mEdtQ2;
    @BindView(R.id.edt_q3)
    EditText mEdtQ3;
    @BindView(R.id.edt_q4)
    EditText mEdtQ4;
    @BindView(R.id.edt_q5)
    EditText mEdtQ5;
    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_question_index)
    TextView mTvQuestionIndex;
    @BindView(R.id.ll_add_question1)
    LinearLayout mLlAddQuestion1;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.edt_question1)
    EditText mEdtQuestion1;
    @BindView(R.id.edt_answer)
    EditText mEdtAnswer;
    @BindView(R.id.tv_next_item1)
    TextView mTvNextItem1;
    @BindView(R.id.tv_completed1)
    TextView mTvCompleted1;
    @BindView(R.id.tv_question_index1)
    TextView mTvQuestionIndex1;



    private TimePickerView pvTime;
    private boolean getTime = true;
    private String courseId = "";
    private List<Question> list;
    private String questionId = "";
    private int index = 0;
    private String endTime = "";

    @Override
    public int getLayout() {
        return R.layout.activity_add_test;
    }

    @Override
    public void onBindView() {
        list = new ArrayList<>();
        initDate();
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            courseId = bundle.getString("courseId");
        }
        mTvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        //创建试题
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtTest.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入试题名称");
                    return;
                }
                if (getTime) {
                    ToastUtil.showMessage("请设置测评截止时间");
                    return;
                }
                Exam exam = new Exam();
                exam.setEndTime(endTime);
                exam.setExamName(mEdtTest.getText().toString());
                exam.setCourseId(courseId);
                exam.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            questionId = exam.getObjectId();
                            mLlAddTest.setVisibility(View.GONE);
                            mLlAddQuestion.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtil.showMessage("创建试题失败" + e.getMessage());
                        }
                    }
                });

            }
        });

        //输入填空题
        mTvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRight()) {
                    Question bean = new Question();
                    bean.setQuestion(mEdtQuestion.getText().toString());
                    bean.setCourseId(questionId);
                    bean.setType("0");
                    bean.setAnswerA(mEdtQ1.getText().toString());
                    bean.setAnswerB(mEdtQ2.getText().toString());
                    bean.setAnswerC(mEdtQ3.getText().toString());
                    bean.setAnswerD(mEdtQ4.getText().toString());
                    bean.setRight(getRight(mEdtQ5.getText().toString()));
                    list.add(bean);
                    index++;
                }
                mLlAddQuestion.setVisibility(View.GONE);
                mLlAddQuestion1.setVisibility(View.VISIBLE);
            }
        });

        //创建填空题
        mTvNextItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtQuestion1.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入题目");
                    return;
                }
                if (mEdtAnswer.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入答案");
                    return;
                }
                Question bean = new Question();
                bean.setQuestion(mEdtQuestion.getText().toString());
                bean.setCourseId(questionId);
                bean.setType("1");
                bean.setRight(mEdtAnswer.getText().toString());
                list.add(bean);
                index++;
                mTvQuestionIndex1.setText("第" + (index + 1) + "题");
                mEdtQuestion1.setText("");
                mEdtAnswer.setText("");
            }
        });

        //创建题目
        mTvNextItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtQuestion.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入题目");
                    return;
                }
                if (mEdtQ1.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入选项A");
                    return;
                }
                if (mEdtQ2.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入选项B");
                    return;
                }
                if (mEdtQ3.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入选项B");
                    return;
                }
                if (mEdtQ4.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入选项D");
                    return;
                }
                if (mEdtQ5.getText().toString().equals("")) {
                    ToastUtil.showMessage("请输入答案");
                    return;
                }
                if (mEdtQ5.getText().toString().trim().length() != 1) {
                    ToastUtil.showMessage("你输入的答案不符合要求，请输入单个字母");
                    return;
                }

                if (getRight(mEdtQ5.getText().toString().trim()).equals("")) {
                    ToastUtil.showMessage("你输入的答案不符合要求，请输入abcdABDC中的字母");
                    return;
                }

                if (questionId.equals("")) {
                    ToastUtil.showMessage("网络环境不好，请稍等一会再进行下一步操作");
                    return;
                }

                Question bean = new Question();
                bean.setQuestion(mEdtQuestion.getText().toString());
                bean.setCourseId(questionId);
                bean.setType("0");
                bean.setAnswerA(mEdtQ1.getText().toString());
                bean.setAnswerB(mEdtQ2.getText().toString());
                bean.setAnswerC(mEdtQ3.getText().toString());
                bean.setAnswerD(mEdtQ4.getText().toString());
                bean.setRight(getRight(mEdtQ5.getText().toString()));
                list.add(bean);
                index++;
                mTvQuestionIndex.setText("第" + (index + 1) + "题");

                mEdtQuestion.setText("");
                mEdtQ1.setText("");
                mEdtQ2.setText("");
                mEdtQ3.setText("");
                mEdtQ4.setText("");
                mEdtQ5.setText("");

            }
        });

        //提交题目
        mTvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 0) {
                    if (!isRight()) {
                        Question bean = new Question();
                        bean.setQuestion(mEdtQuestion.getText().toString());
                        bean.setCourseId(questionId);
                        bean.setType("0");
                        bean.setAnswerA(mEdtQ1.getText().toString());
                        bean.setAnswerB(mEdtQ2.getText().toString());
                        bean.setAnswerC(mEdtQ3.getText().toString());
                        bean.setAnswerD(mEdtQ4.getText().toString());
                        bean.setRight(getRight(mEdtQ5.getText().toString()));
                        list.add(bean);

                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    } else {
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("特别注意！！！").setMessage("您还没有完成编辑题目，是否继续提交？如现在提交则不会生成试卷！！！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Exam exam = new Exam();
                                        exam.setObjectId(questionId);
                                        exam.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    finish();
                                                } else {
                                                    ToastUtil.showMessage("未完成试卷清除失败" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                } else {
                    if (isRight()) {
                        ToastUtil.showMessage("本题未完成编辑");
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    } else {
                        Question bean = new Question();
                        bean.setType("0");
                        bean.setQuestion(mEdtQuestion.getText().toString());
                        bean.setCourseId(questionId);
                        bean.setAnswerA(mEdtQ1.getText().toString());
                        bean.setAnswerB(mEdtQ2.getText().toString());
                        bean.setAnswerC(mEdtQ3.getText().toString());
                        bean.setAnswerD(mEdtQ4.getText().toString());
                        bean.setRight(getRight(mEdtQ5.getText().toString()));
                        list.add(bean);
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }
            }
        });


        //提交填空题题目
        mTvCompleted1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 0) {
                    if (!isRight1()) {
                        Question bean = new Question();
                        bean.setQuestion(mEdtQuestion1.getText().toString());
                        bean.setCourseId(questionId);
                        bean.setType("1");
                        bean.setRight(mEdtAnswer.getText().toString());
                        list.add(bean);

                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    } else {
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("特别注意！！！").setMessage("您还没有完成编辑题目，是否继续提交？如现在提交则不会生成试卷！！！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Exam exam = new Exam();
                                        exam.setObjectId(questionId);
                                        exam.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    finish();
                                                } else {
                                                    ToastUtil.showMessage("未完成试卷清除失败" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                } else {
                    if (isRight1()) {
                        ToastUtil.showMessage("本题未完成编辑");
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    } else {
                        Question bean = new Question();
                        bean.setQuestion(mEdtQuestion1.getText().toString());
                        bean.setCourseId(questionId);
                        bean.setType("1");
                        bean.setRight(mEdtAnswer.getText().toString());
                        list.add(bean);
                        new AlertDialog.Builder(AddTestActivity.this).setTitle("提示").setMessage("已经完成试卷编辑，是否提交？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < list.size(); i++) {
                                            list.get(i).save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                        }
                                        finish();
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }
            }
        });
    }


    private void initDate() {

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                mTvDay.setText("测评截止时间:" + getTimes(date));
                endTime = getTimes(date);
                getTime = false;
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    private String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private String getRight(String s) {
        String result = "";
        if (s.charAt(0) == 'a' || s.charAt(0) == 'A') {
            result = "0";
        }
        if (s.charAt(0) == 'b' || s.charAt(0) == 'B') {
            result = "1";
        }
        if (s.charAt(0) == 'c' || s.charAt(0) == 'C') {
            result = "2";
        }
        if (s.charAt(0) == 'd' || s.charAt(0) == 'D') {
            result = "3";
        }

        return result;
    }

    private boolean isRight() {
        if (mEdtQuestion.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ1.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ2.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ3.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ4.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ5.getText().toString().equals("")) {
            return true;
        }
        if (mEdtQ5.getText().toString().trim().length() != 1) {
            return true;
        }
        if (getRight(mEdtQ5.getText().toString().trim()).equals("")) {
            return true;
        }
        return false;
    }

    private boolean isRight1() {
        if (mEdtQuestion1.getText().toString().equals("")) {
            return true;
        }
        if (mEdtAnswer.getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }
}
