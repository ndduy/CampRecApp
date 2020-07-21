package com.example.camprecapp.features.student.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.features.JobApplicationDetailActivity;
import com.example.camprecapp.features.MainActivity;
import com.example.camprecapp.features.company.CompanyHome;
import com.example.camprecapp.features.company.adapter.JobApplicationAdapter;
import com.example.camprecapp.features.student.adapter.JobApplicationStudentAdapter;
import com.example.camprecapp.models.Company;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.example.camprecapp.models.Student;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class StudentViewSubmittedJobs extends Fragment implements JobApplicationStudentAdapter.OnItemClickListener{
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_student_view_submitted_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final ArrayList<JobApplication> jobApplications = new ArrayList<>();

        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        String student = getActivity().getIntent().getStringExtra("student");
        Task<QuerySnapshot> getJobApplication = ff.collection("JobApplication").whereEqualTo("student", ff.document(student)).get();
        getJobApplication.continueWithTask(new Continuation<QuerySnapshot, Task<List<DocumentSnapshot>>>() {
            @Override
            public Task<List<DocumentSnapshot>> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    JobApplication jobApplication = documentSnapshot.toObject(JobApplication.class);
                    jobApplication.setJobApplication(documentSnapshot.getReference());
                    jobApplications.add(jobApplication);
                    tasks.add(jobApplication.getJobpost().get());
                    tasks.add(jobApplication.getCompany().get());
                }
                Task combinedTask = Tasks.whenAllSuccess(tasks);
                return combinedTask;
            }
        }).addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                for (int i = 0; i < documentSnapshots.size(); i++) {
                    if (i%2==0)
                        jobApplications.get(i/2).setJobPostData(documentSnapshots.get(i).toObject(JobPost.class));
                    else
                        jobApplications.get(i/2).setCompanyData(documentSnapshots.get(i).toObject(Company.class));
                }
                recyclerView.setAdapter(new JobApplicationStudentAdapter(jobApplications.toArray(new JobApplication[jobApplications.size()]), StudentViewSubmittedJobs.this));
            }
        });

        recyclerView.setAdapter(new JobApplicationStudentAdapter(new JobApplication[0], null));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(JobApplication item, int position) {
        Intent i = new Intent(getActivity(), JobApplicationDetailActivity.class);
        i.putExtra("JobApplication", item.getJobApplication().getPath());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}