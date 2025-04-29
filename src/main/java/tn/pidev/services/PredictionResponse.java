// PredictionResponse.java
package tn.esprit.pidev.services;

public class PredictionResponse {
    private String academicPerformance;

    // Constructor, getters, setters
    public PredictionResponse(String academicPerformance) {
        this.academicPerformance = academicPerformance;
    }

    public String getAcademicPerformance() {
        return academicPerformance;
    }

    public void setAcademicPerformance(String academicPerformance) {
        this.academicPerformance = academicPerformance;
    }
}