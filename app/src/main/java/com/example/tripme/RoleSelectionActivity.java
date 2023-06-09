package com.example.tripme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleSelectionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RelativeLayout buttonManager, buttonParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("user");
        Intent i = new Intent(RoleSelectionActivity.this, TripCreationActivity.class);
        Intent intent = new Intent(RoleSelectionActivity.this, TripJoiningActivity.class);
        buttonManager = findViewById(R.id.buttonManager);
        buttonParticipant = findViewById(R.id.buttonParticipant);
        String phone = mAuth.getCurrentUser().getPhoneNumber();
        buttonManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(phone).child("role").setValue("Manager");
                i.putExtra("phone", mAuth.getCurrentUser().getPhoneNumber());
                i.putExtra("role", "Mananger");
                startActivity(i);
            }
        });
        buttonParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(phone).child("role").setValue("Participant");
                intent.putExtra("phone", phone);
                intent.putExtra("name", getIntent().getExtras().getString("name"));
                intent.putExtra("role", "Participant");
                startActivity(intent);
            }
        });
    }
}