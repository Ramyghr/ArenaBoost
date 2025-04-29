package tn.esprit.pidev.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.repository.AthleteProfileRepository;

@Service
public class AthleteProfileService {

    private final AthleteProfileRepository repository;
    private final AcademicPredictor predictor;
    public AthleteProfileService(AthleteProfileRepository repository, AcademicPredictor predictor) {
        this.repository = repository;
        this.predictor = predictor;
    }
    public AthleteProfile createProfile(AthleteProfile profile) throws PredictionException {
        calculateDerivedFields(profile);
        String prediction = predictor.predictPerformance(profile);
        profile.setAcademicPerformance(prediction);
        return repository.save(profile);
    }

    private void calculateDerivedFields(AthleteProfile profile) {
        if (profile.getBmi() == null && profile.getHeight() != null && profile.getWeight() != null) {
            double heightInMeters = profile.getHeight() / 100.0;
            profile.setBmi(profile.getWeight() / (heightInMeters * heightInMeters));
        }
    }
}