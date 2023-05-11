package com.example.nvaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button signup;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.mainSignup);

        //createTestDB();
        FirebaseMessaging.getInstance().getToken();
        SingletonAppTime appTime = new SingletonAppTime();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {// User is signed in
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra("from", "main_login");
            startActivity(i);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        } else {
            // No user logged in yet -> to the Login/Sign up activity! Same thing anyway
            startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
        }



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                finish();
            }
        });
    }
    public void createTestDB(){
        String phonerino = "+8472718212";
        //test user
//        HashMap<String, Object> userino = new HashMap<>();
//        userino.put("name", "viet anh");
//        userino.put("email", "sfsdf@gmail.com");
//        FirebaseDatabase.getInstance().getReference().child("user").child(phonerino).updateChildren(userino);
//        //test taken_phones
//        FirebaseDatabase.getInstance().getReference().child("taken_phones").child(phonerino).setValue("true");
        //test notification
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        HashMap<String, Object> noti = new HashMap<>();
        noti.put("content", "testing stuff");
        noti.put("time", format.format(currentTime));
        FirebaseDatabase.getInstance().getReference().child("trip").child("xxx").
                child("notification").child("aa").updateChildren(noti);
        FirebaseDatabase.getInstance().getReference().child("trip").child("yyy").
                child("notification").child("aa").updateChildren(noti);

        //emulate a trip
        FirebaseDatabase.getInstance().getReference().child("trip").child("xxx").
                child("participant").child("+8456787654567").setValue("true");
        FirebaseDatabase.getInstance().getReference().child("trip").child("yyy").
                child("participant").child("+9090ssss11111").setValue("true");
    }

}