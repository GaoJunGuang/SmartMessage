package com.gjg.learn.smartmessage.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Junguang_Gao on 2016/11/11.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();

    }

    protected abstract void initView();
    protected abstract void initListener();
    protected abstract void initData();

    @Override
    public void onClick(View view) {
        handleClick(view);
    }

    protected abstract void handleClick(View view);
}
