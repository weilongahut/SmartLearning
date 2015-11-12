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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.UiThread;

import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.adapter.AnswerDetailAdapter;
import edu.njupt.wilson.smartlearning.api.CommunicationApi;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.AllQuestionData;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.ObjectChange;
import edu.njupt.wilson.smartlearning.utils.UIHelper;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p> </p>
 * Created by wilson on 15/10/21 下午9:32.
 * Belongs to SmartLearning
 */
@EActivity(R.layout.activity_answerdetail)
public class AnswerDetailActivity extends Activity {

    @ViewById(R.id.reply_headText)
    TextView replyHeadText;

    @ViewById(R.id.answerList)
    PullToRefreshListView pullToRefreshListView;

    @RestService
    CommunicationApi communicationApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private AnswerDetailAdapter adapter;

    private Toast toast;

    private RestResult answerDetail = null;

    private LinkedList<HashMap<String, Object>> dataSource;

    private LinkedList<HashMap<String, Object>> list;

    private int questionId;

    private LayoutInflater inflater;

    private View view;

    private final int REQUESTCODE = 1;

    @AfterViews
    void afterViews(){
        communicationApi.setRestErrorHandler(exceptionHandler);

        ExitApplication.getInstance().addActivity(this);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_answer, null, false);
        list = new LinkedList<HashMap<String, Object>>();
        replyHeadText.setText("评论信息");
        Intent intent = getIntent();
        AllQuestionData allQuestionData = (AllQuestionData) intent.getSerializableExtra("allQuestionData");
        LinkedList<HashMap<String, Object>> linkedList = allQuestionData.getData();
        int position = intent.getIntExtra("position", 0);
        HashMap<String, Object> map = linkedList.get(position);
        questionId = Integer.parseInt(map.get("questionId").toString());
        asyncQuestionTask(0, 0, false);
    }


    /**
     * 后台加载方法
     * @param answerId 回答编号
     * @param type 类型
     * @param flag 是否首次加载
     */
    @Background
    void asyncQuestionTask(final int answerId, final int type, final boolean flag) {
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                if (!flag){ //首次加载
                    UIHelper.showDialogForLoading(AnswerDetailActivity.this, view);
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);

                    answerDetail = communicationApi.findCommentByQuestionId(questionId, answerId, type, URLUtil.getIsAppLogin());

                }catch (Exception e){
                    Log.e("AnswerDetailActivity", "加载答案出错", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                System.out.println("AnswerDetailActivity ----> execute()");
                if (isSuccess){
                    if (answerDetail.getCode() == 0){
                        toast = Toast.makeText(AnswerDetailActivity.this, answerDetail.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        if (answerDetail.getData() == null){
                            pullToRefreshListView.onRefreshComplete();
                            return;
                        }
                        String data = answerDetail.getData().toString();
                        String temp = data.substring(1, data.length() - 1);
                        dataSource = ObjectChange.changeAllAnswer(temp);
                        setView(type);
                    }
                } else {
                    //加载失败
                    toast = Toast.makeText(AnswerDetailActivity.this, "AnswerDetail加载失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                if (!flag){
                    UIHelper.hideDialogForLoading();
                }
            }

        }.execute();
    }


    @UiThread
    void setView(int type){
        if (list.size() == 0){
            list = dataSource;
        } else {
            if (type == 0){
                //上拉更新
                for (HashMap<String, Object> map : dataSource){
                    list.addLast(map);
                }
            } else if (type == 1){
                //下拉刷新
                for (int i = dataSource.size() - 1; i >= 0; i --){
                    list.addFirst(dataSource.get(i));
                }
            }
        }

        adapter = new AnswerDetailAdapter(AnswerDetailActivity.this);
        adapter.setList(list);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(AnswerDetailActivity.this.getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                int answerId = 0;

                if (refreshView.isHeaderShown()) {
                    answerId = Integer.parseInt(list.get(0).get("answerId").toString());
                    System.out.println(" ------>answerId:" + answerId); //debug
                    asyncQuestionTask(answerId, 1, true);
                } else {
                    answerId = Integer.parseInt(list.get(list.size() - 1).get("answerId").toString());
                    asyncQuestionTask(answerId, 0, true);
                }
            }
        });

        ListView actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setAdapter(adapter);

        //通知程序数据集已经改变
        adapter.notifyDataSetChanged();
        pullToRefreshListView.onRefreshComplete();
    }

    @Click(R.id.replyUser)
    void replyUserIsClicked(){
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("questionId", questionId);
        startActivityForResult(intent, REQUESTCODE);
    }

    @Click(R.id.returnbt)
    void returnbtIsClick(){
        AnswerDetailActivity.this.finish();
    }


    @OnActivityResult(REQUESTCODE)
    void onActivityResult(int resultCode, Intent data){
        int answerId = Integer.parseInt(list.get(0).get("answerId").toString());
        asyncQuestionTask(answerId, 1, true);
    }
}
