package com.jianan_jinghua.cutieapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getString(String str) {
//        Log.i("msg", str);
//        ((TextView) findViewById(R.id.tv)).setText(str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        CommonUtil.checkPermission(this);
    }
}
