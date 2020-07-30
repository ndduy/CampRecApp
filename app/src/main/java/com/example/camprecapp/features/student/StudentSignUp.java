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
import com.example.camprecapp.features.MainActivity;
import com.example.camprecapp.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Student studentInfo;
    RadioButton student, company;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        Button btnSignUp = findViewById(R.id.btnStudentSignUp);
        final EditText editTextSignUpEmail = findViewById(R.id.editTextLogInEmail);
        final EditText editTextSignUpName = findViewById(R.id.editTextStuName);
        final EditText editTextPhoneNumber = findViewById(R.id.editTextStuPhoneNumber);
        final EditText editTextSignUpPass = findViewById(R.id.editTextLogInPass);
        studentInfo = new Student();
        editTextSignUpName.requestFocus();

        //establishing connection with firebase
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

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
                                            firebaseUser = firebaseAuth.getCurrentUser();

                                            String phoneNumber = editTextPhoneNumber.getText().toString();
                                            String studentName = editTextSignUpName.getText().toString();
                                            String studentEmail = editTextSignUpEmail.getText().toString();
                                            studentInfo.setPhoneNumber(phoneNumber);
                                            studentInfo.setName(studentName);
                                            studentInfo.setEmail(studentEmail);
                                            studentInfo.setuId(firebaseUser.getUid());

                                            FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                            ff.collection("Student").add(studentInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    DocumentReference student = task.getResult();
                                                    goToStudentPage(student);
                                                    Toast.makeText(StudentSignUp.this, "Thank you for signing up!", Toast.LENGTH_LONG).show();
                                                }
                                            });


                                        } else {
                                            Toast.makeText(StudentSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } catch (Exception ex) {
                    Toast.makeText(StudentSignUp.this, "The fields must not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void goToStudentPage(DocumentReference student) {
        Intent i = new Intent(this, StudentHome.class);
        i.putExtra("student", student.getPath());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
