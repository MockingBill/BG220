package com.illidan.dengqian.bg220.http_conn;


import android.app.DownloadManager;
import android.util.Log;

import com.illidan.dengqian.bg220.tool_bean.information;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;


import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dengqian on 2018/3/27.
 */

public class connNetReq {


    /**
     * post发送一个http请求，该方法会抛出一个异常。
     *
     * @param address
     * @param info
     * @return
     * @throws Exception
     */

    public static String post(String address, String info) throws Exception {

        //info = URLEncoder.encode(info);
        Log.e("json", info);

        info = AesAndToken.encrypt(info, AesAndToken.KEY);
        byte[] data = info.getBytes();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        //这是请求方式为POST
        conn.setRequestMethod("POST");
        //设置post请求必要的请求头

        conn.setRequestProperty("Authorization", AesAndToken.md5());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 请求头, 必须设置
        conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度
        conn.setDoOutput(true); // 准备写出
        conn.getOutputStream().write(data); // 写出数据
        InputStream inptStream = conn.getInputStream();
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] dataReceive = new byte[1024];
        int len = 0;
        try {
            while ((len = inptStream.read(dataReceive)) != -1) {
                byteArrayOutputStream.write(dataReceive, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        if (conn.getResponseCode() == 200)
            return resultData;
        else
            return "err";
    }


    public static versionInfo jsonToVersionInfo(String str) {
        versionInfo version = new versionInfo();
        try {
            JSONObject jsonObject = new JSONObject(str);
            version.setAppName(jsonObject.get("appName").toString());
            version.setServerVersion(jsonObject.get("serverVersion").toString());
            version.setUpdateUrl(jsonObject.get("updateUrl").toString());
            version.setUpgradeinfo(jsonObject.get("upgradeinfo").toString());
        } catch (Exception e) {
            Log.e("jsonToVersionInfoError", e.toString());
            return new versionInfo(e);
        }


        return version;
    }

    // 使用OkHttp上传文件
    public static String uploadFile(File file,String upload_path) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody=RequestBody.create(file,MediaType.parse("audio/wav"));
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar",file.getName(),formBody)
                .build();
        Request request=new Request.Builder()
                .url(upload_path)
                .addHeader("Authorization", AesAndToken.md5())
                .addHeader("token",AesAndToken.md5()+"1366")
                .post(requestBody)
                .build();
        Response response=null;
        try{
             response=client.newCall(request).execute();
            return response.body().toString();
        }catch (Exception e){
            Log.e("wav upload",e.toString());
            return "0";
        }


    }


}
