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

public class AddJobCustomAdapter extends FirestoreRecyclerAdapter<JobPost, AddJobCustomAdapter.ItemHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     * @param listener
     */
    public AddJobCustomAdapter(@NonNull FirestoreRecyclerOptions<JobPost> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(JobPost item, int position);
    }

    private final OnItemClickListener listener;

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView txtViewCompanyName;
        TextView txtViewSalary;
        TextView txtViewLocation;
        TextView txtViewJobType;

        public ItemHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.txtViewJobTitle);
            txtViewCompanyName = itemView.findViewById(R.id.txtViewCompanyName);
            txtViewJobType = itemView.findViewById(R.id.txtViewJobType);
            textViewDescription = itemView.findViewById(R.id.textViewJobDescription);
            txtViewSalary = itemView.findViewById(R.id.txtViewSalary);
            txtViewLocation = itemView.findViewById(R.id.txtViewLocation);

        }


    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.company_job_list_layout, parent, false);

        return new ItemHolder(row);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, final int position, @NonNull final JobPost jobPost) {
        holder.textViewTitle.setText(jobPost.getTitle());
        holder.txtViewCompanyName.setText(jobPost.getCompanyName());
        holder.txtViewJobType.setText(jobPost.getJobType());
        holder.textViewDescription.setText(jobPost.getDescription());
        holder.txtViewSalary.setText(jobPost.getSalary());
        holder.txtViewLocation.setText(jobPost.getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(jobPost, position);
            }
        });

    }
}
