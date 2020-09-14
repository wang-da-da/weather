package com.wdd.weather.gson;

/*  数组
* "daily_forecast":[
*   {
*       "date":"2020-09-11"
*       "cond":{
*           "txt_d":"阵雨"
*       },
*       "tmp":{
*           "max":"34"
*           "min":"27"
*       }
*   },
*   {
*       "date":"2020-09-11"
*       "cond":{
*           "txt_d":"阵雨"
*       },
*       "tmp":{
*           "max":"34"
*           "min":"27"
*       }
*   },
*   ...
* ]
* */

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
