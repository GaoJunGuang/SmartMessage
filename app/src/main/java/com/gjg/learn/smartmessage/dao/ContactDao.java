package com.gjg.learn.smartmessage.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.InputStream;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class ContactDao {
    /**
     * 通过号码获取联系人名字
     * @param resolver
     * @param address
     * @return
     */
    public static String getNameByAddress(ContentResolver resolver, String address){
        String name = null;
        //把号码拼接在uri后面
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
        //根据号码查询联系人名字
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
//		CursorUtils.printCursor(cursor);
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
            cursor.close();
        }
        return name;
    }

    /**
     * 通过号码获取联系人头像
     * @param resolver
     * @param address
     * @return
     */
    public static Bitmap getAvatarByAddress(ContentResolver resolver, String address){
        Bitmap avatar = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
        if(cursor.moveToFirst()){
            String _id = cursor.getString(0);

            //获取联系人照片
            InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(resolver, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, _id));
            avatar = BitmapFactory.decodeStream(is);
        }
        return avatar;

    }
}
