package com.example.camprecapp.features.student.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.features.JobApplicationDetailActivity;
import com.example.camprecapp.features.student.adapter.JobApplicationStudentAdapter;
import com.example.camprecapp.features.student.adapter.RecyclerItemTouchHelper;
import com.example.camprecapp.models.Company;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentViewSubmittedJobs extends Fragment implements JobApplicationStudentAdapter.OnItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private RecyclerView recyclerView;
    final ArrayList<JobApplication> jobApplications = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_student_view_submitted_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

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
                    if (i % 2 == 0)
                        jobApplications.get(i / 2).setJobPostData(documentSnapshots.get(i).toObject(JobPost.class));
                    else
                        jobApplications.get(i / 2).setCompanyData(documentSnapshots.get(i).toObject(Company.class));
                }
                recyclerView.setAdapter(new JobApplicationStudentAdapter(jobApplications, StudentViewSubmittedJobs.this));
            }
        });

        recyclerView.setAdapter(new JobApplicationStudentAdapter(new ArrayList<JobApplication>(), null));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(JobApplication item, int position) {
        Intent i = new Intent(getActivity(), JobApplicationDetailActivity.class);
        i.putExtra("JobApplication", item.getJobApplication().getPath());
        startActivity(i);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("AlertDialog");
        builder.setMessage("Are you sure you want to revoke job application?");

        // add the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore ff = FirebaseFirestore.getInstance();
                ff.document(jobApplications.get(position).getJobApplication().getPath()).delete();
                ((JobApplicationStudentAdapter)recyclerView.getAdapter()).removeItem(position);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}