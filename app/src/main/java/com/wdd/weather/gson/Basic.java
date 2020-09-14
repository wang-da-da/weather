package com.wdd.weather.gson;

import com.google.gson.annotations.SerializedName;

/*
* "basic": {
*   "city": "苏州"
*   "id": "CN101190401"
*   "update": {
*       "loc":"2020-09-11 16:16"
*   }
* }
* */

public class Basic {

    //JSON 中字段不适合直接作为 java字段 直接明明
//    通过SerializedName  让JSON字段和java字段 之间建立映射关系
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }
}
