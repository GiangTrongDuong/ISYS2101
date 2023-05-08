package com.example.tripme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter {
    ArrayList<Notification> notiList = new ArrayList<>();

    public NotificationAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        notiList = objects;
    }

    public NotificationAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_notification_item, null);
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView text = (TextView) v.findViewById(R.id.text);
        time.setText(notiList.get(position).getNotiTime());
        text.setText(notiList.get(position).getNotiText());

        return v;

    }
}