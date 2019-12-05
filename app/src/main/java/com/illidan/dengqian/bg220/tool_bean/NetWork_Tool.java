package com.illidan.dengqian.bg220.tool_bean;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.illidan.dengqian.bg220.MainActivity;

import java.lang.reflect.Method;

public class NetWork_Tool {

    TelephonyManager OnlyTeleMan=null;
    public information information=null;





    /**
     * 启动网络测试
     */
    public void start(){


        //获取网络类型
        information.setNetwork_type(getNetWorkType());
        //获取小区相关信息
        CellRelation();


        //获取手机型号等
        StringBuffer phoneType = new StringBuffer();
        phoneType.append(SystemUtil.getDeviceBrand() + ";");
        phoneType.append(SystemUtil.getSystemModel() + ";");
        phoneType.append(SystemUtil.getIMEI(MainActivity.context));
        information.setPhoneType(phoneType.toString());

    }



    public NetWork_Tool(TelephonyManager tm){
        OnlyTeleMan=tm;
        information=new information();
    }
    public void CellRelation(){
        int cid=-1;
        int lac=-1;
        String operName="未知";
        if (ContextCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }

        if(OnlyTeleMan!=null&&OnlyTeleMan.getAllCellInfo().size()>0){
            CellInfo statcellinfo=OnlyTeleMan.getAllCellInfo().get(0);

            if(statcellinfo instanceof CellInfoGsm){
                CellInfoGsm cellInfoGsm=(CellInfoGsm)statcellinfo;
                CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                lac = cellIdentity.getLac();
                cid = cellIdentity.getCid();

                String mnc=String.valueOf(cellIdentity.getMnc());

                String mcc=String.valueOf(cellIdentity.getMcc());
                operName=mccnc2oper(mcc,mnc);
            }
            else if(statcellinfo instanceof CellInfoWcdma){
                CellInfoWcdma cellInfoWcdma=(CellInfoWcdma)statcellinfo;
                CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
                lac = cellIdentity.getLac();
                cid = cellIdentity.getCid();
                String mcc=String.valueOf(cellIdentity.getMcc());
                String mnc=String.valueOf(cellIdentity.getMnc());
                operName=mccnc2oper(mcc,mnc);
            }
            else if(statcellinfo instanceof CellInfoLte){
                CellInfoLte cellInfoLte=(CellInfoLte)statcellinfo;
                CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
                lac = cellIdentity.getTac();
                cid = cellIdentity.getCi();
                information.setNetwork_type(TelephonyManager.NETWORK_TYPE_LTE);
                String mcc=String.valueOf(cellIdentity.getMcc());
                String mnc=String.valueOf(cellIdentity.getMnc());
                operName=mccnc2oper(mcc,mnc);
            }
            else if(statcellinfo instanceof CellInfoCdma){
                CellInfoCdma cellInfocdma=(CellInfoCdma)statcellinfo;
                CellIdentityCdma cellIdentity= cellInfocdma.getCellIdentity();
                lac=cellIdentity.getNetworkId();
                cid=cellIdentity.getBasestationId();
                operName="中国电信";
            }
            information.setTAC(String.valueOf(lac));
            information.setECI(String.valueOf(cid));
            information.setNetworkOperatorName(operName);
        }
    }

    public int getNetWorkType() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.context.getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo info= connectivityManager.getActiveNetworkInfo();
        if (info!= null&& info.getType() == ConnectivityManager.TYPE_WIFI) {
            information.setNetwork_type(88);
            return 88;
        }

        int networkType = OnlyTeleMan.getNetworkType();
        return networkType;
    }
    public String mccnc2oper(String mcc,String mnc){
        if("460".equals(mcc)){
            if("0".equals(mnc)||"7".equals(mnc)||"2".equals(mnc)){
                return "中国移动";
            }else if("1".equals(mnc)||"6".equals(mnc)){
                return "中国联通";
            }else if("20".equals(mnc)){
                return "中国铁通";
            }else if("3".equals(mnc)||"11".equals(mnc)||"5".equals(mnc)){
                return "中国电信";
            }else{
                return mnc;
            }
        }else{
            return mcc+mnc;
        }


    }






}
