package dhht.android.breakpointcontinue.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dhht.android.breakpointcontinue.Const;

/**
 * Created by AndroidXJ on 2020/4/7.
 */
public class ThreadPoolUtil {
    private ExecutorService mCacheService;
    private ExecutorService mFixService;

    private static ThreadPoolUtil sInstance;

    public static ThreadPoolUtil getInstance() {
        synchronized (ThreadPoolUtil.class) {
            if (sInstance == null) {
                sInstance = new ThreadPoolUtil();
            }
            return sInstance;
        }
    }


    public void init() {
        mCacheService = Executors.newCachedThreadPool();
        mFixService = Executors.newFixedThreadPool(Const.FIX_POOL_COUNT);
    }

    public ExecutorService getCacheThreadPool() {
        return mCacheService;
    }

    public ExecutorService getFixThreadPool() {
        return mFixService;
    }


}
