package com.wdd.weather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wdd.weather.db.City;
import com.wdd.weather.db.County;
import com.wdd.weather.db.Province;
import com.wdd.weather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /*
    * 解析和处理服务器返回的省级数据
    * */
    public static boolean handleProvinceResponse(String response) {

        if (!TextUtils.isEmpty(response)) {
            try {
                //解析数据
                JSONArray allProvinces = new JSONArray(response);

                for (int i = 0; i < allProvinces.length(); i++) {
                    //解析数据
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //提交至数据库
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     * 解析和处理服务器返回的市级数据
     * */
    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                //解析数据
                JSONArray allCities = new JSONArray(response);

                for (int i = 0; i < allCities.length(); i++) {
                    //解析数据
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     * 解析和处理服务器返回的县级数据
     * */
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                //解析数据
                JSONArray allCounties = new JSONArray(response);

                for (int i = 0; i < allCounties.length(); i++) {
                    //解析数据
                    JSONObject countiesObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countiesObject.getString("name"));
                    county.setWeatherId(countiesObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 将返回的JSON数据解析成Weather实体类
    * 解析出的数据形式
    * {
 *       "status":"ok",
 *       "basic":{},
 *       "aqi":{},
 *       "now":{},
 *       "suggestion":{},
 *       "daily_forecast": []
 *   }
    * */
    public static Weather handleWeatherResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            //由于定义过响应的GON实体类，只需要调用fromJson方法就能直接将 JSON 转化 为 Weather对象
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
