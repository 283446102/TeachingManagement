package com.csu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @author
 * @Date 2020-04-09 18:18
 * 功能：
 */
public class MyViewPager extends ViewPager {
    private boolean willIntercept= true;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(willIntercept){
            return super.onInterceptTouchEvent(arg0);
        }else{
            return false;
        }

    }

    public void setTouchIntercept(boolean value){
        willIntercept = value;
    }
}
