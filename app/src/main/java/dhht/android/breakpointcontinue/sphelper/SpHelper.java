package dhht.android.breakpointcontinue.sphelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.contentcapture.ContentCaptureSession;

import dhht.android.breakpointcontinue.Const;

/**
 * Created by AndroidXJ on 2020/4/7.
 */
public class SpHelper {
    private static final String NAME = "config";
    private Context mContext;
    private SharedPreferences mSp;

    private static SpHelper sInstance;

    public static SpHelper getInstance() {
        synchronized (SpHelper.class) {
            if (sInstance == null) {
                sInstance = new SpHelper();
            }
            return sInstance;
        }
    }

    public void init(Context context) {
        this.mContext = context;
        mSp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSp() {
        return mSp;
    }

    /**
     * 保存下载状态
     */
    public void saveLoadType(String type) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(Const.URL_ODD, type);
        editor.apply();
    }

    /**
     * 获取当前下载状态
     *
     * @return
     */
    public String getLoadType() {
        return mSp.getString(Const.URL_ODD, "");
    }

    /**
     * 保存下载进度
     *
     * @param length
     */
    public void saveLoadLength(int length) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.URL, length);
        editor.apply();
    }

    /**
     * 获取当前下载进度
     *
     * @return
     */
    public int getLoadLength() {
        return mSp.getInt(Const.URL, 0);
    }
}
