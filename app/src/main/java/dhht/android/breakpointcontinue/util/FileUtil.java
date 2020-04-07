package dhht.android.breakpointcontinue.util;

import java.io.File;

/**
 * Created by AndroidXJ on 2020/4/7.
 */
public class FileUtil {
    private static FileUtil sInstance;

    public static FileUtil getInstance() {
        synchronized (FileUtil.class) {
            if (sInstance == null) {
                sInstance = new FileUtil();
            }
            return sInstance;
        }
    }

    /**
     * 检查文件是否存在
     *
     * @return
     */
    public boolean checkFileExit(String fileName) {
        File file = new File(fileName);
        return file.getParentFile().exists();
    }
}
