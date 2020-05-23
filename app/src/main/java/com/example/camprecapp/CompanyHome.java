package com.example.camprecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        bottomNavigationView= findViewById(R.id.botComNavigationView);

        bottomNav();

    }
    void bottomNav(){
        //Set the bottom navigation item to checked
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navHome:
                        break;
                    case R.id.navJobs:
                        Intent viewJobs = new Intent(CompanyHome.this, CompanyJobs.class);
                        startActivity(viewJobs);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navViewAppli:
                        Intent profile = new Intent(CompanyHome.this, CompanyViewSubApplication.class);
                        startActivity(profile);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navProfile:
                        Intent comProfile = new Intent(CompanyHome.this, ViewCompanyProfile.class);
                        startActivity(comProfile);
                        overridePendingTransition(0, 0);
                        break;
                }

                return false;
            }
        });
    }
}
