package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.UiThread;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.api.UserApi;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * Author: wilson
 * Date: 2015-10-31
 * Time: 19:54
 * Description: 注册页面
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {

    //注册用户名
    @ViewById(R.id.rg_username)
    EditText edUserName;

    //密码
    @ViewById(R.id.rg_password)
    EditText edUserPw;

    //重复输入密码
    @ViewById(R.id.rg_surepassword)
    EditText edRePw;

    @ViewById(R.id.morebt)
    ImageView morebt;

    //用户信息相关服务接口
    @RestService
    UserApi userApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private LayoutInflater inflater;

    private View views;

    private String userName;

    private String password;

    private String rePassword;

    private Toast toast;

    //Rest请求结果
    private RestResult userInfo = null;

    //注册结果，false：失败，true：成功
    private boolean registerResult = false;

    /**
     * 初始化页面
     */
    @AfterViews
    void init(){
        userApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(this);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_register, null, false);

    }


    @Click(R.id.btn_register)
    void registerBtIsClicked(){
        userName = edUserName.getText().toString().trim();
        password = edUserPw.getText().toString().trim();
        rePassword = edRePw.getText().toString().trim();

        if (!"".equals(userName) && !"".equals(password) && !"".equals(rePassword)){
            if (password.equals(rePassword)){
                registerUser();
            } else {
                toast = Toast.makeText(RegisterActivity.this, "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {
            toast = Toast.makeText(RegisterActivity.this, "请完整填写表格！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 注册用户
     */
    private void registerUser(){

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(RegisterActivity.this, views);
            }

            @Override
            protected Boolean doInBackground(Void... params){
                try {
                    Thread.sleep(2000);
                    //调用接口完成操作
                    String userPw = password;
                    userInfo = userApi.userRegister(userName, userPw);
                    if (userInfo == null){
                        throw new Exception("RegisterActivity,网络无连接！");
                    }
                } catch (Exception e){
                    Log.e("RegisterActivity", "注册失败！");
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                UIHelper.hideDialogForLoading();

                if (isSuccess){
                    int code = userInfo.getCode();
                    if (code == 0){
                        toast = Toast.makeText(RegisterActivity.this, userInfo.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        registerResult = false;
                    } else if (code == 1){
                        tip(); //提示注册成功
                        registerResult = true;
                    }
                } else {
                    //加载失败
                    toast = Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    registerResult = false;
                }

                if (registerResult){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }
            }

        }.execute();
    }


    @UiThread
    void tip(){
        toast = Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Click(R.id.returnbt)
    void returnbtIsClicked(){
        RegisterActivity.this.finish();
    }

    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(RegisterActivity.this, views);
        morePopUpWindow.showPopupWindow(morebt);
    }


}
