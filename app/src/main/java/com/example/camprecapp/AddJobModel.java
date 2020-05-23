package com.example.camprecapp;

public class AddJobModel {
    private String title;
    private String description;
    private String companyName;
    private String salary;
    private String location;
    private String jobType;

    public AddJobModel(){

    }
    public AddJobModel(String title, String description, String companyName, String salary,
                       String location, String jobType){
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.salary = salary;
        this.location = location;
        this.jobType = jobType;
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

}
