package dhht.android.breakpointcontinue;

import android.os.Environment;

import java.io.File;

/**
 * Created by AndroidXJ on 2020/4/7.
 */
public class Const {
    /**
     * 下载文件地址
     */
    public static final String URL = "https://gdown.baidu.com/data/wisegame/0d5a2f3c0e6b889c/qunaerlvxing_146.apk";
    public static final String URL_ODD = "https";
    /**
     * 线程数量
     */
    public static final int THREAD_COUNT = 3;
    public static final int FIX_POOL_COUNT = 5;

    /**
     * 存储文件基础路径
     */
    public static final String FILE_BASE_URI = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * HTTP读取超时时间
     */
    public static final int READ_TIME_OUT = 10 * 1000;
    /**
     * HTTP连接超时时间
     */
    public static final int CONNECT_IME_OUT = 10 * 1000;


    public interface LOAD_TYPE {
        /**
         * 下载中
         */
        String LOADING = "1";
        /**
         * 暂停中
         */
        String PAUSING = "2";
        /**
         * 已完成
         */
        String COMPLETING = "3";
    }

}
