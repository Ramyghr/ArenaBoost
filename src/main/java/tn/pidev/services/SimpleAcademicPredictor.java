package tn.esprit.pidev.services;

import tn.esprit.pidev.entities.AthleteProfile;

public class SimpleAcademicPredictor implements AcademicPredictor {

    @Override
    public String predictPerformance(AthleteProfile profile) throws PredictionException {
        // Simple prediction logic based on training hours and endurance score
        try {
            int trainingHours = profile.getTrainingHoursPerWeek() != null ?
                    profile.getTrainingHoursPerWeek() : 0;
            double enduranceScore = profile.getEnduranceTestScore() != null ?
                    profile.getEnduranceTestScore() : 0.0;

            double predictionScore = (trainingHours * 0.6) + (enduranceScore * 0.4);

            if (predictionScore > 8.0) {
                return "Excellent";
            } else if (predictionScore > 6.0) {
                return "Good";
            } else {
                return "Needs Improvement";
            }
        } catch (Exception e) {
            throw new PredictionException("Error during prediction", e);
        }
    }
}