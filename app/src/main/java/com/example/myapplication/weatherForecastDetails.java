package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.DecimalFormat;

public class weatherForecastDetails extends AppCompatActivity {

    EditText cityName;
    TextView resultWeather;
    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String apiKey = "eb1a770f8e3624d1aebc453838659acd";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

    }

    public void getWeatherDetails(View view){
        String tempUrl = "";
        String city = cityName.getText().toString().trim();
        if(city.equals("")){
            resultWeather.setText("Field Empty");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + apiKey;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        //Loop through list
                        JSONArray jsonArrayList = jsonResponse.getJSONArray("list");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject JSONObjectCity = jsonResponse.getJSONObject("city");
                            String countryCity = JSONObjectCity.getString("name");
                            String countryName = JSONObjectCity.getString("country");
                            JSONObject day0 = jsonArrayList.getJSONObject(0);
                            int timeDay0 = day0.getInt("dt");
                            JSONObject main0 = day0.getJSONObject("main");
                            double temp0 = main0.getDouble("temp") - 273.15;
                            double tempfl0 = main0.getDouble("feels_like") - 273.15;
                            double tempMin0 = main0.getDouble("temp_min") - 273.15;
                            double tempMax0 = main0.getDouble("temp_max") - 273.15;
                            int humidity0 = main0.getInt("humidity");
                            JSONArray weatherlist0 = day0.getJSONArray("weather");
                            JSONObject fl0 = weatherlist0.getJSONObject(0);
                            int weatherid0 = fl0.getInt("id");
                            output += timeDay0 + " " + df.format(temp0) + " " + df.format(tempMax0) + " " +
                                    df.format(humidity0) + " " + df.format(tempMin0) + " " + df.format(tempfl0) + " " + weatherid0
                            + "\n";
                        }


//                        resultWeather.setText(output);
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
}
