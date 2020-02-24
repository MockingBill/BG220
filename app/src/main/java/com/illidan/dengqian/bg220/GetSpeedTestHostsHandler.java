package com.illidan.dengqian.bg220;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        try {
            URL url = new URL("https://www.speedtest.net/speedtest-config.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int code = urlConnection.getResponseCode();

            if (code == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.contains("isp=")) {
                        continue;
                    }
                    selfLat = Double.parseDouble(line.split("lat=\"")[1].split(" ")[0].replace("\"", ""));
                    selfLon = Double.parseDouble(line.split("lon=\"")[1].split(" ")[0].replace("\"", ""));
                    break;
                }

                br.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        String uploadAddress = "";
        String name = "";
        String country = "";
        String cc = "";
        String sponsor = "";
        String lat = "";
        String lon = "";
        String host = "";
        //Best server
        int count = 0;
//            try {
//////                URL url = new URL("https://www.speedtest.net/speedtest-servers-static.php");
//////                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//////                int code = urlConnection.getResponseCode();
//////
//////                if (code == 200) {
//////                    BufferedReader br = new BufferedReader(
//////                            new InputStreamReader(
//////                                    urlConnection.getInputStream()));
//////
//////                    String line;
//////                    while ((line = br.readLine()) != null) {
//////                        if (line.contains("<server url")) {
//////                            uploadAddress = line.split("server url=\"")[1].split("\"")[0];
//////                            lat = line.split("lat=\"")[1].split("\"")[0];
//////                            lon = line.split("lon=\"")[1].split("\"")[0];
//////                            name = line.split("name=\"")[1].split("\"")[0];
//////                            country = line.split("country=\"")[1].split("\"")[0];
//////                            cc = line.split("cc=\"")[1].split("\"")[0];
//////                            sponsor = line.split("sponsor=\"")[1].split("\"")[0];
//////                            host = line.split("host=\"")[1].split("\"")[0];
//////                            if("China".equals(country)){
//////                                List<String> ls = Arrays.asList(lat, lon, name, country, cc, sponsor, host);
//////                                mapKey.put(count, uploadAddress);
//////                                mapValue.put(count, ls);
//////                                count++;
//////                            }
//////
//////
//////                        }
//////                    }
//////
//////                    br.close();
//////                }
//////            } catch (Exception ex) {
//////                ex.printStackTrace();
//////            }

        String[] uploadAddressArray = {"http://speedtest5.xj.chinamobile.com:8080/speedtest/upload.php", "http://5gtest.hl.chinamobile.com:8080/speedtest/upload.php", "http://speedtest1.jlinfo.jl.cn:8080/speedtest/upload.php", "http://speedtest2.jl.chinamobile.com:8080/speedtest/upload.php", "http://4g.xj169.com:8080/speedtest/upload.php", "http://speedtest1.xj.chinamobile.com:8080/speedtest/upload.php", "http://speedtest.open189.net:8080/speedtest/upload.php", "http://speedtest1.ln.chinamobile.com:8080/speedtest/upload.php", "http://www.nmwanwang.com/speedtest/upload.aspx", "http://speedtest1.nm.chinamobile.com:8080/speedtest/upload.php", "http://www2.unicomtest.com:8080/speedtest/upload.php", "http://bj3.unicomtest.com:8080/speedtest/upload.php", "http://speedtest.bmcc.com.cn:8080/speedtest/upload.php", "http://speedtest25.jillbanging.com:8080/speedtest/upload.php", "http://speedtest3.xj.chinamobile.com:8080/speedtest/upload.php", "http://speedtest3.online.tj.cn:8080/speedtest/upload.php", "http://speedtest.nx.chinamobile.com:8080/speedtest/upload.php", "http://testsx.njgean.com:8080/speedtest/upload.php", "http://speedtest2.xj.chinamobile.com:8080/speedtest/upload.php", "http://jnltwy.com:8080/speedtest/upload.php", "http://5g.unicomjnwy.com:8080/speedtest/upload.php", "http://speedtest.sd.chinamobile.com:8080/speedtest/upload.php", "http://221.199.9.35:8080/speedtest/upload.php", "http://speedtest1.sd.huawei.com:8080/speedtest/upload.php", "http://lanzhouunicom.com:8080/speedtest/upload.php", "http://speedtest1.gs.chinamobile.com:8080/speedtest/upload.php", "http://speedtest.bajianjun.com:8080/speedtest/upload.php", "http://speedtest3.sd.huawei.com:8080/speedtest/upload.php", "http://4glianyungang1.speedtest.jsinfo.net:8080/speedtest/upload.php", "http://xatest.wo-xa.com:8080/speedtest/upload.php", "http://speedtest.one-punch.win:8080/speedtest/upload.php", "http://4gsuzhou1.speedtest.jsinfo.net:8080/speedtest/upload.php", "http://speedtest.zjmobile.com:8080/speedtest/upload.php", "http://speedtest02.js165.com:8080/speedtest/upload.php", "http://5gnanjing.speedtest.jsinfo.net:8080/speedtest/upload.php", "http://speedtest2.ah.chinamobile.com:8080/speedtest/upload.php", "http://112.122.10.26:8080/speedtest/upload.php", "http://cmnet-speed.com:8080/speedtest/upload.php", "http://speedtest.shunicomtest.com:8080/speedtest/upload.php", "http://speedtest1.online.sh.cn:8080/speedtest/upload.php", "http://5g.shunicomtest.com:8080/speedtest/upload.php", "http://speedtest4.sh.chinamobile.com:8080/speedtest/upload.php", "http://speedtest1.wangjia.net:8080/speedtest/upload.php", "http://speedtest2.sc.chinamobile.com:8080/speedtest/upload.php", "http://vipspeedtest8.wuhan.net.cn:8080/speedtest/upload.php", "http://113.57.249.2:8080/speedtest/upload.php", "http://vipspeedtest1.wuhan.net.cn:8080/speedtest/upload.php", "http://speedtest1.sc.189.cn:8080/speedtest/upload.php", "http://ltetest1.139site.com:8080/speedtest/upload.php", "http://122.229.136.10:8080/speedtest/upload.php", "http://ltetest3.139site.com:8080/speedtest/upload.php", "http://speedtest1.xz.chinamobile.com:8080/speedtest/upload.php", "http://speedtest1.cqccn.com:8080/speedtest/upload.php", "http://speedtest.nc.jx.chinamobile.com:8080/speedtest/upload.php", "http://speedtest01.hn165.com:8080/speedtest/upload.php", "http://mygod998.vicp.net/speedtest/upload.aspx", "http://speedtest2.hn.chinamobile.com/upload.php", "http://speedtest02.hn165.com/speedtest/upload.php", "http://speedtest1.gz.chinamobile.com:8080/speedtest/upload.php", "http://upload1.testspeed.kaopuyun.com:8080/speedtest/upload.php", "http://speedtest1.fj.chinamobile.com:8080/speedtest/upload.php", "http://speedtest1.yncuc.net:8080/speedtest/upload.php", "http://speedtest1.gzuni.com:8080/speedtest/upload.php", "http://speedtest1.gd.chinamobile.com:8080/speedtest/upload.php", "http://www.gdspeedtest.com:8080/speedtest/upload.php", "http://speedtest1.gx.chinamobile.com:8080/speedtest/upload.php", "http://121.31.15.130:8080/speedtest/upload.php", "http://speed4.gxmenjin.com:8080/speedtest/upload.php", "http://speedtest.75510010.com:8080/speedtest/upload.php", "http://speedtest3.gd.chinamobile.com:8080/speedtest/upload.php", "http://www.suntechspeedtest.com:8080/speedtest/upload.php", "http://shenron.smartone.com:8080/speedtest/upload.php", "http://speedtest.website-solution.net:8080/speedtest/upload.php", "http://speedtest1.hi.chinamobile.com:8080/speedtest/upload.php"};
        String[] latArray = {"47.8449", "45.7500", "43.8800", "43.8800", "43.7958", "43.7958", "43.7958", "41.7333", "40.8424", "40.8424", "39.9139", "39.9139", "39.9139", "39.9139", "39.4677", "39.1333", "38.4872", "37.8694", "37.1142", "36.6667", "36.6667", "36.6667", "36.6000", "36.0671", "36.0333", "36.0333", "36.0333", "35.1047", "34.6000", "34.2667", "34.2667", "34.2600", "32.2069", "32.0602", "32.0500", "31.8667", "31.8667", "31.5667", "31.2000", "31.2000", "31.2000", "31.2000", "30.6586", "30.6586", "30.5833", "30.5833", "30.5833", "30.5728", "30.2500", "30.2500", "29.8667", "29.6500", "29.5583", "28.6829", "28.1961", "28.1961", "28.1854", "27.8280", "26.6500", "26.0761", "26.0761", "25.0667", "23.1333", "23.1333", "23.1333", "22.8167", "22.8167", "22.8167", "22.5333", "22.5333", "22.2500", "22.2500", "22.2500", "20.0428",};
        String[] lonArray = {"88.1413", "126.6333", "125.3228", "125.3228", "87.6089", "87.6089", "87.6089", "123.8833", "111.7500", "111.7500", "116.3917", "116.3917", "116.3917", "116.3917", "75.9938", "117.1833", "106.2309", "112.5603", "79.9222", "116.9833", "116.9833", "116.9833", "105.3200", "120.3826", "103.8000", "103.8000", "103.8000", "118.3564", "119.1667", "108.9000", "108.9000", "117.2100", "119.4490", "118.7968", "118.7667", "117.2833", "117.2833", "120.3000", "121.5000", "121.5000", "121.5000", "121.5000", "104.0647", "104.0647", "114.2833", "114.2833", "114.2833", "104.0668", "120.1667", "120.1667", "121.5500", "91.1000", "106.5667", "115.8582", "112.9722", "112.9722", "113.0325", "113.1339", "106.6333", "119.3064", "119.3064", "102.6833", "113.2667", "113.2667", "113.2667", "108.3167", "108.3167", "108.3167", "114.1333", "114.1333", "114.1667", "114.1667", "114.1667", "110.3417",};
        String[] nameArray = {"Aletai", "Harbin", "Changchun", "Changchun", "Urumqi", "Urumqi", "Urumqi", "Shenyang", "Huhehaote", "Hohhot", "Beijing", "Beijing", "Beijing", "Beijing", "Kashi", "TianJin", "Yinchuan", "Taiyuan", "Hetian", "Jinan", "Jinan", "Jinan", "Ningxia", "Qingdao", "Lanzhou", "Lanzhou", "Lanzhou", "Linyi", "Lianyungang", "Xi'an", "Xi'an", "Suzhou", "ZhenJiang", "Nanjing", "Nanjing", "Hefei", "Hefei", "Wuxi", "Shanghai", "Shanghai", "ShangHai", "Shanghai", "Chengdu", "Chengdu", "Wuhan", "Wuhan", "Wuhan", "成都", "Hangzhou", "Hangzhou", "Ningbo", "Lhasa", "Chongqing", "NanChang", "Changsha", "Changsha", "ChangSha", "Zhuzhou", "Guiyang", "Fuzhou", "Fuzhou", "Kunming", "Guangzhou", "Guangzhou", "Guangzhou", "Nanning", "Nanning", "Nanning", "ShenZhen", "Shenzhen", "Hong", "Hong", "Hong", "Haikou",};
        String[] countryArray = {"China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China", "China",};
        String[] ccArray = {"CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CH", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "HK", "HK", "HK", "CN"};
        String[] sponsorArray = {"China", "ChinaMobile-HeiLongjiang", "China", "China", "xjunicom", "China", "XinJiang", "ChinaMobile,", "nmzl", "China", "Beijing", "Beijing", "China", "China", "China", "ChinaUnicom-5G", "ChinaMobile,Ningxia", "shanxi", "China", "China", "Shandong", "ShanDong", "Chinaunicom", "ShanDong", "China", "Lanzhou,China", "China", "ShanDong", "China", "Xi'an", "xi'an", "China", "China", "China", "China", "安徽移动5G", "ChinaUnicom", "China", "China", "China", "China", "Chinamobile-5G", "China", "China", "wuhan", "China", "China", "四川电信", "China", "China", "China", "China", "CCN", "China", "Changsha,", "Hunan", "China", "Changsha,", "China", "China", "Fuzhou", "Yunnan", "Guangzhou", "China", "ChinaTelecom", "GX", "GX-Unicom", "GX-Telecom", "75510010", "China", "STC", "SmarTone", "Website", "Chinamobile,Hainan",};
        String[] hostArray = {"speedtest5.xj.chinamobile.com:8080", "5gtest.hl.chinamobile.com:8080", "speedtest1.jlinfo.jl.cn:8080", "speedtest2.jl.chinamobile.com:8080", "4g.xj169.com:8080", "speedtest1.xj.chinamobile.com:8080", "speedtest.open189.net:8080", "speedtest1.ln.chinamobile.com:8080", "www.nmwanwang.com:8080", "speedtest1.nm.chinamobile.com:8080", "www2.unicomtest.com:8080", "bj3.unicomtest.com:8080", "speedtest.bmcc.com.cn:8080", "speedtest25.jillbanging.com:8080", "speedtest3.xj.chinamobile.com:8080", "speedtest3.online.tj.cn:8080", "speedtest.nx.chinamobile.com:8080", "testsx.njgean.com:8080", "speedtest2.xj.chinamobile.com:8080", "jnltwy.com:8080", "5g.unicomjnwy.com:8080", "speedtest2.sd.huawei.com:8080", "221.199.9.35:8080", "speedtest1.sd.huawei.com:8080", "lanzhouunicom.com:8080", "speedtest1.gs.chinamobile.com:8080", "speedtest.bajianjun.com:8080", "speedtest3.sd.huawei.com:8080", "4glianyungang1.speedtest.jsinfo.net:8080", "xatest.wo-xa.com:8080", "speedtest.one-punch.win:8080", "4gsuzhou1.speedtest.jsinfo.net:8080", "speedtest.zjmobile.com:8080", "speedtest02.js165.com:8080", "5gnanjing.speedtest.jsinfo.net:8080", "speedtest2.ah.chinamobile.com:8080", "112.122.10.26:8080", "cmnet-speed.com:8080", "speedtest.shunicomtest.com:8080", "speedtest1.online.sh.cn:8080", "5g.shunicomtest.com:8080", "speedtest4.sh.chinamobile.com:8080", "speedtest1.wangjia.net:8080", "speedtest2.sc.chinamobile.com:8080", "vipspeedtest8.wuhan.net.cn:8080", "113.57.249.2:8080", "vipspeedtest1.wuhan.net.cn:8080", "speedtest1.sc.189.cn:8080", "ltetest1.139site.com:8080", "122.229.136.10:8080", "ltetest3.139site.com:8080", "speedtest1.xz.chinamobile.com:8080", "speedtest1.cqccn.com:8080", "speedtest.nc.jx.chinamobile.com:8080", "speedtest01.hn165.com:8080", "mygod998.vicp.net:8080", "speedtest2.hn.chinamobile.com:8080", "speedtest02.hn165.com:8080", "speedtest1.gz.chinamobile.com:8080", "upload1.testspeed.kaopuyun.com:8080", "speedtest1.fj.chinamobile.com:8080", "speedtest1.yncuc.net:8080", "speedtest1.gzuni.com:8080", "speedtest1.gd.chinamobile.com:8080", "www.gdspeedtest.com:8080", "speedtest1.gx.chinamobile.com:8080", "121.31.15.130:8080", "speed4.gxmenjin.com:8080", "speedtest.75510010.com:8080", "speedtest3.gd.chinamobile.com:8080", "www.suntechspeedtest.com:8080", "shenron.smartone.com:8080", "speedtest.website-solution.net:8080", "speedtest1.hi.chinamobile.com:8080",};

        for (int i = 0; i < 74; i++) {
            uploadAddress = uploadAddressArray[i];
            lat = latArray[i];
            lon = lonArray[i];
            name = nameArray[i];
            country = countryArray[i];
            cc = ccArray[i];
            sponsor = sponsorArray[i];
            host = hostArray[i];
            List<String> ls = Arrays.asList(lat, lon, name, country, cc, sponsor, host);
            mapKey.put(i, uploadAddress);
            mapValue.put(i, ls);
        }


        finished = true;
        }


}
