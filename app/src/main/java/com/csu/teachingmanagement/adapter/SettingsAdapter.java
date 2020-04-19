package com.csu.teachingmanagement.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.SettingBean;


import java.util.List;

/**
 * @Date 2020-04-09 13:21
 * 功能：
 */
public class SettingsAdapter extends BaseQuickAdapter<SettingBean, BaseViewHolder> {
    public SettingsAdapter(@NonNull List<SettingBean> data) {
        super(R.layout.recycle_view_item_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SettingBean item) {
        holder.setText(R.id.tv_item_name,item.getName());
    }
}
