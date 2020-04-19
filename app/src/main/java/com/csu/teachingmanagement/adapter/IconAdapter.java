package com.csu.teachingmanagement.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.IconBean;

import java.util.List;

/**
 * @author
 * @Date 2020-04-09 10:41
 * 功能：
 */
public class IconAdapter extends BaseQuickAdapter<IconBean, BaseViewHolder> {
    public IconAdapter(@Nullable List<IconBean> data) {
        super(R.layout.recycler_view_item_icon, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, IconBean item) {
        holder.setImageResource(R.id.iv_icon,item.getIcon());
        holder.setText(R.id.tv_icon_name,item.getName());
    }
}
