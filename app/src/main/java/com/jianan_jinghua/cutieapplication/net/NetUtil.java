package com.jianan_jinghua.cutieapplication.net;

import android.os.Build;
import android.util.Log;
import com.google.gson.Gson;
import com.jianan_jinghua.cutieapplication.CommonUtil;
import com.jianan_jinghua.cutieapplication.object.DownLoadSuccessMsg;
import com.jianan_jinghua.cutieapplication.object.EntranceInfo;
import com.jianan_jinghua.cutieapplication.object.ProgressInfo;
import okhttp3.*;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetUtil {

    public static void getUpdateInfo() {
        getBaseGetRequest(NetConstant.SERVLET_IP
                + NetConstant.SERVICE_NAME
                + NetConstant.SERVICE_UPDATE + "?version=1.0.0", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new Gson().fromJson(response.body().string(),
                            EntranceInfo.class));
                }
            }
        });
    }

    public static void getUpdateInfo(String url, final String fileName) {
        getBaseGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                EventBus.getDefault().post(e.toString());
                Log.i("myokhttp", e.toString());
                EventBus.getDefault().post("下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                EventBus.getDefault().post(response.body().string());
                if (response.code() != 200) {
                    EventBus.getDefault().post("暂时没有新版本");
                    return;
                }
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
                    EventBus.getDefault().post(new ProgressInfo(progress));
                    //下载中更新进度条
//                    listener.onDownloading(progress);
                }
                fos.flush();
                EventBus.getDefault().post(new DownLoadSuccessMsg());
                Log.i("myokhttp", "end");
            }
        });
    }

    public static void getBaseGetRequest(String url, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        final Request request = new Request.Builder().url(url)
                .method("GET", null).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    interface ErrorCallback {
        void error(Call call, IOException e);
    }

    interface SuccessCallback {
        void success(Response response);
    }
}
