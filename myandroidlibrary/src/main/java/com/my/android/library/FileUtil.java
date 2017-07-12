package com.my.android.library;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class FileUtil {

    /**
     * 获取SD卡路径
     */
    public static String GetFileRootPath(Context context) throws Exception {
        File path = null;
        if (IsSdcardExists()) {// sd卡可以操作
            path = Environment.getExternalStorageDirectory();// sd卡根路径
        } else {
            path = context.getFilesDir();// 手机存储默认路径
        }
        return path.toString();
    }

    /**
     * 判断sd卡是否可用
     */
    public static boolean IsSdcardExists() throws Exception {
        boolean tags = false;
        String zt = Environment.getExternalStorageState();// 状态
        if (zt.equals(Environment.MEDIA_MOUNTED)) {// sdcard是可以操作
            tags = true;
        }
        return tags;
    }

    /**
     * 保存图片到SD卡。未测试###############
     */
    @SuppressLint("CommitPrefEdits")
    public static void saveBitmapToFile(Bitmap bitmap, String _filename,
                                        Context content) throws IOException {
        BufferedOutputStream os = null;
        try {
            String pathString = GetFileRootPath(content) + "/" + _filename;
            File file = new File(pathString);
            int end = _filename.lastIndexOf(File.separator);
            String _filePath = _filename.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            } else {
                file.createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 获取文件夹大小
     */
    protected long getFolderSize(File file) {

        File[] filelist = file.listFiles();
        long sizes = 0;
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                sizes = sizes + getFolderSize(filelist[i]);
            } else {
                sizes = sizes + filelist[i].length();
            }
        }
        return sizes;
    }

    /**
     * 删除文件夹
     */
    protected void deletefolderfile(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    deletefolderfile(fileList[i].getAbsolutePath());
                }
            }
            if (!file.isDirectory()) {
                file.delete();
            } else {
                if (file.listFiles().length == 0) {
                    file.delete();
                }
            }
        }
        ;
    }

    /**
     * 格式化单位
     */
    protected String getFormatSize(double size) {
        double kilobyte = size / 1024;
        if (kilobyte < 1) {
            return size + "B";
        }
        double megabyte = kilobyte / 1024;
        if (megabyte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kilobyte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigabyte = megabyte / 1024;
        if (gigabyte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megabyte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double terabyte = gigabyte / 1024;
        if (terabyte < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigabyte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(Double.toString(terabyte));
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
