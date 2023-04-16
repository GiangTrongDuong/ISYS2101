package com.example.nvaphone;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {
    private TextView instructions;
    private EditText name;
    private EditText email;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        instructions = findViewById(R.id.infoInstructions);
        name = findViewById(R.id.infoName);
        email = findViewById(R.id.infoEmail);
        createAccount = findViewById(R.id.infoCreateAccount);
        String phone = getIntent().getStringExtra("com.example.nvaphone.PHONE");

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    Toast.makeText(UserInfoActivity.this, "No information may be blank!"
                            , Toast.LENGTH_SHORT).show();
                }
                else{ //all info is here, get it to the db and go to profile
                    HashMap<String, Object> newUser = new HashMap<>();
                    newUser.put("name", name.getText().toString());
                    newUser.put("email", email.getText().toString());
                    //add user info to the 'user' table
                    FirebaseDatabase.getInstance().getReference().child("user")
                            .child(phone).updateChildren(newUser);
                    //add phone number to the 'taken_phones' table
                    FirebaseDatabase.getInstance().getReference().child("taken_phones")
                            .child(phone).setValue("true");
                    startActivity(new Intent(UserInfoActivity.this, ProfileActivity.class));
                }
            }
        });
    }
}