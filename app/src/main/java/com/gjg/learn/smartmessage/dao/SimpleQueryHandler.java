package com.gjg.learn.smartmessage.dao;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class SimpleQueryHandler extends AsyncQueryHandler {
    public SimpleQueryHandler(ContentResolver cr) {
        super(cr);
    }

    /**
     *查询完毕时调用
     * @param token 查询开始时携带的数据
     * @param cookie 查询开始时携带的数据
     * @param cursor 查询结果
     */
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        //		CursorUtils.printCursor(cursor);
        if(cookie != null && cookie instanceof CursorAdapter){
            //查询得到的cursor，交给CursorAdapter，由它把cursor的内容显示至listView
            ((CursorAdapter)cookie).changeCursor(cursor);
        }
    }
}
