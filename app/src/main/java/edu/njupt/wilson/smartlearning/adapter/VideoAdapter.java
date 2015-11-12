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
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.domain.VideoViewHolder;

/**
 * <p> </p>
 * Created by wilson on 15/10/22 上午9:38.
 * Belongs to SmartLearnning
 */
public class VideoAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;

    private List<HashMap<String, Object>> list;

    /**
     * 构造函数
     * @param context
     */
    public VideoAdapter(Context context){
        this.layoutInflater = layoutInflater.from(context);
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

        final VideoViewHolder videoViewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_videoinfo, null);
            videoViewHolder = new VideoViewHolder();
            videoViewHolder.videoName = (TextView) convertView.findViewById(R.id.videoInfoName);
            videoViewHolder.videoLength = (TextView) convertView.findViewById(R.id.videoInfoVideoLength);

            convertView.setTag(videoViewHolder);
        } else {
            videoViewHolder = (VideoViewHolder) convertView.getTag();
        }

        videoViewHolder.videoName.setText(getList().get(position).get("videoName").toString());
        videoViewHolder.videoLength.setText(getList().get(position).get("videoLength").toString());

        return convertView;
    }

    public List<HashMap<String, Object>> getList() {
        return list;
    }

    public void setList(List<HashMap<String, Object>> list) {
        this.list = list;
    }
}
