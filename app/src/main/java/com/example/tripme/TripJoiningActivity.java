package com.example.tripme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripJoiningActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRefTrip = database.getReference("trip");
    DatabaseReference myRefUser = database.getReference("user");
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    Button buttonJoin;
    String tripID, uid, user_name;
    Boolean valid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_joining);
        mAuth = FirebaseAuth.getInstance();

        otp_textbox_one = findViewById(R.id.otp_edit_box1);
        otp_textbox_two = findViewById(R.id.otp_edit_box2);
        otp_textbox_three = findViewById(R.id.otp_edit_box3);
        otp_textbox_four = findViewById(R.id.otp_edit_box4);
        buttonJoin = findViewById(R.id.buttonJoin);
        uid = getIntent().getExtras().getString("phone");
        user_name = getIntent().getExtras().getString("name");

        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four};

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripID =
                    otp_textbox_one.getText().toString() +
                    otp_textbox_two.getText().toString() +
                    otp_textbox_three.getText().toString() +
                    otp_textbox_four.getText().toString();
                //uid = mAuth.getCurrentUser().getPhoneNumber();
                validateTrip(tripID);

            }
        });
    }
    public void validateTrip(String tripID){
        DatabaseReference ref = database.getReference("trip");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text = "";
                for (DataSnapshot dsp : snapshot.getChildren()){
                    if (dsp.getKey().equals(tripID)){
                        valid = true;
                    }
                }
                updateUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateUI() {
        if (valid){
            myRefUser.child(uid).child("tripID").setValue(tripID);
            myRefTrip.child(tripID).child("participant").child(uid).setValue(new Participant(
                    user_name,
                    "Absent",
                    uid));
            //Go to participant main page
            Intent toMain = new Intent(TripJoiningActivity.this, MainActivity.class);
            toMain.putExtra("role", "Participant");
            toMain.putExtra("tripID", tripID);
            startActivity(toMain);
        } else {
            Toast.makeText(TripJoiningActivity.this, "Invalid Code.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
