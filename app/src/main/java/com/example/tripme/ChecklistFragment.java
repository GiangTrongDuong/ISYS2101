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

import java.awt.font.TextAttribute;

public class ChecklistFragment extends Fragment {
    private FragmentChecklistBinding binding;
    ImageButton buttonLogout;
    TextView textViewTripID;

    ListView checklist;

    public ChecklistFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_list, container, false);
        binding = FragmentChecklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = getActivity().getIntent();
        textViewTripID = (TextView) view.findViewById(R.id.textViewTripCode);
        textViewTripID.setText(intent.getExtras().getString("tripID"));
        // Set the adapter
        checklist = root.findViewById(R.id.checklist);



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