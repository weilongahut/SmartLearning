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

package edu.njupt.wilson.smartlearning.popUpWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.activity.LogoutActivity;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.dataSource.PopupDataSource;

/**
 * 右上角更多菜单窗口
 * Created by wilson on 15/10/21.
 */
public class MorePopUpWindow extends PopupWindow {

    private View contentView;
    private Button cancle;
    private SimpleAdapter adapter;
    private PopupDataSource data;
    private CustomerApplication application;

    /**
     * 构造函数
     * @param context
     * @param view
     */
    public MorePopUpWindow(final Activity context, final View view){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.more_popup_dialog1, null, false);
        ListView listView = (ListView) contentView.findViewById(R.id.popupListView);
        List<HashMap<String, Object>> map;
        application = (CustomerApplication) context.getApplication();
        Log.i("----->more ", application.getValue());

        if ("Login".equals(application.getValue())){
            map = data.getData_2();
        } else {
            map = data.getData_1();
        }

        adapter = new SimpleAdapter(context, map, R.layout.popup_item1,
                new String[] {"popupImage", "popupText"},
                new int[]{R.id.popupimage, R.id.popuptext});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        if(!"Login".equals(application.getValue())){
//                            dismiss();
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            context.startActivity(intent);
//                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        dismiss();
                        Intent intent = new Intent(context, LogoutActivity.class);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        int height = context.getWindowManager().getDefaultDisplay().getHeight();
        int width = context.getWindowManager().getDefaultDisplay().getWidth();

        //设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        //设置弹出窗体的宽度和高度
        this.setWidth( width / 2 + 50);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
    }

    /**
     * 显示弹出窗体
     * @param parent 父窗口
     */
    public void showPopupWindow(View parent){
        if (!this.isShowing()){
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }


}
