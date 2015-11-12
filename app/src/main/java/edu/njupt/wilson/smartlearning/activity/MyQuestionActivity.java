package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.adapter.CommunicationAdapter;
import edu.njupt.wilson.smartlearning.api.CommunicationApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.MyQuestionData;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.ObjectChange;
import edu.njupt.wilson.smartlearning.utils.UIHelper;

/**
 * Author: wilson
 * Date: 2015-10-30
 * Time: 21:21
 * Description: 我发布的问题
 */
@EActivity(R.layout.activity_myquestion)
public class MyQuestionActivity extends Activity{

    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.headTitle)
    TextView headTitle;

    @ViewById(R.id.myQuestionList)
    PullToRefreshListView myQuestionList;

    @RestService
    CommunicationApi communicationApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private MyQuestionData data;

    private LayoutInflater inflater;

    private View view;

    private CommunicationAdapter adapter;

    private Toast toast;

    private RestResult communication = null;

    private LinkedList<HashMap<String, Object>> dataSource;

    private LinkedList<HashMap<String, Object>> list;

    //缓存
    private File cache;

    private final int REQUESTCODE = 1;

    private CustomerApplication application;

    private int userId;

    @AfterViews
    void init(){
        communicationApi.setRestErrorHandler(exceptionHandler);

        ExitApplication.getInstance().addActivity(this);

        //存储状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cache.exists()){
                cache.mkdirs();
            }
        }

        application = (CustomerApplication) getApplication();
        userId = application.getUserId();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_myquestion, null, false);
        headTitle.setText(R.string.myQuestion);
        asyncQeusionTask(0, false);
    }


    public void asyncQeusionTask(final int questionId, final boolean flag){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                if (!flag){ //首次加载
                    //显示加载进度条
                    UIHelper.showDialogForLoading(MyQuestionActivity.this, view);
                }
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
                    Thread.sleep(2000);
                    //TODO 调用接口完成数据操作
                    communication = communicationApi.getUserCommunication(userId, questionId, ((CustomerApplication) getApplication()).getIsAppLogin()); //获取全部问题
                } catch (Exception e){
                    Log.e("MyQuestionActivity", "", e);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                Log.d("MyQestionActivity", "问题列表请求完成！");
                if (isSuccess){
                    // 加载成功
                    //
                    if (communication.getCode() == 0) {
                        toast = Toast.makeText(MyQuestionActivity.this,
                                communication.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        if (communication.getData() == null) {
                            myQuestionList.onRefreshComplete();
                            return;
                        }
                        String data = communication.getData().toString();
                        String temp = data.substring(1, data.length() - 1);
                        dataSource = ObjectChange.changeAllQuestion(temp);
                        setView();
                    }
                } else {
                    // 加载失败
                    toast = Toast.makeText(MyQuestionActivity.this,
                            "error", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                if (!flag) {
                    //关闭对话框
                    UIHelper.hideDialogForLoading();
                }
            }

        }.execute();
    }

    @UiThread
    void setView(){
        if (list.size() == 0){
            list = dataSource;
        } else {
            for (HashMap<String, Object> map : dataSource){
                list.addLast(map);
            }
        }

        data = new MyQuestionData();
        data.setData(list);

        adapter = new CommunicationAdapter(MyQuestionActivity.this, cache);
        adapter.setList(list);

        myQuestionList.setMode(PullToRefreshBase.Mode.BOTH);
        myQuestionList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MyQuestionActivity.this.getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                int questionId = Integer.parseInt(list.get(list.size() - 1).get("questionId").toString());
                asyncQeusionTask(questionId, true);
            }
        });
        ListView actualListView = myQuestionList.getRefreshableView();
        actualListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        myQuestionList.onRefreshComplete();

        myQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), AnswerDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("allQuestionData", data);
                intent.putExtra("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 点击退出按钮
     */
    @Click(R.id.returnbt)
    void returnbtIsClick(){
        MyQuestionActivity.this.finish();
    }

    /**
     * more按钮点击事件
     */
    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(MyQuestionActivity.this, view);
        morePopUpWindow.showPopupWindow(morebt);
    }
}
