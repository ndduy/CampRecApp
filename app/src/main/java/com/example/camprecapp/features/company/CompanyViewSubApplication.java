package com.example.camprecapp.features.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.camprecapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyViewSubApplication extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_sub_application);

        bottomNavigationView = findViewById(R.id.botComNavigationView);

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
                        Intent home = new Intent(CompanyViewSubApplication.this, CompanyHome.class);
                        startActivity(home);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navJobs:
                        Intent viewJobs = new Intent(CompanyViewSubApplication.this, CompanyJobs.class);
                        startActivity(viewJobs);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navViewAppli:
                        break;
                    case R.id.navProfile:
                        Intent comProfile = new Intent(CompanyViewSubApplication.this, ViewCompanyProfile.class);
                        startActivity(comProfile);
                        overridePendingTransition(0, 0);
                        break;
                }

                return false;
            }
        });
    }
}
