package com.csu.teachingmanagement.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.AccountHelper;

import java.util.List;

/**
 * @author
 * @Date 2020-04-09 21:10
 * 功能：
 */
public class ThemeAdapter extends BaseQuickAdapter<ThemeBean, BaseViewHolder> {
    public ThemeAdapter(@Nullable List<ThemeBean> data) {
        super(R.layout.recycler_view_item_theme, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ThemeBean item) {
        String role = item.getIsTeacher();
        if (role.equals("0")){
            holder.findView(R.id.tv_is_teacher).setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_name,item.getName());
        holder.setText(R.id.tv_discuss,item.getDiscuss());
        holder.setText(R.id.tv_discuss_time,item.getDiscussTime());
    }
}
