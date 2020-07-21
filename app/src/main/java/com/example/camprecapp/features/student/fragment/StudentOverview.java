package com.example.camprecapp.features.student.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.camprecapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class StudentOverview extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewName;

    //ViewFlipper for Auto Sliding Image
    ViewFlipper viewFlipper;
    int[] imgFlipper = {R.drawable.microsoft, R.drawable.google, R.drawable.amazon,R.drawable.apple,R.drawable.boeinglogo};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_student_overview, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        txtViewName = getView().findViewById(R.id.txtHomeName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        viewProfile();

        super.onCreate(savedInstanceState);
        try {
            viewFlipper = getView().findViewById(R.id.viewFlipper);
            for (int i = 0; i < imgFlipper.length; ++i) {
                flipImage(imgFlipper[i]);
            }
        } catch (Exception ex) {
            Log.e("ViewFlipper: ", ex.getMessage());
        }
    }
    void viewProfile() {
        if (firebaseUser != null) {

            final String studentName = firebaseUser.getDisplayName();

            txtViewName.setText(studentName);
            FirebaseFirestore ff = FirebaseFirestore.getInstance();

            ff.collection("Student").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        Map<String, Object> map = doc.getData();

                        txtViewName.setText(doc.getString("name"));

                    }
                }
            });
        }
    }
    public void flipImage(int img){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(img);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getContext(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.fade_out);
    }
}
