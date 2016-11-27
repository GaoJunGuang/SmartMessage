package com.gjg.learn.smartmessage.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.gjg.learn.smartmessage.R;
import com.gjg.learn.smartmessage.adapter.GroupListAdapter;
import com.gjg.learn.smartmessage.base.BaseFragment;
import com.gjg.learn.smartmessage.bean.Group;
import com.gjg.learn.smartmessage.dao.GroupDao;
import com.gjg.learn.smartmessage.dao.SimpleQueryHandler;
import com.gjg.learn.smartmessage.dialog.InputDialog;
import com.gjg.learn.smartmessage.dialog.ListDialog;
import com.gjg.learn.smartmessage.ui.activity.GroupDetailActivity;
import com.gjg.learn.smartmessage.util.Constant;
import com.gjg.learn.smartmessage.util.ToastUtils;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class GroupFragment extends BaseFragment {
    private Button bt_group_newgroup;
    private ListView lv_group_list;
    private GroupListAdapter adapter;
    private SimpleQueryHandler queryHandler;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group,null);
        bt_group_newgroup = (Button) view.findViewById(R.id.bt_group_create);
        lv_group_list = (ListView) view.findViewById(R.id.lv_group);
        return view;
    }

    @Override
    public void initData() {
        adapter = new GroupListAdapter(getActivity(), null);
        lv_group_list.setAdapter(adapter);

        queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
        queryHandler.startQuery(0, adapter, Constant.URI.URI_GROUP_QUERY, null, null, null, "create_date desc");
    }

    @Override
    public void initListener() {
        bt_group_newgroup.setOnClickListener(this);
        lv_group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //跳转时携带群组名字、群组id
                Cursor cursor = (Cursor) adapter.getItem(position);
                Group group = Group.createFromCursor(cursor);
                if(group.getThread_count() > 0){
                    //点击群组列表的条目，跳转至群组详细Activity
                    Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                    intent.putExtra("name", group.getName());
                    intent.putExtra("group_id", group.get_id());
                    startActivity(intent);
                }
                else{
                    ToastUtils.ShowToast(getActivity(), "当前群组没有任何会话");
                }
            }
        });

        //给条目设置长按侦听
        lv_group_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Cursor cursor  = (Cursor) adapter.getItem(position);
                final Group group = Group.createFromCursor(cursor);
                ListDialog.showDialog(getActivity(), "选择操作", new String[]{"修改","删除"}, new ListDialog.OnListDialogLietener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        switch (position) {
                            case 0://修改
                                //弹出输入对话框
                                InputDialog.showDialog(getActivity(), "修改群组", new InputDialog.OnInputDialogListener() {

                                    @Override
                                    public void onConfirm(String text) {
                                        //确认修改群组名字
                                        GroupDao.updateGroupName(getActivity().getContentResolver(), text, group.get_id());
                                    }
                                    @Override
                                    public void onCancel() {
                                    }
                                });
                                break;
                            case 1://删除
                                GroupDao.deleteGroup(getActivity().getContentResolver(), group.get_id());
                                break;

                        }

                    }
                });
                return false;
            }
        });
    }

    @Override
    public void handleClick(View view) {
        switch (view.getId()) {
            case R.id.bt_group_create:
                InputDialog.showDialog(getActivity(), "创建群组", new InputDialog.OnInputDialogListener() {

                    @Override
                    public void onConfirm(String text) {
                        if(!TextUtils.isEmpty(text)){
                            GroupDao.insertGroup(getActivity().getContentResolver(), text);
                        }
                        else{
                            ToastUtils.ShowToast(getActivity(), "群组名不能为空");
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                });
                break;

        }
    }
}
