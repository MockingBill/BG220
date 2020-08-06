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
                finished = false;
            }





        } catch (Exception ex) {
            ex.printStackTrace();
            return ;
        }
        }

}
