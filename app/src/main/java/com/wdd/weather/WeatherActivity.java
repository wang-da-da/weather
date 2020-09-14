package com.wdd.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdd.weather.gson.Forecast;
import com.wdd.weather.gson.Weather;
import com.wdd.weather.util.HttpUtil;
import com.wdd.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        //在本地缓存中读取天气数据
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            //从intent中获取 天气id
            String weatherId = getIntent().getStringExtra("weather_id");
            //从服务器请求天气数据之前 将ScrollView隐藏，避免界面空数据
            weatherLayout.setVisibility(View.INVISIBLE);
            //从服务器请求天气数据
            requestWeather(weatherId);
        }
    }

    /*
    * 根据天气id 请求城市天气信息
    * */
    @SuppressLint("LongLogTag")
    public void requestWeather(final String weatherId) {
        Log.d("the weatherid is ==============================", weatherId);

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";

//        向WeatherURl地址发送请求，  服务器会将相应城市的天气信息以JSON格式返回
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                将JSON对象转换成Weather对象
                final String responseText = response.body().string();
                Log.d("responseText=============================", responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d("weather =======================", String.valueOf(weather));
                Log.d("weather status=======================", String.valueOf(weather.status));
//              将当前线程切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                      根据服务器返回的状态进行判断请求天气是否成功
                        if (weather != null && "ok".equals(weather.status)) {
//                          将返回的数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
//                          显示内容
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    /*
    * 处理并展示Weather实体类中的数据
    * */

//    从Weather中获取数据  显示到相应控件上
    @SuppressLint("LongLogTag")
    private void showWeatherInfo (Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;

        Log.d("cityName=================", cityName);
        Log.d("updateTime=================", updateTime);
        Log.d("degree=================", degree);
        Log.d("weatherInfo=================", weatherInfo);

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
//      未来几天的天气预报
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            Log.d("dateText=================", String.valueOf(dateText));
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            Log.d("aqiText info=================", String.valueOf(aqiText));
            Log.d("pm25Text info=================", String.valueOf(pm25Text));
        }

        String comfort = "舒适度： " + weather.suggestion.comfort.info;
        String cardWash = "洗车指数： " + weather.suggestion.carWash.info;
        String sport = "运动建议： " + weather.suggestion.sport.info;
        Log.d("comfort info=================", comfort);
        Log.d("cardWash info=================", cardWash);
        Log.d("sport info=================", sport);


        comfortText.setText(comfort);
        carWashText.setText(cardWash);
        sportText.setText(sport);
//      将ScrollView设置为可见
        weatherLayout.setVisibility(View.VISIBLE);
    }
}