package com.gjg.learn.smartmessage.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gjg.learn.smartmessage.R;
import com.gjg.learn.smartmessage.adapter.ConversationListAdapter;
import com.gjg.learn.smartmessage.base.BaseFragment;
import com.gjg.learn.smartmessage.bean.Conversation;
import com.gjg.learn.smartmessage.bean.Group;
import com.gjg.learn.smartmessage.dao.GroupDao;
import com.gjg.learn.smartmessage.dao.SimpleQueryHandler;
import com.gjg.learn.smartmessage.dao.ThreadGroupDao;
import com.gjg.learn.smartmessage.dialog.ConfirmDialog;
import com.gjg.learn.smartmessage.dialog.DeleteMsgDialog;
import com.gjg.learn.smartmessage.dialog.ListDialog;
import com.gjg.learn.smartmessage.ui.activity.ConversationDetailActivity;
import com.gjg.learn.smartmessage.ui.activity.NewMsgActivity;
import com.gjg.learn.smartmessage.util.Constant;
import com.gjg.learn.smartmessage.util.ToastUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.List;

/**
 * Created by Junguang_Gao on 2016/11/11.
 */
public class ConversationFragment extends BaseFragment {
    private LinearLayout ll_conversation_edit_menu;
    private LinearLayout ll_conversation_select_menu;
    private Button bt_conversation_edit;
    private Button bt_conversation_create;
    private Button bt_conversation_select_all;
    private Button bt_conversation_cancel_select;
    private Button bt_conversation_delete;
    private ConversationListAdapter adapter;
    private ListView lv_conversation_list;
    private List<Integer> selectedConversationIds;
    private DeleteMsgDialog dialog;
    private boolean flag=false;  //默认全不选

    static final int WHAT_DELETE_COMPLETE = 0;
    static final int WHAT_UPDATE_DELETE_PROGRESS = 1;
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_DELETE_COMPLETE:
                    //退出选择模式，显示编辑菜单
                    adapter.setIsSelectMode(false);
                    adapter.notifyDataSetChanged();
                    showEditMenu();
                    dialog.dismiss();
                    break;
                case WHAT_UPDATE_DELETE_PROGRESS:
                    dialog.updateProgressAndTitle(msg.arg1 + 1);
                    break;

            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_conversation,null);
        ll_conversation_edit_menu= (LinearLayout) view.findViewById(R.id.ll_conversation_edit);
        ll_conversation_select_menu= (LinearLayout) view.findViewById(R.id.ll_conversation_select);
        bt_conversation_edit= (Button) view.findViewById(R.id.bt_conversation_edit);
        bt_conversation_create= (Button) view.findViewById(R.id.bt_conversation_create);
        bt_conversation_select_all= (Button) view.findViewById(R.id.bt_conversation_select_all);
        bt_conversation_cancel_select= (Button) view.findViewById(R.id.bt_conversation_cancel_select);
        bt_conversation_delete= (Button) view.findViewById(R.id.bt_conversation_delete);
        lv_conversation_list= (ListView) view.findViewById(R.id.lv_conversation);
        return view;
    }

    @Override
    public void initData() {
        //创建一个CursorAdapter对象
        adapter = new ConversationListAdapter(getActivity(), null);
        lv_conversation_list.setAdapter(adapter);

        SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());

        String[] projection = {
                "sms.body AS snippet",
                "sms.thread_id AS _id",
                "groups.msg_count AS msg_count",
                "address AS address",
                "date AS date"
        };
        //开始异步查询
        //arg0、arg1：可以用来携带一个int型和一个对象
        //arg1:用来携带adapter对象，查询完毕后给adapter设置cursor
        queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, projection, null, null, "date desc");
