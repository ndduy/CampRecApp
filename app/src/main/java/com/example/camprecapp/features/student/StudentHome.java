package com.example.camprecapp.features.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.camprecapp.R;
import com.example.camprecapp.features.company.fragment.CompanyOverview;
import com.example.camprecapp.features.student.fragment.StudentOverview;
import com.example.camprecapp.features.student.fragment.StudentViewJobs;
import com.example.camprecapp.features.student.fragment.ViewStudentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        openFragment(new StudentOverview());

        bottomNavigationView = findViewById(R.id.botNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navHome:
                        openFragment(new StudentOverview());

                        break;
                    case R.id.navViewJobs:
                        openFragment(new StudentViewJobs());

                        break;
                    case R.id.navProfile:
                        openFragment(new ViewStudentProfile());

                        break;
                }

                return false;
            }
        });


    }

    void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
