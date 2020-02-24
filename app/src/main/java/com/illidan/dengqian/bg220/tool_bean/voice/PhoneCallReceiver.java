package com.illidan.dengqian.bg220.tool_bean.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.illidan.dengqian.bg220.MainActivity;
import com.illidan.dengqian.bg220.testActivity;
import com.illidan.dengqian.bg220.tool_bean.SystemUtil;
import com.illidan.dengqian.bg220.tool_bean.voice.AudioRecordManager;

import java.util.MissingFormatArgumentException;

/**
 * Created by hgx on 2016/6/13.
 */
public class PhoneCallReceiver extends BroadcastReceiver {
    private int lastCallState  = TelephonyManager.CALL_STATE_IDLE;
    private boolean isIncoming = false;
    private static String contactNum;
    int stateChange = 0;
    public String dict_path="";
    public PhoneCallReceiver() {
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        //如果是去电
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            contactNum = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
        }
        else
        {
            String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String phoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);



            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                //空闲状态
                stateChange =TelephonyManager.CALL_STATE_IDLE;
                if (isIncoming){
                    onIncomingCallEnded(context,phoneNumber);
                }else {
                    onOutgoingCallEnded(context,phoneNumber);
                }
            }else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                //摘机状态
                stateChange = TelephonyManager.CALL_STATE_OFFHOOK;
                if (lastCallState != TelephonyManager.CALL_STATE_RINGING){
                    //如果最近的状态不是来电响铃的话，意味着本次通话是去电
                    isIncoming =false;
                    onOutgoingCallStarted(context,phoneNumber);
                }else {
                    //否则本次通话是来电
                    isIncoming = true;
                    onIncomingCallAnswered(context, phoneNumber);
                    onIncomingCallStarted(context,phoneNumber);
                }
            }else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                //来电响铃状态
                stateChange = TelephonyManager.CALL_STATE_RINGING;
                lastCallState = stateChange;
                onIncomingCallReceived(context,contactNum);
                onIncomingCallStarted(context,phoneNumber);
            }

        }

    }


    public static boolean isRing=false;

    protected void onOutgoingCallStarted(Context context,String number){
        SystemUtil.showToast(context, "去电开始");
        if (!AudioRecordManager.isStart){
            dict_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/BG2019/";
            AudioRecordManager.destructionInstance();
            AudioRecordManager.getInstance().startRecord(dict_path,System.currentTimeMillis()+".pcm");
            isRing=true;
            }
        else{
                SystemUtil.showToast(MainActivity.context,"录音线程已经开启,请勿重复开启。");
        }
        }
    protected void onOutgoingCallEnded(Context context,String number){
        SystemUtil.showToast(context, "去电结束");
        if(AudioRecordManager.isStart){
            AudioRecordManager.getInstance().stopRecord();
            Intent intent=new Intent(MainActivity.context,testActivity.class);
            MainActivity.instance.startActivityForResult(intent,MainActivity.SPEEED_TEST_REEQUEST);
        }


    }


    protected void onIncomingCallStarted(Context context,String number){

        Intent workIntent = new Intent(context, MainActivity.class);
        workIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(workIntent);
        SystemUtil.showToast(context, "来电开始");

    }
    protected void onIncomingCallEnded(Context context,String number){
        SystemUtil.showToast(context, "来电结束");

    }
    protected void onIncomingCallReceived(Context context,String number){
        SystemUtil.showToast(context, "接听来电");

    }
    protected void onIncomingCallAnswered(Context context, String number) {
        SystemUtil.showToast(context, "来电答复");
    }



}