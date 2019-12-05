package com.illidan.dengqian.bg220;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.LocationManager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import android.os.Message;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;



import com.illidan.dengqian.bg220.tool_bean.Myhander;
import com.illidan.dengqian.bg220.tool_bean.checkBean;
import com.illidan.dengqian.bg220.tool_bean.voice.CallContentObserver;
import com.illidan.dengqian.bg220.tool_bean.NetWork_Tool;
import com.illidan.dengqian.bg220.tool_bean.SystemUtil;

import com.illidan.dengqian.bg220.tool_bean.information;
import com.illidan.dengqian.bg220.tool_bean.listAdapteer;
import com.illidan.dengqian.bg220.tool_bean.listener.myPhoneStateListener;
import com.illidan.dengqian.bg220.tool_bean.listener.mylocationListener;

import org.json.JSONArray;
import org.json.JSONObject;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    /**
     * 常量与静态基本量声明区
     */
    private static final String TAG = "MainActivity";
    //显示在哪区域常量
    public static final int TEXT_VIEW = 1;
    public static final int CHECK_VIEW = 2;
    //扫码回调常量
    public static final int REQUEST_CODE_SCAN = 1;
    public static final int RESULT_CODE_SCAN = 1;
    //sim是否存在
    private boolean isSIM = false;
    //测试进行中标记
    public boolean isInTest = false;



    /**
     * UI组件声明区
     */

    //通话测试
    Button call_test = null;
    //整体测试开关
    Button startOrStop_test = null;
    //上网测试
    Button web_test = null;
    //现废弃
    public static TextView text_view = null;
    //现废弃
    public static TextView check_view = null;
    //任务列表
    private ListView mListView;
    //消息传递
    public static Myhander myhander;
    public static Message msg;



    /**
     * 功能性组建声明区
     */
    //测试工具类
    public static NetWork_Tool net_tool = null;
    //当前电话
    public static String cu_number = "";
    //检查项
    Map<String, ArrayList<String>> check_info = null;

    //默认的测试url
    String[] urlList = {
            "https://www.baidu.com",
            "https://weixin.qq.com/"
    };
    /**
     * 逻辑组件
     */
    //列表适配器
    public static listAdapteer listadpt;
    //MainActive上下文
    public static Context context;
    //手机状态管理类
    public static TelephonyManager telMag = null;
    //位置提供者
    public static String locationProvider;
    //位置管理者
    public static LocationManager locationManager;
    /**
     * 监听器
     */
    private myPhoneStateListener listenerSign = null;
    private myPhoneStateListener listenerNetwork = null;
    public mylocationListener mylocationListener = null;
    /**
     * 权限管理
     */
    private final int mRequestCode = 100;//权限请求码
    List<String> mPermissionList = new ArrayList<>();
    private String[] permissionList = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.VIBRATE,
    };


    public static void appendEd(String showcontent, int panle_id) {
        if (panle_id == 1) {
            if (MainActivity.text_view != null) {
                MainActivity.text_view.append("\n" + showcontent);
            }
        } else if (panle_id == 2) {
            if (MainActivity.check_view != null) {
                MainActivity.check_view.append("\n" + showcontent);
            }
        }


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        call_test = (Button) findViewById(R.id.call_test);
        startOrStop_test = (Button) findViewById(R.id.start_button);
        web_test = (Button) findViewById(R.id.web_test);

        text_view = (TextView) findViewById(R.id.text_view);
        check_view = (TextView) findViewById(R.id.check_view);

        mListView = (ListView) findViewById(R.id.list);

        listadpt = new listAdapteer();
        mListView.setAdapter(listadpt);
        listadpt.notifyDataSetChanged();
        context = getApplicationContext();

        initPermission();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isSIM) {
            try {
                telMag.listen(listenerSign, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                telMag.listen(listenerNetwork, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
            } catch (Exception e) {
               Log.e(TAG,e.toString());
            }
        } else {
            msg.what=Myhander.TOAST_ISSIM;
            myhander.sendMessage(msg);
        }
    }

    /**
     * 权限初始化函数
     */
    private void initPermission() {
        mPermissionList.clear();
        for (int i = 0; i < permissionList.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissionList[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissionList[i]);
            }
        }
        for (String x : mPermissionList) {
            Toast.makeText(MainActivity.context, "无法授权——"+x, Toast.LENGTH_LONG).show();
        }

        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissionList, mRequestCode);
        } else {
            Toast.makeText(MainActivity.context, "授权完成", Toast.LENGTH_LONG).show();


            init();
        }
    }




    /**
     * 上网测试类
     * @param scan_result
     */
    public void testNetWork(Map<String, ArrayList<String>> scan_result) {

        mListView.setVisibility(View.VISIBLE);
        net_tool.start();

        net_tool.information.setGPS(SystemUtil.getGPSLocation());
        ArrayList<String> url_list = scan_result.get("url_list");
        /**
         * 如果扫码得到的list不为空则使用扫码得到的url
         */
        if (url_list.size() > 0) {
            urlList = url_list.toArray(new String[url_list.size()]);
        }




        new Thread() {
            boolean network_test = true;

            @Override
            public void run() {
                try {
                    Log.e(SystemUtil.TAG, "开始GET");


                    for (final String url : urlList) {
                        if (SystemUtil.SendGetRequest(url)) {
                            new Handler(context.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    appendEd(url + "访问通过", TEXT_VIEW);
                                    Looper.loop();
                                }
                            });
                        } else {
                            new Handler(context.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    network_test = false;
                                    appendEd(url + "访问失败", TEXT_VIEW);
                                    Looper.loop();
                                }
                            });
                        }
                    }



                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (network_test) {
                                information.isright[3] = 1;
                                MainActivity.listadpt.notifyDataSetChanged();

                            } else {
                                information.isright[3] = 0;
                                MainActivity.listadpt.notifyDataSetChanged();

                            }

                        }
                    });


                } catch (Exception e) {
                    Log.e(SystemUtil.TAG, e.toString());
                }

            }
        }.start();

    }

    /**
     * 逻辑组件初始化
     */

    public void init() {
        myhander=new Myhander();
        msg = new Message();
        telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        net_tool = new NetWork_Tool(telMag);
        listenerSign = new myPhoneStateListener();
        listenerNetwork = new myPhoneStateListener();
        mylocationListener = new mylocationListener();
        telMag.listen(listenerSign, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telMag.listen(listenerNetwork, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, false, new CallContentObserver(new Handler(), this, ""));
        isSIM = SystemUtil.hasSimCard();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationProvider = locationManager.getBestProvider(SystemUtil.createFineCriteria(), true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(locationProvider, 1000, 10, mylocationListener);
        call_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class<TelephonyManager> c = TelephonyManager.class;
                    Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                    mthEndCall.setAccessible(true);
                    CallPhone();
                } catch (Exception e) {
                    Log.e("MediaPlayer", e.toString());
                }
            }

        });


        startOrStop_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInTest) {
                    isInTest = false;
                    startOrStop_test.setText("开始测试");
                    call_test.setEnabled(false);
                    web_test.setEnabled(false);
                    for(int i=0;i<information.isright.length;i++){
                        information.isright[i]=-1;
                    }
                    listadpt.notifyDataSetChanged();
                    mListView.setVisibility(View.GONE);

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try{

                                /**
                                 * 上传测试报告
                                 */


                            }catch(Exception e){
                                Log.e(TAG,e.toString());
                            }
                        }
                    }.start();
                } else {
                    if (isSIM) {
                        //扫码
                        Intent intent = new Intent(MainActivity.this, OptionsScannerActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_SCAN);

                    } else {
                        Toast.makeText(MainActivity.this, "请插入SIM卡", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        web_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInTest){
                    testNetWork(check_info);
                }

            }
        });
    }



    public void startTest(){
        if(isInTest){
            mListView.setVisibility(View.VISIBLE);
            net_tool.start();
            net_tool.information.setGPS(SystemUtil.getGPSLocation());


            if(checkbean.getUrl()!=null&&checkbean.getUrl().contains(",")){
                String more[]=checkbean.getUrl().split(",");
                for(int i=0;i<more.length;i++){
                    more[i]=more[i];
                }
                if(more.length>0){
                    urlList=more;
                }
            }else if(checkbean.getUrl()!=null&&!"".equals(checkbean.getUrl())){
                urlList[0]=checkbean.getUrl();
            }




            /**
             * 如果扫码得到的list不为空则使用扫码得到的url
             */
            new Thread() {
                boolean network_test = true;
                @Override
                public void run() {
                    try {
                        Log.e(SystemUtil.TAG, "开始GET");
                        for (final String url : urlList) {
                            if (SystemUtil.SendGetRequest(url)) {
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        appendEd(url + "访问通过", TEXT_VIEW);
                                        Looper.loop();
                                    }
                                });
                            } else {
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        network_test = false;
                                        appendEd(url + "访问失败", TEXT_VIEW);
                                        Looper.loop();
                                    }
                                });
                            }
                        }

                        checkbean.check(net_tool.information);



                        int []flag=new int[checkBean.checknum+2];
                        for(int i=0;i<flag.length;i++){
                            flag[i]=-1;
                        }
                        information.isright=flag;

                        String ti[]=new String[checkBean.checknum+2];
                        for(int i=0;i<checkBean.checkItem.size();i++){
                            ti[i]=checkBean.checkItem.get(i);
                        }
                        ti[checkBean.checknum]="URL测试正确性";
                        ti[checkBean.checknum+1]="电话拨打正确性";
                        information.title=ti;
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                int n=0;

                                for(int i=0;i<checkBean.checkItem.size();i++){
                                    String key=checkBean.checkItem.get(i);
                                    switch(key){
                                        case "网络类型核查":{
                                            information.isright[n]=checkbean.getNetwork_type_ok();
                                            n++;
                                            break;
                                        }

                                        case "ECI核查":{
                                            information.isright[n]=checkbean.getECI_ok();
                                            n++;
                                            break;
                                        }

                                        case "TAC核查":
                                        {
                                            information.isright[n]=checkbean.getTAC_ok();
                                            n++;
                                            break;
                                        }
                                        case "时间范围核查":{
                                            information.isright[n]=checkbean.getDatetime_ok();
                                            n++;
                                            break;
                                        }

                                        case "Gps位置核查":{
                                            information.isright[n]=checkbean.getGps_ok();
                                            n++;
                                            break;
                                        }

                                    }

                                }

                                if (network_test) {
                                    Log.e("执行测试","531");
                                    information.isright[checkBean.checknum] = 1;
                                    MainActivity.listadpt.notifyDataSetChanged();
                                } else {
                                    information.isright[checkBean.checknum] = 0;
                                    MainActivity.listadpt.notifyDataSetChanged();
                                }



                                Looper.loop();
                            }
                        });
                        /**
                         * 拨号测试
                         */
                        Class<TelephonyManager> c = TelephonyManager.class;
                        Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                        mthEndCall.setAccessible(true);
                        CallPhone();

                    } catch (Exception e) {
                        Log.e(SystemUtil.TAG, e.toString());
                    }

                }
            }.start();





