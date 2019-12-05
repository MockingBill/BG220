package com.illidan.dengqian.bg220.tool_bean;

import android.content.Intent;
import android.location.Location;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class checkBean {
    public static final String TAG="checkBean_err";
    private  String test_name;
    private  String network_type;
    private  String gps_lon;
    private  String gps_lat;
    private  String ECI;
    private  String TAC;
    private  String start_datetime;
    private  String end_datetime;
    private  String to_number;
    private  String url;
    private int network_type_ok=-1;
    private int ECI_ok=-1;
    private int TAC_ok=-1;
    private int datetime_ok=-1;
    private int gps_ok=-1;

    public static int checknum=0;
    public static List<String> checkItem=new ArrayList<>();


    public checkBean(){

    }

    public checkBean(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("test_anme")){
                this.test_name=jsonObject.get("test_name").toString();
            }else{
                this.test_name="";
            }
            if(jsonObject.has("network_type")){
                this.network_type=jsonObject.get("network_type").toString();

            }else{
                this.network_type="";
            }
            if(jsonObject.has("gps_lon")&&jsonObject.has("gps_lat")){
                this.gps_lon=jsonObject.get("gps_lon").toString();
                this.gps_lat=jsonObject.get("gps_lat").toString();
            }else{
                gps_lon="";
                gps_lat="";
            }

            if(jsonObject.has("ECI")){

                this.ECI=jsonObject.get("ECI").toString();
            }else{

                this.ECI="";
            }


            if(jsonObject.has("TAC")){
                this.TAC=jsonObject.get("TAC").toString();
            }else{
                TAC="";
            }


            if(jsonObject.has("start_datetime")&&jsonObject.has("end_datetime")){
                this.start_datetime=jsonObject.get("start_datetime").toString();
                this.end_datetime=jsonObject.get("end_datetime").toString();
            }else{
                start_datetime="";
                end_datetime="";
            }



            if(jsonObject.has("to_number")){
                setTo_number(jsonObject.get("to_number").toString());
            }else{
                setTo_number("10086");
            }




            if(jsonObject.has("url")){
                setUrl(jsonObject.get("url").toString());
            }else{
                setUrl("");
            }


        }catch(Exception e){
            Log.e(TAG,e.toString());

        }
    }
    public boolean isOK(String a){
        if ("".equals(a)||a==null||a.equals("null")){
            return false;
        }else{
            return true;
        }
    }









    public void check(information current){
        StringBuffer sb=new StringBuffer();


        /**
         * 网络类型核查
         */

        if(isOK(this.network_type) && !this.network_type.equals("0")){

            String network=current.getStrNetWork_type();

            if(this.network_type.equals(network)){
                setNetwork_type_ok(1);
            }else{
                setNetwork_type_ok(0);
            }
            checkItem.add("网络类型核查");
            checknum++;
        }
        /**
         * ECI核查
         */
        if(isOK(this.ECI)){
            if(this.ECI.equals(String.valueOf(current.getECI()))){
                setECI_ok(1);
            }else{
                setECI_ok(0);
            }
            checkItem.add("ECI核查");
            checknum++;
        }
        /**
         * TAC核查
         */
        if(isOK(this.TAC)){
            if(this.TAC.equals(String.valueOf(current.getTAC()))){
               setTAC_ok(1);
            }else{
                setTAC_ok(0);
            }
            checkItem.add("TAC核查");
            checknum++;
        }
        /**
         * 时间核查
         */
        if(isOK(this.start_datetime) && isOK(this.end_datetime)){

            start_datetime=start_datetime.replaceAll("T"," ");
            long start_long = Long.valueOf(start_datetime.replaceAll("[-\\s:]",""));

            end_datetime=end_datetime.replaceAll("T"," ");
            long end_long = Long.valueOf(end_datetime.replaceAll("[-\\s:]",""));

            long curr_long = Long.valueOf(current.getCollTime());
            System.out.println();
            if (curr_long-start_long>0&&end_long-curr_long>0){
                setDatetime_ok(1);
            }else{
                setDatetime_ok(0);
            }
            checkItem.add("时间范围核查");
            checknum++;

        }
        /**
         * gps核查
         */

        if(isOK(this.gps_lat)&&isOK(this.gps_lon)){
            String gps[]=current.getGps().split(",");
            float result[]=new float[1];
            Location.distanceBetween(Double.valueOf(gps_lon), Double.valueOf(gps_lat), Double.valueOf(gps[0]), Double.valueOf(gps[1]),result);
            if(result[0]>1000){
                setGps_ok(0);
            }else{
                setGps_ok(1);
            }
            checkItem.add("Gps位置核查");
            checknum++;
        }
    };


    public void setNetwork_type_ok(int network_type_ok) {
        this.network_type_ok = network_type_ok;
    }

    public void setECI_ok(int ECI_ok) {
        this.ECI_ok = ECI_ok;
    }

    public void setTAC_ok(int TAC_ok) {
        this.TAC_ok = TAC_ok;
    }

    public void setDatetime_ok(int datetime_ok) {
        this.datetime_ok = datetime_ok;
    }

    public void setGps_ok(int gps_ok) {
        this.gps_ok = gps_ok;
    }

    public int getNetwork_type_ok() {
        return network_type_ok;
    }

    public int getECI_ok() {
        return ECI_ok;
    }

    public int getTAC_ok() {
        return TAC_ok;
    }

    public int getDatetime_ok() {
        return datetime_ok;
    }

    public int getGps_ok() {
        return gps_ok;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public void setGps_lon(String gps_lon) {
        this.gps_lon = gps_lon;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public void setECI(String ECI) {
        this.ECI = ECI;
    }

    public void setTAC(String TAC) {
        this.TAC = TAC;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }

    public void setTo_number(String to_number) {
        if ("".equals(to_number) || to_number==null){
            this.to_number="10086";
        }else{
            this.to_number = to_number;
        }

    }

    public void setUrl(String url) {
        if ("".equals(url) || url==null){
            this.url="www.baidu.com";
        }else{
            this.url = url;
        }

        this.url = url;
    }

    public String getTest_name() {
        return test_name;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public String getGps_lon() {
        return gps_lon;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public String getECI() {
        return ECI;
    }

    public String getTAC() {
        return TAC;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public String getTo_number() {
        return to_number;
    }

    public String getUrl() {
        return url;
    }
}
