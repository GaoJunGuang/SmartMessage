package com.gjg.learn.smartmessage.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gjg.learn.smartmessage.util.ToastUtils;


public class SendSmsReceiver extends BroadcastReceiver {
	
	public static final String ACTION_SEND_SMS = "com.gjg.learn.smartmessage.sendsms";
	@Override
	public void onReceive(Context context, Intent intent) {
		int code = getResultCode();
		if(code == Activity.RESULT_OK ){
			ToastUtils.ShowToast(context, "发送成功");
		}
		else{
			ToastUtils.ShowToast(context, "发送失败");
		}
	}

}
