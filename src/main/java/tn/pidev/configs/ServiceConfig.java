package tn.esprit.pidev.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tn.esprit.pidev.services.AcademicPredictor;
import tn.esprit.pidev.services.SimpleAcademicPredictor;

@Configuration
public class ServiceConfig {

    @Bean
    @Primary
    public AcademicPredictor academicPredictor() {
        return new SimpleAcademicPredictor();
    }
}