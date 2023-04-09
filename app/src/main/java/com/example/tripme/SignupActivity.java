package com.example.tripme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private User user;
    String email = "";
    String password = "";
    String phone = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Users");

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        EditText editTextName = findViewById(R.id.editTextName);
        Button buttonSignup = findViewById(R.id.buttonSignup);
        ImageButton buttonCancel = findViewById(R.id.button_cancel);

        Intent intent = getIntent();
        editTextEmail.setText(intent.getExtras().getString("email"));
        editTextPassword.setText(intent.getExtras().getString("password"));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intentMain = new Intent();
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                phone = editTextPhone.getText().toString();
                name = editTextName.getText().toString();
                if(email.equals("") || password.equals("") || phone.equals("") || name.equals("")) {
                    Toast.makeText(SignupActivity.this, "Invalid information.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    user = new User(name, email, "", "", phone);
                    signUp(email, password);
                }
            }
        });
    }
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(SignupActivity.this, "Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser) {
        String uid = mAuth.getCurrentUser().getUid();
        myRef.child(uid).setValue(user); //adding user info to database
        Intent intent = new Intent(this, RoleSelectionActivity.class);
        startActivity(intent);
    }
}