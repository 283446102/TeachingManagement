package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.MyCourseBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-10 12:48
 * 功能：
 */
public class StudentAdapter extends BaseQuickAdapter<MyCourseBean, BaseViewHolder> {
    public StudentAdapter(@Nullable List<MyCourseBean> data) {
        super(R.layout.recycler_view_item_student, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyCourseBean item) {
        holder.setText(R.id.tv_name,item.getStudentName());
        holder.setText(R.id.tv_userId,item.getStudent());
    }
}
