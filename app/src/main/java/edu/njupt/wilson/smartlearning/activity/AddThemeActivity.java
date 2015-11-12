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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.api.CommunicationApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * 添加主题
 */
@EActivity(R.layout.activity_add_theme)
public class AddThemeActivity extends Activity {

    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.themeName)
    EditText themeName;

    @ViewById(R.id.themContent)
    EditText themeContent;

    private LayoutInflater inflater;
    private View view;
    private Toast toast;
    private Intent intent;
    private int userId;
    private RestResult addQuestion = null;

    @RestService
    CommunicationApi communicationApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    @AfterViews
    void init(){

        //设置ErrorHandler
        communicationApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(this);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_add_theme, null, true);
        intent = getIntent();
        userId = intent.getIntExtra("userId", 1);
    }

    @Click(R.id.returnbt)
    void returnbtisClicked(){
        //退出本Activity
        AddThemeActivity.this.finish();
    }

    @Click(R.id.morebt)
    void morebtIsClick(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(AddThemeActivity.this, view);
        morePopUpWindow.showPopupWindow(morebt); //显示more窗口
    }

    @Click(R.id.replyUser)
    void addTheme(){

        final String questionTheme = themeName.getText().toString();
        final String comContent = themeContent.getText().toString();
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(AddThemeActivity.this, view);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                    addQuestion = communicationApi.addCommunication(userId, questionTheme, comContent, ((CustomerApplication) getApplication()).getIsAppLogin());

                } catch (Exception e){
                    Log.e("AddThemeActivity", "添加主题后台任务出错！", e);
                    return false;
                }
                return true;

            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                System.out.println("AddThemeActivity ----> execute()");
                UIHelper.hideDialogForLoading();
                if (isSuccess){
                    //加载成功
                    if (addQuestion.getCode() == 0){
                        toast = Toast.makeText(AddThemeActivity.this, addQuestion.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        setView();
                    }
                } else {
                    //加载失败
                    toast = Toast.makeText(AddThemeActivity.this, "加载失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }.execute();
    }


    @UiThread
    void setView(){
        toast = Toast.makeText(AddThemeActivity.this, addQuestion.getMsg(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        this.setResult(1, intent);
        this.finish();
    }


}
