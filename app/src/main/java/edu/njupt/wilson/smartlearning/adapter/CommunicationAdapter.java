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
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.asyncTask.AsyncImageTask;
import edu.njupt.wilson.smartlearning.domain.CommunicationViewHolder;
import edu.njupt.wilson.smartlearning.utils.URLUtil;

/**
 * <p> 沟通信息列表适配器 </p>
 * Created by wilson on 15/10/21 下午9:52.
 * Belongs to SmartLearnning
 */
public class CommunicationAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;

    private LinkedList<HashMap<String, Object>> list;

    private File cache;

    /**
     * 构造函数
     * @param context
     * @param cache
     */
    public CommunicationAdapter(Context context, File cache){
        this.layoutInflater = layoutInflater.from(context);
        this.cache = cache;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return getList().size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final CommunicationViewHolder communicationViewHolder;

        if (convertView == null ) {
            convertView = layoutInflater.inflate(R.layout.item_question, null);
            communicationViewHolder = new CommunicationViewHolder();
            communicationViewHolder.themeUserName = (TextView)convertView.findViewById(R.id.themeUserName);
            communicationViewHolder.themeUserHeadPhoto = (ImageView)convertView.findViewById(R.id.themeUserHeadPhoto);
            communicationViewHolder.themeDate = (TextView)convertView.findViewById(R.id.themeDate);
            communicationViewHolder.themeName = (TextView)convertView.findViewById(R.id.themeName);
            communicationViewHolder.themeContent = (TextView)convertView.findViewById(R.id.themContent);
            communicationViewHolder.themeCommentCount = (TextView)convertView.findViewById(R.id.themeCommentCount);
            convertView.setTag(communicationViewHolder);

        }else{
            communicationViewHolder = (CommunicationViewHolder)convertView.getTag();
        }

        communicationViewHolder.themeUserName.setText(getList().get(position).get("userName").toString());
        communicationViewHolder.themeDate.setText(getList().get(position).get("questionStartTime").toString());
        communicationViewHolder.themeName.setText(getList().get(position).get("questionTheme").toString());
        communicationViewHolder.themeContent.setText(getList().get(position).get("comContent").toString());
        communicationViewHolder.themeCommentCount.setText(getList().get(position).get("answerCount").toString());
        String path = URLUtil.getUrl(getList().get(position).get("userPhoto").toString());
        AsyncImageTask task = new AsyncImageTask(path, cache, communicationViewHolder.themeUserHeadPhoto);
        task.execute();

        return convertView;
    }

    public LinkedList<HashMap<String, Object>> getList() {
        return list;
    }

    public void setList(LinkedList<HashMap<String, Object>> list) {
        this.list = list;
    }
}
