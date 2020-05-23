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

public class ViewStudentProfile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewName;
    TextView txtViewEmail;
    TextView txtViewPhoneNo;
    Button btnLogOut;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_profile);

        bottomNavigationView = findViewById(R.id.botNavigationView);
        btnLogOut = findViewById(R.id.btnLogOut);
        txtViewName = findViewById(R.id.txtViewStudentName);
        txtViewEmail = findViewById(R.id.txtViewStudentEmail);
        txtViewPhoneNo = findViewById(R.id.txtViewStudentPhoneNo);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        bottomNav();
        viewProfile();

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
                        break;
                    case R.id.navViewJobs:
                        Intent viewJobs = new Intent(ViewStudentProfile.this, StudentViewJobs.class);
                        startActivity(viewJobs);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navProfile:
                        Intent profile = new Intent(ViewStudentProfile.this, ViewStudentProfile.class);
                        startActivity(profile);
                        overridePendingTransition(0, 0);
                        break;
                }

                return false;
            }
        });
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
                    txtViewEmail.setText(doc.getString("email"));
                    txtViewPhoneNo.setText(doc.getString("phoneNumber"));
                }
            });

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ViewStudentProfile.this, MainActivity.class);
                    //whatever activity that was on this flag will be deleted when you press back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}
