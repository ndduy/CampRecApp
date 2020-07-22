package com.example.camprecapp.features.company.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.camprecapp.features.company.CompanyAddJob;
import com.example.camprecapp.features.company.adapter.AddJobCustomAdapter;
import com.example.camprecapp.R;
import com.example.camprecapp.models.JobPost;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CompanyJobs extends Fragment {
    AddJobCustomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_company_view_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton buttonAddJob = getView().findViewById(R.id.floating_action_button);
        buttonAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CompanyAddJob.class));
            }
        });
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        String company = getActivity().getIntent().getStringExtra("company");
        Query query = ff.collection("JobPost").whereEqualTo("company", ff.document(company))
                .orderBy("salary", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<JobPost> options = new FirestoreRecyclerOptions.Builder<JobPost>()
                .setQuery(query, new SnapshotParser<JobPost>() {
                    @NonNull
                    @Override
                    public JobPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        JobPost post = snapshot.toObject(JobPost.class);
                        post.setJobPost(snapshot.getReference());
                        return post;
                    }
                })
                .build();
        adapter = new AddJobCustomAdapter(options, new AddJobCustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(JobPost item, int position) {
                Intent i = new Intent(getActivity(), CompanyAddJob.class);
                i.putExtra("jobpost", item.getJobPost().getPath());
                startActivity(i);
            }
        });
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
