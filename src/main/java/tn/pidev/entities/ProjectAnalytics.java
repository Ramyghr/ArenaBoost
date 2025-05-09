package tn.esprit.pidev.entities;


import lombok.Data;

@Data
public class ProjectAnalytics {
    private float totalFundsCollected;
    private float totalExpenses;
    private float totalContributions;
    private int numberOfContributions;
    private float progressPercentage;
}