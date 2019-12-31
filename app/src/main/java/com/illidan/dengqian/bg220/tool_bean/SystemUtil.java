package com.illidan.dengqian.bg220.tool_bean;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.illidan.dengqian.bg220.MainActivity;
import com.illidan.dengqian.bg220.tool_bean.listener.mylocationListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dengqian on 2018/3/16.
 */

public class SystemUtil {
    public static final String TAG="手机信息工具类";







    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context ctx) {



        if (ContextCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
            return MainActivity.telMag.getDeviceId();


    }

    /**
     * 查看当前是否存在SIM卡
     *
     * @param
     * @return
     */
    public static boolean hasSimCard() {
        int simState = MainActivity.telMag.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }

    public static String getGPSLocation() {
        Location location = null;
        if (ContextCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = MainActivity.locationManager.getLastKnownLocation(MainActivity.locationProvider); // 通过GPS获取位置
        }
        if (location != null) {
            return "(" + String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude()) + ")";
        }else{
            return String.valueOf(mylocationListener.gps_lat)+","+String.valueOf(mylocationListener.gps_lon);
        }
    }


    public static Criteria createFineCriteria() {
        Criteria c = new Criteria();

        c.setAccuracy(Criteria.ACCURACY_FINE);//高精度

        c.setAltitudeRequired(true);//包含高度信息

        c.setBearingRequired(true);//包含方位信息

        c.setSpeedRequired(true);//包含速度信息

        c.setCostAllowed(false);//允许付费

        c.setPowerRequirement(Criteria.POWER_HIGH);//高耗电

        return c;

    }

    public static String getBaiDuPosRequest(String lon, String lat) throws Exception {
        String surl="https://api.map.baidu.com/geocoder/v2/?location="+lat+","+lon+"&output=json&pois=1&ak=y5pwBnfPdgA6dbN6ws0i34Hefk0zD35v";

        URL url = new URL(surl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        //这是请求方式为POST
        conn.setRequestMethod("GET");
        //设置post请求必要的请求头
        conn.setRequestProperty("Content-Type", "application/json"); // 请求头, 必须设置
        conn.setDoOutput(true); // 准备写出
        Log.w("resp：",conn.getResponseMessage());
        InputStream inptStream=conn.getInputStream();
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] dataReceive = new byte[1024];
        int len = 0;
        try {
            while((len = inptStream.read(dataReceive)) != -1) {
                byteArrayOutputStream.write(dataReceive, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        if(conn.getResponseCode() == 200)
            return resultData;
        else
            return "err";
    }


    public static String formatAddress(String resultData) throws Exception {
        JSONObject jsonObject=new JSONObject(resultData);
        JSONObject formatted_address=new JSONObject(jsonObject.get("result").toString());
        return formatted_address.get("formatted_address").toString();
    }


    public static synchronized boolean SendGetRequest(String Strurl) throws Exception{
        StringBuffer sb=new StringBuffer();
        HttpURLConnection conn=null;
        URL url = new URL(Strurl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");




        if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
            InputStream in=conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            sb.toString();
            in.close();
            conn.disconnect();
            return true;
        }
        else {
            Log.i(TAG,"get请求失败");
            conn.disconnect();
            return false;
        }
    }

    public static boolean SendPostRequest(String Strurl,String dataString) throws Exception{
        StringBuffer sb=new StringBuffer();
        HttpURLConnection conn=null;
        URL url = new URL(Strurl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        byte []data=dataString.getBytes();
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outputStream=conn.getOutputStream();
        outputStream.write(data);



        if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
            InputStream in=conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            sb.toString();
            in.close();
            conn.disconnect();
            return true;
        }
        else {
            Log.i(TAG,"get请求失败");
            conn.disconnect();
            return false;
        }
    }


    public static void showToast(Context context,String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }


}
