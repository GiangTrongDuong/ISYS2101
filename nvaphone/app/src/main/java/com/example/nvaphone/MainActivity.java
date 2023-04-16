package com.example.nvaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button signup;
    private Button login;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.mainSignup);
        login = findViewById(R.id.mainLogin);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {// User is signed in
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        } else {
            // No user logged in yet -> to the Login/Sign up activity! Same thing anyway
            startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                finish();
            }
        });
    }
}