package com.example.tripme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("user");
    private DatabaseReference myRef2 = database.getReference("taken_phones");

    private EditText name;
    private EditText email;
    private Button createAccount;
    ImageButton buttonCancel;
    FirebaseAuth auth;
    private ProgressDialog pd;
    private ValueEventListener mUserReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        createAccount = findViewById(R.id.buttonSignup);
        buttonCancel = findViewById(R.id.button_cancel);
        String phone = getIntent().getStringExtra("phone");
        auth = FirebaseAuth.getInstance();
        //init progress dialog so that it runs whenever we need
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Creating account...");
                pd.show();
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    Toast.makeText(SignupActivity.this, "No information may be blank!"
                            , Toast.LENGTH_SHORT).show();
                }
                else{ //all info is here, get it to the db and go to profile
                    HashMap<String, Object> newUser = new HashMap<>();
                    newUser.put("name", name.getText().toString());
                    newUser.put("email", email.getText().toString());
                    newUser.put("phone", phone);
                    newUser.put("role", "");
                    newUser.put("tripID", "");
                    //add user info to the 'user' table
                    myRef
                            .child(phone).updateChildren(newUser);
                    //add phone number to the 'taken_phones' table
                    myRef2
                            .child(phone).setValue("true");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //new data may be inserted by other users
                            //we must check if OUR data has been inserted and can be searched
                            if(snapshot.hasChild(phone)){
                                pd.dismiss();
                                Intent i = new Intent(SignupActivity.this, RoleSelectionActivity.class);
                                i.putExtra("name", name.getText().toString());
                                startActivity(i);
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
        myRef.removeEventListener(mUserReferenceListener);
    }
}