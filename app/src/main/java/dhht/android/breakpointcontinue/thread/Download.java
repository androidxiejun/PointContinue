package dhht.android.breakpointcontinue.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import dhht.android.breakpointcontinue.Const;
import dhht.android.breakpointcontinue.MainActivity;
import dhht.android.breakpointcontinue.sphelper.SpHelper;

/**
 * Created by AndroidXJ on 2020/4/7.
 */
public class Download implements Runnable {
    private int start;
    private int length;
    private File mFile;
    private String mURL;
    private Handler mHandler;

    public Download(int start, int length, File file, String url, Handler handler) {
        this.start = start;
        this.length = length;
        this.mFile = file;
        this.mURL = url;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        BufferedInputStream is = null;
        RandomAccessFile randomAccessFile = null;
        HttpURLConnection connection = null;
        if (mURL != null) {
            try {
                if (SpHelper.getInstance().getLoadType().equals(Const.LOAD_TYPE.LOADING)) {
                    Log.d(MainActivity.TAG, "正在下载------------");
                    return;
                }
                SpHelper.getInstance().saveLoadType(Const.LOAD_TYPE.LOADING);
                URL url = new URL(mURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(Const.CONNECT_IME_OUT);
                connection.setReadTimeout(Const.READ_TIME_OUT);
                connection.setRequestProperty("Range", "bytes=" + start + "-" + length);
                randomAccessFile = new RandomAccessFile(mFile, "rw");
                randomAccessFile.seek(start);
                is = new BufferedInputStream(connection.getInputStream());
                byte[] data = new byte[1024 * 1024];
                int total = start;
                int length = 0;
                while ((length = is.read(data)) > 0) {
                    if (SpHelper.getInstance().getLoadType().equals(Const.LOAD_TYPE.PAUSING)) {
                        return;
                    }
                    randomAccessFile.write(data, 0, length);
                    total += length;
                    SpHelper.getInstance().saveLoadLength(total);
                    sendMessage(total);
                }
                Log.d(MainActivity.TAG, "total: " + total);
                SpHelper.getInstance().saveLoadType(Const.LOAD_TYPE.COMPLETING);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(MainActivity.TAG, "出错了-------" + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendMessage(int total) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = 0;
            message.arg1 = total;
            message.setTarget(mHandler);
            message.sendToTarget();
        }

    }
}
