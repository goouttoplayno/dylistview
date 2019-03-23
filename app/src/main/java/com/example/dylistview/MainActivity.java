package com.example.dylistview;

import android.content.SyncStatusObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import javax.net.ssl.HandshakeCompletedEvent;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
//    Vector<News> news = new Vector<News>();
    ArrayList<News> news = new ArrayList<News>();
    Myadapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView1);

        listView.setOnScrollListener(new ListViewListener());
        View footer = getLayoutInflater().inflate(R.layout.activity_main_load, null);
        listView.addFooterView(footer);

        new loadDataThread().start();

        myadapter = new Myadapter();
        listView.setAdapter(myadapter);
    }

    private class News {
        String title;
        String content;
    }
    int index = 1;
    void initData(){
        System.out.println("initData");
        for (int i = 0; i < 15; i++){
            News n = new News();
            n.title = "title-" + index;
            n.content = "content" + index;
            index++;
            news.add(n);
        }
    }
    int i = 0;
    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.acitvity_main_item, null);
                viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.textView1_title);
                viewHolder.tvContent = (TextView)convertView.findViewById(R.id.textView2_content);
                convertView.setTag(viewHolder);
            }else {
                //
                viewHolder = (ViewHolder)convertView.getTag();
            }
            //
            News n = news.get(position);
            viewHolder.tvTitle.setText(n.title);
            viewHolder.tvContent.setText(n.content);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
    }

    int visibleLastIndex = 0;
    private class ListViewListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if((visibleLastIndex == myadapter.getCount()) && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)){
                new loadDataThread().start();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }
    }

    private class loadDataThread extends Thread{
        @Override
        public void run() {
            initData();
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    myadapter.notifyDataSetChanged();
                    break;
                 default:
                     break;
            }
        }
    };
}
