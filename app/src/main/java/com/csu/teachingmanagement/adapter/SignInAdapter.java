package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.SignInBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-11 9:09
 * 功能：
 */
public class SignInAdapter extends BaseQuickAdapter<SignInBean, BaseViewHolder> {
    public SignInAdapter(@Nullable List<SignInBean> data) {
        super(R.layout.recycler_view_item_sign_record, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SignInBean item) {
        holder.setText(R.id.tv_name, "老师名字： " + item.getName());
        holder.setText(R.id.tv_sign_time, "课程签到时间段： "+item.getDay()+" "+getTimes(item.getStartTime())+"-"+getTimes(item.getEndTime()));
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
