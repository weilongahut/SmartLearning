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

import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by wilson on 15/10/21.
 * 反射工具类，将对象转换为HashMap
 */
public class ObjectMapReflect {

    public static HashMap<String, Object> changeToMap (Object obj){
        HashMap<String, Object> map = new HashMap<String, Object>();

        try{
            Class cls = obj.getClass();
            Method [] m = cls.getDeclaredMethods();
            for (int i = 0; i < m.length; i++){
                if (m[i].getName().startsWith("get")){
                    String field = m[i].getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    map.put(field, m[i].invoke(obj, new Object[0]));
                }
            }
        } catch (Throwable e) {
            Log.e("ObjectMapReflect", "对象反射解析出错", e);
        }
        return map;
    }
}
