package com.illidan.dengqian.bg220.tool_bean.voice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;


import com.illidan.dengqian.bg220.MainActivity;
import com.illidan.dengqian.bg220.tool_bean.information;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class CallContentObserver extends ContentObserver {

    public String Tag = "CallContentObserver";

    private static volatile int initialPos;
    private static final Uri outSMSUri = CallLog.Calls.CONTENT_URI;
    private Context context;
    private String address;
    private Handler mHandler;   //更新UI线程


//    ExecutorService singleThreadExecuto;


    public CallContentObserver(Handler handler, Context context, String number) {
        super(handler);
        this.context = context;
        this.address = number;
        this.mHandler = handler;
//        singleThreadExecuto = Executors.newSingleThreadExecutor();
    }

    public static boolean is_do=false;
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (!is_do){
            queryLastCall();
            is_do=false;
        }

    }

    //标记位置防止多次调用onchange
    public int getLastCallId() {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
            Cursor cur = context.getContentResolver().query(outSMSUri, null, null, null, CallLog.Calls.DATE + " desc");
            cur.moveToFirst();
            int lastMsgId = cur.getInt(cur.getColumnIndex("_id"));
            return lastMsgId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    protected void queryLastCall() {
        is_do=true;
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Cursor cur =context.getContentResolver().query(outSMSUri, null, null, null, CallLog.Calls.DATE + " desc");

            if (cur.moveToNext()) {
                if (initialPos != getLastCallId()) {
                    if (!TextUtils.isEmpty(MainActivity.cu_number)) {
                        if (cur.getString(cur.getColumnIndex("number")).contains(MainActivity.cu_number)) {


                            int type = cur.getInt(cur.getColumnIndex("type"));//通话类型，1 来电 .INCOMING_TYPE；2 已拨 .OUTGOING_；3 未接 .MISSED_
//                            String number = cur.getString(cur.getColumnIndex("number"));// 电话号码
//                            int _id = cur.getInt(cur.getColumnIndex("_id"));
                            int duration = cur.getInt(cur.getColumnIndex("duration"));//通话时长，单位：秒
//                            String msgObj = "\nID：" + _id + "\n类型：" + type + "\n号码：" + number + "\n时长：" + duration;
                            String state="未知";
                            switch (type){
                                case 1:{
                                    state="来电";
                                    break;
                                }
                                case 2:{
                                    state="去电";
                                    break;
                                }
                                case 3:{
                                    state="未接";
                                    break;
                                }
                            }
                            if (type == 2) {
                                if (duration > 0) {
                                    information.isright[4]=1;

                                    MainActivity.appendEd(MainActivity.cu_number+" "+state+" 通话成功,通话时长时长大于0", MainActivity.TEXT_VIEW);
                                } else {
                                    information.isright[4]=0;
                                    MainActivity.appendEd("通话失败,通话时长时长不大于0", MainActivity.TEXT_VIEW);
                                }
                                MainActivity.listadpt.notifyDataSetChanged();
                            }

                        }
                    }
                    initialPos = getLastCallId();
                }
            }
            cur.close();
        } catch (Exception e) {
            MainActivity.appendEd(e.toString(),MainActivity.TEXT_VIEW);
        }


    }
}
