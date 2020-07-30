package com.example.camprecapp.features.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.camprecapp.R;
import com.example.camprecapp.features.student.EditStudentProfile;
import com.example.camprecapp.features.student.fragment.ViewStudentProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditCompanyProfile extends AppCompatActivity {
    EditText userName, phone, companyName, address, city, profileEmail;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_profile);
        Intent data = getIntent();
        String companyUserName = data.getStringExtra("name");
        String companyEmail = data.getStringExtra("email");
        String companyPhone = data.getStringExtra("phone");
        String companyAddress = data.getStringExtra("address");
        String companyCity = data.getStringExtra("city");
        String companyUpName = data.getStringExtra("companyName");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userName = findViewById(R.id.companyUpdateName);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.companyUpdateComName);
        address = findViewById(R.id.comUpdateAddress);
        city = findViewById(R.id.comUpdateCity);
        btnSave = findViewById(R.id.btnUpdateProfile);
        profileEmail = findViewById(R.id.comUpdateEmail);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty() || phone.getText().toString().isEmpty() ||
                        companyName.getText().toString().isEmpty() || address.getText().toString().isEmpty()
                        || city.getText().toString().isEmpty()) {
                    Toast.makeText(EditCompanyProfile.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        fStore.collection("CompanyAdmin").whereEqualTo("uId", user.getUid()).get().addOnSuccessListener(
                                new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                                        Map<String, Object> edited = new HashMap<>();
                                        edited.put("email", email);
                                        edited.put("name", userName.getText().toString());
                                        edited.put("phoneNumber", phone.getText().toString());
                                        //Log.i("Inserted","Error");
                                        final DocumentReference companyRef = queryDocumentSnapshots.getDocuments().get(0).getDocumentReference("company");
                                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EditCompanyProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                Map<String, Object> edited = new HashMap<>();
                                                edited.put("address", address.getText().toString());
                                                edited.put("name", companyName.getText().toString());
                                                edited.put("city", city.getText().toString());
                                                companyRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        setResult(RESULT_OK);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditCompanyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ;
                                    }
                                }
                        );
                        //Toast.makeText(EditCompanyProfile.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditCompanyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        userName.setText(companyUserName);
        profileEmail.setText(companyEmail);
        phone.setText(companyPhone);
        address.setText(companyAddress);
        city.setText(companyCity);
        companyName.setText(companyUpName);

    }
}
