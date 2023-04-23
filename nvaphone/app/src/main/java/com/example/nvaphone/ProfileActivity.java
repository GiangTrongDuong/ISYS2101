package com.example.nvaphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
//show the logged-in phone number and an option to log out
    private TextView name;
    private TextView phone;
    private TextView hello1;
    private TextView hello2;
    private Button logoutbtn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.proName);
        phone = findViewById(R.id.proPhone);
        hello1 = findViewById(R.id.proHello1);
        hello2 = findViewById(R.id.proHello2);
        logoutbtn = findViewById(R.id.proLogOut);
        auth = FirebaseAuth.getInstance();

        checkUserStatus(); //set phone details to the textview, if applicable
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                checkUserStatus();
            }
        });
    }

    //get the current user
    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){ //user is logged in
            String userPhone = user.getPhoneNumber();
            phone.setText(userPhone);

            //get the user's name
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(userPhone);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userino = snapshot.getValue(User.class);
                    try {
                        name.setText(String.format(userino.getName() + ", email " + userino.getEmail()));
                    }
                    catch (NullPointerException e){
                        System.out.println(""+e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{ //not logged in
            finish();
        }
    }
}