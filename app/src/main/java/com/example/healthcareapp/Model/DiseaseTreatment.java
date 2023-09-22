package com.example.healthcareapp.Model;

public class DiseaseTreatment {
    private String diseaseName;
    private String treatment;
    private String extraComments;
    private String description;
    public DiseaseTreatment()
    {

    }
    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getExtraComments() {
        return extraComments;
    }

    public void setExtraComments(String extraComments) {
        this.extraComments = extraComments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
