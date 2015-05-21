package com.yukti.housedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class LungNoduleActivity extends Activity {

    private TextView mTextView;
    private Intent mIntent;
    private TopicModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_nodule);

        mIntent = getIntent();
        module = (TopicModule) mIntent.getSerializableExtra("TopicModule");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText("This module:" + module.toString());
            }
        });
    }
}
