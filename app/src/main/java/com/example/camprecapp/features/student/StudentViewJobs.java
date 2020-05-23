package com.example.camprecapp.features.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.camprecapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentViewJobs extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_jobs);

        bottomNavigationView = findViewById(R.id.botNavigationView);
        bottomNav();

    }
    void bottomNav(){
        //Set the bottom navigation item to checked
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navHome:
                        Intent home = new Intent(StudentViewJobs.this, StudentHome.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navViewJobs:
                        break;
                    case R.id.navProfile:
                        Intent updateProfile = new Intent(StudentViewJobs.this, ViewStudentProfile.class);
                        startActivity(updateProfile);
                        overridePendingTransition(0, 0);
                        break;
                }

                return false;
            }
        });
    }
}
