package com.example.camprecapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddJobCustomAdapter extends RecyclerView.Adapter {
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> companyName = new ArrayList<>();
    ArrayList<String> salary = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> jobType = new ArrayList<>();
    Context context;

    public AddJobCustomAdapter(ArrayList<String>titles,ArrayList<String>description,ArrayList<String>companyName,
                               ArrayList<String>salary,ArrayList<String>location,
                               ArrayList<String>jobType,Context context){
        this.context = context;
        this.titles = titles;
        this.description = description;
        this.companyName = companyName;
        this.salary = salary;
        this.location = location;
        this.jobType = jobType;
    }
    private class ItemHolder extends RecyclerView.ViewHolder {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.company_job_list_layout, parent, false);

        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).textViewTitle.setText(titles.get(position));
        ((ItemHolder) holder).txtViewCompanyName.setText(description.get(position));
        ((ItemHolder) holder).txtViewJobType.setText(description.get(position));
        ((ItemHolder) holder).textViewDescription.setText(description.get(position));
        ((ItemHolder) holder).txtViewSalary.setText(description.get(position));
        ((ItemHolder) holder).txtViewLocation.setText(description.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
