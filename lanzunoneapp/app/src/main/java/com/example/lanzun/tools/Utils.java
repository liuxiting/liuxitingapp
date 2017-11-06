package com.example.lanzun.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.lanzun.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/9/14 0014.
 * 方法工具类，刘希亭
 */

public class Utils {

    /* 二次压缩，先按照像素压缩再按照质量压缩
    * @param imgUrl 图片路径
    * @param reqWidth 期望宽度 可以根据市面上的常用分辨率来设置
    * @param size 期望图片的大小，单位为kb
    * @param quality 图片压缩的质量，取值1-100，越小表示压缩的越厉害，如输入30，表示压缩70%
            * @return Bitmap 压缩后得到的图片
    */
    public static Bitmap compressBitmap(String imgUrl, int reqWidth, int size, int quality) {
        // 创建bitMap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight;
        reqHeight = (reqWidth * height) / width;
        // 在内存中创建bitmap对象，这个对象按照缩放比例创建的
        options.inSampleSize = calculateInSampleSize(
                options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(
                imgUrl, options);
        Bitmap mBitmap = compressImage(Bitmap.createScaledBitmap(
                bm, 600, reqHeight, false), size, quality);
        return mBitmap;
    }


    private static Bitmap compressImage(Bitmap image, int size, int options) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 计算像素压缩的缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /*
*bitmap转base64
*/
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = "";
        ByteArrayOutputStream bos = null;
        try {
            if (null != bitmap) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte[] bitmapByte = bos.toByteArray();
                result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static byte[] getbyte(File file) {
        final int BUFFER_SIZE = 0x300000;//缓冲区为3m
        MappedByteBuffer inputBuffer = null;
        byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容

        try {
            inputBuffer = new RandomAccessFile(file, "r")
                    .getChannel().map(FileChannel.MapMode.READ_ONLY, file.length() / 2
                            , file.length() / 2);


            long start = System.currentTimeMillis();

            for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

                if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

                    for (int i = 0; i < BUFFER_SIZE; i++)

                        dst[i] = inputBuffer.get(offset + i);

                } else {

                    for (int i = 0; i < inputBuffer.capacity() - offset; i++)
                        dst[i] = inputBuffer.get(offset + i);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("读取文件文件一半内容花费：" + (end - start) + "毫秒");
        } catch (IOException e) {
            e.printStackTrace();
        }

       return dst;

  }
    public static String getluyinfile(File file){
        FileInputStream is = null;
        // 获取文件大小
        long length = file.length();
        Log.i("多大",length+"/"+(int)length);
        // 创建一个数据来保存文件数据q
        byte[] fileData= new byte[1024];
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bytesRead=0;
        // 读取数据到byte数组中
        while(bytesRead != fileData.length) {
            try {
                bytesRead += is.read(fileData, bytesRead, fileData.length - bytesRead);
                if(is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String result= Base64.encodeToString(fileData, Base64.DEFAULT);
        return result;
    }
    /**
     *  ByteArrayInputStream ins = new ByteArrayInputStream(picBytes);
     * @param file
     * @return
     */
    public static String getByetsFromFile(File file){
        FileInputStream is = null;
        // 获取文件大小
        long length = file.length();
        Log.i("多大",length+"/"+(int)length);
        // 创建一个数据来保存文件数据
        byte[] fileData;
            fileData= new byte[1024*1024];

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bytesRead=0;
        // 读取数据到byte数组中
        while(bytesRead != fileData.length) {
            try {
                bytesRead += is.read(fileData, bytesRead, fileData.length - bytesRead);
                if(is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String result= Base64.encodeToString(fileData, Base64.DEFAULT);
        return result;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
   * 将时间转换为时间戳
   */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
    * 判断网络是否可以用
    * */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    * QMUIDialog提示框,文字
    * */
    public static void setdialog(QMUITipDialog qmuiTipDialog,Context context,String str){
        qmuiTipDialog = new QMUITipDialog.Builder(context)
                .setTipWord(str)
                .create();
        qmuiTipDialog.show();
        final QMUITipDialog finalQmuiTipDialog = qmuiTipDialog;
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finalQmuiTipDialog.dismiss();
                timer.cancel();
            }
        },2000);


    }
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
