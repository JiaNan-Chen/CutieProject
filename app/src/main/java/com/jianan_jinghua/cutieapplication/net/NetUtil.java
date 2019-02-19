package com.jianan_jinghua.cutieapplication.net;

import android.util.Log;
import com.jianan_jinghua.cutieapplication.CommonUtil;
import okhttp3.*;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetUtil {


    public static void getUpdateInfo(String url, final String fileName) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        final Request request = new Request.Builder().url("http://203.195.224.187/TestPro/app/aaa.zip")
                .method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                EventBus.getDefault().post(e.toString());
                Log.i("myokhttp", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                EventBus.getDefault().post(response.body().string());
                Log.i("myokhttp", "start");
                Log.i("myokhttp", response.body().contentLength() + "");
                response.body().byteStream();

                InputStream is = response.body().byteStream();
                byte[] buf = new byte[2048];
                int len;
                long size = response.body().contentLength();
                FileOutputStream fos = null;
                //储存下载文件的目录
                File dst = new File(CommonUtil.getExternalPath(), fileName);
                if (dst.exists()) {
                    dst.delete();
                }
                fos = new FileOutputStream(dst);
                long sum = 0;

                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / size * 100);
                    Log.i("progress", progress + "%");
                    //下载中更新进度条
//                    listener.onDownloading(progress);
                }
                fos.flush();
                Log.i("myokhttp", "end");
            }
        });
    }
}
