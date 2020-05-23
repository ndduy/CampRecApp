package com.example.camprecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanyAddJob extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    AddJobModel addJobModel;
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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Job");
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextCompanyName = findViewById(R.id.editTextJobCompanyName);
        editTextJobType = findViewById(R.id.editTextJobType);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextSalary = findViewById(R.id.editTextSalary);
        editTextWorkLocation = findViewById(R.id.editTextWorkLocation);
        btnAddJob = findViewById(R.id.btnAddJob);
        addJobModel = new AddJobModel();
        editTextTitle.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        //addJob();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_job, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_job:
                saveJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /*void addJob() {
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTextTitle.getText().toString();
                String companyName = editTextCompanyName.getText().toString();
                String jobType = editTextJobType.getText().toString();
                String description = editTextDescription.getText().toString();
                String salary = editTextSalary.getText().toString();
                String location = editTextWorkLocation.getText().toString();

                addJobModel.setTitle(title);
                addJobModel.setCompanyName(companyName);
                addJobModel.setJobType(jobType);
                addJobModel.setDescription(description);
                addJobModel.setSalary(salary);
                addJobModel.setLocation(location);

                FirebaseFirestore ff = FirebaseFirestore.getInstance();
                documentReference.collection("CampRecApp").document("Job").collection("jobs");

                Toast.makeText(CompanyAddJob.this, "Job Added!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CompanyAddJob.this, CompanyJobs.class));
            }
        });
    }*/
}