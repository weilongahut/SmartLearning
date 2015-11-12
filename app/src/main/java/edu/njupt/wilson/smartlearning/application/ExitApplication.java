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

package edu.njupt.wilson.smartlearning.application;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wilson on 15/10/21.
 * 完全退出应用程序,单例模式
 */
public class ExitApplication extends Application{

    private List<Activity> activities = null;

    private static ExitApplication instance;

    private ExitApplication(){
        activities = new LinkedList<Activity>();
    }

    /**
     * 获取单例对象
     * @return ExitApplication的实例
     */
    public static ExitApplication getInstance(){
        if (null == instance){
            instance = new ExitApplication();
        }
        return instance;
    }

    /**
     * 添加存活Activity
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activities != null && activities.size() > 0){
            if (!activities.contains(activity)){
                activities.add(activity);
            }
        } else {
            activities.add(activity);
        }
    }

    /**
     * 清除所有存活Activity并退出应用程序
     */
    public void exit(){
        if (activities != null && activities.size() > 0){
            for (Activity activity : activities) {
                activity.finish(); //结束activiy
            }
        }

        //缓存
        File cahe = new File(Environment.getExternalStorageDirectory(), "cache");

        //clear the cahe
        File [] files = cahe.listFiles();
        for (File file : files){
            file.delete();
        }
        System.exit(0);

    }
}

