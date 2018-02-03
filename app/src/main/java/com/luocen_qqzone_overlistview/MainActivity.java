package com.luocen_qqzone_overlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ParallaxListView mListView;
    private ImageView iv;
    private ImageView iv_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        View header=View.inflate(this,R.layout.header_view,null);
        mListView=findViewById(R.id.lv);
        iv=header.findViewById(R.id.iv_header);
        iv_icon=header.findViewById(R.id.iv_icon);
        ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                new String[]{
                        "星期一   去动脑学院上课",
                        "星期二   去有心课堂上课",
                        "星期三   去慕课网上课",
                        "星期四   去极客学院上课",
                        "星期五   去菜鸟教程上课",
                        "星期六   去泡在网上的日子上课",
                        "星期日   去Android开发中文站上课",
                });
        mListView.addHeaderView(header);
        mListView.setZoomImageView(iv);
        mListView.setHeaderImageView(iv_icon);
        mListView.setAdapter(adapter);
    }
}
