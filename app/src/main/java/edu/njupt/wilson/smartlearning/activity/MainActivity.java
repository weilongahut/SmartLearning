package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.Timer;
import java.util.TimerTask;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.ExitApplication;


@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @AfterViews
    public void init(){
        ExitApplication.getInstance().addActivity(this);
        //提示
        System.out.print("Have been here!");
        final Intent intent = new Intent(this, LoginActivity_.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                MainActivity.this.finish();
            }
        };
        
        timer.schedule(task, 1500);

    }

}
