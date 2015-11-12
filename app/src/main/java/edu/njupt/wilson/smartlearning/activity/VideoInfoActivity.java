package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.adapter.VideoAdapter;
import edu.njupt.wilson.smartlearning.api.CourseApi;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.VideoInfoData;
import edu.njupt.wilson.smartlearning.domain.RestResult;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.MyExceptionHandler;
import edu.njupt.wilson.smartlearning.utils.ObjectChange;
import edu.njupt.wilson.smartlearning.utils.UIHelper;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * Author: wilson
 * Date: 2015-10-31
 * Time: 21:22
 * Description: 视频信息页
 */
@EActivity(R.layout.activity_videoinfo)
public class VideoInfoActivity extends Activity{

    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.headTitle)
    TextView headTitle;

    @ViewById(R.id.videoInfoList)
    ListView videoList;

    @RestService
    CourseApi courseApi;

    @Bean
    MyExceptionHandler exceptionHandler;

    private LayoutInflater inflater;

    private View views;

    private VideoAdapter adapter;

    //视频信息数据对象
    private VideoInfoData videoInfoData;

    private LinkedList<HashMap<String, Object>> list;

    //接口请求结果
    private RestResult videoInfo = null;

    private Toast toast;

    private boolean videoResult = false;

    //列表数据源
    private LinkedList<HashMap<String, Object>> dataSource;

    @AfterViews
    void init(){
        courseApi.setRestErrorHandler(exceptionHandler);
        ExitApplication.getInstance().addActivity(this);

        headTitle.setText(R.string.videoTitle);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_videoinfo, null, false);

        Intent intent = getIntent();
        loadData(intent.getIntExtra("courseId", 0));
    }

    /**
     * 载入视频信息
     * @param courseId 视频编号
     */
    public void loadData(final int courseId){

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                UIHelper.showDialogForLoading(VideoInfoActivity.this, views);
            }

            @Override
            protected Boolean doInBackground(Void... params){
                try{
                    Thread.sleep(2000);

                    //调用接口
                    videoInfo = courseApi.getVideoByCourse(courseId, URLUtil.getIsAppLogin());
                    if (videoInfo == null){
                        throw new Exception("无网络连接！");
                    }
                } catch (Exception e){
                    Log.e("VideoInfoActivity", "获取视频失败！", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccuess){
                Log.d("VideoInfoActivity", "后台载入视频信息任务完成！");
                if (isSuccuess){
                    if (videoInfo.getCode() == 0){
                        toast = Toast.makeText(VideoInfoActivity.this, videoInfo.getMsg(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        videoResult = true;
                    }
                    if (videoResult){
                        String data = videoInfo.getData().toString();
                        String temp = data.substring(1, data.length() - 1);
                        dataSource = ObjectChange.changeVideoInfo(temp);
                        videoInfoData = new VideoInfoData();
                        videoInfoData.setData(dataSource);
                        setView();
                    }
                } else {
                    //加载失败
                    toast = Toast.makeText(VideoInfoActivity.this, "加载失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                UIHelper.hideDialogForLoading();
            }

        }.execute();


    }

    @UiThread
    void setView(){
        adapter = new VideoAdapter(this);
        adapter.setList(dataSource);
        videoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoInfoActivity.this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoInfoData", videoInfoData);
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Click(R.id.returnbt)
    void retrunbtIsClicked(){
        VideoInfoActivity.this.finish();
    }

    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(VideoInfoActivity.this, views);
        morePopUpWindow.showPopupWindow(morebt);
    }
}
