package tn.esprit.pidev.entities;


import lombok.Data;

@Data
public class ProjectPrediction {
    private boolean willReachGoal;  // Classification
    private float predictedFunds;   // Régression
}