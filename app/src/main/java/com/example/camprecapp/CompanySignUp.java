package com.example.camprecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanySignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    CompanyInfo companyInfo;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_sign_up);

        Button btnSignUp = findViewById(R.id.btnComSignUp);
        final EditText editTextCompanyName = findViewById(R.id.editTextCompanyName);
        final EditText editTextComUserName = findViewById(R.id.editTextComUserName);
        final EditText editTextPhoneNumber = findViewById(R.id.editTextComPhoneNo);
        final EditText editTextSignUpPass = findViewById(R.id.editTextComSignUpPass);
        final EditText editTextAddress = findViewById(R.id.editTextComAddress);
        final EditText editTextCity = findViewById(R.id.editTextComCity);
        final EditText editTextComEmail = findViewById(R.id.editTextComLogInEmail);
        companyInfo =new CompanyInfo();
        editTextCompanyName.requestFocus();

        //establishing connection with firebase
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    if (!editTextPhoneNumber.getText().toString().equals("") &&
                            !editTextComUserName.getText().toString().equals("") &&
                            !editTextSignUpPass.getText().toString().equals("") &&
                            !editTextCompanyName.getText().toString().equals("") &&
                            !editTextAddress.getText().toString().equals("") &&
                            !editTextCity.getText().toString().equals("") &&
                            !editTextComEmail.getText().toString().equals("")) {
                        firebaseAuth.createUserWithEmailAndPassword(editTextComEmail.getText().toString(),
                                editTextSignUpPass.getText().toString())
                                //help us operation is successful or not
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String phoneNumber = editTextPhoneNumber.getText().toString();
                                            String companyName = editTextCompanyName.getText().toString();
                                            String companyEmail = editTextComEmail.getText().toString();
                                            String companyUserName = editTextComUserName.getText().toString();
                                            String companyAddress = editTextAddress.getText().toString();
                                            String companyCity = editTextCity.getText().toString();

                                            companyInfo.setPhoneNumber(phoneNumber);
                                            companyInfo.setCompanyName(companyName);
                                            companyInfo.setCompanyEmail(companyEmail);
                                            companyInfo.setAddress(companyAddress);
                                            companyInfo.setCity(companyCity);
                                            companyInfo.setName(companyUserName);
                                            companyInfo.setType("Company");

                                            FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                            ff.collection("CampRecApp").document(companyEmail).set(companyInfo);
                                            //below is to create subdocument in firebase
                                            //ff.collection("CampRecApp").document(companyEmail).collection("jobs").document("jobid").set(companyInfo);

                                            Toast.makeText(CompanySignUp.this, "Thank you for signing up!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(CompanySignUp.this, CompanyHome.class));
                                        } else {
                                            Toast.makeText(CompanySignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                }catch (Exception ex) {
                    Toast.makeText(CompanySignUp.this, "The fields must not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
