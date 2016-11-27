package com.gjg.learn.smartmessage.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjg.learn.smartmessage.R;
import com.gjg.learn.smartmessage.adapter.MainPagerAdapter;
import com.gjg.learn.smartmessage.base.BaseActivity;
import com.gjg.learn.smartmessage.ui.fragment.ConversationFragment;
import com.gjg.learn.smartmessage.ui.fragment.GroupFragment;
import com.gjg.learn.smartmessage.ui.fragment.SearchFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private LinearLayout ll_tab_conversation;
    private LinearLayout ll_tab_group;
    private LinearLayout ll_tab_search;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private TextView tv_tab_conversation;
    private TextView tv_tab_group;
    private TextView tv_tab_search;
    private View v_indicate_line;
    private MainPagerAdapter adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ll_tab_conversation= (LinearLayout) this.findViewById(R.id.ll_conversation);
        ll_tab_group= (LinearLayout) this.findViewById(R.id.ll_group);
        ll_tab_search= (LinearLayout) this.findViewById(R.id.ll_search);
        mViewPager= (ViewPager) this.findViewById(R.id.viewPager);
        tv_tab_conversation= (TextView) this.findViewById(R.id.tv_tab_conversation);
        tv_tab_group= (TextView) this.findViewById(R.id.tv_tab_group);
        tv_tab_search= (TextView) this.findViewById(R.id.tv_tab_search);
        v_indicate_line=this.findViewById(R.id.v_indicate_line);

        

    }

    @Override
    protected void initListener() {
        ll_tab_conversation.setOnClickListener(this);
        ll_tab_group.setOnClickListener(this);
        ll_tab_search.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 滑动过程中调用
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算红线位移的距离
                int distance = positionOffsetPixels / 3;
                ViewPropertyAnimator.animate(v_indicate_line).translationX(distance+position*v_indicate_line.getWidth()).setDuration(200);

            }

            @Override
            public void onPageSelected(int position) {
                textLightAndScale();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //隱藏輸入法
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


    }

    private void textLightAndScale() {
        int item=mViewPager.getCurrentItem();
        //根据viewPager的界面索引决定选项卡颜色
        tv_tab_conversation.setTextColor(item == 0? Color.WHITE : 0xaa666666);
        tv_tab_group.setTextColor(item == 1? Color.WHITE : 0xaa666666);
        tv_tab_search.setTextColor(item == 2? Color.WHITE : 0xaa666666);

        //                        要操作的对象                                         改变宽度至指定比例
        ViewPropertyAnimator.animate(tv_tab_conversation).scaleX(item == 0? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_group).scaleX(item == 1? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_search).scaleX(item == 2? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(item == 0? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_group).scaleY(item == 1? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_search).scaleY(item == 2? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(item == 0? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_group).scaleY(item == 1? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_search).scaleY(item == 2? 1.2f : 1).setDuration(200);
    }

    @Override
    protected void initData() {
        //设置红线的宽度
        computeIndicateLineWidth();
        fragments=new ArrayList<Fragment>();
        ConversationFragment conversationFragment=new ConversationFragment();
        GroupFragment groupFragment=new GroupFragment();
        SearchFragment searchFragment=new SearchFragment();
        fragments.add(conversationFragment);
        fragments.add(groupFragment);
        fragments.add(searchFragment);

        adapter=new MainPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
        textLightAndScale();

        //设置红线的宽度
        computeIndicateLineWidth();

    }

    /**
     * 设置红线的宽度
     */
    private void computeIndicateLineWidth() {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        v_indicate_line.getLayoutParams().width = width / 3;
    }

    @Override
    protected void handleClick(View view) {
        switch (view.getId()){
            case R.id.ll_conversation:
                mViewPager.setCurrentItem(0);
                Log.e("MainActivity","0");
                break;
            case R.id.ll_group:
                mViewPager.setCurrentItem(1);
                Log.e("MainActivity","1");
                break;
            case R.id.ll_search:
                mViewPager.setCurrentItem(2);
                Log.e("MainActivity","2");
                break;
        }
    }
}
