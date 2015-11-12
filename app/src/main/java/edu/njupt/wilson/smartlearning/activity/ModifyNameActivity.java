package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.api.UserApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * Author: wilson
 * Date: 2015-10-30
 * Time: 20:28
 * Description: Modify user's nickName
 */
@EActivity(R.layout.activity_modifyname)
public class ModifyNameActivity extends Activity {

    //显示名字EditText
    @ViewById(R.id.modifyName)
    EditText inputName;

    @RestService
    UserApi userApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private String oldName;

    private String newName;

    private Toast toast;

    private Intent intent;

    private int userId;

    //Rest请求结果
    private RestResult modifyName = null;

    private View views;

    private LayoutInflater inflater;

    private CustomerApplication application;

    @AfterViews
    void init(){
        //set exception hanlder
        userApi.setRestErrorHandler(exceptionHandler);

        ExitApplication.getInstance().addActivity(this);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate( //获取自定义的布局文件
                R.layout.activity_modifyname, null, false
            );

        application = (CustomerApplication) getApplication();
        userId = application.getUserId();
        intent = getIntent();

        oldName = application.getUserName();
        inputName.setText(oldName);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        this.setFinishOnTouchOutside(true);
    }


    /**
     * 保存用户名修改
     */
    @Click(R.id.nameSave)
    void saveIsClick(){
        newName = inputName.getText().toString();
        //如果用户名改变，则更新本地和服务器
        if (!newName.equals(this.oldName)){
            //TODO更新数据到服务器，成功后再保存到本地
            new AsyncTask<Void, Void, Boolean>(){

                @Override
                protected void onPreExecute(){
                    super.onPreExecute();
                    UIHelper.showDialogForLoading(ModifyNameActivity.this, views);
                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    try{
                        Thread.sleep(2000);
                        //TODO 接口获取数据，并更新数据
                        modifyName = userApi.modifyName(userId, newName, ((CustomerApplication) getApplication()).getIsAppLogin());
                    } catch (Exception e){
                        Log.e("ModifyNameActivity", "修改用户名出错！", e);
                        return false;
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean isSuccess){

                    Log.d("ModifyNameActivity", "修改用户名后台任务运行结束！");
                    UIHelper.hideDialogForLoading();
                    if (isSuccess){
                        if (modifyName.getCode() == 0){
                            toast = Toast.makeText(ModifyNameActivity.this, modifyName.getMsg(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            setView();
                        }
                    } else {
                        //请求失败
                        toast = Toast.makeText(ModifyNameActivity.this, modifyName.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }

            }.execute();
        }
    }

    @UiThread
    void setView(){
        application.setUserName(newName);
        toast = Toast.makeText(ModifyNameActivity.this, modifyName.getMsg(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        this.setResult(1, intent);
        this.finish();
    }
}
