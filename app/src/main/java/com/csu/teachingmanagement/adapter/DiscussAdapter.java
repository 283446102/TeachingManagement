package com.csu.teachingmanagement.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * @author
 * @Date 2020-04-09 20:34
 * 功能：
 */
public class DiscussAdapter extends BaseQuickAdapter<DiscussBean, BaseViewHolder> {
    public DiscussAdapter(@Nullable List<DiscussBean> data) {
        super(R.layout.recycler_view_item_discuss, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DiscussBean item) {
        if (item.getFile() != null) {
            TextView textView = holder.findView(R.id.tv_file_name);
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            textView.setText(item.getFile().getFilename());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"正在下载中...",Toast.LENGTH_LONG).show();
                }
            });
        }else {
            holder.findView(R.id.tv_file_name).setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_discuss, item.getDiscuss());
        holder.setText(R.id.tv_discuss_time, item.getDay());
        holder.setText(R.id.tv_course_name, item.getCourseName());
    }
}
