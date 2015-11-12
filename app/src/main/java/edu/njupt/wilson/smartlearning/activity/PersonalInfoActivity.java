package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.CustomerApplication;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.asyncTask.AsyncImageTask;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * Author: wilson
 * Date: 2015-10-31
 * Time: 19:32
 * Description: 个人信息
 */
@EActivity(R.layout.activity_personalinfo)
public class PersonalInfoActivity extends Activity {

    //More button
    @ViewById(R.id.morebt)
    ImageView morebt;

    //Title
    @ViewById(R.id.headTitle)
    TextView headTitle;

    @ViewById(R.id.infoHeadIcon)
    ImageView infoHeadIcon;

    @ViewById(R.id.infoName)
    TextView infoName;

    @ViewById(R.id.infoSex)
    TextView infoSex;

    private LayoutInflater inflater;

    private View views;

    private CustomerApplication application;

    private File cache;

    //nickName request code
    private static final int REQUESTCODE_NAME = 1;

    //sex request code
    private static final int REQUESTCODE_SEX = 2;

    //headPhoto request code
    private static final int REQUESTCODE_HEADPHOTO = 3;


    @AfterViews
    void init(){
        application = (CustomerApplication) getApplication();
        ExitApplication.getInstance().addActivity(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cache = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cache.exists()){
                cache.mkdirs();
            }
        }

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_personalinfo, null, false);

        infoName.setText(application.getUserName());
        infoSex.setText(application.getUserSex());
        String path = URLUtil.getUrl(application.getUserPhoto());
        //异步加载图片
        AsyncImageTask task = new AsyncImageTask(path, cache, infoHeadIcon);
        task.execute();
    }


    /**
     * 修改头像事件
     */
    @Click(R.id.myHeadPhoto)
    void myHeadPhotoIsClicked(){
        Intent intent = new Intent(this, ModifyHeadPhotoActivity.class);
        Bitmap bitmap = ((BitmapDrawable) infoHeadIcon.getDrawable()).getBitmap();
        intent.putExtra("headPhoto", bitmap);
        startActivityForResult(intent, PersonalInfoActivity.REQUESTCODE_HEADPHOTO);
    }

    /**
     * 修改昵称事件
     */
    @Click(R.id.myName)
    void myNameIsClicked(){
        Intent intent = new Intent(this, ModifyNameActivity.class);
        startActivityForResult(intent, PersonalInfoActivity.REQUESTCODE_NAME);
    }

    /**
     * 性别修改事件
     */
    @Click(R.id.mySex)
    void mySexIsClicked(){
        Intent intent = new Intent(this, ModifySexActivity.class);
        startActivityForResult(intent, PersonalInfoActivity.REQUESTCODE_SEX);
    }


    @OnActivityResult(PersonalInfoActivity.REQUESTCODE_HEADPHOTO)
    void setHeadPhoto(int resultCode, Intent data){
        //获取头像的bitmap对象
        Bitmap bitmap = data.getParcelableExtra("userPhoto");
        infoHeadIcon.setImageBitmap(bitmap);
        //TODO 保存修改到服务器
    }

    @OnActivityResult(PersonalInfoActivity.REQUESTCODE_NAME)
    void setName(int resultCode, Intent data){
        String userName = data.getStringExtra("userName");
        infoName.setText(userName);
        //TODO 保存修改到服务器
    }

    @OnActivityResult(PersonalInfoActivity.REQUESTCODE_SEX)
    void setSex(int resultCode, Intent data){
        String userSex = data.getStringExtra("userSex");
        infoName.setText(userSex);
        //TODO 保存修改到服务器
    }


    //跳转到我的问题列表页面
    @Click(R.id.myquestion)
    void myQuestion(){
        Intent intent = new Intent(this, MyQuestionActivity.class);
        startActivity(intent);
    }

    //回退到上个页面
    @Click(R.id.returnbt)
    void stepBack(){
        headTitle.setText("");
        PersonalInfoActivity.this.finish();
    }

    @Click(R.id.morebt)
    void morebtIsClicked(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(PersonalInfoActivity.this, views);
        morePopUpWindow.showPopupWindow(morebt);
    }

}
