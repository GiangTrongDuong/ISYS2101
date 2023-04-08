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
    }

    public void openNews(){
        Intent intentOpenNews = new Intent(this, NewsPages.class);
        startActivity(intentOpenNews);
    }

    public void openWeather(){
        Intent intentOpenWeather = new Intent(this, weatherForecast.class);
        startActivity(intentOpenWeather);
    }
}