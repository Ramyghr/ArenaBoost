//package tn.esprit.pidev.services;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//import tn.esprit.pidev.entities.AthleteProfile;
//
//@Service
//public class PredictionService {
//
//    private PipelineModel model;
//    private StandardScaler scaler;
//    private LabelEncoder labelEncoder;
//
//    @PostConstruct
//    public void init() throws Exception {
//        // Load artifacts
//        this.model = (PipelineModel) Joblib.load(
//                getClass().getResource("/models/model.joblib").getFile()
//        );
//        this.scaler = (StandardScaler) Joblib.load(
//                getClass().getResource("/models/scaler.joblib").getFile()
//        );
//        this.labelEncoder = (LabelEncoder) Joblib.load(
//                getClass().getResource("/models/label_encoder.joblib").getFile()
//        );
//    }
////training intensity = training hours per week * training frequency
//    public String predict(AthleteProfile input) {
//        // 1. Preprocess input
//        double[] features = new double[]{
//                input.getHeight(),
//                input.getWeight(),
//                input.getBmi(),
//                input.getTrainingIntensity(),
//                input.getEnduranceTestScore()
//        };
//
//        // 2. Scale features
//        double[] scaledFeatures = scaler.transform(features);
//
//        // 3. Make prediction
//        double[] probabilities = model.predictProba(scaledFeatures);
//        int classIdx = model.predict(scaledFeatures);
//
//        // 4. Decode label
//        return labelEncoder.inverseTransform(classIdx);
//    }
//}
