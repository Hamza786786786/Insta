package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.insta.Fragment.AddFragment;
import com.example.insta.Fragment.HomeFragment;
import com.example.insta.Fragment.NotificationFragment;
import com.example.insta.Fragment.ProfileFragment;
import com.example.insta.Fragment.SearchFragment;
import com.example.insta.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = 1;

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.nav_home && i!=1){
                    selectorFragment = new HomeFragment();
                    i = 1;
                }

                else if (item.getItemId() == R.id.nav_search && i!=2){
                    selectorFragment = new SearchFragment();
                    i = 2;
                }

                else if (item.getItemId() == R.id.nav_add && i!=3){
                    selectorFragment = new AddFragment();
                    i = 3;
                }

                else if (item.getItemId() == R.id.nav_heart && i!=4){
                    selectorFragment = new NotificationFragment();
                    i = 4;
                }

                else if (item.getItemId() == R.id.nav_profile && i!=5){
                    selectorFragment = new ProfileFragment();
                    i = 5;
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return true;
            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }
    }
}