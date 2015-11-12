package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.FileNotFoundException;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.ExitApplication;

/**
 * Author: wilson
 * Date: 2015-10-30 
 * Time: 19:11 
 * Descrition: 修改头像
 */
@EActivity(R.layout.activity_modifyheadphoto)
public class ModifyHeadPhotoActivity extends Activity {

    @ViewById(R.id.modifyName)
    ImageView imageView;

    private  final int REQUESTCODE = 1;

    Bitmap headPhoto;
    Bitmap oldHeadPhoto;

    @AfterViews
    void init(){
        ExitApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        this.oldHeadPhoto = (Bitmap) intent.getParcelableExtra("headPhoto");
        imageView.setImageBitmap((oldHeadPhoto));
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口的大小及透明度
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        //点击外部退出
        this.setFinishOnTouchOutside(true);
    }


    /**
     * 选择本地图片
     */
    @Click(R.id.photoSelect)
    void selectLocalImage(){
        Intent intent = new Intent();
        //开启Pitures画面Type设定为image
        intent.setType("image/*");
        //使用Intent.ACTION_GET_CONTENT这个Action
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUESTCODE);
    }


    @OnActivityResult(REQUESTCODE)
    void onActivityResult(int requestCode, Intent data){
        if (requestCode == RESULT_OK){
            Uri uri = data.getData();
            ContentResolver resolver = this.getContentResolver();
            try{
                this.headPhoto = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                //将Bitmap设定到ImageView
                imageView.setImageBitmap(headPhoto);
            } catch (FileNotFoundException e){
                Log.e("ModifyHeadPhotoActivity", "图片加载出错，找不到文件！", e);
            }
        }
    }


    /**
     * 保存选择的本地图片
     */
    @Click(R.id.photoSave)
    void saveHeadPhoto(){

        //TODO:上传用户头像到服务器

        this.finish();
    }



}
