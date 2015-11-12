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
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.asyncTask.AsyncImageTask;
import edu.njupt.wilson.smartlearning.dataSource.CourseInfoData;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p> </p>
 * Created by wilson on 15/10/22 下午3:02.
 * Belongs to SmartLearnning
 */
@EActivity(R.layout.activity_courseinfodetail)
public class CourseInfoDetailActivity extends Activity {

    @ViewById(R.id.courseInfoDetailPhoto)
    ImageView courseInfoDetailPhoto;

    @ViewById(R.id.courseInfoDetailName)
    TextView courseInfoDetailName;

    @ViewById(R.id.courseInfoDetailTotalNum)
    TextView courseInfoDetailTotalNum;

    @ViewById(R.id.courseInfoDetailDescription)
    TextView courseInfoDetailDescription;

    @ViewById(R.id.morebt)
    ImageView morebt;

    @ViewById(R.id.headTitle)
    TextView headTitle;

    private LayoutInflater inflater;

    private View view;

    private File cache;

    private int courseId;

    @AfterViews
    void init(){
        ExitApplication.getInstance().addActivity(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cache.exists()){
                cache.mkdirs();
            }
        }

        headTitle.setText("课程信息");
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_courseinfodetail, null, false);

        Intent intent = getIntent();
        CourseInfoData courseInfoData = (CourseInfoData) intent.getSerializableExtra("courseInfoData");
        List<HashMap<String, Object>> list = courseInfoData.getData();
        int position = intent.getIntExtra("position", 0);
        HashMap<String, Object> map = list.get(position);

        courseId = Integer.parseInt(map.get("courseId").toString());
        courseInfoDetailName.setText(map.get("courseName").toString());
        courseInfoDetailTotalNum.setText(map.get("courseCount").toString());
        courseInfoDetailDescription.setText(map.get("courseDescription").toString());

        String path = URLUtil.getUrl(map.get("coursePhoto").toString());
        AsyncImageTask task = new AsyncImageTask(path, cache, courseInfoDetailPhoto);
        task.execute();
    }


    @Click(R.id.checkVideo)
    void checkCourseIsClicked(){
        Intent intent = new Intent(this, VideoInfoActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @Click(R.id.returnbt)
    void returnbtIsClicked(){
        CourseInfoDetailActivity.this.finish();
    }

    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(CourseInfoDetailActivity.this, view);
        morePopUpWindow.showPopupWindow(morebt);
    }

}
