package com.example.camprecapp.features.company.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camprecapp.R;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AddJobCustomAdapter extends RecyclerView.Adapter<AddJobCustomAdapter.ItemHolder> implements Filterable {

    private ArrayList<JobPost> mDataset;
    private ArrayList<JobPost> mStringFilterList;
    ValueFilter valueFilter;

    public AddJobCustomAdapter(ArrayList<JobPost> mDataset, AddJobCustomAdapter.OnItemClickListener listener) {
        this.mDataset = mDataset;
        this.listener = listener;
        this.mStringFilterList = mDataset;
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

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.company_job_list_layout, parent, false);

        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        final JobPost jobPost = mDataset.get(position);

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

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<JobPost> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getTitle().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mDataset = (ArrayList<JobPost>) results.values;
            notifyDataSetChanged();
        }

    }

}
