package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import org.androidannotations.annotations.EActivity;

import edu.njupt.wilson.smartlearning.R;

/**
 * Author: wilson
 * Date: 2015-10-31
 * Time: 22:08
 * Description: 视频播放页面
 */
@EActivity(R.layout.activity_videoplay)
public class VideoPlayActivity extends Activity{

    //播放、暂停按钮
    private Button start2stop;

    //绘图容器对象，用于把视频显示在屏幕上
    private SurfaceView playerView;

    //视频URL
    private String videoURL;

    //播放器控件
    private MediaPlayer mediaPlayer;

    //保存已播放视频大小
    private int postSize;

    //进度条控件
    private SeekBar seekBar;

    //是否在播放视频，默认为yes
    private boolean flag = true;

    private RelativeLayout layout;

    //是否显示其他按钮
    private boolean displayButtons;

    //返回按钮
    private Button backButton;

    private View view;

    //更新进度条
    private UpdateSeekBar update;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //播放时保持屏幕点亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //初始化
        init();

        videoURL = "";//视频播放地址

        setListener();
    }

    /**
     * 初始化数据
     */
    private void init(){
        //创建一个播放器对象
        mediaPlayer = new MediaPlayer();
        //创建更新进度条对象
        update = new UpdateSeekBar();
        //加载布局文件
        setContentView(R.layout.activity_videoplay);
        backButton = (Button) findViewById(R.id.back);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        start2stop = (Button) findViewById(R.id.play);
        //刚进入页面时，播放按钮不可用
        start2stop.setEnabled(false);

        playerView = (SurfaceView) findViewById(R.id.mSurfaceView);
        playerView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //不缓冲
        playerView.getHolder().setKeepScreenOn(true);
        playerView.getHolder().addCallback(new SurfaceViewListener()); //设置监听对象
        layout = (RelativeLayout) findViewById(R.id.rl2);
        view = findViewById(R.id.pb);
    }

    //播放视频的线程
    class PlayMovie extends Thread {

        int  position = 0;

        public PlayMovie(int position){
            this.position = position;
        }

        @Override
        public void run(){
            Message message = Message.obtain();
            try {
                Log.i("VideoPlayActivity", "播放视频" + videoURL);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(videoURL);
                mediaPlayer.setDisplay(playerView.getHolder()); //将视频显示在SurfaceView上

                //监听事件
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    final int postSize = position;

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.i("VideoPlayActivity", "play");
                        Log.i("VideoPlayActivity", "post " + postSize);
                        playerView.setVisibility(View.GONE);
                        start2stop.setVisibility(View.GONE);
                        layout.setVisibility(View.GONE);
                        displayButtons = false;

                        if (mediaPlayer != null){
                            mediaPlayer.start(); //开始播放
                        } else {
                            return;
                        }

                        if (postSize > 0){ //说明中途停止过（activit调用过pause方法，不是用户点击停止按钮），跳到停止位置开始播放
                            Log.i("VideoPlayActivity", "seekTo");
                            mediaPlayer.seekTo(postSize);
                        }

                        //启动线程，更新进度条
                        new Thread(update).start();

                    }
                });
                mediaPlayer.prepareAsync(); //准备播放

            } catch (Exception e){
                message.what = 2;
                Log.e("VideoPlayActivity", e.toString());
            }

            super.run();
        }
    }

    /**
     * 播放器事件监听对象
     */
    class SurfaceViewListener implements SurfaceHolder.Callback {

        /**
         * This is called immediately after the surface is first created.
         * Implementations of this should start up whatever rendering code
         * they desire.  Note that only one thread can ever draw into
         * a {@link SurfaceHolder}, so you should not draw into the Surface here
         * if your normal rendering will be in another thread.
         *
         * @param holder The SurfaceHolder whose surface is being created.
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //说明停止过activity，调用股哟pause方法，跳到停止位置播放
            if (postSize > 0 && videoURL != null){
                new PlayMovie(postSize).start();
                flag = true;
                int sMax = seekBar.getMax();
                int mMax = mediaPlayer.getDuration();
                seekBar.setProgress(postSize * sMax / mMax);
                postSize = 0;
                view.setVisibility(View.GONE);
            } else {
                new PlayMovie(0).start();
            }
        }

        /**
         * This is called immediately after any structural changes (format or
         * size) have been made to the surface.  You should at this point update
         * the imagery in the surface.  This method is always called at least
         * once, after {@link #surfaceCreated}.
         *
         * @param holder The SurfaceHolder whose surface has changed.
         * @param format The new PixelFormat of the surface.
         * @param width  The new width of the surface.
         * @param height The new height of the surface.
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //TODO 未实现
        }

        /**
         * This is called immediately before a surface is being destroyed. After
         * returning from this call, you should no longer try to access this
         * surface.  If you have a rendering thread that directly accesses
         * the surface, you must ensure that thread is no longer touching the
         * Surface before returning from this function.
         *
         * @param holder The SurfaceHolder whose surface is being destroyed.
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                //保存当前播放位置
                postSize = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
                flag = false;
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置各类监听对象
     */
    private void setListener(){

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = false;
                start2stop.setBackgroundResource(R.drawable.movie_play_bt);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        /**
         * 如果视频在播放，则调用mediaPlayer.pause()； 停止播放视频，反之则调用mediaPlayer.start()
         * 同时更换按钮背景图片
         */
        start2stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    start2stop.setBackgroundResource(R.drawable.movie_play_bt);
                    mediaPlayer.pause();
                    postSize = mediaPlayer.getCurrentPosition();
                } else {
                    if (flag == false){
                        flag = true;
                        new Thread(update).start();
                    }
                    mediaPlayer.start();
                    start2stop.setBackgroundResource(R.drawable.movie_stop_bt);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //计算进度条需要前进的位置数据
                int value = seekBar.getProgress() * mediaPlayer
                        .getDuration() / seekBar.getMax();

                mediaPlayer.seekTo(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO
            }
        });

        /**
         * 点击屏幕，切换控件显示状态
         */
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayButtons){
                    start2stop.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                    displayButtons = false;
                } else {
                    layout.setVisibility(View.VISIBLE);
                    start2stop.setVisibility(View.VISIBLE);
                    playerView.setVisibility(View.VISIBLE);

                    //设置全屏
                    ViewGroup.LayoutParams params = playerView.getLayoutParams();
                    params.height = RelativeLayout.LayoutParams.FILL_PARENT;
                    params.width = RelativeLayout.LayoutParams.FILL_PARENT;
                    playerView.setLayoutParams(params);
                    displayButtons = true;
                }
            }
        });

        /**
         * 后退按钮监听事件
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                mediaPlayer = null;
                VideoPlayActivity.this.finish();
            }
        });
    }


    /**
     * 更新进度条
     */
    Handler mHanlder = new Handler(){
        public void handleMessage(Message message){
            if (mediaPlayer == null){
                flag = false;
            } else if (mediaPlayer.isPlaying()){
                flag = true;
                int position = mediaPlayer.getCurrentPosition();
                int mMax = mediaPlayer.getDuration();
                int sMax = seekBar.getMax();
            } else {
                return;
            }
        }
    };


    class UpdateSeekBar implements Runnable{
        @Override
        public void run(){
            mHanlder.sendMessage(Message.obtain());
            if (flag){
                mHanlder.postDelayed(update, 1000);
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //建议回收
        System.gc();
    }

}
