package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;

/**
 * Author: wilson
 * Date: 2015-10-30 
 * Time: 18:29 
 * Description: 退出页面
 */
@EActivity(R.layout.activity_logout_dialog)
public class LogoutActivity extends Activity {

    @ViewById(R.id.logoff)
    LinearLayout logoff;

    private CustomerApplication application;

    //退出
    @Click(R.id.closeApp)
    void closeAppIsClick(){
        ExitApplication.getInstance().exit();
    }

    @Click(R.id.logoff)
    void logoutIsClick(){
        Log.d("LogoutActivity", "退出系统");
        application = (CustomerApplication)getApplication();
        application.setValue("logout");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("logout", 1);
        startActivity(intent);
    }


    @AfterViews
    void init(){
        ExitApplication.getInstance().addActivity(this);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口的大小及透明度
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        this.setFinishOnTouchOutside(true);

        application = (CustomerApplication) getApplication();
        if ("Login".equals(application.getValue())) {
            logoff.setVisibility(View.VISIBLE);
        } else {
            logoff.setVisibility(View.GONE);
        }
    }



}
