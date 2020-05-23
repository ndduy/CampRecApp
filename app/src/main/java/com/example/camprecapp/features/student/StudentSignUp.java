package com.example.camprecapp.features.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.camprecapp.R;
import com.example.camprecapp.models.StudentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    StudentInfo studentInfo;
    RadioButton student, company;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        Button btnSignUp = findViewById(R.id.btnStudentSignUp);
        final EditText editTextSignUpEmail = findViewById(R.id.editTextLogInEmail);
        final EditText editTextSignUpName = findViewById(R.id.editTextStuName);
        final EditText editTextPhoneNumber = findViewById(R.id.editTextStuPhoneNumber);
        final EditText editTextSignUpPass = findViewById(R.id.editTextLogInPass);
        studentInfo =new StudentInfo();
        editTextSignUpName.requestFocus();

        //establishing connection with firebase
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    if (!editTextPhoneNumber.getText().toString().equals("") &&
                        !editTextSignUpName.getText().toString().equals("") &&
                        !editTextSignUpEmail.getText().toString().equals("") &&
                        !editTextSignUpPass.getText().toString().equals("")) {
                        firebaseAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(),
                                editTextSignUpPass.getText().toString())
                                //help us operation is successful or not
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String phoneNumber = editTextPhoneNumber.getText().toString();
                                            String studentName = editTextSignUpName.getText().toString();
                                            String studentEmail = editTextSignUpEmail.getText().toString();
                                            String studentPassword = editTextSignUpEmail.getText().toString();
                                            studentInfo.setPhoneNumber(phoneNumber);
                                            studentInfo.setName(studentName);
                                            studentInfo.setEmail(studentEmail);
                                            studentInfo.setPassword(studentPassword);
                                            studentInfo.setType("Student");

                                            FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                            ff.collection("CampRecApp").document(studentEmail).set(studentInfo);

                                            Toast.makeText(StudentSignUp.this, "Thank you for signing up!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(StudentSignUp.this, StudentHome.class));
                                        } else {
                                            Toast.makeText(StudentSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }catch (Exception ex) {
                    Toast.makeText(StudentSignUp.this, "The fields must not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
