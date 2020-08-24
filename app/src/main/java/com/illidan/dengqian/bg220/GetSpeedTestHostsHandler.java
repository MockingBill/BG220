package com.illidan.dengqian.bg220;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class GetSpeedTestHostsHandler extends Thread {

    HashMap<Integer, String> mapKey = new HashMap<>();
    HashMap<Integer, List<String>> mapValue = new HashMap<>();
    double selfLat = 0.0;
    double selfLon = 0.0;
    boolean finished = false;


    public HashMap<Integer, String> getMapKey() {
        return mapKey;
    }

    public HashMap<Integer, List<String>> getMapValue() {
        return mapValue;
    }

    public double getSelfLat() {
        return selfLat;
    }

    public double getSelfLon() {
        return selfLon;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        //Get latitude, longitude
        String line=null;
        JSONArray jsonarray;
        try {
            URL url = new URL("https://www.speedtest.net/api/js/servers?engine=js");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            int code = urlConnection.getResponseCode();

            if (code == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                line=br.readLine();
                br.close();
                jsonarray=new JSONArray(line);

                for(int i=0;i<jsonarray.length();i++){
                    JSONObject json_obj=jsonarray.getJSONObject(i);
                    String net_url=json_obj.getString("url");
                    String lat=json_obj.getString("lat");
                    String lon=json_obj.getString("lon");
                    String distance=json_obj.getString("distance");
                    String name=json_obj.getString("name");
                    String country=json_obj.getString("country");
                    String cc=json_obj.getString("cc");
                    String sponsor=json_obj.getString("sponsor");
                    String id=json_obj.getString("id");
                    String preferred=json_obj.getString("preferred");
                    String host=json_obj.getString("host");
                    List<String> ls = Arrays.asList(lat, lon, name, country, cc, sponsor, host,distance);
                    mapKey.put(i, net_url);
                    mapValue.put(i, ls);
                }
                finished = true;
            }else{
                do_static();
            }
        } catch (Exception ex) {
            do_static();
            ex.printStackTrace();
        }
        }

        public void do_static(){
            mapKey.clear();
            mapValue.clear();
            String lines="[{\"url\":\"http:\\/\\/speedtest1.gz.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"26.6500\",\"lon\":\"106.6333\",\"distance\":46,\"name\":\"Guiyang\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Mobile,GuiZhou\",\"id\":\"16398\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.gz.chinamobile.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest2.cq.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"29.5583\",\"lon\":\"106.5667\",\"distance\":192,\"name\":\"Chongqing\",\"country\":\"CN\",\"cc\":\"CN\",\"sponsor\":\"Chongqing Mobile Company\",\"id\":\"17584\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest2.cq.chinamobile.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest.cqwin.com:8080\\/speedtest\\/upload.php\",\"lat\":\"29.5583\",\"lon\":\"106.5667\",\"distance\":192,\"name\":\"Chongqing\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Unicom\",\"id\":\"31985\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest.cqwin.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.cqccn.com:8080\\/speedtest\\/upload.php\",\"lat\":\"29.5583\",\"lon\":\"106.5667\",\"distance\":192,\"name\":\"Chongqing\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"CCN\",\"id\":\"5530\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.cqccn.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.gx.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"22.8167\",\"lon\":\"108.3167\",\"distance\":287,\"name\":\"Nanning\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"GX ChinaMobile\",\"id\":\"15863\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.gx.chinamobile.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speed4.gxmenjin.com:8080\\/speedtest\\/upload.php\",\"lat\":\"22.8167\",\"lon\":\"108.3167\",\"distance\":287,\"name\":\"Nanning\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"GX-Telecom\",\"id\":\"27810\",\"preferred\":0,\"https_functional\":1,\"host\":\"speed4.gxmenjin.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/121.31.15.130:8080\\/speedtest\\/upload.php\",\"lat\":\"22.8167\",\"lon\":\"108.3167\",\"distance\":287,\"name\":\"Nanning\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"GX-Unicom\",\"id\":\"5674\",\"preferred\":0,\"https_functional\":1,\"host\":\"121.31.15.130.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.yncuc.net:8080\\/speedtest\\/upload.php\",\"lat\":\"25.0667\",\"lon\":\"102.6833\",\"distance\":315,\"name\":\"Kunming\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"Yunnan Chinaunicom\",\"id\":\"5103\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.yncuc.net.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.sc.189.cn:8080\\/speedtest\\/upload.php\",\"lat\":\"30.5728\",\"lon\":\"104.0668\",\"distance\":324,\"name\":\"\\u6210\\u90fd\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Telecom\",\"id\":\"29071\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.sc.189.cn.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.sc.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"30.6586\",\"lon\":\"104.0647\",\"distance\":329,\"name\":\"Chengdu\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Mobile Group Sichuan\",\"id\":\"4575\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.sc.chinamobile.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.wangjia.net:8080\\/speedtest\\/upload.php\",\"lat\":\"30.6586\",\"lon\":\"104.0647\",\"distance\":329,\"name\":\"Chengdu\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Unicom\",\"id\":\"2461\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.wangjia.net.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest2.sc.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"30.6586\",\"lon\":\"104.0647\",\"distance\":329,\"name\":\"Chengdu\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Mobile Group Sichuan Co.,Ltd.\",\"id\":\"24337\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest2.sc.chinamobile.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest01.hn165.com:8080\\/speedtest\\/upload.php\",\"lat\":\"28.1961\",\"lon\":\"112.9722\",\"distance\":357,\"name\":\"Changsha\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"\\u6e56\\u5357\\u8054\\u901a5G\",\"id\":\"4870\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest01.hn165.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/mygod998.vicp.net:8080\\/speedtest\\/upload.php\",\"lat\":\"28.1961\",\"lon\":\"112.9722\",\"distance\":357,\"name\":\"Changsha\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"Hunan Telecom 5G\",\"id\":\"28225\",\"preferred\":0,\"https_functional\":1,\"host\":\"mygod998.vicp.net.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest2.hn.chinamobile.com:8080\\/speedtest\\/upload.php\",\"lat\":\"28.1854\",\"lon\":\"113.0325\",\"distance\":361,\"name\":\"ChangSha\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"China Mobile HuNan 5G\",\"id\":\"28491\",\"preferred\":0,\"host\":\"speedtest2.hn.chinamobile.com:8080\"},{\"url\":\"http:\\/\\/speedtest02.hn165.com:8080\\/speedtest\\/upload.php\",\"lat\":\"27.8280\",\"lon\":\"113.1339\",\"distance\":362,\"name\":\"Zhuzhou\",\"country\":\"China\",\"cc\":\"CN\",\"sponsor\":\"Changsha, Hunan Unicom\",\"id\":\"26677\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest02.hn165.com.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtest1.vtn.com.vn:8080\\/speedtest\\/upload.php\",\"lat\":\"21.0285\",\"lon\":\"105.8542\",\"distance\":415,\"name\":\"Hanoi\",\"country\":\"Vietnam\",\"cc\":\"VN\",\"sponsor\":\"VNPT-NET\",\"id\":\"6085\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtest1.vtn.com.vn.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtestkv1a.viettel.vn:8080\\/speedtest\\/upload.php\",\"lat\":\"21.0285\",\"lon\":\"105.8542\",\"distance\":415,\"name\":\"Hanoi\",\"country\":\"Vietnam\",\"cc\":\"VN\",\"sponsor\":\"Viettel Network\",\"id\":\"9903\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtestkv1a.viettel.vn.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/speedtesthn.fpt.vn:8080\\/speedtest\\/upload.php\",\"lat\":\"21.0285\",\"lon\":\"105.8542\",\"distance\":415,\"name\":\"Hanoi\",\"country\":\"Vietnam\",\"cc\":\"VN\",\"sponsor\":\"FPT Telecom\",\"id\":\"2552\",\"preferred\":0,\"https_functional\":1,\"host\":\"speedtesthn.fpt.vn.prod.hosts.ooklaserver.net:8080\"},{\"url\":\"http:\\/\\/hnispeedtest.cmctelecom.vn:8080\\/speedtest\\/upload.php\",\"lat\":\"21.0285\",\"lon\":\"105.8542\",\"distance\":415,\"name\":\"Hanoi\",\"country\":\"Vietnam\",\"cc\":\"VN\",\"sponsor\":\"CMC Telecom\",\"id\":\"6342\",\"preferred\":0,\"https_functional\":1,\"host\":\"hnispeedtest.cmctelecom.vn.prod.hosts.ooklaserver.net:8080\"}]";
            try{
                JSONArray jsonarray=new JSONArray(lines);
                for(int i=0;i<jsonarray.length();i++){
                    JSONObject json_obj=jsonarray.getJSONObject(i);
                    String net_url=json_obj.getString("url");
                    String lat=json_obj.getString("lat");
                    String lon=json_obj.getString("lon");
                    String distance=json_obj.getString("distance");
                    String name=json_obj.getString("name");
                    String country=json_obj.getString("country");
                    String cc=json_obj.getString("cc");
                    String sponsor=json_obj.getString("sponsor");
                    String id=json_obj.getString("id");
                    String preferred=json_obj.getString("preferred");
                    String host=json_obj.getString("host");
                    List<String> ls = Arrays.asList(lat, lon, name, country, cc, sponsor, host,distance);
                    mapKey.put(i, net_url);
                    mapValue.put(i, ls);
                }
                finished = true;
            }catch (Exception e){
                finished = false;
            }
        }

}
