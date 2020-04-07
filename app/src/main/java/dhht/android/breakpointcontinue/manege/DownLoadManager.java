package dhht.android.breakpointcontinue.manege;


import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dhht.android.breakpointcontinue.Const;
import dhht.android.breakpointcontinue.MainActivity;
import dhht.android.breakpointcontinue.sphelper.SpHelper;
import dhht.android.breakpointcontinue.thread.Download;
import dhht.android.breakpointcontinue.thread.ThreadPoolUtil;

/**
 * Created by AndroidXJ on 2020/4/7.
 * 下载文件管理类
 */
public class DownLoadManager {

    private static DownLoadManager sInstance;

    public static DownLoadManager getInstance() {
        synchronized (DownLoadManager.class) {
            if (sInstance == null) {
                sInstance = new DownLoadManager();
            }
            return sInstance;
        }
    }

    public void startDownload(int start, int length, final String path, Handler handler) {
        File file = new File(Const.FILE_BASE_URI + "test.apk");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        } else {
            if (SpHelper.getInstance().getLoadType().equals(Const.LOAD_TYPE.COMPLETING)) {
                file.getParentFile().delete();
                file.getParentFile().mkdirs();
            }
        }
        ThreadPoolUtil.getInstance().getCacheThreadPool().execute(new Download(start, length, file, path, handler));
    }

    public int getCOnnectionLength(String path) {
        int contentLength = -1;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(Const.READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(Const.CONNECT_IME_OUT);
            contentLength = httpURLConnection.getContentLength();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return contentLength;
    }


}