//		Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_SMS_CONVERSATION, null, null, null, null);
    }

    @Override
    public void initListener() {
        bt_conversation_edit.setOnClickListener(this);
        bt_conversation_create.setOnClickListener(this);
        bt_conversation_select_all.setOnClickListener(this);
        bt_conversation_cancel_select.setOnClickListener(this);
        bt_conversation_delete.setOnClickListener(this);

        lv_conversation_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入单项选择模式
                if(adapter.getIsSelectMode()){
                    adapter.selectSingle(position);
                }
                else{
                    //进入详细的会话界面
                    Intent intent=new Intent(getActivity(),ConversationDetailActivity.class);
                    //携带数据：address和thread_id
                    Cursor cursor = (Cursor) adapter.getItem(position);
                    Conversation conversation = Conversation.createFromCursor(cursor);
                   /* Bundle bundle=new Bundle();
                    bundle.putString("address", conversation.getAddress());
                    bundle.putInt("thread_id", conversation.getThread_id());
                    intent.putExtras(bundle);*/
                    intent.putExtra("address", conversation.getAddress());
                    intent.putExtra("thread_id", conversation.getThread_id());
                    startActivity(intent);

                }


            }
        });

        lv_conversation_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Cursor cursor = (Cursor)adapter.getItem(position);
                Conversation conversation = Conversation.createFromCursor(cursor);
                //判断选中的会话是否有所属的群组
                if(ThreadGroupDao.hasGroup(getActivity().getContentResolver(), conversation.getThread_id())){
                    //该会话已经被添加，弹出ConfirmDialog
                    showExitDialog(conversation.getThread_id());
                }
                else{
                    //该会话没有被添加过，弹出ListDialog，列出所有群组
                    showSelectGroupDialog(conversation.getThread_id());
                }
                //消费掉这个事件，否则会传递给OnItemClickListener
                return true;
            }
        });
    }

    @Override
    public void handleClick(View view) {
        switch (view.getId()){
            case R.id.bt_conversation_edit:
                showSelectMenu();
                //进入选择模式
                adapter.setIsSelectMode(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_conversation_create:
                Intent intent = new Intent(getActivity(), NewMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_conversation_select_all:
                if(!flag){
                    adapter.selectAll();
                    bt_conversation_select_all.setText("全不选");
                    flag=true;
                }else{
                    adapter.unSelectAll();
                    bt_conversation_select_all.setText("全选");
                    flag=false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_conversation_cancel_select:
                showEditMenu();
                adapter.setIsSelectMode(false);
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_conversation_delete:
                selectedConversationIds = adapter.getSelectedConversationIds();
                if(selectedConversationIds.size() == 0)
                    return;
                showDeleteDialog();
                break;
        }

    }

    private void showExitDialog(final int thread_id) {
        //先通过会话id查询群组id
        final int group_id= ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), thread_id);
        //通过群组id查询群组名字
        String name = GroupDao.getGroupNameByGroupId(getActivity().getContentResolver(), group_id);

        String message = "该会话已经被添加至[" + name + "]群组，是否要退出该群组？";
        ConfirmDialog.showDialog(getActivity(), "提示", message, new ConfirmDialog.OnConfirmListener() {

            @Override
            public void onConfirm() {
                //把选中的会话从群组中删除
                boolean isSuccess = ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), thread_id, group_id);
                ToastUtils.ShowToast(getActivity(), isSuccess? "退出成功" : "退出失败");
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void showSelectGroupDialog(final int thread_id) {
        //查询一共有哪些群组，取出名字全部存入items
        final Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY,
                null, null, null, null);
        if(cursor.getCount() == 0){
            ToastUtils.ShowToast(getActivity(), "当前没有群组，请先创建");
            return;
        }
        String[] items = new String[cursor.getCount()];
        //遍历cursor，取出名字
        while(cursor.moveToNext()){
            Group group = Group.createFromCursor(cursor);
            items[cursor.getPosition()] = group.getName();
        }
        ListDialog.showDialog(getActivity(), "选择群组", items, new ListDialog.OnListDialogLietener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //cursor就是查询groups表得到的，里面就是群组的所有信息
                cursor.moveToPosition(position);
                Group group = Group.createFromCursor(cursor);
                //把指定会话存入指定群组
                boolean isSuccess = ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
                ToastUtils.ShowToast(getActivity(), isSuccess? "插入成功" : "插入失败");
            }
        });
    }

    /**
     * 显示删除对话框
     */
    private void showDeleteDialog() {
        ConfirmDialog.showDialog(getActivity(), "提示", "确定要删除会话吗？", new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                deleteMessage();
            }
        });
    }

    boolean isStopDelete = false;
    /**
     * 删除信息
     */
    private void deleteMessage() {
        dialog = DeleteMsgDialog.showDeleteDialog(getActivity(), selectedConversationIds.size(), new DeleteMsgDialog.OnDeleteCancelListener() {
            @Override
            public void stopDeleteMsg() {
                isStopDelete = true;
            }
        });
        Thread t = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < selectedConversationIds.size(); i++){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //中断删除
                    if(isStopDelete){
                        isStopDelete = false;
                        break;
                    }
                    //取出集合中的会话id,以id作为where条件删除所有符合条件的短信
                    String where = "thread_id = " + selectedConversationIds.get(i);
                    getActivity().getContentResolver().delete(Constant.URI.URI_SMS, where, null);

                    //发送消息，让删除进度条刷新，同时把当前的删除进度传给进度条
                    Message msg = handler.obtainMessage();
                    msg.what = WHAT_UPDATE_DELETE_PROGRESS;
                    //把当前删除进度存入消息中
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                }
                //删除会话后，清空集合
                selectedConversationIds.clear();
                handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
            }
        };
        t.start();
    }

    /**
     * 显示编辑菜单
     */
    private void showEditMenu() {
        ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(ll_conversation_select_menu.getHeight()).setDuration(200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(0).setDuration(200);
            }
        }, 200);
    }

    /**
     * 顯示选择菜单
     */
    private void showSelectMenu() {
        ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(ll_conversation_edit_menu.getHeight()).setDuration(200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(0).setDuration(200);
            }
        }, 200);


    }
}
