package com.example.camprecapp.features.company.adapter;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddJobCustomAdapter extends FirestoreRecyclerAdapter<JobPost, AddJobCustomAdapter.ItemHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AddJobCustomAdapter(@NonNull FirestoreRecyclerOptions<JobPost> options) {
        super(options);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView txtViewCompanyName;
        TextView txtViewSalary;
        TextView txtViewLocation;
        TextView txtViewJobType;

        public ItemHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.txtViewJobTitle);
            txtViewCompanyName = (TextView) itemView.findViewById(R.id.txtViewCompanyName);
            txtViewJobType = (TextView) itemView.findViewById(R.id.txtViewJobType);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewJobDescription);
            txtViewSalary = (TextView) itemView.findViewById(R.id.txtViewSalary);
            txtViewLocation = (TextView) itemView.findViewById(R.id.txtViewLocation);
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
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull JobPost jobPost) {
        ((ItemHolder) holder).textViewTitle.setText(jobPost.getTitle());
        ((ItemHolder) holder).txtViewCompanyName.setText(jobPost.getCompanyName());
        ((ItemHolder) holder).txtViewJobType.setText(jobPost.getJobType());
        ((ItemHolder) holder).textViewDescription.setText(jobPost.getDescription());
        ((ItemHolder) holder).txtViewSalary.setText(jobPost.getSalary());
        ((ItemHolder) holder).txtViewLocation.setText(jobPost.getLocation());
    }
}
