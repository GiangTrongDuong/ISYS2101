package com.example.tripme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tripme.databinding.FragmentChecklistBinding;
import com.example.tripme.MainActivity;
import com.example.tripme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class ChecklistFragment extends Fragment {
    private FragmentChecklistBinding binding;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("Trips");
    ImageButton buttonLogout;
    TextView textViewTripID;
    ListView checklist;
    ArrayList<Participant> participantList = new ArrayList<>();

    public ChecklistFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_list, container, false);
        binding = FragmentChecklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = getActivity().getIntent();
        String tripID = intent.getExtras().getString("tripID");
        textViewTripID = (TextView) view.findViewById(R.id.textViewTripCode);
        textViewTripID.setText(tripID);
        // Set the adapter
        checklist = view.findViewById(R.id.checklist);
        ChecklistAdapter listAdapter = new ChecklistAdapter(inflater.getContext(), R.layout.fragment_checklist, participantList);
        checklist.setAdapter(listAdapter);
        myRef.child(tripID).child("participant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()){
                    participantList.add(new Participant(dsp.child("participantName").getValue().toString(),
                            dsp.child("participantName").getValue().toString(),
                            dsp.child("participantName").getValue().toString()));
                    checklist.invalidateViews();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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