package dhht.android.breakpointcontinue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import dhht.android.breakpointcontinue.manege.DownLoadManager;
import dhht.android.breakpointcontinue.sphelper.SpHelper;
import dhht.android.breakpointcontinue.thread.ThreadPoolUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTxt;
    private Button mBtnLoad;
    private Button mBtnPause;
    private int totalLength;
    private int startLength;
    private ProgressBar mProgressBar;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressBar.setProgress(msg.arg1);
                    mTxt.setText(((long) msg.arg1 * 100) / totalLength + "%");
                    break;
                case 1:
                    mProgressBar.setMax(msg.arg1);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void initView() {
        mTxt = findViewById(R.id.txt);
        mBtnLoad = findViewById(R.id.btn_load);
        mBtnPause = findViewById(R.id.btn_pause);
        mProgressBar = findViewById(R.id.progress);
        mBtnLoad.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
    }

    /**
     * 对各工具进行初始化
     */
    private void init() {
//        getStorageReadPermission();
//        getStorageWritePermission();
        verifyStoragePermissions(this);
        ThreadPoolUtil.getInstance().init();
        SpHelper.getInstance().init(this.getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                ThreadPoolUtil.getInstance().getCacheThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (SpHelper.getInstance().getLoadType().equals(Const.LOAD_TYPE.PAUSING)) {
                            startLength = SpHelper.getInstance().getLoadLength();
                        } else {
                            startLength = 0;
                        }
                        totalLength = DownLoadManager.getInstance().getCOnnectionLength(Const.URL);
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = totalLength;
                        mHandler.sendMessage(message);
                        DownLoadManager.getInstance().startDownload(startLength, totalLength, Const.URL, mHandler);
                        Log.d(MainActivity.TAG, "资源长度======" + totalLength);
                    }
                });
                break;
            case R.id.btn_pause:
                SpHelper.getInstance().saveLoadType(Const.LOAD_TYPE.PAUSING);
                break;
        }


    }

//    /**
//     * 获取手机SD卡读取权限
//     */
//    private void getStorageReadPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            //判断未授权时，动态授权
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                //判断andriod版本是否在6.0以上
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
//        }
//    }
//
//    /**
//     * 获取手机SD卡写入权限
//     */
//    private void getStorageWritePermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            //判断未授权时，动态授权
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                //判断andriod版本是否在6.0以上
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//            }
//        }
//    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 0);
        }
    }
}
