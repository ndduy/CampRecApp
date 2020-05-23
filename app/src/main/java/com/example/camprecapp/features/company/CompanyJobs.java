package com.example.camprecapp.features.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.camprecapp.features.company.adapter.AddJobCustomAdapter;
import com.example.camprecapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

public class CompanyJobs extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference;
    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private CollectionReference jobRef = db.collection("CampRecApp");
    RecyclerView recyclerView;
    ArrayList<String> titles;
    AddJobCustomAdapter adapter;
    ArrayList<String> description;
    ArrayList<String> companyName;
    ArrayList<String> salary;
    ArrayList<String> location;
    ArrayList<String> jobType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_jobs);

        addItems();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //list = new ArrayList<AddJobModel>();

        /*databaseReference = FirebaseDatabase.getInstance().getReference().child("AddJobModel");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    AddJobModel job = dataSnapshot1.getValue(AddJobModel.class);
                    list.add(job);
                }
                adapter = new AddJobCustomAdapter(CompanyJobs.this,list);
                RecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CompanyJobs.this, "Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });*/
        recyclerView.addItemDecoration(new VerticalSpace(2));
        recyclerView.setAdapter(new AddJobCustomAdapter(titles,companyName,jobType, description,
             salary, location,this));
        FloatingActionButton buttonAddJob = findViewById(R.id.floating_action_button);
        buttonAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyJobs.this, CompanyAddJob.class));
            }
        });
        bottomNavigationView= findViewById(R.id.botComNavigationView);
        bottomNav();

    }
    void addItems(){
        titles = new ArrayList<String>(Arrays.asList("title1", "title2", "title3", "title4"));
        description = new ArrayList<String>(Arrays.asList("description", "description", "description", "description"));
        companyName = new ArrayList<String>(Arrays.asList("companyName", "companyName", "companyName", "companyName"));
        salary = new ArrayList<String>(Arrays.asList("salary", "salary", "salary", "salary"));
        location = new ArrayList<String>(Arrays.asList("location", "location", "location", "location"));
        jobType = new ArrayList<String>(Arrays.asList("jobType", "jobType", "jobType", "jobType"));

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
                        Intent home = new Intent(CompanyJobs.this, CompanyHome.class);
                        startActivity(home);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navJobs:
                        break;
                    case R.id.navViewAppli:
                        Intent viewApplication = new Intent(CompanyJobs.this,
                                CompanyViewSubApplication.class);
                        startActivity(viewApplication);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navProfile:
                        Intent comProfile = new Intent(CompanyJobs.this, ViewCompanyProfile.class);
                        startActivity(comProfile);
                        overridePendingTransition(0, 0);
                        break;
                }

                return false;
            }
        });
    }

}
