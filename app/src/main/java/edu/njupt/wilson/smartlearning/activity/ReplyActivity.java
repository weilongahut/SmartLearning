package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
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
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.api.CommunicationApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * Author: wilson
 * Date: 2015-10-31
 * Time: 20:26
 * Description: 回复问题页面
 */
@EActivity(R.layout.activity_reply)
public class ReplyActivity extends Activity{

    @ViewById(R.id.beReplyContent)
    EditText edReplyContent;

    @RestService
    CommunicationApi communicationApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private Intent intent;

    private int questionId;

    private RestResult addAnswer = null;

    private LayoutInflater inflater;

    private View views;

    private Toast toast;

    private int userId;

    private CustomerApplication app;

    private String replyContent;


    @AfterViews
    void init(){
        communicationApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(this);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        this.setFinishOnTouchOutside(true);

        app = (CustomerApplication) getApplication();
        userId = app.getUserId();

        intent = getIntent();
        questionId = intent.getIntExtra("questionId", 1);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_reply, null, false);
    }

    //提交按钮点击事件
    @Click(R.id.submitReply)
    void onSubmit(){
        replyContent = edReplyContent.getText().toString();
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(ReplyActivity.this, views);
            }

            @Override
            protected Boolean doInBackground(Void... params){
                try {
                    Thread.sleep(2000);
                    //调用接口完成操作
                    String comContent = replyContent;
                    addAnswer = communicationApi.addComment(userId, questionId, comContent, ((CustomerApplication) getApplication()).getIsAppLogin());
                    if (addAnswer == null){
                        throw new Exception("无网络连接！");
                    }
                } catch (Exception e){
                    Log.e("ReplyActivity", "添加回复失败！", e);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                UIHelper.hideDialogForLoading();
                if (isSuccess){
                    if (addAnswer.getCode() == 0){
                        toast = Toast.makeText(ReplyActivity.this, addAnswer.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        //回复失败，更新视图
                        setView();
                    }
                } else {
                    toast = Toast.makeText(ReplyActivity.this, "回复失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

        }.execute();
    }


    /**
     * 回复失败，显示提示信息，退出页面
     */
    @UiThread
    void setView(){
        toast = Toast.makeText(ReplyActivity.this, "回复失败！\n" + addAnswer.getMsg(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        this.setResult(1, intent);
        this.finish();
    }
}
