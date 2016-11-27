package com.gjg.learn.smartmessage.util;

import android.util.Log;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class LogUtils {
    public static boolean isDebug = true;
    public static void i(String tag, String msg){
        if(isDebug){
            Log.i(tag, msg);
        }
    }
    public static void i(Object tag, String msg){
        if(isDebug){
            Log.i(tag.getClass().getSimpleName(), msg);
        }
    }
    public static void e(String tag, String msg){
        if(isDebug){
            Log.i(tag, msg);
        }
    }
}
