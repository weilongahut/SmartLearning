package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.api.UserApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.domain.UserResult;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.JsonUtil;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * 登录
 * Created by wilson on 15/10/21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.et_username)
    EditText editUserName;

    @ViewById(R.id.et_password)
    EditText editUserPwd;

    @ViewById(R.id.checkboxpw)
    CheckBox cb_pwd;

    @ViewById(R.id.checkboxlg)
    CheckBox cb_autoLog;

    @RestService
    UserApi userApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private String userName;
    private String pwd;
    private SharedPreferences.Editor sharedData;
    private LayoutInflater inflater;
    private View view;
    private CustomerApplication app;
    private boolean loginResult = false;
    private Toast toast;
    private RestResult userInfo = null;

    @AfterViews
    void init(){
        userApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(this);
        app = (CustomerApplication) getApplication();
        sharedData = getSharedPreferences("userInfo", 0).edit();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_login, null, false);
        SharedPreferences sp = this.getSharedPreferences("userInfo", 0);

        //如果记住密码勾选
        if (sp.getBoolean("remem_pwd", false)){
            editUserName.setText(sp.getString("userName", ""));
            editUserPwd.setText(sp.getString("userPwd", ""));
            cb_pwd.setChecked(true);
        }

        //如果勾选自动登录
        if (sp.getBoolean("auto_log", false)){
            cb_autoLog.setChecked(true);
            Intent intent = getIntent();
            if (intent.getIntExtra("logout", 0) == 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginActionDeal();
                    }
                }, 100);
            }
        }
    }

    //登录处理逻辑
    public void loginActionDeal(){
        userName = editUserName.getText().toString().trim();
        pwd = editUserPwd.getText().toString().trim();

        //登录账户和密码不为空
        if (!"".equals(userName) && !"".equals(pwd)){
            Log.d("LoginActivity", userName + " login in using " + pwd);
            login(); //登录方法
        } else {
            toast = Toast.makeText(this, "账号和密码不可以为空！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    @Click(R.id.btn_login)
    void btnLoginIsClicked(){
        loginActionDeal();
    }

    @Click(R.id.btn_register)
    void btnRegisterIsClicked(){
        Intent intent = new Intent(this, RegisterActivity_.class);
        startActivity(intent); //跳转至注册页面
    }

    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(LoginActivity.this, view);
        morePopUpWindow.showPopupWindow(morebt); //显示more窗口
    }

    /**
     * 登录系统
     */
    public void login(){
        Log.d("LoginActivity", "登录系统！");
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                UIHelper.showDialogForLoading(LoginActivity.this, view); //加载进度提示对话框
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);

                    String userPwd = pwd;
                    userInfo = userApi.userLogin(userName, userPwd); //验证登录

                    if (userInfo == null){
                        throw new Exception("无网络连接！");
                    }
                } catch (Exception e){
                    Log.e("LoginActivity", "登录异常！" , e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {

                //关闭加载进度对话框
                UIHelper.hideDialogForLoading();

                if (isSuccess){

                    int code = userInfo.getCode();
                    if (code == 0){
                        toast = Toast.makeText(LoginActivity.this, userInfo.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        loginResult = false;
                    } else if (code == 1){
                        String json = userInfo.getData().toString();
                        Log.d("LoginActivity.login() --->", json);
                        UserResult userResult = JsonUtil.fromObject(json, UserResult.class);
                        app.setUserId(userResult.getUserId());
                        app.setUserName(userResult.getUserName());
                        app.setUserSex(userResult.getUserSex());
                        app.setUserPhoto(userResult.getUserPhoto());
                        loginResult = true;
                    }

                } else {
                    //加载失败
                    toast = Toast.makeText(LoginActivity.this, "登录失败，无网络！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    loginResult = false;
                }

                //成功登录
                if (loginResult){
                    app.setValue("login");
                    if (cb_pwd.isChecked()){//记住用户名、密码
                        sharedData.putString("userName", userName);
                        sharedData.putString("userPwd", pwd);
                        sharedData.putBoolean("remem_pwd", true);
                        if (cb_autoLog.isChecked()){ //自动登录
                            sharedData.putBoolean("auto_log", true);
                        } else {
                            sharedData.putBoolean("auto_log", false);
                        }

                    } else {
                        sharedData.putBoolean("remem_pwd", false);
                        sharedData.putBoolean("auto_log", false);
                    }

                    sharedData.commit(); //提交数据
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        Log.e("LoginActivity", "登录逻辑线程出错！", e);
                    }

                    //跳转到首页
                    Intent intent = new Intent(LoginActivity.this, VideoPlayActivity_.class);
                    startActivity(intent);

                    LoginActivity.this.finish();
                }
            }
        }.execute();
    }
}
