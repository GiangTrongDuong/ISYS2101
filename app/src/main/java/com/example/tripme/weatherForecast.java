package com.example.tripme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class weatherForecast extends AppCompatActivity {

    EditText cityName;
    TextView resultWeather;
    private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private final String urlSm = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiKey = "eb1a770f8e3624d1aebc453838659acd";
    DecimalFormat df = new DecimalFormat("#.##");
    DecimalFormat ds = new DecimalFormat("#");
    String[] city = {"Bac Lieu","Ben Tre","Bien Hoa","Cam Ranh","Can Tho",
            "Cao Lanh","Con Son","Da Lat","Da Nang","Ha Long","Hai Duong","Haiphong","Hanoi",
            "Ho Chi Minh City","Hoa Binh","Hue","Kon Tum","Lao Cai","Long Xuyen","My Tho","Nam Dinh","Nha Trang",
            "Phan Thiet","Pleiku","Quang Ngai","Qui Nhon","Rach Gia","Sa Dec","Tay Ninh","Thai Binh","Thai Nguyen",
            "Thanh Hoa","Thu Dau Mot","Tuy Hoa","Vinh","Vinh Long","Vung Tau"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    String citySelected;
    TextView result;
    TextView location, temperature, weather, feels_like, min_temp, max_temp, advice, box1, box2, box3;
    GridView gridList;
    HorizontalScrollView gridlistLayout;
    ArrayList<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        gridList = (GridView) findViewById(R.id.gridView);
        GridElementAdapter gridAdapter = new GridElementAdapter(this, R.layout.grid_element, itemList);
        location = findViewById(R.id.location);
        temperature = findViewById(R.id.temperature);
        weather = findViewById(R.id.weather);
        feels_like = findViewById(R.id.feelsLike);
        min_temp = findViewById(R.id.minTemp);
        max_temp = findViewById(R.id.maxTemp);
        advice = findViewById(R.id.advice);
        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        gridList = (GridView) findViewById(R.id.gridView);
        gridlistLayout = findViewById(R.id.gridlistLayout);
        //Hide everything
        showAndHideGUI(false);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(this,R.layout.list_city,city);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                citySelected = parent.getItemAtPosition(position).toString();
                getWeatherDetails();
                getWeatherToday();
                //Show everything
                showAndHideGUI(true);
            }
        });
        gridList.setAdapter(gridAdapter);
    }

    public void getWeatherToday(){
        String tempUrl = "";
        tempUrl = urlSm + "?q=" + citySelected + "&appid=" + apiKey;
            StringRequest stringRequest;
                    stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    String adv = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        //Loop through list
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        int id = jsonObjectWeather.getInt("id");
                        String description = jsonObjectWeather.getString("description");
                        String mainDesc = jsonObjectWeather.getString("main");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp_min = jsonObjectMain.getDouble("temp_min") - 273.15;
                        double temp_max = jsonObjectMain.getDouble("temp_max") - 273.15;
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectCloud.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String countryCity = jsonResponse.getString("name");

                        switch (mainDesc) {
                            case "Thunderstorm":
                                adv = "When thunder roars, go indoors!!!";
                                break;
                            case "Drizzle":
                                adv = "Wear protective clothes and bring umbrella";
                                break;
                            case "Rain":
                                adv = "Raincoat and/or umbrella, keep sock dry!";
                                break;
                            case "Snow":
                                adv = "Wear appropriate outdoor clothing against cold!";
                                break;
                            case "Clear":
                                adv = "Perfect sky for a travel day! Enjoy";
                                break;
                            case "Clouds":
                                adv = "Cloudy for a travel day, enjoy!";
                                break;
                        }
                        advice.setText(adv);
                        location.setText(countryCity + ", " +countryName);
                        temperature.setText(String.valueOf(ds.format(temp)) + " °C");
                        weather.setText(description);
                        feels_like.setText("Feels like " + String.valueOf(ds.format(feelsLike)) + " °C");
                        min_temp.setText("Min: " + String.valueOf(ds.format(temp_min)) + " °C");
                        max_temp.setText("Max " + String.valueOf(ds.format(temp_max)));
                        box1.setText("Wind" + "\nSpeed" + "\n" + wind + "m/s");
                        box2.setText("Humidity" + "\n" + humidity + "%");
                        box3.setText("Clouds" + "\n" + clouds + "%");


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
        itemList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //Loop through list
                    JSONArray jsonArrayList = jsonResponse.getJSONArray("list");
                    int timeDay0;
                    for (int i = 0;i < 30;
                         //Due to length~! cannot exceed 30 value
//                            jsonArrayList.length();
                         i++) {
                        JSONObject JSONObjectCity = jsonResponse.getJSONObject("city");
                        JSONObject day0 = jsonArrayList.getJSONObject(i);
                        timeDay0 = day0.getInt("dt");
                        String timeString = String.valueOf(new java.util.Date((long)timeDay0*1000));
                        String[] timeStringArray = null;
                        timeStringArray = timeString.split(" ");
                        String timeFinal = timeStringArray[2] + " " + timeStringArray[1] + "\n" +timeStringArray[3];
                        JSONObject main0 = day0.getJSONObject("main");
                        double temp0 = main0.getDouble("temp") - 273.15;
//                        int humidity0 = main0.getInt("humidity");
//                        JSONArray weatherlist0 = day0.getJSONArray("weather");
//                        JSONObject fl0 = weatherlist0.getJSONObject(0);
//                        int weatherid0 = fl0.getInt("id");
                        String tempStr = ds.format(temp0) + "°C";
                        itemList.add(new Item(tempStr,timeFinal));

                    }
                    gridList.invalidateViews();
                    gridList.refreshDrawableState();

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

    public void showAndHideGUI(boolean bool){
        if (bool){
            location.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            weather.setVisibility(View.VISIBLE);
            feels_like.setVisibility(View.VISIBLE);
            min_temp.setVisibility(View.VISIBLE);
            max_temp.setVisibility(View.VISIBLE);
            advice.setVisibility(View.VISIBLE);
            box1.setVisibility(View.VISIBLE);
            box2.setVisibility(View.VISIBLE);
            box3.setVisibility(View.VISIBLE);
            gridList.setVisibility(View.VISIBLE);
            gridlistLayout.setVisibility(View.VISIBLE);
        } else {
            location.setVisibility(View.INVISIBLE);
            temperature.setVisibility(View.INVISIBLE);
            weather.setVisibility(View.INVISIBLE);
            feels_like.setVisibility(View.INVISIBLE);
            min_temp.setVisibility(View.INVISIBLE);
            max_temp.setVisibility(View.INVISIBLE);
            advice.setVisibility(View.INVISIBLE);
            box1.setVisibility(View.INVISIBLE);
            box2.setVisibility(View.INVISIBLE);
            box3.setVisibility(View.INVISIBLE);
            gridList.setVisibility(View.INVISIBLE);
            gridlistLayout.setVisibility(View.INVISIBLE);
        }
    }
}