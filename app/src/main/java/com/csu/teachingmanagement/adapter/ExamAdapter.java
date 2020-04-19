package com.csu.teachingmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.Exam;
import com.csu.teachingmanagement.bean.Grade;
import com.csu.teachingmanagement.bean.Question;
import com.csu.teachingmanagement.course.QuestionActivity;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @Date 2020-04-10 9:48
 * 功能：
 */
public class ExamAdapter extends BaseQuickAdapter<Exam, BaseViewHolder> {

    public ExamAdapter(@Nullable List<Exam> data) {
        super(R.layout.recycler_view_item_exam, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Exam item) {
        String role = AccountHelper.getLoginInfo(getContext(), "role");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String today = df.format(new Date());

        BmobQuery<Question> query = new BmobQuery<>();
        query.addWhereEqualTo("courseId", item.getObjectId());
        query.count(Question.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    holder.setText(R.id.tv_num, "题目:" + count + "道题");
                }
            }
        });


        if (role.equals("0")) {
            if (Integer.parseInt(today) > Integer.parseInt(getTime(item.getEndTime()))) {
                TextView textView = holder.findView(R.id.tv_into_exam);
                textView.setText("关闭测试");
                textView.setEnabled(false);
            }
            BmobQuery<Grade> query1 = new BmobQuery<>();
            query1.addWhereEqualTo("examId", item.getObjectId());
            query1.order("-grade");
            query1.findObjects(new FindListener<Grade>() {
                @Override
                public void done(List<Grade> object, BmobException e) {
                    if (e == null) {
                        if (object.size() == 0) {
                            holder.findView(R.id.tv_history).setVisibility(View.GONE);
                        } else if (object.size()<3){
                            holder.findView(R.id.tv_history).setVisibility(View.VISIBLE);
                            holder.setText(R.id.tv_into_exam,"再次测验");
                            holder.setText(R.id.tv_history, "历史最高分 " + object.get(0).getGrade());
                        }else {
                            holder.setText(R.id.tv_into_exam,"关闭测试");
                            holder.findView(R.id.tv_into_exam).setEnabled(false);
                        }
                    }
                }
            });


        } else {
            holder.findView(R.id.tv_history).setVisibility(View.GONE);
            holder.findView(R.id.tv_into_exam).setVisibility(View.GONE);
        }


        holder.setText(R.id.tv_exam_name, "试题名称：" + item.getExamName());
        holder.setText(R.id.tv_day, "截止时间  " + item.getEndTime());

        holder.findView(R.id.tv_into_exam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("exam", item);
                Intent intent = new Intent(getContext(), QuestionActivity.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
    }

    private String getTime(String time) {
        String day = "";
        for (int i = 0; i < time.length(); i++) {
            char ch = time.charAt(i);
            if (ch != '-') {
                day += ch;
            }
        }
        return day;
    }
}
