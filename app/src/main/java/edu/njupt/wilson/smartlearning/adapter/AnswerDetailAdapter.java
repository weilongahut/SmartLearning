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
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.domain.AnswerViewHolder;

/**
 * <p>答案列表适配器</p>
 * Created by wilson on 15/10/21 下午9:36.
 * Belongs to SmartLearnning
 */
public class AnswerDetailAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private LinkedList<HashMap<String, Object>> list;

    /**
     * 构造函数
     * @param context
     */
    public AnswerDetailAdapter(Context context){
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
        final AnswerViewHolder answerViewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_answer, null);
            answerViewHolder = new AnswerViewHolder();
            answerViewHolder.userName = ((TextView) convertView.findViewById(R.id.answerUserName));
            answerViewHolder.comTime = ((TextView) convertView.findViewById(R.id.answerDate));
            answerViewHolder.comContent = ((TextView) convertView.findViewById(R.id.answerContent));
            convertView.setTag(answerViewHolder);
        } else {
            answerViewHolder = (AnswerViewHolder) convertView.getTag();
        }

        //为holder赋值
        answerViewHolder.userName.setText(getList().get(position).get("userName").toString());
        answerViewHolder.comTime.setText(getList().get(position).get("comTime").toString());
        answerViewHolder.comContent.setText(getList().get(position).get("comContent").toString());

        return convertView;
    }


    public LinkedList<HashMap<String, Object>> getList(){
        return list;
    }

    public void setList(LinkedList<HashMap<String, Object>> list){
        this.list = list;
    }

}
