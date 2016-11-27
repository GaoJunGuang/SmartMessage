package com.gjg.learn.smartmessage.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gjg.learn.smartmessage.R;

/**
 * Created by Junguang_Gao on 2016/11/18.
 */
public class ListDialog extends BaseDialog{
    private String title;
    private String[] items;
    private TextView tv_listdialog_title;
    private ListView lv_listdialog;
    private OnListDialogLietener onListDialogLietener;
    private Context context;

    protected ListDialog(Context context, String title, String[] items, OnListDialogLietener onListDialogLietener) {
        super(context);
        this.context = context;
        this.title = title;
        this.items = items;
        this.onListDialogLietener = onListDialogLietener;
    }

    public static void showDialog(Context context, String title, String[] items, OnListDialogLietener onListDialogLietener){
        ListDialog dialog = new ListDialog(context, title, items, onListDialogLietener);
        dialog.show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_list);
        tv_listdialog_title = (TextView) findViewById(R.id.tv_listdialog_title);
        lv_listdialog = (ListView) findViewById(R.id.lv_listdialog);
    }

    @Override
    protected void initData() {
        tv_listdialog_title.setText(title);
        lv_listdialog.setAdapter(new MyAdapter());
    }

    @Override
    protected void initListener() {
        //给lv_listdialog设置条目侦听
        lv_listdialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(onListDialogLietener != null){
                    onListDialogLietener.onItemClick(parent, view, position, id);
                }
                dismiss();
            }
        });
    }

    @Override
    public void handleClick(View v) {

    }
    public interface OnListDialogLietener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.item_listdialog, null);
            TextView tv_item_listdialog = (TextView) view.findViewById(R.id.tv_item_listdialog);
            tv_item_listdialog.setText(items[position]);
            return view;
        }

    }
}
