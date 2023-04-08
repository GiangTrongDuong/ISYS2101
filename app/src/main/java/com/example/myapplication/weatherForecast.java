package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.text.DecimalFormat;

public class weatherForecast extends AppCompatActivity {

    private EditText cityName;
    private TextView resultWeather;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String apiKey = "eb1a770f8e3624d1aebc453838659acd";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        cityName = findViewById(R.id.editTxtCityName);
        resultWeather = findViewById(R.id.resultWeather);
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
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}