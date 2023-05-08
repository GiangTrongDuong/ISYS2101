package com.example.tripme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class TripCreationActivity extends AppCompatActivity {
    EditText editTextTripName, editTextTripLocation, editTextTripInformation;
    Button buttonCreate;
    String tripID = "", tripName = "", tripLocation = "", tripInformation = "";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creation);
        Random random = new Random();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("user");

        editTextTripName = findViewById(R.id.editTextTripName);
        editTextTripLocation = findViewById(R.id.editTextTripLocation);
        editTextTripInformation = findViewById(R.id.editTextTripInformation);
        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripID = String.format("%04d", random.nextInt(10000));
                tripName = editTextTripName.getText().toString();
                tripLocation = editTextTripLocation.getText().toString();
                tripInformation = editTextTripInformation.getText().toString();
                if(tripName.equals("") || tripLocation.equals("")) {
                    Toast.makeText(TripCreationActivity.this, "Invalid information.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    myRef.child(mAuth.getCurrentUser().getPhoneNumber()).child("tripID").setValue(tripID);
                    createTrip(tripID, tripName, tripLocation, tripInformation);
                }
            }
        });
    }

    public void createTrip(String id, String name, String location, String information){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("trip");
        myRef.child(id).child("name").setValue(name);
        myRef.child(id).child("location").setValue(location);
        myRef.child(id).child("information").setValue(information);
        myRef.child(id).child("managerPhone").setValue(mAuth.getCurrentUser().getPhoneNumber());
    }
}