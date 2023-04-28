package com.example.tripme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.tripme.databinding.FragmentChecklistBinding;
import com.example.tripme.databinding.FragmentTripToolsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class TripToolsFragment extends Fragment {
    Button buttonLogout, buttonNews, buttonWeather, buttonMap;
    private FragmentTripToolsBinding binding;
    public TripToolsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_tools, container, false);
        binding = FragmentTripToolsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
            }
        });


//        buttonLogout = view.findViewById(R.id.buttonLogout);
//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                getActivity().finish();
//            }
//        });
        return view;
    }
}