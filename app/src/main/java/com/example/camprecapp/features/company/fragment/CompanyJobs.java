package com.example.camprecapp.features.company.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.features.VerticalSpace;
import com.example.camprecapp.features.company.CompanyAddJob;
import com.example.camprecapp.features.company.adapter.AddJobCustomAdapter;
import com.example.camprecapp.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CompanyJobs extends Fragment {
    AddJobCustomAdapter adapter;
    final ArrayList<JobPost> jobPosts = new ArrayList<>();
    RecyclerView recyclerView;

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
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CompanyAddJob.class);
                startActivityForResult(i, 1001);
            }
        });
        adapter = new AddJobCustomAdapter(new ArrayList<JobPost>(), null);
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpace(20));
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);

        JobPost();


        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }


        });
        return;
    }

    void JobPost() {
        String company = getActivity().getIntent().getStringExtra("company");

        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection("JobPost")
                .whereEqualTo("company", ff.document(company))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Log.i("JobPosts", "count = " + task.getResult().getDocuments().size());
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    JobPost jobPost = documentSnapshot.toObject(JobPost.class);
                    jobPost.setJobPost(documentSnapshot.getReference());
                    jobPosts.add(jobPost);
                }
                adapter = new AddJobCustomAdapter(jobPosts, new AddJobCustomAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(JobPost item, int position) {
                        Intent i = new Intent(getActivity(), CompanyAddJob.class);
                        i.putExtra("jobpost", item.getJobPost().getPath());
                        startActivity(i);
                    }
                });
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                JobPost();
            }
        }
    }
}
