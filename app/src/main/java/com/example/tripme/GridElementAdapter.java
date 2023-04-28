package com.example.tripme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridElementAdapter extends ArrayAdapter {
    Context context;
    String[] itemName;
    int[] itemTime;

    ArrayList<Item> itemList;


    public GridElementAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        itemList = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_element, null);
        TextView itemNameView = (TextView) v.findViewById(R.id.txtTemp);
        TextView itemTimeView = (TextView) v.findViewById(R.id.txtTime);

        itemNameView.setText(itemList.get(position).getitemName());
        itemTimeView.setText(itemList.get(position).getitemTime());
        return v;
    }
}