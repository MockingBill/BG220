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
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

import com.illidan.dengqian.bg220.tool_bean.Myhander;
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
    //现废弃
    EditText ed = null;
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
            "https://weixin.qq.com/",
            "https://www.sina.com.cn/",
            "https://www.iqiyi.com/",
            "https://v.qq.com/",
            "http://www.sohu.com",
            "https://ai.taobao.com/",
            "https://www.zhihu.com/signin?next=%2F",
            "https://www.163.com/",
            "http://www.xinhuanet.com/",
            "http://www.ce.cn/",
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

    public static void cleadEd(int panle_id) {
        if (panle_id == 1) {
            if (MainActivity.text_view != null) {
                MainActivity.text_view.setText("");
            }
        } else if (panle_id == 2) {
            if (MainActivity.check_view != null) {
                MainActivity.check_view.setText("");
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //手机号框
        ed = (EditText) findViewById(R.id.ed);

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
     * 扫码结果转Map
     * @param str
     * @return
     */
    public static Map<String, ArrayList<String>> jsonToMap(String str) {
        Map<String, ArrayList<String>> map = new HashMap<>();
        map.put("tele", new ArrayList<String>());
        map.put("url_list", new ArrayList<String>());
        map.put("ECI", new ArrayList<String>());
        map.put("TAC", new ArrayList<String>());

        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray tele = jsonObject.getJSONArray("tele");
            JSONArray url_list = jsonObject.getJSONArray("url_list");
            JSONArray ECI = jsonObject.getJSONArray("ECI");
            JSONArray TAC = jsonObject.getJSONArray("TAC");

            for (int i = 0; i < tele.length(); i++) {
                map.get("tele").add(tele.getString(i));
            }
            for (int i = 0; i < url_list.length(); i++) {
                map.get("url_list").add(url_list.getString(i));
            }
            for (int i = 0; i < ECI.length(); i++) {
                map.get("ECI").add(ECI.getString(i));
            }
            for (int i = 0; i < TAC.length(); i++) {
                map.get("TAC").add(TAC.getString(i));
            }

        } catch (Exception e) {
            Log.e("json转map失败", e.toString());
        }
        return map;

    }


    /**
     * 上网测试类
     * @param scan_result
     */
    public void testNetWork(Map<String, ArrayList<String>> scan_result) {
        cleadEd(TEXT_VIEW);
        mListView.setVisibility(View.VISIBLE);
        net_tool.start();
        net_tool.information.setCheckList(scan_result);
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
                            String isPassStr = "";
                            if (net_tool.information.isPassTest()) {
                                isPassStr = "通过";
                            } else {
                                isPassStr = "不通过";
                            }
                            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("上网测试结果")//标题
                                    .setMessage("上网测试" + isPassStr+"\n"+net_tool.information.show())//内容
                                    .setIcon(R.mipmap.ic_launcher)//图标
                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            alertDialog1.show();
                            Looper.loop();
                        }
                    });


                } catch (Exception e) {
                    Log.e(SystemUtil.TAG, e.toString());
                }

            }
        }.start();
        Log.e("show", net_tool.information.show());
        appendEd(net_tool.information.show(), MainActivity.TEXT_VIEW);
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
            locationManager.requestLocationUpdates(locationProvider, 1000, 10, mylocationListener);
        }
        call_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class<TelephonyManager> c = TelephonyManager.class;
                    Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                    mthEndCall.setAccessible(true);

                    CallPhone();
//                    final Object obj = mthEndCall.invoke(telMag, (Object[]) null);
//                    if ("".equals(ed.getText().toString())) {
//                        MediaPlayer mediaPlayer = new MediaPlayer();
//                        mediaPlayer.reset();
//                        mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/BG2019/test_music.mp3");
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                    } else {



//                    }
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
                                SystemUtil.SendPostRequest("http://10.196.135.137:8000/upload/",net_tool.information.show());
                            }catch(Exception e){
                                Log.e(TAG,e.toString());
                            }
                        }
                    }.start();
                } else {
                    if (isSIM) {
                        //扫码
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);

                        startActivityForResult(intent, REQUEST_CODE_SCAN);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            //UI动态设置
            startOrStop_test.setText("停止测试");
            isInTest = true;
            call_test.setEnabled(true);
            web_test.setEnabled(true);
            check_info = jsonToMap("{tele:['18785185684'],url_list:['https://www.baidu.com','https://www.sina.com'],ECI:['141424397'],TAC:['34062']}");

            /**
             * 扫描结果完成后响应
             * 将导入的结果映射到面板
             */
            String scan_result = data.getExtras().getString("ResultQRCode");
            check_info = jsonToMap(scan_result);
            StringBuffer sb = new StringBuffer();
            sb.append("测试要求\n");
            sb.append("ECI = " + check_info.get("ECI").get(0).toString() + "\n");
            sb.append("TAC = " + check_info.get("TAC").get(0).toString() + "\n");
            sb.append("tele = " + check_info.get("tele").get(0).toString() + "\n");
            sb.append("url_list = " + check_info.get("url_list").toString() + "\n");
            appendEd(sb.toString(), MainActivity.CHECK_VIEW);
            call_test.setEnabled(true);
            web_test.setEnabled(true);
            startOrStop_test.setText("停止测试");
            isInTest = true;
        }
    }

    private void CallPhone() {
        // 拨号：激活系统的拨号组件
        String number = ed.getText().toString();
        if ("".equals(number)) {
            Toast.makeText(MainActivity.this, "输入手机号", Toast.LENGTH_SHORT).show();
        } else {
            cu_number = number;
            Intent intent = new Intent(); // 意图对象：动作 + 数据
            intent.setAction(Intent.ACTION_CALL); // 设置动作
            Uri data = Uri.parse("tel:" + number); // 设置数据
            intent.setData(data);
            startActivity(intent); // 激活Activity组件
        }
    }




}
