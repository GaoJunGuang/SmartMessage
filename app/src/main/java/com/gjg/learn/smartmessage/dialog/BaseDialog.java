package com.gjg.learn.smartmessage.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gjg.learn.smartmessage.R;

/**
 * Created by Junguang_Gao on 2016/11/16.
 */
public abstract class  BaseDialog extends AlertDialog implements View.OnClickListener {

    protected BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();

        initListener();

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    public abstract void handleClick(View v);
}
