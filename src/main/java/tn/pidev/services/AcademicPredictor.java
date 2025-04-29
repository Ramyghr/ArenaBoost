package tn.esprit.pidev.services;

import tn.esprit.pidev.entities.AthleteProfile;

public interface AcademicPredictor {
    String predictPerformance(AthleteProfile profile) throws PredictionException;
}