package com.example.camprecapp.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class JobApplication {
    private DocumentReference company;
    private DocumentReference jobpost;
    private DocumentReference student;

    private @Exclude JobPost jobPostData;
    private @Exclude Student studentData;

    public JobApplication(DocumentReference company, DocumentReference jobpost, DocumentReference student) {
        this.company = company;
        this.jobpost = jobpost;
        this.student = student;
    }

    public JobApplication() {
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

    public void setJobPostData(JobPost jobPostData) {
        this.jobPostData = jobPostData;
    }

    @Exclude
    public Student getStudentData() {
        return studentData;
    }

    public void setStudentData(Student studentData) {
        this.studentData = studentData;
    }
}
