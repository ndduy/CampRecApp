package com.example.camprecapp.features.company.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.example.camprecapp.models.Student;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class JobApplicationAdapter extends RecyclerView.Adapter<JobApplicationAdapter.ApplicantItemHolder> {
    private JobApplication[] mDataset;

    public JobApplicationAdapter(JobApplication[] mDataset, OnItemClickListener listener) {
        this.mDataset = mDataset;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(JobApplication item, int position);
    }

    private final OnItemClickListener listener;

    class ApplicantItemHolder extends RecyclerView.ViewHolder {
        TextView textViewJobName;
        TextView textViewJobID;
        TextView textViewApplicantName;


        public ApplicantItemHolder(View itemView) {
            super(itemView);

            textViewJobName = itemView.findViewById(R.id.textViewJobName);
            textViewJobID = itemView.findViewById(R.id.textViewJobID);
            textViewApplicantName = itemView.findViewById(R.id.textViewApplicantName);
        }

    }

    @NonNull
    @Override
    public ApplicantItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.company_job_application_list_layout, parent, false);

        return new ApplicantItemHolder(row);
    }

    @Override
    public void onBindViewHolder(ApplicantItemHolder holder, final int position) {
        final JobApplication jobApplication = mDataset[position];

        holder.textViewJobName.setText(jobApplication.getJobPostData().getTitle());
        holder.textViewJobID.setText(jobApplication.getJobPostData().getCompanyName());
        holder.textViewApplicantName.setText(jobApplication.getStudentData().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(jobApplication, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
