package com.example.nvaphone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileActivity extends AppCompatActivity {
    //show the logged-in phone number and an option to log out
    private TextView name;
    private TextView phone;
    private TextView hello1;
    private TextView hello2;
    private Button logoutbtn;
    private Button notibtn;
    private DatabaseReference mParticipant = FirebaseDatabase.getInstance().getReference().child("trip").
            child("xxx");
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.proName);
        phone = findViewById(R.id.proPhone);
        hello1 = findViewById(R.id.proHello1);
        hello2 = findViewById(R.id.proHello2);
        logoutbtn = findViewById(R.id.proLogOut);
        notibtn = findViewById(R.id.proNotification);
        auth = FirebaseAuth.getInstance();

        checkUserStatus(); //set phone details to the textview, if applicable
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                checkUserStatus();
            }
        });
        notibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
            }
        });
        //the user is logged in and added to the trip -> listen for notification changes

        DatabaseReference notiRef = FirebaseDatabase.getInstance().getReference().child("trip").
                child("xxx").child("notification");
        notiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Notification n = snapshot.getValue(Notification.class);
                    String body = n.toString();
                    sendNotification(body);
                } catch (Exception e) {
                    System.out.println("================1" + e);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Notification n = snapshot.getValue(Notification.class);
                    String body = n.toString();
                    sendNotification(body);
                } catch (Exception e) {
                    System.out.println("================2" + e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //get the current user
    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) { //user is logged in
            String userPhone = user.getPhoneNumber();
            phone.setText(userPhone);

            //get the user's name
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(userPhone);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userino = snapshot.getValue(User.class);
                    try {
                        name.setText(String.format(userino.getName() + ", email " + userino.getEmail()));
                        //add participant info to the trip (currently xxx)
                        mParticipant.child("participant").child(userPhone).
                                setValue(userino);
                    } catch (NullPointerException e) {
                        System.out.println("" + e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else { //not logged in
            finish();
        }
    }

    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle("Notification from nvaphone - " + channelId)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);
//                        .addAction(new NotificationCompat.Action(
//                                android.R.drawable.sym_call_missed,
//                                "Cancel",
////                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)))
//                        .addAction(new NotificationCompat.Action(
//                                android.R.drawable.sym_call_outgoing,
//                                "OK",
//                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        //not a zero but a capital O (Oh, like, after N)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this, );
            return;
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}