package com.illidan.dengqian.bg220;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.illidan.dengqian.bg220.http_conn.AesAndToken;
import com.illidan.dengqian.bg220.http_conn.connNetReq;
import com.illidan.dengqian.bg220.http_conn.versionInfo;
import com.illidan.dengqian.bg220.tool_bean.Myhander;
import com.illidan.dengqian.bg220.tool_bean.checkBean;
import com.illidan.dengqian.bg220.tool_bean.checkListAdapteer;
import com.illidan.dengqian.bg220.tool_bean.voice.CallContentObserver;
import com.illidan.dengqian.bg220.tool_bean.NetWork_Tool;
import com.illidan.dengqian.bg220.tool_bean.SystemUtil;
import com.illidan.dengqian.bg220.tool_bean.information;
import com.illidan.dengqian.bg220.tool_bean.listAdapteer;
import com.illidan.dengqian.bg220.tool_bean.listener.myPhoneStateListener;
import com.illidan.dengqian.bg220.tool_bean.listener.mylocationListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    /**
     * 常量与静态基本量声明区
     */
    private static final String TAG = "MainActivity";
    public static MainActivity instance = null;
    public static final int SPEED_TEST_RETURN = 201;
    public static final int SPEEED_TEST_REEQUEST = 333;

    public static checkBean checkbean = null;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "AutoUpdate" + "/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "bg2020.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //安装应用
    private String apk_path = "";
    public static String check_id = null;


    //用户信息
    String username = "";
    String phonenumber = "";


    //扫码回调常量
    public static final int REQUEST_CODE_SCAN = 1;
    public static final int RESULT_CODE_SCAN = 1;
    private static final int INSTALL_PERMISS_CODE = 10001;
    //sim是否存在
    private boolean isSIM = false;
    //测试进行中标记
    public boolean isInTest = false;
    //速度测试中转变量
    public String currentDownSpeed = "无";
    public String currentUpSpeed = "无";

    //测试失败的URL
    public String testErrUrl = "";


    /**
     * UI组件声明区
     */


    //整体测试开关
    public TextView check_title_lable = null;
    Button startOrStop_test = null;


    //查看报告
    public static Button look_report = null;
    public Button start_test = null;


    //任务列表
    private ListView mListView;
    private ListView checkListView;
    //消息传递
    public static Myhander myhander = new Myhander();
    public static Message msg = new Message();
    //测试报告弹窗
    private View contentView;
    private PopupWindow popupWindow;
    public static String currnt_pcm_file = "";


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
    public static checkListAdapteer checklistadpt;
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
            Manifest.permission.REQUEST_INSTALL_PACKAGES,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        startOrStop_test = (Button) findViewById(R.id.start_button);

        look_report = (Button) findViewById(R.id.look_report);

        start_test = (Button) findViewById(R.id.start_test);
        //check_title_lable=(TextView)findViewById(R.id.check_title_lable);

        mListView = (ListView) findViewById(R.id.list);
        listadpt = new listAdapteer();
        mListView.setAdapter(listadpt);
        listadpt.notifyDataSetChanged();


        checkListView = (ListView) findViewById(R.id.check_list);
        checklistadpt = new checkListAdapteer();
        checkListView.setAdapter(checklistadpt);
        checklistadpt.notifyDataSetChanged();
        context = getApplicationContext();

        //check_title_lable.setVisibility(View.GONE);
        checkListView.setVisibility(View.GONE);
        instance = this;
        apk_path = getString(R.string.apkurl);
        initPermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.persionInfo: {
                showPopWindowns_per();

            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (isSIM) {
            try {
                telMag.listen(listenerSign, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                telMag.listen(listenerNetwork, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            SystemUtil.showToast(context, "未检测到SIM卡");
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_CODE = 10000;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissionDismiss = false;      //有权限没有通过
        if (MY_PERMISSIONS_REQUEST_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;   //发现有未通过权限
                    break;
                }
            }
        }

        if (hasPermissionDismiss) {                //如果有没有被允许的权限
            //假如存在有没被允许的权限,可提示用户手动设置 或者不让用户继续操作
            SystemUtil.showToast(context, "请打开全部权限重启app");

        } else {
            Log.e(TAG, "已全部授权");
            init();
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
            SystemUtil.showToast(MainActivity.this, "请打开授权:" + x);

        }

        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissionList, mRequestCode);
        } else {
            SystemUtil.showToast(MainActivity.this, "授权完成");

        }
    }


    /**
     * 逻辑组件初始化
     */

    public void init() {
        sp = context.getSharedPreferences("person_config", MODE_PRIVATE);
        msg = new Message();
        telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        isSIM = SystemUtil.hasSimCard();
        net_tool = new NetWork_Tool(telMag);
        listenerSign = new myPhoneStateListener();
        listenerNetwork = new myPhoneStateListener();
        mylocationListener = new mylocationListener();
        telMag.listen(listenerSign, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telMag.listen(listenerNetwork, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, false, new CallContentObserver(new Handler(), this, ""));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationProvider = locationManager.getBestProvider(SystemUtil.createFineCriteria(), true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(locationProvider, 1000, 10, mylocationListener);


        startOrStop_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInTest) {
                    isInTest = false;
                    startOrStop_test.setText("扫描测试要求");
                    /**
                     * 清空面板
                     */
                    destroyTimer();
                    checkBean.check_title.clear();
                    checkBean.checkItem.clear();
                    checkBean.checknum = 0;
                    checklistadpt.notifyDataSetChanged();
                    checkListView.setVisibility(View.GONE);


                    look_report.setEnabled(false);
                    start_test.setEnabled(false);
                    for (int i = 0; i < information.isright.length; i++) {
                        information.isright[i] = -1;
                    }

                    information.title = new String[0];
                    information.isright = new int[0];
                    listadpt.notifyDataSetChanged();
                    mListView.setVisibility(View.GONE);

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {

                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    }.start();
                } else {
                    if (isSIM) {
                        //扫码
                        Intent intent = new Intent(MainActivity.this, OptionsScannerActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);

                    } else {

                        SystemUtil.showToast(MainActivity.this, "请插入SIM卡");
                    }
                }
            }
        });


        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isInTest) {
                    check_id = UUID.randomUUID().toString();
                    net_tool.start();
                    startTest();
                } else {
                    SystemUtil.showToast(MainActivity.this, "当前不在测试中");
                    check_id = null;
                }


            }
        });

        look_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindowns();
            }
        });

        getCheckVersion();
        net_tool.start();

    }

    boolean network_test = true;

    public void startTest() {
        network_test = true;
        if (isInTest) {
            net_tool.start();


            if (checkbean.getUrl() != null && checkbean.getUrl().contains(",")) {
                String more[] = checkbean.getUrl().split(",");
                for (int i = 0; i < more.length; i++) {
                    more[i] = more[i];
                }
                if (more.length > 0) {
                    urlList = more;
                }
            } else if (checkbean.getUrl() != null && !"".equals(checkbean.getUrl()) && !checkbean.getUrl().contains(",")) {
                urlList[0] = checkbean.getUrl();
            }

            /**
             * 如果扫码得到的list不为空则使用扫码得到的url
             */
            Thread thread1 = new Thread() {


                @Override
                public void run() {
                    try {
                        for (final String url : urlList) {
                            if (SystemUtil.SendGetRequest(url)) {
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.loop();
                                    }
                                });
                            } else {
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        network_test = false;
                                        testErrUrl = url;
                                        Looper.loop();
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        Log.e(SystemUtil.TAG, e.toString());
                        network_test = false;
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                SystemUtil.showToast(MainActivity.this, "网络测试失败");
                                Looper.loop();
                            }
                        });
                    }


                }
            };


            Thread thread2 = new Thread() {
                @Override
                public void run() {

                    checkBean.checknum = 0;
                    checkBean.checkItem.clear();
                    checkbean.check(net_tool.information);
                    String ti[] = new String[checkBean.checknum + 2];
                    for (int i = 0; i < checkBean.checkItem.size(); i++) {
                        ti[i] = checkBean.checkItem.get(i);
                    }
                    ti[checkBean.checknum] = "URL测试正确性";
                    ti[checkBean.checknum + 1] = "电话拨打正确性";
                    information.title = ti;


                    int[] flag = new int[checkBean.checknum + 2];
                    for (int i = 0; i < flag.length; i++) {
                        flag[i] = -1;
                    }
                    information.isright = flag;


                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            int n = 0;

                            for (int i = 0; i < checkBean.checkItem.size(); i++) {
                                String key = checkBean.checkItem.get(i);
                                switch (key) {
                                    case "网络类型核查": {
                                        information.isright[n] = checkbean.getNetwork_type_ok();
                                        n++;
                                        break;
                                    }

                                    case "ECI核查": {
                                        information.isright[n] = checkbean.getECI_ok();
                                        n++;
                                        break;
                                    }

                                    case "TAC核查": {
                                        information.isright[n] = checkbean.getTAC_ok();
                                        n++;
                                        break;
                                    }
                                    case "时间范围核查": {
                                        information.isright[n] = checkbean.getDatetime_ok();
                                        n++;
                                        break;
                                    }

                                    case "Gps位置核查": {
                                        information.isright[n] = checkbean.getGps_ok();
                                        n++;
                                        break;
                                    }
                                    case "信号强度核查": {
                                        information.isright[n] = checkbean.getBsss_ok();
                                        n++;
                                        break;
                                    }

                                }

                            }

                            if (network_test) {

                                information.isright[checkBean.checknum] = 1;
                                MainActivity.listadpt.notifyDataSetChanged();
                            } else {
                                information.isright[checkBean.checknum] = 0;
                                MainActivity.listadpt.notifyDataSetChanged();
                            }

                            try {
                                Class<TelephonyManager> c = TelephonyManager.class;
                                Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                                mthEndCall.setAccessible(true);
                                CallPhone();
                            } catch (Exception e) {
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        SystemUtil.showToast(MainActivity.this, "拨号测试失败");
                                        Looper.loop();
                                    }
                                });

                            }
                            Looper.loop();

                        }
                    });

                }
            };


            try {
                thread1.start();
                thread1.join();
                thread2.start();
                thread2.join();


            } catch (Exception e) {

            }


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        Log.e("其他activity返回", "回来了requestCode:" + String.valueOf(requestCode) + "resultCode:" + String.valueOf(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_CODE_SCAN) {
            String checkJsonStr = data.getExtras().getString("ResultQRCode");
            if (checkJsonStr != null && !checkJsonStr.equals("0")) {

                checkbean = new checkBean(checkJsonStr);

                //UI动态设置
                startOrStop_test.setText("停止测试");
                //check_title_lable.setVisibility(View.VISIBLE);
                checkListView.setVisibility(View.VISIBLE);
                isInTest = true;

                LunchTimer();

                /**
                 * 设置检查项目列表
                 */
                checklistadpt.notifyDataSetChanged();


                start_test.setEnabled(true);
                mListView.setVisibility(View.VISIBLE);
            } else {
                SystemUtil.showToast(MainActivity.this, "扫码失败");

            }
        } else if (resultCode == SPEED_TEST_RETURN && requestCode == SPEEED_TEST_REEQUEST) {
            currentDownSpeed = data.getExtras().getString("speed_download");
            currentUpSpeed = data.getExtras().getString("speed_upload");


            if ("0 Mbps".equals(currentDownSpeed) && "0 Mbps".equals(currentUpSpeed)) {
                information.TitleAddItem("速度测试");
                information.isrightAddItem(0);
                listadpt.notifyDataSetChanged();
                currentUpSpeed = "未测试";
                currentDownSpeed = "未测试";
            } else {
                information.TitleAddItem("速度测试");
                information.isrightAddItem(1);
                listadpt.notifyDataSetChanged();
            }


        }
    }


    private void CallPhone() {
        // 拨号：激活系统的拨号组件
        String number = MainActivity.checkbean.getTo_number();
        if ("".equals(number)) {
            SystemUtil.showToast(MainActivity.this, "没有手机号");
        } else {
            cu_number = number;
            Intent intent = new Intent(); // 意图对象：动作 + 数据
            intent.setAction(Intent.ACTION_CALL); // 设置动作
            Uri data = Uri.parse("tel:" + number); // 设置数据
            intent.setData(data);


            startActivity(intent); // 激活Activity组件
        }
    }


    String resp = "";
    String upload_warm = "";

    public void showPopWindowns() {
        contentView = LayoutInflater.from(this).inflate(R.layout.window_layout, null);
        popupWindow = new PopupWindow(contentView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        TextView ValueNetworkOperatorName = (TextView) contentView.findViewById(R.id.NetworkOperatorName);

        TextView ValueECI = (TextView) contentView.findViewById(R.id.ValueECI);
        TextView ValueTAC = (TextView) contentView.findViewById(R.id.ValueTAC);
        TextView ValueBSSS = (TextView) contentView.findViewById(R.id.ValueBSSS);
        TextView ValueGPS = (TextView) contentView.findViewById(R.id.ValueGPS);
        TextView ValueNetWorkType = (TextView) contentView.findViewById(R.id.ValueNetWorkType);
        TextView ValuePhonetype = (TextView) contentView.findViewById(R.id.ValuePhonetype);
        TextView address = (TextView) contentView.findViewById(R.id.address);

        TextView ValueCollTime = (TextView) contentView.findViewById(R.id.ValueCollTime);

        Button singleUpload = (Button) contentView.findViewById(R.id.singleUpload);

        TextView upspeed = (TextView) contentView.findViewById(R.id.uploadID);
        TextView downspeed = (TextView) contentView.findViewById(R.id.downloadID);

        upspeed.setText(currentUpSpeed);
        downspeed.setText(currentDownSpeed);


        if (checkBean.checkItem.contains("网络类型核查")) {
            StringBuffer sb = new StringBuffer();
            sb.append("采集值:");
            sb.append(String.valueOf(net_tool.information.getStrNetWork_type()) + "\r\n");
            sb.append(" 核查值:");
            sb.append(checkbean.getNetwork_type() + "\r\n");
            if (checkbean.getNetwork_type_ok() == 1) {
                sb.append(" 检查通过");
            } else {
                sb.append(" 检查不通过");
            }
            ValueNetWorkType.setText(sb.toString());
        } else {
            ValueNetWorkType.setText(String.valueOf(net_tool.information.getStrNetWork_type()));
        }


        if (checkBean.checkItem.contains("ECI核查")) {
            StringBuffer sb = new StringBuffer();
            sb.append("采集值:");
            sb.append(String.valueOf(net_tool.information.getECI()) + "\r\n");
            sb.append(" 核查值:");
            sb.append(checkbean.getECI() + "\r\n");
            if (checkbean.getECI_ok() == 1) {
                sb.append(" 检查通过");
            } else {
                sb.append(" 检查不通过");
            }
            ValueECI.setText(sb.toString());

        } else {
            ValueECI.setText(String.valueOf(net_tool.information.getECI()));
        }

        if (checkBean.checkItem.contains("TAC核查")) {
            StringBuffer sb = new StringBuffer();
            sb.append("采集值:");
            sb.append(String.valueOf(net_tool.information.getTAC()) + "\r\n");
            sb.append(" 核查值:");
            sb.append(checkbean.getTAC() + "\r\n");
            if (checkbean.getTAC_ok() == 1) {
                sb.append(" 检查通过");
            } else {
                sb.append(" 检查不通过");
            }
            ValueTAC.setText(sb.toString());

        } else {
            ValueTAC.setText(String.valueOf(net_tool.information.getTAC()));
        }


        if (checkBean.checkItem.contains("时间范围核查")) {
            StringBuffer sb = new StringBuffer();
            sb.append("采集值:");
            sb.append(String.valueOf(net_tool.information.getCollTime2()) + "\r\n");
            sb.append(" 核查值:");
            sb.append(checkbean.getStart_datetime() + " 到 " + checkbean.getEnd_datetime() + "\r\n");
            if (checkbean.getDatetime_ok() == 1) {
                sb.append(" 检查通过");
            } else {
                sb.append(" 检查不通过");
            }
            ValueCollTime.setText(sb.toString());
        } else {
            ValueCollTime.setText(String.valueOf(net_tool.information.getCollTime()));
        }


        if (checkBean.checkItem.contains("Gps位置核查")) {

            StringBuffer sb = new StringBuffer();
            sb.append("采集值:");
            sb.append(String.valueOf(net_tool.information.getGPS()) + "\r\n");
            sb.append(" 核查值:");
            sb.append("(" + checkbean.getGps_lon() + "," + checkbean.getGps_lat() + ")\r\n");
            if (checkbean.getGps_ok() == 1) {
                sb.append(" 检查通过");
            } else {
                sb.append(" 检查不通过");

            }
            ValueGPS.setText(sb.toString());
        } else {
            ValueGPS.setText(String.valueOf(net_tool.information.getGPS()));
        }

        address.setText(String.valueOf(net_tool.information.getAddress()));
        ValueNetworkOperatorName.setText(String.valueOf(net_tool.information.getNetworkOperatorName()));
        ValueBSSS.setText(String.valueOf(net_tool.information.getBSSS()) + "dbm");
        ValuePhonetype.setText(String.valueOf(net_tool.information.getPhoneType()));
        singleUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = sp.getString("username", "");
                String p = sp.getString("phonenumber", "");
                upload_warm = "";


                if ("".equals(u) || "".equals(p)) {
                    SystemUtil.showToast(context, "请点击右上角三个点填写个人信息。");
                } else {
                    username = u;
                    phonenumber = p;
                    //录音


                    Thread thread1 = new Thread() {
                        public String upload_log_str = "0";

                        @Override
                        public void run() {

                            try {
                                resp = "";
                                StringBuffer sb = new StringBuffer();
                                sb.append("{");
                                sb.append("\"check_id\":\"" + check_id + "\",");
                                sb.append("\"username\":\"" + username + "\",");
                                sb.append("\"phonenumber\":\"" + phonenumber + "\",");
                                sb.append("\"test_id\":\"" + checkbean.getTest_id() + "\",");
                                sb.append("\"network_operator_name\":\"" + net_tool.information.getNetworkOperatorName() + "\",");
                                sb.append("\"address\":\"" + net_tool.information.getAddress() + "\",");
                                sb.append("\"eci\":\"" + net_tool.information.getECI() + "\",");
                                sb.append("\"tac\":\"" + net_tool.information.getTAC() + "\",");
                                sb.append("\"bsss\":\"" + net_tool.information.getBSSS() + "\",");
                                sb.append("\"gps\":\"" + net_tool.information.getGPS() + "\",");
                                sb.append("\"network_type\":\"" + net_tool.information.getStrNetWork_type() + "\",");
                                sb.append("\"phone_type\":\"" + net_tool.information.getPhoneType() + "\",");
                                sb.append("\"coll_time\":\"" + net_tool.information.getCollTime2() + "\",");
                                sb.append("\"download_speed\":\"" + currentDownSpeed + "\",");
                                sb.append("\"to_number\":\"" + checkbean.getTo_number() + "\",");
                                sb.append("\"system_mark\":\"" + checkbean.getSystem_mark() + "\",");


                                if (checkBean.checkItem.size() == 0) {
                                    sb.append("\"upload_speed\":\"" + currentUpSpeed + "\"");
                                } else {
                                    sb.append("\"upload_speed\":\"" + currentUpSpeed + "\",");
                                }

                                if (checkBean.checkItem.contains("信号强度核查")) {

                                    sb.append("\"bsss_check\":\"" + checkbean.getBsss_ok() + "\",");
                                    sb.append("\"verify_bsss_value\":\"" + checkbean.getTest_name() + "\",");
                                } else {
                                    sb.append("\"bsss_check\":\"" + String.valueOf(-1) + "\",");
                                }

                                if (checkBean.checkItem.contains("网络类型核查")) {
                                    sb.append("\"network_type_check\":\"" + checkbean.getNetwork_type_ok() + "\",");
                                    sb.append("\"verify_network_type_value\":\"" + checkbean.getNetwork_type() + "\",");
                                } else {
                                    sb.append("\"network_type_check\":\"" + String.valueOf(-1) + "\",");
                                }

                                if (checkBean.checkItem.contains("Gps位置核查")) {
                                    sb.append("\"gps_check\":\"" + checkbean.getGps_ok() + "\",");
                                    sb.append("\"verify_gps_value\":\"" + checkbean.getGps_lon() + "," + checkbean.getGps_lat() + "\",");
                                } else {
                                    sb.append("\"gps_check\":\"" + String.valueOf(-1) + "\",");
                                }

                                if (checkBean.checkItem.contains("ECI核查")) {
                                    sb.append("\"ECI_check\":\"" + checkbean.getECI_ok() + "\",");
                                    sb.append("\"verify_ECI_value\":\"" + checkbean.getECI() + "\",");
                                } else {
                                    sb.append("\"ECI_check\":\"" + String.valueOf(-1) + "\",");
                                }

                                if (checkBean.checkItem.contains("TAC核查")) {
                                    sb.append("\"TAC_check\":\"" + checkbean.getTAC_ok() + "\",");
                                    sb.append("\"verify_TAC_value\":\"" + checkbean.getTAC() + "\",");
                                } else {
                                    sb.append("\"TAC_check\":\"" + String.valueOf(-1) + "\",");
                                }
                                if (checkBean.checkItem.contains("时间范围核查")) {
                                    sb.append("\"datetime_check\":\"" + checkbean.getDatetime_ok() + "\",");
                                    sb.append("\"verify_datetime_value\":\"" + checkbean.getStart_datetime() + "_" + checkbean.getEnd_datetime() + "\",");
                                } else {
                                    sb.append("\"datetime_check\":\"" + String.valueOf(-1) + "\",");
                                }
                                if (network_test) {
                                    sb.append("\"verify_url_value\":\"" + checkbean.getUrl() + "\",");
                                    sb.append("\"url_check\":\"" + String.valueOf(1) + "\"");
                                } else {
                                    sb.append("\"url_check\":\"" + String.valueOf(0) + "\"");
                                }

                                sb.append("}");
                                Log.e("", sb.toString());
                                resp = connNetReq.post(getString(R.string.upload_url), sb.toString());

                                /**
                                 * 该报告已上传成功，请勿重复上传 2
                                 * 该报告上传成功 1
                                 * 上传失败：
                                 * 未知失败 错误码0
                                 * 由于获取数据库接口导致失败 错误码401
                                 * test_id查询出错 402
                                 * test数据保存出错 403
                                 * check_id查询出错 404
                                 * check数据保存出错 405
                                 * 保存id出错 406
                                 *
                                 * @type {number}
                                 */
                                switch (resp) {
                                    case "1":
                                        upload_log_str = "测试报告上传成功。";
                                        break;
                                    case "-1":
                                        upload_log_str = "测试报告上传失败";
                                        break;
                                    case "2":
                                        upload_log_str = "记录已上传，请勿重复上传。";
                                        break;
                                    case "0":
                                        upload_log_str = "错误0 app时间同步失败";
                                        break;
                                    case "401":
                                        upload_log_str = "错误401 服务器数据库接口获取失败";
                                        break;
                                    case "402":
                                        upload_log_str = "错误402 测试记录ID查询错误";
                                        break;
                                    case "403":
                                        upload_log_str = "错误403 测试数据保存错误";
                                        break;
                                    case "404":
                                        upload_log_str = "错误404 核查报告ID查询错误";
                                        break;
                                    case "405":
                                        upload_log_str = "错误405 核查报告保存出错";
                                        break;
                                    case "406":
                                        upload_log_str = "错误406 关系ID保存错误";
                                        break;
                                }
                            } catch (Exception e) {
                                Log.e("upload", e.toString());
                                upload_log_str = "错误 407 app上传接口错误";
                            } finally {
                                upload_warm = upload_warm + upload_log_str;
                            }
                        }
                    };

                    Thread thread2 = new Thread() {
                        public String upload_wav_str = "";

                        @Override
                        public void run() {
                            if ("1".equals(resp)) {
                                try {
                                    String res = connNetReq.uploadFile(new File(currnt_pcm_file), getString(R.string.file_upload));

                                    switch (res) {
                                        case "0":
                                            upload_wav_str = "错误0 录音上传失败";
                                        case "300":
                                            upload_wav_str = "录音上传成功";
                                        case "301":
                                            upload_wav_str = "错误301 录音上传失败";
                                        case "302":
                                            upload_wav_str = "错误302录音上传失败";
                                    }
                                } catch (Exception e) {
                                    upload_wav_str = "错误303 app异常导致录音上传失败";

                                }
                            } else {


                            }
                            upload_warm = upload_warm + " " + upload_wav_str;
                            new Handler(context.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    SystemUtil.showToast(MainActivity.this, upload_warm);
                                    Looper.loop();
                                }
                            });


                        }
                    };


                    try {
                        thread1.start();
                        thread1.join();
                        thread2.start();
                        thread2.join();


                    } catch (Exception e) {
                        SystemUtil.showToast(context, "报告上传进程异常");
                    }
                }
            }
        });
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(rootview, Gravity.TOP, 0, 30);


    }

    public Button submit_inf = null;
    //测试报告弹窗
    private View personView;
    private PopupWindow personpopupWindow;
    SharedPreferences sp;

    public void showPopWindowns_per() {
        personView = LayoutInflater.from(this).inflate(R.layout.window_personal_inf, null);
        personpopupWindow = new PopupWindow(personView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

        submit_inf = (Button) personView.findViewById(R.id.submit_inf);


        ((EditText) personView.findViewById(R.id.phonenumber)).setText(sp.getString("phonenumber", ""));
        ((EditText) personView.findViewById(R.id.username)).setText(sp.getString("username", ""));

        submit_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = ((EditText) personView.findViewById(R.id.username)).getText().toString();
                String p = ((EditText) personView.findViewById(R.id.phonenumber)).getText().toString();

                if ("".equals(u) && "".equals(p)) {
                    Toast.makeText(context, "请输入有效值", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", u);
                    edit.putString("phonenumber", p);
                    if (edit.commit()) {
                        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        personpopupWindow.showAtLocation(rootview, Gravity.TOP, 30, 30);
    }

    /**
     * 发布注册任务
     */


    public Timer timer = null;
    public RefreshCheckList timetask = null;

    public void LunchTimer() {
        timer = new Timer();
        timetask = new RefreshCheckList();
        int refresh_cycle = Integer.valueOf(getString(R.string.refresh_cycle));
        timer.schedule(timetask, 0, 1000 * refresh_cycle);

    }


    /**
     * destory上次使用的 Timer
     */
    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timetask != null) {
            timetask.cancel();
            timetask = null;
        }
    }

    /**
     * 周期性任务
     * 采集网络信息更新核查列表
     */

    class RefreshCheckList extends TimerTask {
        @Override
        public void run() {

            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    net_tool.start();
                    checklistadpt.notifyDataSetChanged();
                    Looper.loop();
                }
            });


        }
    }

    private void getCheckVersion() {
        new Thread() {
            String warmText = "版本已是最新";
            versionInfo version = new versionInfo();
            boolean isRequire = false;

            public void run() {
                String res = "";
                try {
                    String phoneNumber = "13508522561";
                    res = connNetReq.post(getString(R.string.getCheckVersion), "{\"phoneNumber\":\"" + phoneNumber + "\"}");
                    res = AesAndToken.decrypt(res, AesAndToken.KEY);
                    version = connNetReq.jsonToVersionInfo(res);
                    if (version.getAppName().equals("")) {
                        warmText = "版本更新检查错误";
                    } else {
                        double currentVersion = Double.valueOf(getString(R.string.currentVersion));
                        double serverVersion = Double.valueOf(version.getServerVersion());
                        if (serverVersion > currentVersion) {
                            isRequire = true;
                        } else {
                            isRequire = false;
                        }
                    }
                } catch (Exception e) {
                    Log.e("errMain:版本更新检查，大概率是时间错误", e.toString());
                    SystemUtil.showToast(context,"请调整手机为北京时间");
                }

                try {
                    new Handler(MainActivity.this.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequire) {
                                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                //    设置Title的图标
                                //builder.setIcon(R.drawable.ic_launcher);
                                //    设置Title的内容
                                builder.setTitle("版本更新检查");
                                //    设置Content来显示一个信息
                                builder.setMessage(version.getUpgradeinfo());
                                //    设置一个PositiveButton
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        apk_path = version.getUpdateUrl();
                                        showDownloadDialog();
                                        //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //    设置一个NegativeButton
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                //    显示出该对话框
                                builder.show();
                            } else {
                                Toast.makeText(MainActivity.this, warmText, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("errHander", e.toString());
                }
            }
        }.start();
    }

    ProgressDialog progressDialog = null;

    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new downloadAsyncTask().execute();
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "执行至--onPreExecute");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            Log.e(TAG, "执行至--doInBackground");

            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(apk_path);
                connection = (HttpURLConnection) url.openConnection();

                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;

                while ((len = in.read(buffer)) != -1) {

                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;

                    int curProgress = (int) (((float) readLength / fileLength) * 100);

                    publishProgress(curProgress);

                    if (readLength >= fileLength) {

                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            //安装应用
            installApp();

        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //if(Build.VERSION.SDK_INT>=24)

        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.example.dengqian.bg220.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        MainActivity.this.startActivity(intent);

    }


}
