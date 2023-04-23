package com.example.nvaphone;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {
    private TextView instructions;
    private EditText name;
    private EditText email;
    private Button createAccount;
    FirebaseAuth auth;
    private ProgressDialog pd; //use this when loading
    private DatabaseReference mUserReference;
    private ValueEventListener mUserReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        instructions = findViewById(R.id.infoInstructions);
        name = findViewById(R.id.infoName);
        email = findViewById(R.id.infoEmail);
        createAccount = findViewById(R.id.infoCreateAccount);
        String phone = getIntent().getStringExtra("com.example.nvaphone.PHONE");
        auth = FirebaseAuth.getInstance();
        //init progress dialog so that it runs whenever we need
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Creating account...");
                pd.show();
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

                    //listen for the database, if a new entry has been added with the current phone
                    //then we move to the profile page
                    mUserReference = FirebaseDatabase.getInstance().getReference()
                            .child("user");
                    mUserReferenceListener = mUserReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //new data may be inserted by other users
                            //we must check if OUR data has been inserted and can be searched
                            if(snapshot.hasChild(phone)){
                                pd.dismiss();
                                startActivity(new Intent(UserInfoActivity.this, ProfileActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        pd.dismiss();
        super.onDestroy();
        mUserReference.removeEventListener(mUserReferenceListener);
    }
}