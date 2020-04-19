package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.StudentSignInBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-11 11:03
 * 功能：
 */
public class StudentSignInAdapter extends BaseQuickAdapter<StudentSignInBean, BaseViewHolder> {
    public StudentSignInAdapter(@Nullable List<StudentSignInBean> data) {
        super(R.layout.recycler_view_item_sign_record, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StudentSignInBean item) {
        holder.setText(R.id.tv_name,"学生名字："+item.getName());
        holder.setText(R.id.tv_sign_time,"签到时间："+item.getDay()+" "+getTimes(item.getSignTime()));
    }

    private String getTimes(String time) {
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
}
