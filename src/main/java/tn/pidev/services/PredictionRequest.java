// PredictionRequest.java
package tn.esprit.pidev.services;

public class PredictionRequest {
    private Integer height;
    private Double weight;
    private Double bmi;
    private Double trainingIntensity;
    private Double enduranceTestScore;

    // Constructor, getters, setters
    public PredictionRequest(Integer height, Double weight, Double bmi,
                             Double trainingIntensity, Double enduranceTestScore) {
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.trainingIntensity = trainingIntensity;
        this.enduranceTestScore = enduranceTestScore;
    }

    // Getters and setters
    // (Add all getters and setters for the fields)
}