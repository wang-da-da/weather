package com.wdd.weather.util;

import android.text.TextUtils;

import com.wdd.weather.db.City;
import com.wdd.weather.db.County;
import com.wdd.weather.db.Province;

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
}
