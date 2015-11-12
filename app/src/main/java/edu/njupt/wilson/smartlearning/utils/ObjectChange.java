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

import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.domain.CommentResult;
import edu.njupt.wilson.smartlearning.domain.CommunicationResult;
import edu.njupt.wilson.smartlearning.domain.CourseResult;
import edu.njupt.wilson.smartlearning.domain.VideoResult;

/**
 * Created by wilson on 15/10/21.
 * 调用ObjectMapReflect，将list中的对象转换成HashMap后，再将list返回
 */
public class ObjectChange {

    private static LinkedList<HashMap<String, Object>> list;
    private static HashMap<String, Object> map;

    /**
     * 转换课程对象
     * @param temp
     * @return
     */
    public static LinkedList<HashMap<String, Object>> changeCourse(String temp){

        LinkedList<HashMap<String, Object>> listCource = new LinkedList<HashMap<String, Object>>();
        map = new HashMap<String, Object>();

        String [] array = temp.split("\\}, ");
        for (int i = 0; i < array.length; i++){
            String str = "";
            if (i < array.length - 1){
                str = array[i] + "}";
            } else {
                str = array[i];
            }
            CourseResult courseResult = JsonUtil.fromObject(str, CourseResult.class);
            map = ObjectMapReflect.changeToMap(courseResult);
            listCource.add(map);
        }

        return listCource;
    }

    /**
     * 转换视频对象
     * @param temp
     * @return
     */
    public static LinkedList<HashMap<String,Object>> changeVideoInfo( String temp){
        LinkedList<HashMap<String,Object>> listVideoInfo = new LinkedList<HashMap<String, Object>>();
        map = new HashMap<String, Object>();

        String[] array = temp.split("\\}, ");
        for (int i = 0; i < array.length; i++) {
            String str = "";
            if (i < array.length - 1) {
                str = array[i] + "}";
            } else {
                str = array[i];
            }
            VideoResult videoResult = JsonUtil.fromObject(str, VideoResult.class);
            map = ObjectMapReflect.changeToMap(videoResult);
            listVideoInfo.add(map);
        }
        return listVideoInfo;
    }

    /**
     *
     * @param temp
     * @return
     */
    public static LinkedList<HashMap<String,Object>> changeAllQuestion(String  temp){
        list = new LinkedList<HashMap<String, Object>>();
        map = new HashMap<String, Object>();

        String[] array = temp.split("\\}, ");
        for (int i = 0; i < array.length; i++) {
            String str = "";
            if (i < array.length - 1) {
                str = array[i] + "}";
            } else {
                str = array[i];
            }
            CommunicationResult communicationResult = JsonUtil.fromObject(str, CommunicationResult.class);
            map = ObjectMapReflect.changeToMap(communicationResult);
            list.add(map);
        }
        return list;
    }

    /**
     *
     * @param temp
     * @return
     */
    public static LinkedList<HashMap<String,Object>> changeAllAnswer(String temp){
        list = new LinkedList<HashMap<String, Object>>();
        map = new HashMap<String, Object>();

        String[] array = temp.split("\\}, ");
        for (int i = 0; i < array.length; i++) {
            String str = "";
            if (i < array.length - 1) {
                str = array[i] + "}";
            } else {
                str = array[i];
            }
            CommentResult allQuestionResult = JsonUtil.fromObject(str, CommentResult.class);
            map = ObjectMapReflect.changeToMap(allQuestionResult);
            list.add(map);
        }
        return list;
    }

}
