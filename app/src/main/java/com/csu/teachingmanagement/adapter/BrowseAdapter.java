package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.BrowseBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-10 13:06
 * 功能：
 */
public class BrowseAdapter extends BaseQuickAdapter<BrowseBean, BaseViewHolder> {
    public BrowseAdapter(@Nullable List<BrowseBean> data) {
        super(R.layout.recycler_view_item_browse, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BrowseBean item) {
        holder.setText(R.id.tv_day,item.getDay());
        holder.setText(R.id.tv_course_name,item.getCourse());

    }
}
