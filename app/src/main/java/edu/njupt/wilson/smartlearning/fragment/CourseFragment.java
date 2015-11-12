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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.annotation.Nullable;


import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.activity.CourseInfoDetailActivity;
import edu.njupt.wilson.smartlearning.adapter.CourseAdapter;
import edu.njupt.wilson.smartlearning.api.CourseApi;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.CourseInfoData;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.ObjectChange;
import edu.njupt.wilson.smartlearning.utils.UIHelper;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p> </p>
 * Created by wilson on 15/10/22 下午2:21.
 * Belongs to SmartLearnning
 */
@EFragment(R.layout.tab1)
public class CourseFragment extends BaseFragment {

    private View mFragmentView;

    private boolean isPrepared;

    private boolean mHashLoaded;

    private CourseAdapter adapter;

    private Toast toast;

    private RestResult course = null;

    private LinkedList<HashMap<String, Object>> dataSource;

    private File cache;

    private CourseInfoData courseInfoData;

    @ViewById(R.id.courseInfoList)
    ListView listView;

    @RestService
    CourseApi courseApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    @AfterViews
    void afterViews(){

        ExitApplication.getInstance().addActivity(getActivity());
        courseApi.setRestErrorHandler(exceptionHandler);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //创建缓存目录
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (! cache.exists()){
                cache.mkdirs();
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if (mFragmentView == null){
            mFragmentView = inflater.inflate(R.layout.tab1, container, false);
            isPrepared = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lazyLoad();
                }
            }, 100);
        }

        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null){
            parent.removeView(mFragmentView);
        }
        return mFragmentView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHashLoaded){
            return ;
        }

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(getActivity(), mFragmentView);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);

                    course = courseApi.getAllCourse(URLUtil.getIsAppLogin());

                } catch (Exception e){
                    Log.e("CourseFragment", "课程数据加载失败", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess){
                System.out.println("CourseFragment --> execute()");
                if (isSuccess){
                    if (course.getCode() == 0){
                        toast = Toast.makeText(getActivity(), course.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        String data = course.getData().toString();
                        String temp = data.substring(1, data.length() - 1);
                        dataSource = ObjectChange.changeCourse(temp);
                        courseInfoData = new CourseInfoData();
                        courseInfoData.setData(dataSource);
                        setView();
                    }

                    mHashLoaded = true;

                } else {
                    //加载失败
                    toast = Toast.makeText(getActivity(), "课程数据加载失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                //关闭对话框
                UIHelper.hideDialogForLoading();
            }
        }.execute();
    }

    @UiThread
    void setView(){
        adapter = new CourseAdapter(getActivity(), cache);
        adapter.setList(dataSource);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CourseInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("courseInfoData", courseInfoData);
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}
