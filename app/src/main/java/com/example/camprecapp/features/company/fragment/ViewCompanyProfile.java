package com.example.camprecapp.features.company.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.camprecapp.features.MainActivity;
import com.example.camprecapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ViewCompanyProfile extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewName;
    TextView txtViewEmail;
    TextView txtViewPhoneNo;
    TextView txtViewAddress;
    TextView txtViewCity;
    TextView txtViewCompanyName;
    BottomNavigationView bottomNavigationView;
    Button btnLogOut;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_view_company_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogOut = getView().findViewById(R.id.btnLogOut);
        txtViewName = getView().findViewById(R.id.txtViewCompanyUserName);
        txtViewEmail = getView().findViewById(R.id.txtViewComEmail);
        txtViewPhoneNo = getView().findViewById(R.id.txtViewComPhone);
        txtViewAddress = getView().findViewById(R.id.txtViewAddress);
        txtViewCity = getView().findViewById(R.id.txtViewCity);
        txtViewCompanyName = getView().findViewById(R.id.txtViewComName);
        bottomNavigationView = getView().findViewById(R.id.botComNavigationView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        viewProfile();

    }

    void viewProfile(){
        if (firebaseUser != null) {

            final String studentEmail = firebaseUser.getEmail();

            txtViewEmail.setText(studentEmail);
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            ff.collection("CompanyAdmin").document(studentEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    Map<String, Object> map = doc.getData();

                    txtViewName.setText(doc.getString("name"));
                    txtViewEmail.setText(doc.getString("email"));
                    txtViewPhoneNo.setText(doc.getString("phoneNumber"));



                    txtViewCompanyName.setText(doc.getString("companyName"));
                    txtViewAddress.setText(doc.getString("address"));
                    txtViewCity.setText(doc.getString("city"));
                }
            });

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //whatever activity that was on this flag will be deleted when you press back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}
