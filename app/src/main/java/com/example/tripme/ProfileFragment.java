package com.example.tripme;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripme.R;
import com.example.tripme.databinding.FragmentProfileBinding;
import com.example.tripme.databinding.FragmentTripToolsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    TextView name, phone, email, role, tripID;
    EditText editName, editPhone, editEmail;
    ImageView editNameBtn, editPhoneBtn, editEmailBtn;
    ImageButton buttonLogout;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("user");
    private FirebaseAuth mAuth;
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        name = view.findViewById(R.id.textViewName);
        phone = view.findViewById(R.id.textViewPhone);
        email = view.findViewById(R.id.textViewEmail);
        role = view.findViewById(R.id.textViewRole);
        tripID = view.findViewById(R.id.textViewTripID);
        editName = view.findViewById(R.id.editTextName);
        editPhone = view.findViewById(R.id.editTextName);
        editEmail = view.findViewById(R.id.editTextName);
        editNameBtn = view.findViewById(R.id.btnName);
        editPhoneBtn = view.findViewById(R.id.btnPhone);
        editEmailBtn = view.findViewById(R.id.btnEmail);

        mAuth = FirebaseAuth.getInstance();
        myRef.child(mAuth.getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                phone.setText("Phone: " + snapshot.child("phone").getValue().toString());
                email.setText("Email: " + snapshot.child("email").getValue().toString());
                role.setText("Role: " + snapshot.child("role").getValue().toString());
                tripID.setText("TripID: " + snapshot.child("tripID").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(mAuth.getCurrentUser().getPhoneNumber()).child("name").setValue(editName.getText().toString());
            }
        });
        editPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(mAuth.getCurrentUser().getPhoneNumber()).child("name").setValue(editName.getText().toString());
            }
        });
        editEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(mAuth.getCurrentUser().getPhoneNumber()).child("name").setValue(editName.getText().toString());
            }
        });

        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });
        return view;
    }
}