package com.example.camprecapp.features.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.camprecapp.R;
import com.example.camprecapp.features.company.fragment.CompanyJobs;
import com.example.camprecapp.features.company.fragment.CompanyOverview;
import com.example.camprecapp.features.company.fragment.CompanyViewSubApplication;
import com.example.camprecapp.features.company.fragment.ViewCompanyProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CompanyHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        openFragment(new CompanyOverview());

        bottomNavigationView = findViewById(R.id.botComNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navHome:
                        openFragment(new CompanyOverview());
                        break;
                    case R.id.navJobs:
                        openFragment(new CompanyJobs());
                        break;
                    case R.id.navViewAppli:
                        openFragment(new CompanyViewSubApplication());
                        break;
                    case R.id.navProfile:
                        openFragment(new ViewCompanyProfile());
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
