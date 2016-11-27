package com.gjg.learn.smartmessage.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gjg.learn.smartmessage.R;

/**
 * Created by Junguang_Gao on 2016/11/16.
 */
public class ConfirmDialog extends BaseDialog {
    private String title;
    private String message;
    private TextView tv_dialog_title;
    private TextView tv_dialog_message;
    private Button bt_dialog_cancel;
    private Button bt_dialog_confirm;
    private OnConfirmListener onConfirmListener;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected ConfirmDialog(Context context) {
        super(context);
    }

    public static void showDialog(Context context, String title, String message, OnConfirmListener confirmListener){
        ConfirmDialog dialog=new ConfirmDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setOnConfirmListener(confirmListener);
        dialog.show();

    }

    private void setOnConfirmListener(OnConfirmListener confirmListener) {
        this.onConfirmListener=confirmListener;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_confirm);
        tv_dialog_title= (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_message= (TextView) findViewById(R.id.tv_dialog_message);
        bt_dialog_cancel= (Button) findViewById(R.id.bt_dialog_cancel);
        bt_dialog_confirm= (Button) findViewById(R.id.bt_dialog_confirm);

    }

    @Override
    protected void initData() {
        tv_dialog_title.setText(title);
        tv_dialog_message.setText(message);

    }

    @Override
    protected void initListener() {
        bt_dialog_confirm.setOnClickListener(this);
        bt_dialog_cancel.setOnClickListener(this);
    }

    @Override
    public void handleClick(View v) {
        switch (v.getId()){
            case R.id.bt_dialog_cancel:
                if(onConfirmListener!=null){
                    onConfirmListener.onCancel();
                }
                break;
            case R.id.bt_dialog_confirm:
                if(onConfirmListener!=null){
                    onConfirmListener.onConfirm();
                }
                break;
        }
        //对话框消失
        dismiss();
    }

    public interface OnConfirmListener{
        void onCancel();
        void onConfirm();
    }
}
