package com.example.camprecapp.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class JobApplication {
    private DocumentReference company;
    private DocumentReference jobpost;
    private DocumentReference student;

    private String documentUrl;

    private @Exclude DocumentReference jobApplication;

    private @Exclude JobPost jobPostData;
    private @Exclude Student studentData;
    private @Exclude Company companyData;

    public JobApplication(DocumentReference company, DocumentReference jobpost, DocumentReference student) {
        this.company = company;
        this.jobpost = jobpost;
        this.student = student;
    }

    public JobApplication() {
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public DocumentReference getCompany() {
        return company;
    }

    public void setCompany(DocumentReference company) {
        this.company = company;
    }

    public DocumentReference getJobpost() {
        return jobpost;
    }

    public void setJobpost(DocumentReference jobpost) {
        this.jobpost = jobpost;
    }

    public DocumentReference getStudent() {
        return student;
    }

    public void setStudent(DocumentReference student) {
        this.student = student;
    }

    @Exclude
    public JobPost getJobPostData() {
        return jobPostData;
    }

    @Exclude
    public void setJobPostData(JobPost jobPostData) {
        this.jobPostData = jobPostData;
    }

    @Exclude
    public Student getStudentData() {
        return studentData;
    }

    @Exclude
    public void setStudentData(Student studentData) {
        this.studentData = studentData;
    }

    @Exclude
    public Company getCompanyData() {
        return companyData;
    }

    @Exclude
    public void setCompanyData(Company companyData) {
        this.companyData = companyData;
    }

    public DocumentReference getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(DocumentReference jobApplication) {
        this.jobApplication = jobApplication;
    }
}
