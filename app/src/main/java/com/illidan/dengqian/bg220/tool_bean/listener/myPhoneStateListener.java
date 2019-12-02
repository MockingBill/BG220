package com.illidan.dengqian.bg220.tool_bean.listener;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.illidan.dengqian.bg220.MainActivity;

import java.lang.reflect.Method;

/**
 * 手机状态监听
 * 1、网络连接情况
 * 2、信号强度情况
 */

public class myPhoneStateListener extends PhoneStateListener {

    SignalStrength signal = null;
    int networktype = 0;

    /**
     * 监听网络类型变化
     *
     * @param state
     * @param networkType
     */
    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        MainActivity.net_tool.information.setNetwork_type(MainActivity.net_tool.getNetWorkType());
        switch (state) {
            case TelephonyManager.DATA_DISCONNECTED://网络断开
                break;
            case TelephonyManager.DATA_CONNECTING://网络正在连接
                break;
            case TelephonyManager.DATA_CONNECTED://网络连接上
                break;
        }

    }

    /**
     * 监听信号强度变化
     *
     * @param signalStrength
     */

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        signal = signalStrength;
        setDbm(signal);

    }

    private void setDbm(SignalStrength signalStrength) {
        try {


            if (signalStrength != null) {

                Method method = signalStrength.getClass().getMethod("getDbm");
                String dbm = method.invoke(signalStrength).toString();

                MainActivity.net_tool.information.setBSSS(Integer.valueOf(dbm));

            } else {
                MainActivity.net_tool.information.setBSSS(-2);
            }

        } catch (Exception e) {
            MainActivity.net_tool.information.setBSSS(-3);
        }
    }
}
