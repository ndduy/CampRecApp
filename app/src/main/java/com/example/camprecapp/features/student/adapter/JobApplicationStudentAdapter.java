package com.example.camprecapp.features.student.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.features.company.adapter.JobApplicationAdapter;
import com.example.camprecapp.models.JobApplication;

public class JobApplicationStudentAdapter extends RecyclerView.Adapter<JobApplicationStudentAdapter.ApplicantItemHolder> {
    private JobApplication[] mDataset;

    public JobApplicationStudentAdapter(JobApplication[] mDataset, JobApplicationStudentAdapter.OnItemClickListener listener) {
        this.mDataset = mDataset;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(JobApplication item, int position);
    }

    private final JobApplicationStudentAdapter.OnItemClickListener listener;

    class ApplicantItemHolder extends RecyclerView.ViewHolder {
        TextView textViewJobName;
        TextView textViewJobID;
        TextView textViewCompanyName;


        public ApplicantItemHolder(View itemView) {
            super(itemView);

            textViewJobName = itemView.findViewById(R.id.textViewJobName);
            textViewJobID = itemView.findViewById(R.id.textViewJobID);
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
        }

    }

    @NonNull
    @Override
    public JobApplicationStudentAdapter.ApplicantItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.student_job_application_list_layout, parent, false);

        return new JobApplicationStudentAdapter.ApplicantItemHolder(row);
    }

    @Override
    public void onBindViewHolder(JobApplicationStudentAdapter.ApplicantItemHolder holder, final int position) {
        final JobApplication jobApplication = mDataset[position];

        holder.textViewJobName.setText(jobApplication.getJobPostData().getTitle());
        holder.textViewJobID.setText(jobApplication.getJobPostData().getCompanyName());
        holder.textViewCompanyName.setText(jobApplication.getCompanyData().getName());

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
