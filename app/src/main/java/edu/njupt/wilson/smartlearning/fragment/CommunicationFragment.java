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

package edu.njupt.wilson.smartlearning.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.activity.AddThemeActivity;
import edu.njupt.wilson.smartlearning.activity.AnswerDetailActivity;
import edu.njupt.wilson.smartlearning.adapter.CommunicationAdapter;
import edu.njupt.wilson.smartlearning.api.CommunicationApi;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.AllQuestionData;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.ObjectChange;
import edu.njupt.wilson.smartlearning.utils.UIHelper;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p>交流页面卡</p>
 * Created by wilson on 15/10/22 上午11:36.
 * Belongs to SmartLearnning
 */
@EFragment(R.layout.tab2)
public class CommunicationFragment extends BaseFragment {

    private View mFragmentView;

    private boolean isPrepared;

    /**
     * 已经被加载过久不再请求数据
     */
    private boolean mHashLoaded;

    private CommunicationAdapter adapter;

    private Toast toast;

    private RestResult communication = null;

    private LinkedList<HashMap<String, Object>> dataSource;

    private LinkedList<HashMap<String, Object>> list;

    private File cache;

    private AllQuestionData allQuestionData;

    private final int requestCode = 1;

    private CustomerApplication app;

    @ViewById(R.id.themeList)
    PullToRefreshListView pullToRefreshListView;

    @RestService
    CommunicationApi communicationApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    @AfterViews
    void afterViews(){
        //设置ErrorHandler
        communicationApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(getActivity());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //创建缓存目录
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cache.exists()){
                cache.mkdirs();
            }
        }
        app = (CustomerApplication) getActivity().getApplication();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if(mFragmentView == null){
            mFragmentView = inflater.inflate(R.layout.tab2, container, false);
            list = new LinkedList<HashMap<String, Object>>();
            isPrepared = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lazyLoad();
                }
            }, 100);
        }

        //由于公用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null){
            parent.removeView(mFragmentView);
        }
        return  mFragmentView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHashLoaded){
            return;
        }
        asyncQuestionTask(0, 0, false);
    }

    public void asyncQuestionTask(final  int id, final int type, final boolean flag){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                if (!flag){//初次加载
                    UIHelper.showDialogForLoading(getActivity(), mFragmentView);
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    Thread.sleep(2000);
                    //在这里添加调用接口获取数据的代码

                    communication = communicationApi.getAllCommunication(id, type, URLUtil.getIsAppLogin()); //获取全部课程
                } catch (Exception e){
//                    e.printStackTrace();
                    Log.e("CommunicationFragment", "加载数据出错", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                System.out.println("execute()");
                if (isSuccess){
                    //加载数据成功
                    if (communication.getCode() == 0){
                        toast = Toast.makeText(getActivity(), communication.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        if (communication.getData() == null){
                            pullToRefreshListView.onRefreshComplete();
                            return;
                        }
                        String data = communication.getData().toString();
                        String temp = data.substring(1, data.length() - 1);
                        dataSource = ObjectChange.changeAllQuestion(temp);
                        setView(type);
                    }
                    mHashLoaded = true;
                } else {
                    //加载失败
                    toast = Toast.makeText(getActivity(), "加载数据失败！", Toast.LENGTH_SHORT);
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
            if(type == 0){
                //上拉加载
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

        allQuestionData = new AllQuestionData();
        allQuestionData.setData(list);

        adapter = new CommunicationAdapter(getActivity(), cache);
        adapter.setList(list);

        //设置pull-to-refresh模式为Mode.Both
        pullToRefreshListView.setMode(PullToRefreshAdapterViewBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                //更新标签
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                int themeId = 0;

                if (refreshView.isHeaderShown()) {
                    //更新列表
                    themeId = Integer.parseInt(list.get(0).get("questionId").toString());
                    System.out.println("--------->themeId: " + themeId); //debug
                    asyncQuestionTask(themeId, 1, true);
                } else {
                    themeId = Integer.parseInt(list.get(list.size() - 1).get("questionId").toString());
                    asyncQuestionTask(themeId, 0, true);
                }
            }
        });


        ListView actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setAdapter(adapter);

        //通知程序数据集已经改变，如果不通知，那么将不会刷新mListItems的集合
        adapter.notifyDataSetChanged();
        pullToRefreshListView.onRefreshComplete();

        //列表项选择事件监听
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), AnswerDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("allQuestionData", allQuestionData);
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Click(R.id.writePanel)
    void addQuestion(){
        Intent intent = new Intent(getActivity(), AddThemeActivity.class);

        int userId = app.getUserId();
        intent.putExtra("userId", userId);
        startActivityForResult(intent, requestCode);
    }

    @OnActivityResult(requestCode)
    void onActivityResult(int resultCode, Intent data){
        int themeId = Integer.parseInt(list.get(0).get("questionId").toString());
        asyncQuestionTask(themeId, 1, true);
    }
}
