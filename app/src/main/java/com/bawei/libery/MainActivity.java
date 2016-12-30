package com.bawei.libery;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.bwei.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {

    private ArrayAdapter<String> arrayAdapter;
    private List<String> items = new ArrayList<>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private XListView xlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getItems();
        xlv = (XListView) findViewById(R.id.xlv);
        xlv.setPullLoadEnable(true);
        xlv.setPullRefreshEnable(true);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        xlv.setAdapter(arrayAdapter);
        xlv.setXListViewListener(this);
        mHandler = new Handler();
    }

    private void getItems() {
        for (int i = 0; i < 15; i++) {

            items.add("refresh on" + (++start));
        }
    }


    //重新加载
    private void onLoad() {
        xlv.stopRefresh();
        xlv.stopLoadMore();
        long time = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
        Date date = new Date(time);
        String strTime = dateFormat.format(date);
        xlv.setRefreshTime(strTime);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                items.clear();
                getItems();

                // mAdapter.notifyDataSetChanged();
                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, items);
                xlv.setAdapter(arrayAdapter);
                onLoad();
            }
        }, 2000);
    }

    //上拉加载
    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getItems();
                arrayAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
}