//            /**
//             * 拨号测试
//             */
//            try {
//
//            } catch (Exception e) {
//                Log.e("MediaPlayer", e.toString());
//            }
        }





    }

    public static checkBean checkbean=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_CODE_SCAN) {
            String checkJsonStr=data.getExtras().getString("ResultQRCode");
            if(checkJsonStr!=null&&!checkJsonStr.equals("0")){
                Toast.makeText(MainActivity.this, checkJsonStr, Toast.LENGTH_SHORT).show();
                checkbean=new checkBean(checkJsonStr);
                //UI动态设置
                startOrStop_test.setText("停止测试");
                isInTest = true;
                call_test.setEnabled(true);
                web_test.setEnabled(true);
                /**
                 * 开始测试
                 */
                startTest();
            }else{
                Toast.makeText(MainActivity.this, "扫码失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CallPhone() {
        // 拨号：激活系统的拨号组件
        String number=MainActivity.checkbean.getTo_number();
        if ("".equals(number)) {
            Toast.makeText(MainActivity.this, "没有手机号", Toast.LENGTH_SHORT).show();
        } else {
            cu_number = number;
            Intent intent = new Intent(); // 意图对象：动作 + 数据
            intent.setAction(Intent.ACTION_CALL); // 设置动作
            Uri data = Uri.parse("tel:" + number); // 设置数据
            intent.setData(data);
            startActivity(intent); // 激活Activity组件
        }
    }

    private View contentView;
    private PopupWindow popupWindow;
    public void showPopWindowns(){
        contentView=LayoutInflater.from(this).inflate(R.layout.window_layout,null);
        popupWindow=new PopupWindow(contentView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
    }




}
