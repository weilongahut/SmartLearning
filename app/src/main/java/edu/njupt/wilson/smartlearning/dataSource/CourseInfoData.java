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

/**
 * <p>ListView数据源</p>
 * Created by wilson on 15/10/22 下午1:53.
 * Belongs to SmartLearnning
 */
public class CourseInfoData implements Serializable{

    private LinkedList<HashMap<String, Object>> data;

    public LinkedList<HashMap<String, Object>> getData() {
        return data;
    }

    public void setData(LinkedList<HashMap<String, Object>> data) {
        this.data = data;
    }
}
