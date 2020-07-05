package com.example.camprecapp.features;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button btnLogIn;
    Button btnStudentSignUp;
    Button btnCompanySignUp;
    EditText editTextLogInEmail;
    EditText editTextLogInPass;
    Intent intentRecognizer;
    ImageButton voiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogIn = findViewById(R.id.btnLogin);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        btnCompanySignUp = findViewById(R.id.btnCompanySignUp);
        voiceBtn = findViewById(R.id.imgViewVoiceRec);
        editTextLogInEmail = findViewById(R.id.editTextLogInEmail);
        editTextLogInPass = findViewById(R.id.editTextLogInPass);
        editTextLogInEmail.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //button to show speech to text dialog
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        logIn();
        signUp();
        activeUser();

    }
    void speak() {
        //intent to show speech to text dialog
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Your LogIn Email Address");
        //start intent
        try {
            startActivityForResult(intentRecognizer, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //receive voice input and handle it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intentx
                    ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set the text view
                    editTextLogInEmail.setText(matches.get(0));
                }
                break;
            }
        }
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

                                        ff.collection("Company").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    DocumentReference company = task.getResult().getDocuments().get(0).getReference();//getDocumentReference("company");
                                                    goToCompanyPage(company);

                                                }
                                            }
                                        });

                                        ff.collection("Student").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().size() > 0) {
                                                        DocumentReference student = task.getResult().getDocuments().get(0).getReference();
                                                        goToStudentPage(student);
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
            ff.collection("Company").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentReference company = task.getResult().getDocuments().get(0).getReference();
                        goToCompanyPage(company);
                    }
                }

            });

            ff.collection("Student").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            DocumentReference student = task.getResult().getDocuments().get(0).getReference();
                            goToStudentPage(student);
                        }
                    }
                }
            });
        }

    }

    void goToStudentPage(DocumentReference student) {
        Intent i = new Intent(MainActivity.this, StudentHome.class);
        i.putExtra("student", student.getPath());
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
