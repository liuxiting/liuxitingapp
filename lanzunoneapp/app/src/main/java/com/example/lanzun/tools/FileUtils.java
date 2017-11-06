package com.example.lanzun.tools;

import android.icu.text.Normalizer;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class FileUtils {

    /*
    * 上传大文件
    * */
    public static  void upBigFile(final String filePath, final String bigFileNme){
       // progressDialog.show();//显示进度条

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BigRandomAccessFile bigRandomAccessFile=new
                            BigRandomAccessFile(filePath + bigFileNme, 0);
                    long startPos = 0L;
                    Long length = bigRandomAccessFile.getFileLength();//得到文件的长度
                    int mBufferSize = 1024 * 100; //每次处理1024 * 100字节
                    byte[] buffer = new byte[mBufferSize];//创建一个mBufferSize大小的缓存数组
                    BigRandomAccessFile.Detail detail;//文件的详情类
                    long nRead = 0l;//读取文件的当前长度
                    String vedioFileName = bigFileNme; //分配一个文件名
                    long nStart = startPos;//开始读的位置
                    while (nStart < length) {//当开始都的位置比长度小的时候
                       // progressDialog.setProgress((int) ((nStart/(float)length)*100));//设置progressDialog的进度

                        detail = bigRandomAccessFile.getContent(startPos,mBufferSize);//开始读取文件
                        nRead = detail.length;//读取的文件的长度
                        buffer = detail.b;//读取文件的缓存
                        Message message = Message.obtain();
                        String result = "";//上传的结果
                        try {
                              //上传大文件
                           /* QueryUploadUtil quu = new QueryUploadUtil(BigBase64Util.getBase64String(buffer), bigFileName);//将数据进行上传
                            result = quu.call(wsdl, bigUrl,"BigFileUploadByBase64String");*/

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        nStart += nRead;//下一次从哪里开始读取
                        startPos = nStart;

                       /* message.obj = result;//返回的结果
                        handler.sendMessage(message);*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    * 上传方法
    * */
    public class QueryUploadUtil{
        private String filename, base64string;

        public QueryUploadUtil(String base64str, String fileName) {
            this.filename = fileName;
            this.base64string = base64str;
        }
        //需要实现Callable的Call方法
        public String call(String url,String methodName){
            String str="";
            SoapObject  rpc=new SoapObject("http://tempuri.org/",methodName);
            //设置参数
            rpc.addProperty("",base64string);
            rpc.addProperty("filename",filename);
            //创建SoapSerializationEnvelope对象，并指定版本号
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope
                    (SoapEnvelope.VER12);
              //设置bodyOut属性
            envelope.dotNet=true;
            envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht=new HttpTransportSE(url);
            ht.debug=false;
            try {
                ht.call(url,envelope);
                String result=String.valueOf(envelope.getResponse());
                if (result.toString().equals("true")){
                    //上传成功
                   SoapObject resultSoapObject = (SoapObject) envelope.bodyIn;
                    return resultSoapObject.getProperty(0).toString();
                }else {
                    //上传失败
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return str;
        }
    }


}
