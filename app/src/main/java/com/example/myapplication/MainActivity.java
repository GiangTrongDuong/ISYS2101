package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewNews = findViewById(R.id.btnNews);
        Button viewWeather = findViewById(R.id.btnWeather);
        Button viewMap = findViewById(R.id.buttonMap);

        viewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNews();
            }
        });

        viewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openWeather();}
        });

//        viewMap.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {openMap();}
//        });
    }

    public void openNews(){
        Intent intentOpenNews = new Intent(this, NewsPages.class);
        startActivity(intentOpenNews);
    }

    public void openWeather(){
        Intent intentOpenWeather = new Intent(this, weatherForecast.class);
        startActivity(intentOpenWeather);
    }

//    public void openMap(){
//        Intent intentOpenMap = new Intent(this, MapsActivity.class);
//        startActivity(intentOpenMap);
//    }
}