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
        DatabaseReference myRef = database.getReference("Users");
        Intent i = new Intent(RoleSelectionActivity.this, TripCreationActivity.class);
        buttonManager = findViewById(R.id.buttonManager);
        buttonManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(mAuth.getCurrentUser().getUid()).child("role").setValue("Manager");
                i.putExtra("phone", getIntent().getExtras().getString("phone"));
                startActivity(i);
            }
        });
    }
}