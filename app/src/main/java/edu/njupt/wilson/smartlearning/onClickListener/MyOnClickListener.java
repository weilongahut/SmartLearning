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

package edu.njupt.wilson.smartlearning.onClickListener;


import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * 底部菜单点击事件监听
 * Created by wilson on 15/10/21.
 */
public class MyOnClickListener implements View.OnClickListener{

    private int index = 0;
    private ViewPager viewPager;
    private List<TextView> textViewList;

    /**
     * 构造函数
     * @param pager
     * @param textViews
     * @param i 索引位置
     */
    public MyOnClickListener(ViewPager pager, List<TextView> textViews, int i){
        index = i;
        this.viewPager = pager;
        this.textViewList = textViews;
    }

    @Override
    public void onClick(View v) {
        textViewList.get(viewPager.getCurrentItem()).setTextColor(Color.parseColor("#ffffff"));
        textViewList.get(index).setTextColor(Color.parseColor("#55c8d6"));
        viewPager.setCurrentItem(index);
    }
}
