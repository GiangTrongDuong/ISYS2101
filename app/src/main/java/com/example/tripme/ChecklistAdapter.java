package com.example.tripme;

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChecklistAdapter extends ArrayAdapter {

//    Context context;
//    String[] itemName;
//    int[] itemStock;
//    String[] itemCategory;
//    int[] itemPrice;
//    int[] image;

    ArrayList<Participant> participantList = new ArrayList<>();

    public ChecklistAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        participantList = objects;
    }

    public ChecklistAdapter(@NonNull Context context, int resource) {
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
        v = inflater.inflate(R.layout.fragment_checklist, null);
        TextView participantName = (TextView) v.findViewById(R.id.name);
        ImageButton participantRole = (ImageButton) v.findViewById(R.id.role);

        participantName.setText(participantList.get(position).getParticipantName());
//        itemNameView.setText(cartList.get(position).getitemName());
//        itemStockView.setText("x  " + cartList.get(position).getitemStock());
//        itemPriceView.setText("$" + cartList.get(position).getitemPrice());
        if (participantList.get(position).getParticipantRole().equals("Arrived")) {
            participantRole.setImageResource(R.drawable.check_circle);
        } else {
            participantRole.setImageResource(R.drawable.cross_circle);
        }
//            if (cartList.get(position).getitemCategory().equals("GAME")) {
//                itemImageView.setImageResource(R.drawable.game);
//            }
//            if (cartList.get(position).getitemCategory().equals("CONSOLE")) {
//                itemImageView.setImageResource(R.drawable.console);
//            }
//            if (cartList.get(position).getitemCategory().equals("ACCESSORY")) {
//                itemImageView.setImageResource(R.drawable.accessories);
//            }
//
//            if (cartList.get(position).getitemStock() == 0) {
//                itemStockView.setTextColor(Color.RED);
//            }
//        }

        return v;

    }
}