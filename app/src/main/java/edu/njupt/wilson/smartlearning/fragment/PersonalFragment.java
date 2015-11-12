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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.activity.ModifyHeadPhotoActivity;
import edu.njupt.wilson.smartlearning.activity.ModifyNameActivity;
import edu.njupt.wilson.smartlearning.activity.ModifySexActivity;
import edu.njupt.wilson.smartlearning.activity.MyQuestionActivity;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.asyncTask.AsyncImageTask;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p>app底部菜单的个人中心</p>
 * Created by wilson on 15/10/22 下午3:04.
 * Belongs to SmartLearning
 */
@EFragment(R.layout.activity_personalinfo)
public class PersonalFragment extends Fragment {

    @ViewById(R.id.infoHeadIcon)
    ImageView infoHeadIcon; //头像

    @ViewById(R.id.infoName)
    TextView infoName; //信息

    @ViewById(R.id.infoSex)
    TextView infoSex; //性别

    private LayoutInflater inflater;

    private CustomerApplication app;

    private File cache;

    private int userId;

    //昵称的请求码
    private static final int REQUESTCODE_NAME = 1;

    //性别请求码
    private static final int REQUESTCODE_SEX = 2;

    //头像请求码
    private static final int REQESTCODE_HEADPHOTO = 3;

    @AfterViews
    void init(){
        app = (CustomerApplication) getActivity().getApplication();
        ExitApplication.getInstance().addActivity(getActivity());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cache.exists()){
                cache.mkdirs();
            }
        }

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userId = app.getUserId();
        //设置TextView显示用户属性
        infoName.setText(app.getUserName());
        infoSex.setText(app.getUserSex());

        String path = URLUtil.getUrl(app.getUserPhoto());
        AsyncImageTask task = new AsyncImageTask(path, cache, infoHeadIcon);
        task.execute();

    }

    @Click(R.id.myHeadPhoto)
    void myHeadPhotoIsClicked(){
        Intent intent = new Intent(getActivity(), ModifyHeadPhotoActivity.class);
        Bitmap bitmap = ((BitmapDrawable) infoHeadIcon.getDrawable()).getBitmap();
        intent.putExtra("headPhoto", bitmap);
        startActivityForResult(intent, REQESTCODE_HEADPHOTO);
    }

    @Click(R.id.myName)
    void myNameIsClicked(){
        Intent intent = new Intent(getActivity(), ModifyNameActivity.class);
        startActivityForResult(intent, REQUESTCODE_NAME);
    }

    @Click(R.id.mySex)
    void mySexIsClicked(){
        Intent intent = new Intent(getActivity(), ModifySexActivity.class);
        startActivityForResult(intent, REQUESTCODE_SEX);
    }

    //修改头像
    @OnActivityResult(REQESTCODE_HEADPHOTO)
    void setHeadPhoto(int resultCode, Intent data){
        Bitmap bitmap = data.getParcelableExtra("userPhoto");
        infoHeadIcon.setImageBitmap(bitmap);
    }

    @OnActivityResult(REQUESTCODE_NAME)
    void setName(int resultCode, Intent data){
        String userName = data.getStringExtra("userName");
        infoName.setText(userName);
    }

    @OnActivityResult(REQUESTCODE_SEX)
    void setSex(int resultCode, Intent data){
        String userSex = data.getStringExtra("userSex");
        infoSex.setText(userSex);
    }

    @Click(R.id.myquestion)
    void myQuestion(){
        Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
