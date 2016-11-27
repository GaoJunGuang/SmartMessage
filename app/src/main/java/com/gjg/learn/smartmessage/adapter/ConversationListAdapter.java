package com.gjg.learn.smartmessage.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjg.learn.smartmessage.R;
import com.gjg.learn.smartmessage.bean.Conversation;
import com.gjg.learn.smartmessage.dao.ContactDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class ConversationListAdapter extends CursorAdapter {
    private boolean isSelectMode=false;

    //记录选择模式下选中哪些条目
    private List<Integer> selectedConversationIds = new ArrayList<Integer>();
    public ConversationListAdapter(Context context, Cursor c) {
        super(context, c);


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_conversation_list,null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);
        //根据cursor内容创建会话对象，此时cursor的指针已经移动至正确的位置
        Conversation conversation = Conversation.createFromCursor(cursor);
        //判断当前是否进入选择模式


        if(isSelectMode){
            holder.iv_check.setVisibility(View.VISIBLE);
            //判断集合中是否包含会话id，从而确定该条目是否被选中
            if(selectedConversationIds.contains(conversation.getThread_id())){
                holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_checked);
            }
            else{
                holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_normal);
            }
        }
        else{
            holder.iv_check.setVisibility(View.GONE);
        }

        //设置号码
        //按号码查询是否存有联系人
        String name = ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
        if(TextUtils.isEmpty(name)){
            holder.tv_conversation_address.setText(conversation.getAddress() + "(" + conversation.getMsg_count() + ")");
        }
        else{
            holder.tv_conversation_address.setText(name + "(" + conversation.getMsg_count() + ")");
        }
        holder.tv_conversation_body.setText(conversation.getSnippet());

        //设置时间
        //判断是否今天
        if(DateUtils.isToday(conversation.getDate())){
            //如果是，显示时分
            holder.tv_conversation_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
        }
        else{
            //如果不是，显示年月日
            holder.tv_conversation_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
        }

        //获取联系人头像
        Bitmap avatar = ContactDao.getAvatarByAddress(context.getContentResolver(), conversation.getAddress());
        //判断是否成功拿到头像
        if(avatar == null){
            holder.iv_conversation_avatar.setBackgroundResource(R.drawable.img_default_avatar);
        }
        else{
            holder.iv_conversation_avatar.setBackgroundDrawable(new BitmapDrawable(avatar));
        }
    }


    public boolean getIsSelectMode() {
        return isSelectMode;
    }

    public void setIsSelectMode(boolean isSelectMode) {
        this.isSelectMode = isSelectMode;
    }


    //参数就是条目的View对象
    private ViewHolder getHolder(View view) {
        //先判断条目view对象中是否有holder
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder == null){
            //如果没有，就创建一个，并存入view对象
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class ViewHolder{
        private ImageView iv_conversation_avatar;
        private TextView tv_conversation_address;
        private TextView tv_conversation_body;
        private TextView tv_conversation_date;
        private ImageView iv_check;

        //参数就是条目的View对象
        public ViewHolder(View view) {
            //在构造方法中完成封装条目的所有组件
            iv_conversation_avatar = (ImageView) view.findViewById(R.id.iv_conversation_avatar);
            tv_conversation_address = (TextView) view.findViewById(R.id.tv_conversation_address);
            tv_conversation_body = (TextView) view.findViewById(R.id.tv_conversation_body);
            tv_conversation_date = (TextView) view.findViewById(R.id.tv_conversation_date);
            iv_check = (ImageView) view.findViewById(R.id.iv_check);
        }
    }

    /**
     * 把选中的条目存入集合
     */
    public void selectSingle(int position){
        //从cursor中取出position对应的会话
        Cursor cursor = (Cursor) getItem(position);
        Conversation conversation = Conversation.createFromCursor(cursor);
        if(selectedConversationIds.contains(conversation.getThread_id())){
            //强转为integer,否则是把参数作为索引而不是要删除的元素
            selectedConversationIds.remove((Integer)conversation.getThread_id());
        }
        else{
            selectedConversationIds.add(conversation.getThread_id());
        }

        notifyDataSetChanged();
    }

    public void selectAll(){
        Cursor cursor = getCursor();
        cursor.moveToPosition(-1);
        //遍历cursor取出所有会话id
        //把所有会话id,全部添加到集合中
        selectedConversationIds.clear();
        while(cursor.moveToNext()){
            Conversation conversation = Conversation.createFromCursor(cursor);
            selectedConversationIds.add(conversation.getThread_id());
        }

        notifyDataSetChanged();
    }
    public void unSelectAll(){
        selectedConversationIds.clear();
        notifyDataSetChanged();
    }

    public void cancelSelect(){
        //清空集合
        selectedConversationIds.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedConversationIds() {
        return selectedConversationIds;
    }
}
