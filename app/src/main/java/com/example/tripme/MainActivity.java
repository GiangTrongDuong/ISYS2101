package com.example.tripme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tripme.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    BottomNavigationView myMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_checklist, R.id.navigation_profile)
//                .build();
        myMenu = findViewById(R.id.nav_view);
        String role = getIntent().getExtras().getString("role");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Singleton classes init
        SingletonAppTime appTime = new SingletonAppTime(); //app time recorded
//      SingletonLastNoti lastNoti = new SingletonLastNoti(); //starts recording notification
       if (role.equals("Participant")) {
           //Participant cannot see: checklist
           //hide checklist
           MenuItem item = myMenu.getMenu().findItem(R.id.navigation_checklist);
           item.setVisible(false);
           myMenu.setSelectedItemId(R.id.navigation_notification);
           this.invalidateOptionsMenu();
        } else if (role.equals(""))
        {
            finish();
        }
    }

}
