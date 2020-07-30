package com.example.camprecapp.features.company;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camprecapp.R;
import com.example.camprecapp.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

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
    Button btnAddJob, btnEditJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_job);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final FirebaseFirestore ff = FirebaseFirestore.getInstance();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Job");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextCompanyName = findViewById(R.id.editTextJobCompanyName);
        editTextJobType = findViewById(R.id.editTextJobType);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextSalary = findViewById(R.id.editTextSalary);
        editTextWorkLocation = findViewById(R.id.editTextWorkLocation);
        btnAddJob = findViewById(R.id.btnAddJob);
        btnEditJob = findViewById(R.id.btnEditJob);
        jobPost = new JobPost();
        editTextTitle.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();

        if (getIntent().getStringExtra("jobpost") != null) {
            btnAddJob.setVisibility(View.INVISIBLE);
            btnEditJob.setVisibility(View.VISIBLE);

            ff.document(getIntent().getStringExtra("jobpost")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    jobPost = task.getResult().toObject(JobPost.class);
                    jobPost.setJobPost(task.getResult().getReference());
                    editTextTitle.setText(jobPost.getTitle());
                    editTextCompanyName.setText(jobPost.getCompanyName());
                    editTextJobType.setText(jobPost.getJobType());
                    editTextDescription.setText(jobPost.getDescription());
                    editTextSalary.setText(jobPost.getSalary());
                    editTextWorkLocation.setText(jobPost.getLocation());
                }
            });
        }

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

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
                    if (!title.isEmpty() && !companyName.isEmpty() && !jobType.isEmpty() && !description.isEmpty() &&
                            !salary.isEmpty() && !location.isEmpty()) {

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
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CompanyAddJob.this, "The fields must not be empty", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnEditJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTextTitle.getText().toString();
                String companyName = editTextCompanyName.getText().toString();
                String jobType = editTextJobType.getText().toString();
                String description = editTextDescription.getText().toString();
                String salary = editTextSalary.getText().toString();
                String location = editTextWorkLocation.getText().toString();

                HashMap<String, Object> myObject = new HashMap<String, Object>();
                myObject.put("title", title);
                myObject.put("companyName", companyName);
                myObject.put("description", description);
                myObject.put("jobType", jobType);
                myObject.put("location", location);
                myObject.put("salary", salary);

                ff.document(jobPost.getJobPost().getPath()).update(myObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CompanyAddJob.this, "Job Updated!", Toast.LENGTH_LONG).show();
                        //setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}