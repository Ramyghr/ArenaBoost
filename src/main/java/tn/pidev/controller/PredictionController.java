//package tn.esprit.pidev.controller;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.DecimalMax;
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tn.esprit.pidev.services.AcademicPredictor;
//import tn.esprit.pidev.services.PredictionException;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/predictions")
//@RequiredArgsConstructor
//public class PredictionController {
//
//    private final AcademicPredictor academicPredictor;
//
//    @PostMapping("/academic-performance")
//    public ResponseEntity<PredictionResponse> predictAcademicPerformance(
//            @Valid @RequestBody PredictionRequest request
//    ) {
//        try {
//            // Calculate derived fields
//            double bmi = calculateBMI(request.getHeight(), request.getWeight());
//            int trainingIntensity = request.getTrainingHoursPerWeek() * request.getTrainingFrequency();
//
//            // Create feature array matching Python model expectations
//            double[] features = new double[] {
//                    request.getHeight(),
//                    request.getWeight(),
//                    bmi,
//                    trainingIntensity,
//                    request.getEnduranceTestScore()
//            };
//
//            // Get prediction
//            PredictionResult result = academicPredictor.predict(features);
//
//            return ResponseEntity.ok(
//                    new PredictionResponse(
//                            result.getAcademicPerformance(),
//                            result.getProbabilities()
//                    )
//            );
//        } catch (PredictionException e) {
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                    .body(new PredictionResponse("Service unavailable", null));
//        }
//    }
//
//    private double calculateBMI(int heightCm, double weightKg) {
//        double heightMeters = heightCm / 100.0;
//        return weightKg / (heightMeters * heightMeters);
//    }
//
//    // Request/Response DTOs
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PredictionRequest {
//        @Min(100) @Max(250)
//        private Integer height;
//
//        @Min(30) @Max(150)
//        private Double weight;
//
//        @Min(1) @Max(168)  // 24*7
//        private Integer trainingHoursPerWeek;
//
//        @Min(1) @Max(7)
//        private Integer trainingFrequency;
//
//        @DecimalMin("0.0") @DecimalMax("10.0")
//        private Double enduranceTestScore;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PredictionResponse {
//        private String academicPerformance;
//        private Map<String, Double> probabilities;
//    }
//}