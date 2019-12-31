package com.illidan.dengqian.bg220.tool_bean;

import android.nfc.Tag;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.illidan.dengqian.bg220.MainActivity;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class information {

    //记录ID
    private String ID = "";

    // LAC，Location Area Code，位置区域码；
    private String TAC = "";

    //ID，cellID，基站编号；
    private String ECI = "";

    //BSSS，Base station signal strength，基站信号强度。
    private int BSSS;

    //当前GPS定位
    private String GPS = "";

    //手机号码
    private String phoneNumber = "";

    //手机品牌型号
    private String phoneType = "";


    //采集时间
    private String collTime = "";

    //地理未知
    private String address = "";


    //网络类型
    private int network_type = 0;


    private String NetworkOperatorName = "";

    private String solveStatus = "";

    private String solveTime = "";


    public information(String ID, String TAC, String ECI, int BSSS, String GPS, String phoneNumber, String phoneType, String collTime, String NetworkOperatorName) {
        this.ID = ID;
        this.TAC = TAC;
        this.ECI = ECI;
        this.BSSS = BSSS;
        this.GPS = GPS;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;

        this.collTime = collTime;

        this.NetworkOperatorName = NetworkOperatorName;
    }

    public information(information in) {
        this.ID = in.getID();
        this.TAC = in.getTAC();
        this.ECI = in.getECI();
        this.BSSS = in.getBSSS();
        this.GPS = in.getGPS();
        this.phoneNumber = in.getPhoneNumber();
        this.phoneType = in.getPhoneType();

        this.collTime = in.collTime;

        this.NetworkOperatorName = in.getNetworkOperatorName();

        this.address = in.getAddress();
        this.solveStatus = in.getSolveStatus();
        this.solveTime = in.getSolveTime();
    }

    public information() {

    }


    public void setSolveStatus(String solveStatus) {
        this.solveStatus = solveStatus;
    }

    public void setSolveTime(String solveTime) {
        this.solveTime = solveTime;
    }

    public String getSolveStatus() {
        return solveStatus;
    }

    public String getSolveTime() {
        return solveTime;
    }

    public String getNetworkOperatorName() {
        return NetworkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        NetworkOperatorName = networkOperatorName;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTAC(String TAC) {
        this.TAC = TAC;
    }

    public void setECI(String ECI) {
        this.ECI = ECI;
    }

    public void setBSSS(int BSSS) {
        this.BSSS = BSSS;
    }

    public void setGPS(String GPS) {

        this.GPS = GPS;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }


    public void setCollTime(String collTime) {
        this.collTime = collTime;
    }


    public String getID() {
        return ID;
    }

    public String getTAC() {
        return TAC;
    }

    public String getECI() {
        return ECI;
    }

    public int getBSSS() {
        return BSSS;

    }

    public String getGPS() {
        if (GPS.equals("")){
            return "(-1,-1)";
        }else{

            String []g=GPS.split(",");
            String lon=String.format("%.3f",Double.valueOf(g[0]));
            String lat=String.format("%.3f",Double.valueOf(g[1]));

            return "("+lon+","+lat+")";
        }

    }
    public String getGps() {
        if (GPS.equals("")){
            return "-1,-1";
        }

        return GPS;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }



    public String getCollTime() {

        Date date=new Date();
        SimpleDateFormat dateformate=new SimpleDateFormat("yyyyMMddHHmmss");
        String sim=dateformate.format(date);
        collTime=sim;

        return collTime;
    }
    public String getCollTime2() {

        Date date=new Date();
        SimpleDateFormat dateformate=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sim=dateformate.format(date);


        return sim;
    }



    public int getNetwork_type() {return network_type; }

    public String getStrNetWork_type(){


        switch (network_type) {

            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "2G";
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:


                return "3G";
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 20:
                return "4G";
            case 88:
                return "WIFI";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:

                return "未知网络";
            default:{
                String res="默认";
//                try{
//                    Class<?> threeclazz=Class.forName("android.telephony.TelephonyManager");
//
//                    Method getNetType=threeclazz.getMethod("getNetworkClass",Integer.class);
//                    res =getNetType.invoke(null,network_type).toString();
//                }catch(Exception e){
//                    Log.e("info",e.toString());
//
//                }finally {
//                    return res;
//                }

            }
            return "未知";
        }
    }

    public void setNetwork_type(int network_type) {
        this.network_type = network_type;
    }


    /**
     * 对本类的数据进行非空检查
     * @return
     */
    public boolean checkData(){
        boolean flag=false;
        if(!this.getID().equals("")&&
                !this.getCollTime().equals("")&&
                !this.getNetworkOperatorName().equals("")&&
                !this.getAddress().equals("")&&
                this.getBSSS()!=0&&
                !this.getECI().equals("")&&
                !this.getGPS().equals("")&&
                !this.getPhoneNumber().equals("")&&
                !this.getPhoneType().equals("")&&
                !this.getTAC().equals(""))
            flag=true;

        return flag;
    }



    public  static String[] title = {};
    public  static int[] isright = {};




}
