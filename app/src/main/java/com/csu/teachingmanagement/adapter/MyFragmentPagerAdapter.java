package com.csu.teachingmanagement.adapter;

import android.view.ViewGroup;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.csu.teachingmanagement.course.DiscussFragment;
import com.csu.teachingmanagement.course.SignInFragment;
import com.csu.teachingmanagement.course.StudyActivity;
import com.csu.teachingmanagement.course.TestFragment;

/**
 * @author
 * @Date 2020-04-09 15:53
 * 功能：
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private SignInFragment fragment1 = null;
    private DiscussFragment fragment2 = null;
    private TestFragment fragment3 = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = SignInFragment.newInstance();
        fragment2 = DiscussFragment.newInstance();
        fragment3 = TestFragment.newInstance();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case StudyActivity.PAGE_ONE:
                fragment = fragment1;
                break;
            case StudyActivity.PAGE_TWO:
                fragment = fragment2;
                break;
            case StudyActivity.PAGE_THREE:
                fragment = fragment3;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
