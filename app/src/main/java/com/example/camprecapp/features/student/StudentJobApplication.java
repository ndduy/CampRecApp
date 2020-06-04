package com.example.camprecapp.features.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camprecapp.R;
import com.example.camprecapp.features.company.CompanyAddJob;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.example.camprecapp.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentJobApplication extends AppCompatActivity {

    Button btnApplyJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_apply_job);


        final FirebaseFirestore ff = FirebaseFirestore.getInstance();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Apply Job");

        btnApplyJob = findViewById(R.id.btnApplyJob);

        btnApplyJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DocumentReference student = ff.document(getIntent().getStringExtra("student"));
                final DocumentReference jobpost = ff.document(getIntent().getStringExtra("jobpost"));
                jobpost.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        JobPost jobPostData = task.getResult().toObject(JobPost.class);
                        JobApplication jobApplication = new JobApplication(jobPostData.getCompany(), jobpost, student);
                        ff.collection("JobApplication").add(jobApplication).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(StudentJobApplication.this, "Job Applied!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                });


            }
        });
    }

}