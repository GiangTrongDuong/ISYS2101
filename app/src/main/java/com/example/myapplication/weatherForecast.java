package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;

public class weatherForecast extends AppCompatActivity {

    EditText cityName;
    TextView resultWeather;
    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String urlSm = "http://api.openweathermap.org/data/2.5/weather";
    private final String apiKey = "eb1a770f8e3624d1aebc453838659acd";
    DecimalFormat df = new DecimalFormat("#.##");
    String[] city = {"Bac Lieu","Ben Thuy","Ben Tre","Bien Hoa","Buon Me Thuot","Cam Ranh","Can Tho","Cao Lanh","Cho Lon","Con Son","Da Lat","Da Nang","Ha Long","Hai Duong","Haiphong","Hanoi","Ho Chi Minh City","Hoa Binh","Hue","Kon Tum","Lao Cai","Long Xuyen","My Tho","Nam Dinh","Nha Trang","Phan Thiet","Pleiku","Quang Ngai","Qui Nhon","Rach Gia","Sa Dec","Tay Ninh","Thai Binh","Thai Nguyen","Thanh Hoa","Thu Dau Mot","Tuy Hoa","Vinh","Vinh Long","Vung Tau"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    String citySelected;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        result = findViewById(R.id.resultWeather);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<>(this,R.layout.list_city,city);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                citySelected = parent.getItemAtPosition(position).toString();
                getWeatherDetails();
            }
        });

    }

    public void getWeatherToday(View view){
        String tempUrl = "";
        tempUrl = urlSm + "?q=" + citySelected + "&appid=" + apiKey;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        //Loop through list
                        JSONArray jsonArrayList = jsonResponse.getJSONArray("list");
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                            Log.d("JSONReturn", description);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            Log.d("JSONReturn", String.valueOf(temp));
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectCloud.getString("all");
                            Log.d("JSONReturn", clouds);
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String countryCity = jsonResponse.getString("name");

                        output += countryCity + " " + countryName +
                        "\n" + df.format(temp) + " " + df.format(feelsLike);
                        result.setText(output);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
    }
public void getWeatherDetails(){
    String tempUrl = "";
    tempUrl = url + "?q=" + citySelected + "&appid=" + apiKey;
    StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //Loop through list
                    JSONArray jsonArrayList = jsonResponse.getJSONArray("list");

                    String countryCity;
                    String countryName;
                    int timeDay0;
                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        JSONObject JSONObjectCity = jsonResponse.getJSONObject("city");
                        countryCity = JSONObjectCity.getString("name");
                        countryName = JSONObjectCity.getString("country");
                        JSONObject day0 = jsonArrayList.getJSONObject(i);
                        timeDay0 = day0.getInt("dt");
                        String time = String.valueOf(new java.util.Date((long)timeDay0*1000));
                        JSONObject main0 = day0.getJSONObject("main");
                        double temp0 = main0.getDouble("temp") - 273.15;
                        double tempfl0 = main0.getDouble("feels_like") - 273.15;
                        double tempMin0 = main0.getDouble("temp_min") - 273.15;
                        double tempMax0 = main0.getDouble("temp_max") - 273.15;
                        int humidity0 = main0.getInt("humidity");
                        JSONArray weatherlist0 = day0.getJSONArray("weather");
                        JSONObject fl0 = weatherlist0.getJSONObject(0);
                        int weatherid0 = fl0.getInt("id");
                        output += time + " " + df.format(temp0) + " " + df.format(tempMax0) + " " +
                                df.format(humidity0) + " " + df.format(tempMin0) + " " + df.format(tempfl0) + " " + weatherid0
                                + "\n";

                    }


                    result.setText(output);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
}
}