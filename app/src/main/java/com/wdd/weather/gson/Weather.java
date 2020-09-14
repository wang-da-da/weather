package com.wdd.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
* 总的实体类，来引用各个实体类
*
* "Weather":[
*   {
*       "status":"ok",
*       "basic":{},
*       "aqi":{},
*       "now":{},
*       "suggestion":{},
*       "daily_forecast": []
*   }
* ]
* */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
