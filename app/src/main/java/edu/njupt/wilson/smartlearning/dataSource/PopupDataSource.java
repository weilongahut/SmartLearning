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

package edu.njupt.wilson.smartlearning.dataSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;

/**
 * <p>右上角更多菜单的数据源</p>
 * Created by wilson on 15/10/22 下午1:55.
 * Belongs to SmartLearnning
 */
public class PopupDataSource implements Serializable {

    public static LinkedList<HashMap<String, Object>> getData_1(){
        LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("popupImage", R.drawable.defalt_head);
        map1.put("popupText", "wilson");

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("popupImage", R.drawable.default_about);
        map2.put("popupText", "关于");

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("popupImage", R.drawable.ofm_blacklist_icon);
        map3.put("popupText", "退出");

        list.add(map1);
        list.add(map2);
        list.add(map3);

        return list;
    }


    public static LinkedList<HashMap<String, Object>> getData_2(){
        LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("popupImage", R.drawable.default_about);
        map2.put("popupText", "关于");

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("popupImage", R.drawable.ofm_blacklist_icon);
        map3.put("popupText", "退出");

        list.add(map2);
        list.add(map3);

        return list;
    }

}
