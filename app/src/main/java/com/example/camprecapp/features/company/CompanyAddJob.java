package com.example.camprecapp.features.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.camprecapp.R;
import com.example.camprecapp.features.company.fragment.CompanyJobs;
import com.example.camprecapp.models.AddJobModel;
import com.example.camprecapp.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CompanyAddJob extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    JobPost jobPost;
    private EditText editTextTitle;
    private EditText editTextCompanyName;
    private EditText editTextJobType;
    private EditText editTextDescription;
    private EditText editTextSalary;
    private EditText editTextWorkLocation;
    Button btnAddJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_job);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseFirestore ff = FirebaseFirestore.getInstance();



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Job");
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextCompanyName = findViewById(R.id.editTextJobCompanyName);
        editTextJobType = findViewById(R.id.editTextJobType);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextSalary = findViewById(R.id.editTextSalary);
        editTextWorkLocation = findViewById(R.id.editTextWorkLocation);
        btnAddJob = findViewById(R.id.btnAddJob);
        jobPost = new JobPost();
        editTextTitle.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTextTitle.getText().toString();
                String companyName = editTextCompanyName.getText().toString();
                String jobType = editTextJobType.getText().toString();
                String description = editTextDescription.getText().toString();
                String salary = editTextSalary.getText().toString();
                String location = editTextWorkLocation.getText().toString();

                jobPost.setTitle(title);
                jobPost.setCompanyName(companyName);
                jobPost.setJobType(jobType);
                jobPost.setDescription(description);
                jobPost.setSalary(salary);
                jobPost.setLocation(location);

                final FirebaseFirestore ff = FirebaseFirestore.getInstance();
                ff.collection("CompanyAdmin").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentReference company = task.getResult().getDocuments().get(0).getDocumentReference("company");
                            jobPost.setCompany(company);
                            ff.collection("JobPost").add(jobPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(CompanyAddJob.this, "Job Added!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    }
                });

            }
        });
    }

}