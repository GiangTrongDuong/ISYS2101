package com.example.tripme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tripme.databinding.FragmentTripToolsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripToolsFragment extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("trip");
    private DatabaseReference myRef2 = database.getReference("user");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button  buttonNews, buttonWeather, buttonMap, buttonEnd;
    ImageButton buttonLogout;
    private FragmentTripToolsBinding binding;
    public TripToolsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_tools, container, false);
        binding = FragmentTripToolsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = getActivity().getIntent();
        String tripID = intent.getExtras().getString("tripID");
        String role = intent.getExtras().getString("role");
        buttonNews = view.findViewById(R.id.box1);
        buttonNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the page
                Intent intent = new Intent(getActivity(), NewsPages.class);
                startActivity(intent);
            }
        });
        buttonWeather = view.findViewById(R.id.box2);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the page
                Intent intent = new Intent(getActivity(), weatherForecast.class);
                startActivity(intent);
            }
        });
        buttonMap = view.findViewById(R.id.box3);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the page
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        buttonEnd = view.findViewById(R.id.box4);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mAuth.getCurrentUser().getPhoneNumber();
                if (role.equals("Participant")){
                    myRef.child(tripID).child("participant").child(phone).removeValue();
                    myRef2.child(phone).child("tripID").setValue("");
                    myRef2.child(phone).child("role").setValue("");
                    Toast.makeText(getContext(), "Trip Quit Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), RoleSelectionActivity.class));
                    getActivity().finish();
                } else {
                    myRef.child(tripID).child("participant").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dsp: snapshot.getChildren()){
                                Toast.makeText(getContext(), dsp.getKey(), Toast.LENGTH_SHORT).show();
                                myRef2.child(dsp.getKey()).child("tripID").setValue("");
                                myRef2.child(dsp.getKey()).child("role").setValue("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                        }
                    });
                    myRef.child(tripID).removeValue();
                    myRef2.child(phone).child("tripID").setValue("");
                    myRef2.child(phone).child("role").setValue("");
                    Toast.makeText(getContext(), "Trip Ended Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), RoleSelectionActivity.class));
                    getActivity().finish();
                }
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