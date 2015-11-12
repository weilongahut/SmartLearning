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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * URL工具类
 * Created by wilson on 15/10/21.
 */
public class URLUtil {

    //服务器地址
    public static final String root = "http://192.168.1.102:8080/InteractTeachingSys/api/";

    /**
     * 获取绝对路径
     * @param path 相对路径
     * @return 绝对路径
     */
    public static final String getUrl(String path){
        return root + path;
    }


    /**
     * 获取媒体类型
     * @return
     */
    public static final HttpHeaders getMediaType(){
        MediaType type = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        return headers;
    }
}
