package com.gjg.learn.smartmessage.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void ShowToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
