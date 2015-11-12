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

package edu.njupt.wilson.smartlearning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.asyncTask.AsyncImageTask;
import edu.njupt.wilson.smartlearning.domain.CourseViewHolder;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p>课程列表适配器</p>
 * Created by wilson on 15/10/22 上午9:12.
 * Belongs to SmartLearnning
 */
public class CourseAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<HashMap<String, Object>> list;

    private File cache;

    /**
     * 构造函数
     * @param context
     * @param cache
     */
    public CourseAdapter(Context context, File cache){
        this.layoutInflater = layoutInflater.from(context);
        this.cache = cache;
    }

    @Override
    public int getCount(){
        return getList().size();
    }

    @Override
    public Object getItem(int position){
        return getList().get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final CourseViewHolder courseViewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_courseinfo, null);
            courseViewHolder = new CourseViewHolder();
            courseViewHolder.courseName = (TextView) convertView.findViewById(R.id.courseInfoName);
            courseViewHolder.coursePhoto = (ImageView) convertView.findViewById(R.id.courseInfoPhoto);

            convertView.setTag(courseViewHolder);
        } else {
            courseViewHolder = (CourseViewHolder) convertView.getTag();
        }

        courseViewHolder.courseName.setText(getList().get(position).get("courseName").toString());
        String path = URLUtil.getUrl(getList().get(position).get("coursePhoto").toString());
        AsyncImageTask task = new AsyncImageTask(path, cache, courseViewHolder.coursePhoto);
        task.execute();

        return convertView;
    }


    public List<HashMap<String, Object>> getList() {
        return list;
    }

    public void setList(List<HashMap<String, Object>> list) {
        this.list = list;
    }
}
