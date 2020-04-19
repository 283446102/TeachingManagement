package com.csu.teachingmanagement.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.utils.AccountHelper;

import java.util.List;

/**
 * @author
 * @Date 2020-04-09 11:07
 * 功能：
 */
public class HomeCourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {


    private int [] icon = {R.drawable.ic_home_daily_welfare,R.drawable.ic_home_hotel,R.drawable.ic_home_delicious_food,R.drawable.ic_home_entertainment,
            R.drawable.ic_home_haircut,R.drawable.ic_home_market,R.drawable.ic_home_must_eat,R.drawable.ic_home_movice};

    public HomeCourseAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.recycler_view_item_course_home, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean item) {
        if (item.getTeacher().equals(AccountHelper.getLoginInfo(getContext(),"userName"))){
            holder.findView(R.id.tv_is_my).setVisibility(View.VISIBLE);
        }
        holder.setImageResource(R.id.iv_icon,icon[item.getType()]);
        holder.setText(R.id.tv_course_name,item.getName());
    }
}
