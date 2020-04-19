package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.Grade;

import java.util.List;

/**
 * @author
 * @Date 2020-04-11 19:24
 * 功能：
 */
public class GradeAdapter extends BaseQuickAdapter<Grade, BaseViewHolder> {
    public GradeAdapter(@Nullable List<Grade> data) {
        super(R.layout.recycler_view_item_grade, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Grade item) {
        holder.setText(R.id.tv_name, "姓名："+item.getUserName());
        holder.setText(R.id.tv_exam, "测评名称："+item.getExamName());
        holder.setText(R.id.tv_grade, "分数："+item.getGrade());
        holder.setText(R.id.tv_day, item.getDay());

    }
}
