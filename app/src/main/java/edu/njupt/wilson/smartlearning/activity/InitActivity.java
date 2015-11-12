/*
 *    Copyright @2015 wilson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.njupt.wilson.smartlearning.activity;/**
 * <p> </p>
 * Created by wilson on 15/10/24 上午10:53.
 * Belongs to SmartLearnning
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.adapter.MyFragementAdapter;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.onClickListener.MyOnClickListener;
import edu.njupt.wilson.smartlearning.onPageChangeListener.MyOnPageChangeListener;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;

/**
 * Author: wilson
 * Date: 2015-10-24 
 * Time: 10:53 
 *
 */
@EActivity(R.layout.activity_init)
public class InitActivity extends BaseFragmentActivity {

    @ViewById(R.id.viewpage1)
    ViewPager viewPager;

    //更多按钮
    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.coursese)
    TextView course;

    @ViewById(R.id.communication)
    TextView discuss;

    @ViewById(R.id.me)
    TextView personal;

    private List<TextView> textViewList;

    MyFragementAdapter adapters;

    //当前页卡编号
    private int currentIndex = 0;

    private LayoutInflater inflater;

    private View views;


    /**
     * 点击更多菜单，跳出更多菜单窗口
     */
    @Click(R.id.morebt)
    void morebtIsClick(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(InitActivity.this, views);
        morePopUpWindow.showPopupWindow(morebt);
    }

    @AfterViews
    void init(){
        initTextViews();
        initViewPager();
        ExitApplication.getInstance().addActivity(this);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate( //获取自定义的布局视图
                R.layout.activity_main, null, false);
    }

    /**
     * 初始化TextViews
     */
    public void initTextViews(){
        textViewList = new ArrayList<TextView>();

        textViewList.add(course);
        textViewList.add(discuss);
        textViewList.add(personal);

        course.setTextColor(Color.parseColor("#55c8d6"));//设置初始背景颜色
        //设置事件监听
        course.setOnClickListener(new MyOnClickListener(viewPager, textViewList, 0));
        course.setOnClickListener(new MyOnClickListener(viewPager, textViewList, 1));
        course.setOnClickListener(new MyOnClickListener(viewPager, textViewList, 2));
    }


    public void initViewPager(){
        adapters = new MyFragementAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapters);
        //监听ViewPager的变化事件，通知数据变化
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener(textViewList, currentIndex));
        adapters.notifyDataSetChanged();
    }



}
