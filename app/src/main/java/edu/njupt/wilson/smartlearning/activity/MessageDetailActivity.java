package edu.njupt.wilson.smartlearning.activity;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.LinkedList;

import edu.njupt.wilson.smartlearning.R;
import edu.njupt.wilson.smartlearning.application.ExitApplication;
import edu.njupt.wilson.smartlearning.dataSource.AnswerData;
import edu.njupt.wilson.smartlearning.popUpWindow.MorePopUpWindow;

/**
 * Author: wilson
 * Date: 2015-10-30 
 * Time: 18:34 
 * Description: 消息界面
 */

@EActivity(R.layout.activity_messagedetail)
public class MessageDetailActivity extends Activity {

    //更多菜单按钮
    @ViewById(R.id.morebt)
    ImageView morebt;

    //标题
    @ViewById(R.id.headTitle)
    TextView headTitle;

    //下拉刷新列表
    private PullToRefreshListView pullToRefreshListView;

    private SimpleAdapter adapter;

    //数据源
    private AnswerData data;

    private LinkedList<HashMap<String, Object>> list;

    private LayoutInflater inflater;

    private View views;

    @AfterViews
    void init(){
        //将此视图加入活动视图集合
        ExitApplication.getInstance().addActivity(this);

        //渲染视图
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.activity_messagedetail, null, false);

        headTitle.setText(R.string.messageTitle);

        pullToRefreshListView = (PullToRefreshListView) this.findViewById(R.id.messageDetailList);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                //Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                //Do work to refresh the list here
                //new AnwserGetDataTask(list, pullToRefreshListView, adapter, false).execute()
            }
        });

        adapter = new SimpleAdapter(this, list, R.layout.item_answer,
                new String[]{"name", "videoLength", "rating"},
                new int[]{R.id.answerUserName, R.id.answerDate, R.id.answerContent});

        ListView actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setAdapter(adapter);

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do work when the item been clicked
//                Intent intent = new Intent(view.getContext(), SelectReplyActivity_.class);
//                startActivity(intent);
            }
        });
    }

    @Click(R.id.returnbt)
    void returnbtIsClick(){
        MessageDetailActivity.this.finish();
    }

    //more按钮点击时弹出more窗口
    @Click(R.id.morebt)
    void morebtIsClick(){
        MorePopUpWindow morePopUpWindow = new MorePopUpWindow(MessageDetailActivity.this, views);
        morePopUpWindow.showPopupWindow(morebt);
    }


}
