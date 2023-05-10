package com.example.nvaphone;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private int notiCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notiCount = 0;
        EditText etNoti = findViewById(R.id.etNoti);
        Button btnNoti = findViewById(R.id.btnNoti);

        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNoti.getText().toString().isEmpty()){
                    Toast.makeText(NotificationActivity.this,
                            "Content cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Notification n1 = new Notification(etNoti.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("trip").
                            child("xxx").child("notification").
                            child(Integer.toString(notiCount)).setValue(n1);
                    Toast.makeText(NotificationActivity.this, "Notification created!", Toast.LENGTH_SHORT).show();
                    notiCount--;
                }
            }
        });
    }
}