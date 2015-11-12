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
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * Author: wilson
 * Date: 2015-10-30
 * Time: 20:55
 * Description: 修改用户性别
 */
@EActivity(R.layout.activity_modifysex)
public class ModifySexActivity extends Activity {

    @ViewById(R.id.selectBoy)
    ImageView imageViewMan;

    @ViewById(R.id.selectGirl)
    ImageView imageViewWoman;

    @RestService
    UserApi userApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private Toast toast;

    private Intent intent;

    private int userId;

    //请求结果
    private RestResult modifySex = null;

    private View views;

    private LayoutInflater inflater;

    private CustomerApplication application;

    private String oldSex;

    private String newSex;

    @AfterViews
    void init(){
        userApi.setRestErrorHandler(exceptionHandler);

        ExitApplication.getInstance().addActivity(this);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_modifysex, null, false);
        application = (CustomerApplication) getApplication();
        userId = application.getUserId();
        intent = getIntent();

        this.oldSex = application.getUserSex();
        if (("男").equals(this.oldSex)){
            this.imageViewMan.setImageResource(R.drawable.defalt_head);
        } else {
            this.imageViewWoman.setImageResource(R.drawable.defalt_head);
        }

        //初始化页面
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口大小及透明度
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        this.setFinishOnTouchOutside(true);
    }

    /**
     * 性别TextView的监听器，将点击的TextView保存到本地
     */
       @Click(R.id.linear_man)
    void textViewOnClick(){
        //修改前性别为女
        if (!"男".equals(this.oldSex)){
            //TODO 将数据修改上传到服务器
            //修改本地记录
            sendUerSex("男");
        } else {
            ModifySexActivity.this.setResult(1, intent);
            //关闭当前页面
            ModifySexActivity.this.finish();
        }
    }

    /**
     * 性别TextView的监听器，将点击的TextView保存到本地
     */
    @Click(R.id.linear_woman)
    void textViewWomanOnClick(){
        //修改前性别为男
        if (!"女".equals(this.oldSex)){
            //TODO 将数据修改上传到服务器
            //修改本地记录
            sendUerSex("女");
        } else {
            ModifySexActivity.this.setResult(1, intent);
            //关闭当前页面
            ModifySexActivity.this.finish();
        }
    }


    public void sendUerSex(final String sex){
        //TODO 更新数据到服务器

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(ModifySexActivity.this, views);
            }


            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    //TODO 调用接口修改用户数据
                    modifySex = userApi.modifyUserSex(userId, sex, URLUtil.getIsAppLogin());

                } catch (Exception e){
                    Log.e("ModifySexActivity", "修改用户性别出错！", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                Log.d("ModifySexActivity", "修改用户性别请求完成！");
                UIHelper.hideDialogForLoading();

                if (isSuccess){
                    if (modifySex.getCode() == 0){
                        toast = Toast.makeText(ModifySexActivity.this, modifySex.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                       application.setUserSex(sex);
                    }
                } else {
                    toast = Toast.makeText(ModifySexActivity.this, modifySex.getMsg() + "\n请求失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                ModifySexActivity.this.setResult(1, intent);
                ModifySexActivity.this.finish();
            }
        }.execute();
    }

}
