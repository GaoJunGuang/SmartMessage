package com.gjg.learn.smartmessage.util;

import android.database.Cursor;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class CursorUtils{
    public static void printCursor(Cursor cursor){
        //								获取行数
        LogUtils.i(cursor, "一共有" + cursor.getCount() + "条数据");
        while(cursor.moveToNext()){
            //					获取字段数量
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                //获取字段名
                String name = cursor.getColumnName(i);
                String content = cursor.getString(i);

                LogUtils.i(cursor, name + ":" + content);
            }
            LogUtils.i(cursor, "=================================");
        }
    }
}
