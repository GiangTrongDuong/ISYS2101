package com.example.nvaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {// User is signed in
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
        HashMap<String, Object> userino =new HashMap<>();
        userino.put("name", "viet anh");
        userino.put("email", "sfsdf@gmail.com");
        FirebaseDatabase.getInstance().getReference().child("user").child(phonerino).updateChildren(userino);
        //test taken_phones
        FirebaseDatabase.getInstance().getReference().child("taken_phones").child(phonerino).setValue("true");
    }
}