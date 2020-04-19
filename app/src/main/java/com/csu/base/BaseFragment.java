package com.csu.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {
    private View mRootView;
    public abstract Object setLayout();
    protected View setLayout(@NonNull LayoutInflater inflater, @NonNull View container){
        return null;
    }
    public abstract void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            if (setLayout() instanceof Integer) {
                mRootView = inflater.inflate((int) setLayout(), container, false);
            } else if (setLayout() instanceof View) {
                mRootView = (View) setLayout();
            } else if (setLayout(inflater, container) != null) {
                mRootView = setLayout(inflater, container);
            }else {
                throw new ClassCastException("type of setLayout() must be int or View!");
            }
            ButterKnife.bind(this,mRootView);
            onBindView(savedInstanceState,mRootView);
        }
        ViewGroup parent=(ViewGroup)mRootView.getParent();
        if (parent!=null){
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    protected void replaceContainer(@IdRes int containerViewId, Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment);
        ft.commit();
    }
}
