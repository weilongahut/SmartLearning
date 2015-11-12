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

package edu.njupt.wilson.smartlearning.utils;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wilson on 15/10/21.
 */
public class ImageURIUtil {
    /**
     * 从网络上获取图片，如果图片在本地有缓存直接去本地备份，如果不存在再去服务器上下载图片
     * 这里的path是图片的地址
     */
    public static Uri getImageURI(String path, File cache){
        String name = MD5Util.MD5(path) + path.substring(path.lastIndexOf("."));
        final File file = new File(cache, name);
        final String root = path;
        //如果图片在本地有缓存，则不去服务器下载
        if (file.exists()){
            return Uri.fromFile(file); //Uri.fromFile(path) 可以获取该文件的URI
        } else {
            //从网络上下载图片
            try{
                URL url = new URL(root);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000); //超时时长不应超过10秒，否则可能会被Android系统收回
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() != 200){
                    throw new RuntimeException("请求图片失败@" + root);
                }
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte [] buffer = new byte[1024];
                int len = -1;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            } catch (Exception e){
                Log.e("ImageURIUtil", "图片加载失败！");
            }
            //返回一个URI对象
            return Uri.fromFile(file);
        }
    }
}
