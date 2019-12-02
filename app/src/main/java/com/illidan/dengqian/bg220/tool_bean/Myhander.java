package com.illidan.dengqian.bg220.tool_bean;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.illidan.dengqian.bg220.MainActivity;

public class Myhander extends Handler {
    public static final int TOAST_ISSIM=1001;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int code = msg.what;//接受处理码
        int arg1=msg.arg1;
        switch (code) {
            case TOAST_ISSIM: {
                Toast.makeText(MainActivity.context, "未检测到SIM卡,或SIM卡无效", Toast.LENGTH_LONG).show();
                break;
            }
            case 2: {

               break;
            }
        }

    }

}
