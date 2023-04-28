package com.example.tripme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ChecklistPhoneAdapter extends ArrayAdapter {
    ArrayList<Participant> participantList = new ArrayList<>();

    public ChecklistPhoneAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        participantList = objects;
    }

    public ChecklistPhoneAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public Participant getItem(int position) {
        return participantList.get(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_checklist_phone, null);
        return v;

    }
}