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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class information {

    //记录ID
    private String ID="";

    // LAC，Location Area Code，位置区域码；
    private String TAC="";

    //ID，cellID，基站编号；
    private String ECI="";

    //BSSS，Base station signal strength，基站信号强度。
    private int BSSS;

    //当前GPS定位
    private String GPS="";

    //手机号码
    private String phoneNumber="";

    //手机品牌型号
    private String phoneType="";


    //采集时间
    private String collTime="";

    //地理未知
    private  String address="";





    //网络类型
    private int network_type=0;





    private String NetworkOperatorName="";

    private String solveStatus="";

    private String solveTime="";


    private Map<String,ArrayList<String>> checkList=null;






    public information(String ID, String TAC, String ECI, int BSSS, String GPS, String phoneNumber, String phoneType,  String collTime,  String NetworkOperatorName) {
        this.ID = ID;
        this.TAC = TAC;
        this.ECI = ECI;
        this.BSSS = BSSS;
        this.GPS = GPS;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;

        this.collTime = collTime;

        this.NetworkOperatorName=NetworkOperatorName;
    }
    public information(information in) {
        this.ID=in.getID();
        this.TAC = in.getTAC();
        this.ECI = in.getECI();
        this.BSSS = in.getBSSS();
        this.GPS = in.getGPS();
        this.phoneNumber = in.getPhoneNumber();
        this.phoneType = in.getPhoneType();

        this.collTime = in.collTime;

        this.NetworkOperatorName=in.getNetworkOperatorName();

        this.address=in.getAddress();
        this.solveStatus=in.getSolveStatus();
        this.solveTime=in.getSolveTime();
    }

    public information(){

    }

    public void setCheckList(Map<String, ArrayList<String>> checkList) {
        this.checkList = checkList;
    }

    public Map<String, ArrayList<String>> getCheckList() {
        return checkList;
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
        if(this.GPS.equals("")){
            GPS="No Gps";
        }else{
            String pattern = "\\d+\\.\\d{0,5}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(GPS);
            List<String> arr=new ArrayList<>();
            while (m.find()){
                System.out.println(m.group());
                arr.add(m.group());
            }
            if(arr.size()==2){
                GPS="("+arr.get(0)+","+arr.get(1)+")";
            }
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
        return collTime;
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
                return "2G网络";
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


                return "3G网络";
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 20:

                return "4G网络";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:

                return "未知网络";
            default:{
                String res="默认";
                try{
                    Class<?> threeclazz=Class.forName("android.telephony.TelephonyManager");

                    Method getNetType=threeclazz.getMethod("getNetworkClass",Integer.class);
                    res =getNetType.invoke(null,network_type).toString();
                }catch(Exception e){
                    Log.e("info",e.toString());

                }finally {
                    return res;
                }

            }
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

    /**
     * 调试程序时使用，可查看该对象的详细情况
     * @return
     */


    public boolean isPassTest(){
        if(show().toString().contains("失败")){
            return false;
        }else{
            return true;
        }
    }

    public final static String[] title = {"ECI正确性", "TAC正确性", "信号强度正确性", "网站访问测试","通话测试"};
    public final static int[] isright = {-1, -1, -1, -1,-1};
    public String show(){
        StringBuffer sb=new StringBuffer();
        SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = s_format.format(new Date());
        this.setCollTime(time);

        String check_ECI=checkList.get("ECI").get(0);
        String check_TAC=checkList.get("TAC").get(0);
        sb.append("运营商信息:"+this.getNetworkOperatorName()+"\n");
        sb.append("网络类型:"+this.getStrNetWork_type()+"\n");
        sb.append("手机类型:"+this.getPhoneType()+"\n");
        sb.append("位置:"+this.getAddress()+"\n");
        sb.append("ECI:"+this.getECI()+"\n");
        sb.append("TAC:"+this.getTAC()+"\n");
        sb.append("BSSS:"+this.getBSSS()+"\n");
        sb.append("GPS:"+this.getGPS()+"\n");
        sb.append("详细位置:"+this.getAddress()+"\n");
        sb.append("测试时间:"+this.getCollTime()+"\n");
        sb.append("检查项目:"+this.getCheckList().toString()+"\n");

        if(this.getECI().equals(check_ECI)){
            sb.append("ECI:测试通过\n");
            isright[0]=1;
        }else{
            sb.append("ECI:测试失败\n");
            isright[0]=0;
        }

       if( this.TAC.equals(check_TAC)){
           sb.append("TAC:测试通过\n");
           isright[1]=1;
       }else{
           sb.append("TAC:测试失败\n");
           isright[1]=0;
       }


        if (this.BSSS>-110){
            sb.append("信号强度:测试通过\n");
            isright[2]=1;
        }else{
            sb.append("信号强度:测试失败\n");
            isright[2]=0;
        }
        //更新列表
        MainActivity.listadpt.notifyDataSetChanged();
        return sb.toString();
    }


    public String toJsonString() {
        boolean falg=true;
        for(int x:isright){
            if( x==-1){
                falg=false;
                break;
            }
        }




        return "";
    }
}
