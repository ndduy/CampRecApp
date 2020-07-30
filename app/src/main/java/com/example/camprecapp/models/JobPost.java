package com.example.camprecapp.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class JobPost {
    private String title;
    private String description;
    private String companyName;
    private String salary;
    private String location;
    private String jobType;
    private DocumentReference company;
    private @Exclude
    DocumentReference jobPost;

    private @Exclude
    DocumentReference documentId;

    public JobPost(String title, String description, String companyName, String salary, String location, String jobType, DocumentReference company) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.salary = salary;
        this.location = location;
        this.jobType = jobType;
        this.company = company;
    }

    public JobPost() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public DocumentReference getCompany() {
        return company;
    }

    public void setCompany(DocumentReference company) {
        this.company = company;
    }

    public DocumentReference getDocumentId() {
        return documentId;
    }

    public void setDocumentId(DocumentReference documentId) {
        this.documentId = documentId;
    }

    @Exclude
    public DocumentReference getJobPost() {
        return jobPost;
    }

    @Exclude
    public void setJobPost(DocumentReference jobPost) {
        this.jobPost = jobPost;
    }
}
