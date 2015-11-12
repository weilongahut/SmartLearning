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
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.ExitApplication;

/**
 * Created by wilson on 15/10/21.
 * 退出确认弹出窗
 */
public class LogoutPopWindow {

    private PopupWindow popupWindow;

    private ListView listView;

    public LogoutPopWindow(final Activity context, final View view){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupWindowView = inflater.inflate(R.layout.activity_logout_dialog, null, false);

        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        LinearLayout closeApp = (LinearLayout)popupWindowView.findViewById(R.id.closeApp); //dialog.xml视图里的控件
        LinearLayout logoff = (LinearLayout) popupWindowView.findViewById(R.id.logoff);
        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitApplication.getInstance().exit();
            }
        });

        logoff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });
    }

}
