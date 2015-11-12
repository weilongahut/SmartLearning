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

package edu.njupt.wilson.smartlearning.onPageChangeListener;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.widget.TextView;


import java.util.List;

/**
 * 自定义页面滑动切换监听
 * Created by wilson on 15/10/21.
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

    private int currenctIndex = 0; //当前页卡编号
    private List<TextView> textViewList;

    /**
     * 构造函数
     * @param textViews
     * @param currenctIndex
     */
    public MyOnPageChangeListener(List<TextView> textViews, int currenctIndex){
        this.textViewList = textViews;
        this.currenctIndex = currenctIndex;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        textViewList.get(currenctIndex).setTextColor(Color.parseColor("#FFFFFF"));
        textViewList.get(position).setTextColor(Color.parseColor("#55c8d6"));
        currenctIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
