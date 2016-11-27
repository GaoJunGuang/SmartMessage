package com.gjg.learn.smartmessage.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Junguang_Gao on 2016/11/11.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return initView(inflater, container, savedInstanceState);
        
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListener();
        initData();
    }

    public abstract void initData();
    public abstract void initListener();

    @Override
    public void onClick(View view) {
        handleClick(view);
    }

    public abstract void handleClick(View view);
}
