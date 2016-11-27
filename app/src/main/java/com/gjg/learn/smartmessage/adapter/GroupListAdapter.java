package com.gjg.learn.smartmessage.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gjg.learn.smartmessage.R;
import com.gjg.learn.smartmessage.bean.Group;

/**
 * Created by Junguang_Gao on 2016/11/21.
 */
public class GroupListAdapter extends CursorAdapter {

    public GroupListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_group_list,null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);

        Group group = Group.createFromCursor(cursor);

        holder.tv_grouplist_name.setText(group.getName() + " (" + group.getThread_count() + ")");
        if(DateUtils.isToday(group.getCreate_date())){
            holder.tv_grouplist_date.setText(DateFormat.getTimeFormat(context).format(group.getCreate_date()));
        }
        else{
            holder.tv_grouplist_date.setText(DateFormat.getDateFormat(context).format(group.getCreate_date()));
        }
    }

    private ViewHolder getHolder(View view){
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder == null){
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }
    class ViewHolder{
        private TextView tv_grouplist_name;
        private TextView tv_grouplist_date;

        public ViewHolder(View view) {
            tv_grouplist_name = (TextView) view.findViewById(R.id.tv_grouplist_name);
            tv_grouplist_date = (TextView) view.findViewById(R.id.tv_grouplist_date);
        }
    }
}
