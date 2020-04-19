package com.csu.teachingmanagement.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.Exam;
import com.csu.teachingmanagement.bean.Grade;
import com.csu.teachingmanagement.bean.Question;
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
 * @Date 2020-04-10 10:16
 * 功能：
 */
public class QuestionActivity extends BaseActivity {

    @BindView(R.id.ques_question)
    TextView mTvQuestion;
    @BindView(R.id.btn_previous)
    Button mBtnPrevious;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.ques_radiogroup)
    RadioGroup mRgQuestion;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.edt_answer1)
    EditText mEdtAnswer1;

    private RadioButton[] radioButtons;
    private int currentIndex = 0;
    private Exam exam;
    private String userName;
    private String name;
    private int correct = 0;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int getLayout() {
        return R.layout.activity_question;
    }

    @Override
    public void onBindView() {
        userName = AccountHelper.getLoginInfo(getApplication(), "userName");
        name = AccountHelper.getLoginInfo(getApplication(), "name");

        exam = (Exam) getIntent().getSerializableExtra("exam");
        if (exam != null) {
            mTvName.setText(exam.getExamName());
        }
        initView();

    }

    private void initView() {
        radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.ques_an1);
        radioButtons[1] = findViewById(R.id.ques_an2);
        radioButtons[2] = findViewById(R.id.ques_an3);
        radioButtons[3] = findViewById(R.id.ques_an4);


        BmobQuery<Question> query = new BmobQuery<>();
        query.addWhereEqualTo("courseId", exam.getObjectId());
        query.order("type");
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        setQuestionToGadioGroup(currentIndex, list.get(0));
                        addListener(list);
                    }
                } else {
                    ToastUtil.showMessage("没有数据");
                }
            }
        });


    }

    private void setQuestionToGadioGroup(int currentIndex, Question ques) {
        if (ques.getType().equals("0")) {
            radioButtons[0].setText("A、 " + ques.getAnswerA());
            radioButtons[1].setText("B、 " + ques.getAnswerB());
            radioButtons[2].setText("C、 " + ques.getAnswerC());
            radioButtons[3].setText("D、 " + ques.getAnswerD());
            mTvQuestion.setText("第" + (currentIndex + 1) + "题  " + ques.getQuestion());
        } else {
            mEdtAnswer1.setVisibility(View.VISIBLE);
            mRgQuestion.setVisibility(View.GONE);
            mTvQuestion.setText("第" + (currentIndex + 1) + "题  " + ques.getQuestion());
        }

    }

    private void addListener(List<Question> list) {

        // 下一题
        mBtnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (currentIndex < list.size() - 1) {
                    currentIndex++;

                    Question ques = list.get(currentIndex);
                    setQuestionToGadioGroup(currentIndex, ques);

                    mRgQuestion.clearCheck();
                    if (ques.getSelectedId() != -1&&ques.getType().equals("0")) {
                        radioButtons[ques.getSelectedId()].setChecked(true);
                    }else {
                        ques.setAnswer(mEdtAnswer1.getText().toString());
                    }
                } else if (currentIndex == list.size() - 1) { // 最后一题

                    if (list.get(list.size()-1).getSelectedId() != -1&&list.get(list.size()-1).getType().equals("0")) {
                        radioButtons[list.get(list.size()-1).getSelectedId()].setChecked(true);
                    }else {
                        list.get(list.size()-1).setAnswer(mEdtAnswer1.getText().toString());
                    }

                    new AlertDialog.Builder(QuestionActivity.this).setTitle("提示").setMessage("已经到最后一题，是否提交答案？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getRight().equals(list.get(i).getSelectedId() + "")) {
                                            correct++;
                                        }
                                        if (list.get(i).getRight().equals(list.get(i).getAnswer())){
                                            correct++;
                                        }
                                    }
                                    Grade grade = new Grade();
                                    grade.setExamName(exam.getExamName());
                                    grade.setUserId(userName);
                                    grade.setUserName(name);
                                    grade.setExamId(exam.getObjectId());
                                    grade.setGrade(100 * correct / list.size());
                                    grade.setDay(df.format(new Date()));
                                    grade.setCourseId(exam.getCourseId());
                                    grade.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                ToastUtil.showMessage("完成测评");
                                                QuestionActivity.this.finish();
                                            } else {
                                                ToastUtil.showMessage("保存考试信息出错");
                                            }
                                        }
                                    });
                                }
                            }).setNegativeButton("取消", null).show();
                }
            }
        });

        // 上一题
        mBtnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {

                    currentIndex--;
                    Question ques = list.get(currentIndex);
                    setQuestionToGadioGroup(currentIndex, ques);

                    mRgQuestion.clearCheck();

                    if (ques.getSelectedId() != -1&&ques.getType().equals("0")) {
                        radioButtons[ques.getSelectedId()].setChecked(true);
                    }else {
                        mEdtAnswer1.setText(ques.getAnswer());
                    }

                }
            }
        });

        mRgQuestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (radioButtons[i].isChecked()) {
                        list.get(currentIndex).setSelectedId(i);
                    }
                }
            }
        });
    }
}
