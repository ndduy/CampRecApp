package com.example.camprecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ViewCompanyProfile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewName;
    TextView txtViewEmail;
    TextView txtViewPhoneNo;
    TextView txtViewAddress;
    TextView txtViewCity;
    TextView txtViewCompanyName;
    BottomNavigationView bottomNavigationView;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company_profile);

        btnLogOut = findViewById(R.id.btnLogOut);
        txtViewName = findViewById(R.id.txtViewCompanyUserName);
        txtViewEmail = findViewById(R.id.txtViewComEmail);
        txtViewPhoneNo = findViewById(R.id.txtViewComPhone);
        txtViewAddress = findViewById(R.id.txtViewAddress);
        txtViewCity = findViewById(R.id.txtViewCity);
        txtViewCompanyName = findViewById(R.id.txtViewComName);
        bottomNavigationView = findViewById(R.id.botComNavigationView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        viewProfile();
        bottomNav();

        }
    void viewProfile(){
        if (firebaseUser != null) {

            final String studentEmail = firebaseUser.getEmail();

            txtViewEmail.setText(studentEmail);
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            ff.collection("CampRecApp").document(studentEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    Map<String, Object> map = doc.getData();

                    txtViewName.setText(doc.getString("name"));
                    txtViewEmail.setText(doc.getString("companyEmail"));
                    txtViewPhoneNo.setText(doc.getString("phoneNumber"));
                    txtViewCompanyName.setText(doc.getString("companyName"));
                    txtViewAddress.setText(doc.getString("address"));
                    txtViewCity.setText(doc.getString("city"));
                }
            });

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ViewCompanyProfile.this, MainActivity.class);
                    //whatever activity that was on this flag will be deleted when you press back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }
    void bottomNav(){
        //Set the bottom navigation item to checked
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navHome:
                        Intent home = new Intent(ViewCompanyProfile.this, CompanyHome.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navJobs:
                        Intent viewJobs = new Intent(ViewCompanyProfile.this, CompanyJobs.class);
                        startActivity(viewJobs);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navViewAppli:
                        Intent viewApplication = new Intent(ViewCompanyProfile.this, CompanyViewSubApplication.class);
                        startActivity(viewApplication);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navProfile:
                        break;
                }

                return false;
            }
        });
    }
}
