package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.StudentRecordBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-17 10:04
 * 功能：
 */
public class StudentRecordAdapter extends BaseQuickAdapter<StudentRecordBean, BaseViewHolder> {
    private int index = 1;
    public StudentRecordAdapter(@Nullable List<StudentRecordBean> data) {
        super(R.layout.recycler_view_item_student_record, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StudentRecordBean item) {
        holder.setText(R.id.tv_index,index+"");
        index++;
        holder.setText(R.id.tv_name,item.getName());
        holder.setText(R.id.tv_record_num,item.getCount());
    }
}
