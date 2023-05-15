package com.example.tripme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseReference myRef = database.getReference("trip");
    ImageButton buttonLogout;
    TextView textViewTripID, textViewTripName, textViewCount;
    ListView checklist, phonelist;
    ArrayList<Participant> participantList = new ArrayList<>();
    int count = 0;
    int presentCount = 0;

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
        textViewCount = (TextView) view.findViewById(R.id.textViewCount);
        //Set trip name
        textViewTripName = (TextView) view.findViewById(R.id.textViewTripName);
    if(intent.getExtras().getString("role").equals("Manager")) {
        myRef.child(tripID).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewTripName.setText(" " + snapshot.getValue().toString() + ":");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Set the adapter
        checklist = view.findViewById(R.id.checklist);
        ChecklistAdapter listAdapter = new ChecklistAdapter(inflater.getContext(), R.layout.fragment_checklist, participantList);
        checklist.setAdapter(listAdapter);
        phonelist = view.findViewById(R.id.checklistPhone);
        ChecklistPhoneAdapter listAdapter2 = new ChecklistPhoneAdapter(inflater.getContext(), R.layout.fragment_checklist_phone, participantList);
        phonelist.setAdapter(listAdapter2);
        myRef.child(tripID).child("participant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = 0;
                presentCount = 0;
                participantList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    count++;
                    participantList.add(new Participant(dsp.child("participantName").getValue().toString(),
                            dsp.child("participantRole").getValue().toString(),
                            dsp.child("participantPhone").getValue().toString()));
                    if (dsp.child("participantRole").getValue().equals("Present")) {
                        presentCount++;
                    }
                    textViewCount.setText("Present: " + presentCount + "/" + count);
                }
                checklist.invalidateViews();
                phonelist.invalidateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Set item click listener
        phonelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + participantList.get(i).getParticipantPhone()));
                startActivity(intent);
            }
        });
        checklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (participantList.get(i).getParticipantRole().equals("Absent")) {
                    myRef.child(tripID).child("participant").child(participantList.get(i).getParticipantPhone()).child("participantRole").setValue("Present");
                } else {
                    myRef.child(tripID).child("participant").child(participantList.get(i).getParticipantPhone()).child("participantRole").setValue("Absent");
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
    }
        return view;
    }
}