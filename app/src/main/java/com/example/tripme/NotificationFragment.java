package com.example.tripme;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.tripme.databinding.FragmentChecklistBinding;
import com.example.tripme.databinding.FragmentNotificationBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapp-4d5c1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("trip");
    ListView listView;
    EditText editText;
    Button btn;
    ArrayList<Notification> notifList = new ArrayList<>();
    int count = 0;
    private boolean firstTime = true;
    private Date appDate;
    private Date notiDate;
    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = getActivity().getIntent();
        String tripID = intent.getExtras().getString("tripID");
//        Date currentTime = Calendar.getInstance().getTime();
//        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//        TextView textView = view.findViewById(R.id.user_name);
//        textView.setText(format.format(currentTime));
        editText = view.findViewById(R.id.editText);
        btn = view.findViewById(R.id.btn);
        //TODO: if role is participant -> hide the editText and the send button

        if(false){
            editText.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                Notification noti = new Notification(String.valueOf(count),format.format(currentTime),editText.getText().toString());
                myRef.child(tripID).child("notification").child(String.valueOf(count)).setValue(noti);
            }
        });

        listView = view.findViewById(R.id.notificationList);
        NotificationAdapter listAdapter = new NotificationAdapter(inflater.getContext(), R.layout.fragment_notification_item, notifList);
        listView.setAdapter(listAdapter);

        //get the time that the app was launched
        String appTime = SingletonAppTime.getInstance().getAppTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        try{
            appDate = sdf.parse(appTime);
        }
        catch (Exception e){
            System.out.println("" + e);
        }
        myRef.child(tripID).child("notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifList.clear();
                count = 0;
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    count--;
                    notifList.add(new Notification(dsp.child("notiNo").getValue().toString(),
                            dsp.child("notiTime").getValue().toString(),
                            dsp.child("notiText").getValue().toString()));
                        //decide if this notification is worth alerting
                }
                //get last notification
                Notification n = notifList.get(0);
                System.out.println("=============== list = " + notifList);
                System.out.println("===============last noti = " + n.toString());
                System.out.println("===============app time  = " + appTime);
                try {
                    notiDate = sdf.parse(n.getNotiTime());
                } catch (ParseException e) {
                    System.out.println("============parse" + e);
                }
                if(appDate.compareTo(notiDate) < 0){ //noti is after before app launch
                    sendNotification(n.getNotiText(), n.getNotiTime(), context);
                }
                    listView.invalidateViews();
                    firstTime = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void sendNotification(String messageBody, String messageTime, Context context) {
        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle("New notification at " + messageTime)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        //not a zero but a capital O (Oh, like, after N)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
            if(notificationManager.areNotificationsEnabled()){
                Toast.makeText(context, "Missing permission", Toast.LENGTH_SHORT).show();
            }
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}