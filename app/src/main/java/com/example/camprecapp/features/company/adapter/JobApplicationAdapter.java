package com.example.camprecapp.features.company.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.models.JobPost;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class JobApplicationAdapter extends FirestoreRecyclerAdapter<JobPost, AddJobCustomAdapter.ItemHolder>{

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     * @param listener
     */
    public JobApplicationAdapter(@NonNull FirestoreRecyclerOptions<JobPost> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(JobPost item, int position);
    }

    private final OnItemClickListener listener;

    class ApplicantItemHolder extends RecyclerView.ViewHolder {
        TextView textViewJobTitle;
        TextView getTextViewApplicantName;
        TextView textViewStatus;


        public ApplicantItemHolder(View itemView) {
            super(itemView);

            textViewJobTitle = itemView.findViewById(R.id.txtViewJobTitle);
            getTextViewApplicantName = itemView.findViewById(R.id.txtViewApplicantName);
            textViewStatus = itemView.findViewById(R.id.txtViewStatus);

        }


    }

    @NonNull
    @Override
    public ApplicantItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.activity_company_view_sub_application, parent, false);

        return new ApplicantItemHolder(row);
    }

    @Override
    protected void onBindViewHolder(@NonNull ApplicantItemHolder holder, final int position, @NonNull final JobPost jobPost) {
        holder.textViewJobName.setText(jobPost.getTitle());
        holder.textViewJobID.setText(jobPost.getCompanyName());
        holder.textViewApplicantName.setText(jobPost.getJobType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(jobPost, position);
            }
        });

    }

}
