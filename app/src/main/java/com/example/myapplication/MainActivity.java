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
        viewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNews();
            }
        });
    }

    public void openNews(){
        Intent intent = new Intent(this, NewsPages.class);
        startActivity(intent);
    }
}