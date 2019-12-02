package com.illidan.dengqian.bg220.tool_bean.listener;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.illidan.dengqian.bg220.MainActivity;
import com.illidan.dengqian.bg220.tool_bean.SystemUtil;


/**
 * 位置更新信息监听
 */

public class mylocationListener implements LocationListener {

    /**
     * 位置变化
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        getLocalion();
        Toast.makeText(MainActivity.context, "位置更新", Toast.LENGTH_SHORT).show();

    }

    /**
     * 状态变化
     *
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(MainActivity.context, "当前GPS状态为服务区外状态", Toast.LENGTH_SHORT).show();
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(MainActivity.context, "当前GPS状态为暂停服务状态", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * gps开启时
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MainActivity.context, "GPS已开启", Toast.LENGTH_SHORT).show();
        getGPSLocation();
    }

    private String getGPSLocation() {
        Location location = null;
        if (ContextCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = MainActivity.locationManager.getLastKnownLocation(MainActivity.locationProvider); // 通过GPS获取位置
        }


        if (location != null) {

            return "(" + String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude()) + ")";
        }

        return "no GPS";
    }

    /**
     * gps关闭时
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.context, "请打开GPS 否则无法定位", Toast.LENGTH_SHORT).show();
    }
    public static int gps_lat=-1;
    public static int gps_lon=-1;


    public void getLocalion() {
        String Gpsstr = getGPSLocation();
        String Gps = Gpsstr.replaceAll("\\(|\\)", "").replaceAll("（", "").replaceAll("）", "");
        String[] GpsArr = Gps.split(",");

        if (GpsArr.length >= 2) {
            gps_lat=Integer.valueOf(GpsArr[1]);
            gps_lon=Integer.valueOf(GpsArr[0]);
            final String latt = GpsArr[1];
            final String lonn = GpsArr[0];
            new Thread() {
                public String res = "未知地址";

                @Override
                public void run() {
                    super.run();
                    try {
                        String jj = SystemUtil.getBaiDuPosRequest(lonn, latt);
                        res = SystemUtil.formatAddress(jj);

                    } catch (Exception e) {
                        res = "未知地址";
                        Log.e("BDdt", e.toString());
                    } finally {
                        MainActivity.net_tool.information.setAddress(res);
                    }
                }
            }.start();
        }

    }

}
