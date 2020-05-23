package com.example.camprecapp.features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.camprecapp.R;
import com.example.camprecapp.features.student.StudentHome;
import com.example.camprecapp.features.student.StudentSignUp;
import com.example.camprecapp.features.company.CompanyHome;
import com.example.camprecapp.features.company.CompanySignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button btnLogIn;
    Button btnStudentSignUp;
    Button btnCompanySignUp;
    EditText editTextLogInEmail;
    EditText editTextLogInPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogIn = findViewById(R.id.btnLogin);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        btnCompanySignUp = findViewById(R.id.btnCompanySignUp);
        editTextLogInEmail = findViewById(R.id.editTextLogInEmail);
        editTextLogInPass = findViewById(R.id.editTextLogInPass);
        editTextLogInEmail.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        logIn();
        signUp();
        activeUser();

    }

    void logIn() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    firebaseAuth.signInWithEmailAndPassword(editTextLogInEmail.getText().toString(), editTextLogInPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        firebaseUser = firebaseAuth.getCurrentUser();

                                        FirebaseFirestore ff = FirebaseFirestore.getInstance();

                                        ff.collection("CompanyAdmin").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    DocumentReference company = task.getResult().getDocuments().get(0).getDocumentReference("company");
                                                    goToCompanyPage(company);

                                                }
                                            }
                                        });

                                        ff.collection("Student").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().size() > 0) {
                                                        goToStudentPage();
                                                    }
                                                }
                                            }
                                        });
                                    } else if (!task.equals(editTextLogInPass)) {
                                        Toast.makeText(MainActivity.this, "The password is not correct", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(MainActivity.this, "The field must not be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void signUp() {
        btnStudentSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this, StudentSignUp.class);
                startActivity(signUp);
            }
        });
        btnCompanySignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this, CompanySignUp.class);
                startActivity(signUp);
            }
        });
    }

    void activeUser() {
        if (firebaseUser != null) {
            //when user is active show the login activity
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            ff.collection("CompanyAdmin").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentReference company = task.getResult().getDocuments().get(0).getDocumentReference("company");
                        goToCompanyPage(company);
                    }
                }

            });

            ff.collection("Student").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            goToStudentPage();
                        }
                    }
                }
            });
        }

    }

    void goToStudentPage() {
        Intent i = new Intent(MainActivity.this, StudentHome.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    void goToCompanyPage(DocumentReference company) {
        Intent i = new Intent(MainActivity.this, CompanyHome.class);
        i.putExtra("company", company.getPath());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
