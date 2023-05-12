package com.example.tripme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("user");
    private DatabaseReference myRef2 = database.getReference("taken_phones");
    private User user;
    String email = "";
    String password = "";
    private EditText phone;
    private Button login;
    private TextView askOTP;
    private EditText otp;
    private TextView resendOTP;
    private Button submitOTP;
    private RelativeLayout linearInput, linearInput2;
    private LinearLayout linearOTP;
    private FirebaseAuth auth;
    private ProgressDialog pd; //use this when loading
    //if code send fails: use this to resend OTP
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerification; //OTP code
    private static final String TAG = "LOGIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        phone = findViewById(R.id.editTextPhone);
        login = findViewById(R.id.buttonLogin);
        askOTP = findViewById(R.id.liAskOTP);
        otp = findViewById(R.id.liOTP);
        submitOTP = findViewById(R.id.liSubmitOTP);
        resendOTP = findViewById(R.id.liResendOTP);
        linearInput = findViewById(R.id.liInput);
        linearInput2 = findViewById(R.id.liInput2);
        linearOTP = findViewById(R.id.liLinearOTP);

        //init progress dialog so that it runs whenever we need
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        //init: show the input (phone) layout, hide the otp layout
        linearInput.setVisibility(View.VISIBLE);
        linearInput2.setVisibility(View.VISIBLE);
        linearOTP.setVisibility(View.GONE);

        //Check if logged in
        if (auth.getCurrentUser() != null){
            updateUI(auth.getCurrentUser());
        }
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //invoked: 1. Instant verification
                // Auto-retrieval
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //invoked when an invalid request is made e.g. phone is invalid
                pd.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationID, forceResendingToken);
                //sms code is sent. we need to ask the user for the code and make some credentials
                //by combining the code with an id
                Log.d(TAG, "onCodeSent: "+ verificationID);

                mVerification = verificationID;
                forceResendingToken = token;
                pd.dismiss();

                linearInput.setVisibility(View.GONE);
                linearInput2.setVisibility(View.GONE);
                linearOTP.setVisibility(View.VISIBLE);
                askOTP.setText("Code sent to " + phone.getText().toString().trim());
            }
        };



        //user clicks log in -> check phone details
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_phone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(entered_phone)){
                    Toast.makeText(LoginActivity.this, "Phone cannot be blank!", Toast.LENGTH_SHORT).show();
                }
                else{
                    startVerifyPhoneNumber(entered_phone);
                }
            }
        });
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_phone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(entered_phone)){
                    Toast.makeText(LoginActivity.this, "Phone cannot be blank!", Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerificationCode(entered_phone, forceResendingToken);
                }
            }
        });
        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_otp = otp.getText().toString().trim();
                if (TextUtils.isEmpty(entered_otp)){
                    Toast.makeText(LoginActivity.this, "OTP cannot be blank!", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyOTP(mVerification, entered_otp);
                }
            }
        });
    }
    private void startVerifyPhoneNumber(String phone) {
        pd.setMessage("Verifying phone number ...");
        pd.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending verification code...");
        pd.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyOTP(String mVerification, String entered_otp) {
        pd.setMessage("Verifying code...");
        pd.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, entered_otp);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        pd.setMessage("Signing in...");
        pd.show();
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String current_phone = auth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(LoginActivity.this, "Logged in as " + current_phone, Toast.LENGTH_SHORT).show();

                        //check if there is a record of this phone number in taken_phones
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
//                                .child("taken_phones");
                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(current_phone)){
                                    //Phone exist -> go to other pages
                                    updateUI(auth.getCurrentUser());
                                }
                                else {
                                    startActivity(new Intent(LoginActivity.this, SignupActivity.class).putExtra("phone", current_phone));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to log in
                        pd.dismiss();
                        //Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(""+e.getMessage());
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser) {
        Intent intentRoleSelection = new Intent(this, RoleSelectionActivity.class);
        Intent intentManager = new Intent(this, TripCreationActivity.class);
        Intent intentManagerMain = new Intent(this, MainActivity.class);
        Intent intentParticipantNotInTrip = new Intent(this, TripJoiningActivity.class);
        Intent intentParticipantInTrip = new Intent(this, MainActivity.class);
        myRef.child(currentUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = new User(snapshot.child("name").getValue().toString(),
                        snapshot.child("email").getValue().toString(),
                        snapshot.child("role").getValue().toString(),
                        snapshot.child("tripID").getValue().toString(),
                        snapshot.child("phone").getValue().toString());
                if (user.getRole().equals("")){
                    intentRoleSelection.putExtra("phone", user.getPhone());
                    intentRoleSelection.putExtra("name", user.getName());
                    startActivity(intentRoleSelection);
                } else {
                    if (user.getRole().equals("Manager")) {
                        if (user.getTripID().equals("")){
                            intentManager.putExtra("phone", user.getPhone());
                            intentManager.putExtra("role", user.getRole());
                            startActivity(intentManager);
                        } else {
                            intentManagerMain.putExtra("tripID", user.getTripID());
                            intentManagerMain.putExtra("role", user.getRole());
                            startActivity(intentManagerMain);
                        }
                    } else { //Not manager and not empty = participant
                        if (user.getTripID().equals("")){ //not in any trip -> selection
                            intentParticipantNotInTrip.putExtra("tripID", user.getTripID());
                            intentParticipantNotInTrip.putExtra("phone", user.getPhone());
                            intentParticipantNotInTrip.putExtra("name", user.getName());
                            intentParticipantNotInTrip.putExtra("role", user.getRole());
                            startActivity(intentParticipantNotInTrip);
                        }
                        else{
                            intentParticipantInTrip.putExtra("tripID", user.getTripID());
                            intentParticipantInTrip.putExtra("phone", user.getPhone());
                            intentParticipantInTrip.putExtra("name", user.getName());
                            intentParticipantInTrip.putExtra("role", user.getRole());
                            startActivity(intentParticipantInTrip);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}