package com.jianan_jinghua.cutieapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.jianan_jinghua.cutieapplication.net.NetConstant;
import com.jianan_jinghua.cutieapplication.net.NetUtil;
import com.jianan_jinghua.cutieapplication.object.DownLoadSuccessMsg;
import com.jianan_jinghua.cutieapplication.object.EntranceInfo;
import com.jianan_jinghua.cutieapplication.object.ProgressInfo;
import com.jianan_jinghua.cutieapplication.object.ToastMsg;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdate(DownLoadSuccessMsg msg) {
//        Log.i("msg", str);
//        ((TextView) findViewById(R.id.tv)).setText(str);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        intent.setDataAndType(Uri.fromFile(new File(CommonUtil.getExternalPath(), "lijinghua.apk")), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getProgress(ProgressInfo progressInfo) {
        ((ProgressBar) findViewById(R.id.main_update_progress)).setProgress(progressInfo.percent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEntranceInfo(EntranceInfo entranceInfo) {
        StaticMember.DOWNLOAD_PATH = entranceInfo.url;
        findViewById(R.id.main_update_corner).setVisibility(
                !entranceInfo.url.equals("") ? View.VISIBLE : View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getString(String msg) {
//        Log.i("msg", str);
//        ((TextView) findViewById(R.id.tv)).setText(str);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        CommonUtil.checkPermission(this);
        findViewById(R.id.main_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StaticMember.DOWNLOAD_PATH == null || StaticMember.DOWNLOAD_PATH.equals("")) {
                    Toast.makeText(MainActivity.this, "暂时没有新版本", Toast.LENGTH_SHORT).show();
                } else {
                    NetUtil.getUpdateInfo(NetConstant.SERVLET_IP + StaticMember.DOWNLOAD_PATH, "lijinghua.apk");
                }
            }
        });
        NetUtil.getUpdateInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
