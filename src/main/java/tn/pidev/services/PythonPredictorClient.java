package tn.esprit.pidev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tn.esprit.pidev.entities.AthleteProfile;

@Component
public class PythonPredictorClient implements AcademicPredictor {

    private final RestTemplate restTemplate;
//    private final String pythonServiceUrl;

    @Value("${python.service.url}")
    private String pythonServiceUrl;
    @Autowired
    public PythonPredictorClient(RestTemplate restTemplate,
                                 @Value("${python.service.url}/predict") String pythonServiceUrl) {
        this.restTemplate = restTemplate;
        this.pythonServiceUrl = pythonServiceUrl;
    }


    @Override
    public String predictPerformance(AthleteProfile profile){
        String fullUrl = pythonServiceUrl + "/predict";
        try {
            PredictionRequest request = createRequest(profile);
            PredictionResponse response = restTemplate.postForObject(
                    fullUrl,
                    request,
                    PredictionResponse.class
            );
            return response.getAcademicPerformance();
        } catch (Exception e) {
            throw new PredictionException("Failed to get prediction from Python service", e);
        }
    }

    private PredictionRequest createRequest(AthleteProfile profile) {
        return new PredictionRequest(
                profile.getHeight(),
                profile.getWeight(),
                profile.getBmi(),
                calculateTrainingIntensity(profile),
                profile.getEnduranceTestScore()
        );
    }

    private double calculateTrainingIntensity(AthleteProfile profile) {
        return profile.getTrainingHoursPerWeek() * profile.getTrainingFrequency();
    }
}