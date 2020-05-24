package com.example.camprecapp.models;

import com.google.firebase.firestore.DocumentReference;

public class JobApplication {
    private DocumentReference jobpost;
    private DocumentReference student;

    public JobApplication(DocumentReference jobpost, DocumentReference student) {
        this.jobpost = jobpost;
        this.student = student;
    }

    public JobApplication() {
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
}
